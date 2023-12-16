package view;

import java.io.IOException;
import java.util.ArrayList;

import database.PasswordManager;
import database.ProjectDB;
import database.StudentDB;
import database.SupervisorDB;
import model.FYPCoordinator;
import model.Project;
import model.Supervisor;
import model.Project.ProjectStatus;
import model.Student;

public class FYPCoordinatorController extends SupervisorController{
	
	FYPCoordinator fypCoordinator;
	public FYPCoordinatorController(FYPCoordinator user) {
		super(user);
		this.fypCoordinator=user;
	}

	@Override
	public void mainMenu() {
		do {
			headerView();
			System.out.println("1. Change password");
			System.out.println("\u001B[1mProject\u001B[0m");
			System.out.println("2. Create new project");
			System.out.println("3. Modify project title");
			System.out.println("4. View submitted projects");
			System.out.println("5. View all projects");
			System.out.println("\u001B[1mRequest\u001B[0m");
			System.out.println("6. View/Approve/Reject pending requests");
			System.out.println("7. View my request history");
			System.out.println("8. Exit");
			
			System.out.print("\nEnter your choice: ");
			choice = sc.nextInt();
			sc.nextLine(); //consume the new line character if the previous input was an integer
			System.out.println();
			switch(choice)
			{
			case 1:
				PasswordManager.changePassword(fypCoordinator);
				start();
				break;
			case 2:
				if(fypCoordinator.getNumSupervised()>=2) {
					System.out.println("Sorry! You are already supervising 2 projects.");
					break;
				}
				System.out.println("Enter your project title: ");
				String projectTitle = sc.next();
				try {
					fypCoordinator.createProject(projectTitle);
				} catch (IOException e) {
					System.out.println("Error. Project file not found.");
				}
				break;
			case 3:
				//modify project title
				ArrayList <Project> alr = new ArrayList<>();
				Project proj = supervisorModifyProjectTitleView();
				alr.add(proj);
				System.out.println("\nProject title modified successfully.\n");
				supervisorProjectMenu(alr);
				break;
			case 4:
				supervisorProjectMenu(fypCoordinator.getProjects()); //share the same with supervisor
				break;
			case 5:
				//View All Projects
				allProjectMenu();
				int filter;
				//generate project details report according to searching filters, e.g., status, or supervisor
				do {
					System.out.println("\u001B[1mGenerate Project Details based on filters: \u001B[0m");
					System.out.println("1. Project Status");
					System.out.println("2. Supervisor ID");
					System.out.println("3. Student ID");
					System.out.println("4. Exit");
					System.out.print("\nEnter your choice: ");
					filter = sc.nextInt();
					sc.nextLine();
					switch(filter) 
					{
					case 1: 
						filteredProjectMenu(true,false,false);
						break;
					case 2:
						filteredProjectMenu(false,true,false);
						break;
					case 3:
						filteredProjectMenu(false,false,true);
						break;
					case 4:
						break;
					}
				}while(filter>4||filter<1);
				break;
			case 6:
				//View/Approve/Reject pending requests
				supervisorPendingRequestView();
				break;
			case 7: 
				//View my request history
				supervisorRequestMenu();
			case 8:
				System.out.println("Thank you for using the FYP Management System. ");
				break;
			}
			if (choice >= 1 && choice < 8) {
	            System.out.print("Press Enter to go back.");
	            sc.nextLine();
	            choice=0; //prevent termination after checking of while condition
	            System.out.println();
	            continue;
	        }
		}while(choice>8 || choice<1);
	}
	
	public void allProjectMenu() {
		
		System.out.println("\u001B[1m\u001B[4mList of All Projects\u001B[0m");
		ArrayList<Project> projects = ProjectDB.readFile(ProjectDB.fileName);
		for (Project project : projects) {
			System.out.println("Project ID: " + project.getProjectID());
			System.out.println("Project Title: " +project.getProjectTitle());
			System.out.println("Project Status: "+project.getProjectStatus());
			String supervisorName = SupervisorDB.findUserByID(project.getSupervisorID()).getName();
			System.out.println("Supervisor Name: " + supervisorName);
			System.out.println("Supervisor ID: " + project.getSupervisorID());
			System.out.println("Supervisor Email: "+SupervisorDB.findUserByID(project.getSupervisorID()).getEmail());
			if (project.getStudent()==null) {
				System.out.println("Student Name: NIL");
				System.out.println("Student ID: NIL");
				System.out.println("Student Email: NIL");
			}
			else {
				String studentName = StudentDB.findUserByID(project.getStudentID()).getName();
				System.out.println("Student Name: "+ studentName);
				System.out.println("Student ID: "+ project.getStudentID());
				System.out.println("Student Email: "+StudentDB.findUserByID(project.getStudentID()).getEmail());
			}
			System.out.println();
		}
	}
	
