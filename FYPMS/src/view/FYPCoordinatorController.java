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
				//System.out.println("Generate Project Details based on filters: ");
				//generate project details report according to searching filters, e.g., status, or supervisor
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
			if (project.getStudent()==null) {
				System.out.println("Student Name: NIL");
				System.out.println("Student ID: NIL");
			}
			else {
				String studentName = StudentDB.findUserByID(project.getStudentID()).getName();
				System.out.println("Student Name: "+ studentName);
				System.out.println("Student ID: "+ project.getStudentID());
			}
			System.out.println();
		}
	}
	
	public void filteredProjectMenu() {
		
	}
}