	public void filteredProjectMenu(boolean projStatus, boolean supID, boolean studentID) {
		if (projStatus) {
			int choice;
			do {
				System.out.println();
				System.out.println("View:");
				System.out.println("1. AVAILABLE projects");
				System.out.println("2. RESERVED projects");
				System.out.println("3. ALLOCATED projects");
				System.out.println("4. UNAVAILABLE projects");
				System.out.println("5. Back");
				System.out.print("\nEnter your choice: ");
				choice=sc.nextInt();
				sc.nextLine();
				switch(choice) {
				case 1:
					filteredProjectReport(ProjectStatus.AVAILABLE,null,null);
					break;
				case 2:
					filteredProjectReport(ProjectStatus.RESERVED,null,null);
					break;
				case 3:
					filteredProjectReport(ProjectStatus.ALLOCATED,null,null);
					break;
				case 4:
					filteredProjectReport(ProjectStatus.UNAVAILABLE,null,null);
					break;
				case 5:
					break;
				}
			}while(choice>5||choice<1);
		}
		
		if (supID) {
			System.out.println();
			System.out.print("Enter the Supervisor ID you would like to view: ");
			String supervisorID= sc.nextLine().toUpperCase();
			Supervisor sup = SupervisorDB.findUserByID(supervisorID);
			if (sup==null) {
				System.out.println("No such supervisor found!");
				return;
			}
			filteredProjectReport(null,supervisorID,null);
		}
		if (studentID) {
			System.out.println();
			System.out.print("Enter the Student ID you would like to view: ");
			String studID= sc.nextLine().toUpperCase();
			Student student = StudentDB.findUserByID(studID);
			if (student==null) {
				System.out.println("No such student found!");
				return;
			}
			filteredProjectReport(null,null,studID);
		}
	}
	
	public void filteredProjectReport(ProjectStatus projStatus, String supID, String studentID) {
		if (projStatus!=null) {
			ArrayList<Project> projects = ProjectDB.findProjectByStatus(projStatus);
			if (projects.isEmpty()) {
				System.out.println("No "+projStatus+" found. ");
				System.out.println();
				return;
			}
			System.out.println("List of "+projStatus+" Projects");
			for (Project project : projects) {
				System.out.println("Project ID: " + project.getProjectID());
				System.out.println("Project Title: " +project.getProjectTitle());
				System.out.println("Project Status: "+project.getProjectStatus());
				String supervisorName = SupervisorDB.findUserByID(project.getSupervisorID()).getName();
				System.out.println("Supervisor Name: " + supervisorName);
				System.out.println("Supervisor ID: " + project.getSupervisorID());
				System.out.println("Supervisor Email: "+SupervisorDB.findUserByID(project.getSupervisorID()).getEmail());
				if (project.getStudent()==null) {
					System.out.println("Student Name: NIL");
					System.out.println("Student ID: NIL");
					System.out.println("Student Email: NIL");
				}
				else {
					String studentName = StudentDB.findUserByID(project.getStudentID()).getName();
					System.out.println("Student Name: "+ studentName);
					System.out.println("Student ID: "+ project.getStudentID());
					System.out.println("Student Email: "+StudentDB.findUserByID(project.getStudentID()).getEmail());
				}
				System.out.println();
			}
		}
		else if (supID!=null) {
			ArrayList<Project> projects = ProjectDB.findProjectBySupervisorID(supID);
			if (projects.isEmpty()) {
				System.out.println("No projects supervised by "+SupervisorDB.findUserByID(supID).getName());
				System.out.println();
				return;
			}
			System.out.println("List of Projects supervised/created by "+SupervisorDB.findUserByID(supID).getName());
			for (Project project : projects) {
				System.out.println("Project ID: " + project.getProjectID());
				System.out.println("Project Title: " +project.getProjectTitle());
				System.out.println("Project Status: "+project.getProjectStatus());
				String supervisorName = SupervisorDB.findUserByID(project.getSupervisorID()).getName();
				System.out.println("Supervisor Name: " + supervisorName);
				System.out.println("Supervisor ID: " + project.getSupervisorID());
				System.out.println("Supervisor Email: "+SupervisorDB.findUserByID(project.getSupervisorID()).getEmail());
				if (project.getStudent()==null) {
					System.out.println("Student Name: NIL");
					System.out.println("Student ID: NIL");
					System.out.println("Student Email: NIL");
				}
				else {
					String studentName = StudentDB.findUserByID(project.getStudentID()).getName();
					System.out.println("Student Name: "+ studentName);
					System.out.println("Student ID: "+ project.getStudentID());
					System.out.println("Student Email: "+StudentDB.findUserByID(project.getStudentID()).getEmail());
				}
				System.out.println();
			}
		}
		else {
			Project project = ProjectDB.findProjectByStudentID(studentID);
			if (project==null) {
				System.out.println("No project allocated to "+StudentDB.findUserByID(studentID).getName());
				System.out.println();
				return;
			}
			System.out.println("Project allocated to "+StudentDB.findUserByID(studentID).getName());
			System.out.println("Project ID: " + project.getProjectID());
			System.out.println("Project Title: " +project.getProjectTitle());
			System.out.println("Project Status: "+project.getProjectStatus());
			String supervisorName = SupervisorDB.findUserByID(project.getSupervisorID()).getName();
			System.out.println("Supervisor Name: " + supervisorName);
			System.out.println("Supervisor ID: " + project.getSupervisorID());
			System.out.println("Supervisor Email: "+SupervisorDB.findUserByID(project.getSupervisorID()).getEmail());
			String studentName = StudentDB.findUserByID(project.getStudentID()).getName();
			System.out.println("Student Name: "+ studentName);
			System.out.println("Student ID: "+ studentID);
			System.out.println("Student Email: "+StudentDB.findUserByID(project.getStudentID()).getEmail());
			System.out.println();
		}
	}
}
