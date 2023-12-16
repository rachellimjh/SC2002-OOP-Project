package view;

import java.io.IOException;
import java.util.ArrayList;

import database.PasswordManager;
import database.ProjectDB;
import database.RequestDB;
import database.SupervisorDB;
import model.Project;
import model.Project.ProjectStatus;
import model.Request;
import model.Request.RequestDesc;
import model.RequestTitleChange;
import model.Student;
import model.Supervisor;
import model.User;

public class StudentController extends UserController{
	
	private Student student;
	public StudentController(Student user) {
		this.student=user;
	}

	@Override
	public void mainMenu() {
		
		do {
			headerView();
			System.out.println("1. Change password");
			System.out.println("\u001B[1mProject\u001B[0m");
			System.out.println("2. View available projects");
			System.out.println("3. View my project information");
			System.out.println("\u001B[1mRequest\u001B[0m");
			System.out.println("4. View my request history and status");
			System.out.println("5. Request project allocation");
			System.out.println("6. Request project title change");
			System.out.println("7. Request FYP deregistration");
			System.out.println("8. Exit");
			
			System.out.print("\nEnter your choice: ");
			
			choice = sc.nextInt();
			System.out.println();
			switch(choice)
			{
			case 1:
				PasswordManager.changePassword(student);
				start();
				break;
			case 2:
				//View available projects
				if (student.isDeregistered()) {
					System.out.println("Sorry! You have already been degistered from this FYP System. ");
				}
				else {
					availableProjectMenu(student.isAssigned());
				}
				sc.nextLine();
				break;
			case 3:
				//View my project information
				studentProjectMenu(student.isAssigned());
				sc.nextLine();
				break;
			case 4:
				//View my request history and status
				studentRequestHistory();
				sc.nextLine();
				break;
			case 5:
				//Request project allocation
				availableProjectMenu(student.isAssigned());
				ArrayList<Request> prevReqs =RequestDB.findRequestBySenderID(student.getUserID());
				for (Request prevReq: prevReqs) {
					if (prevReq.getRequestDesc()==RequestDesc.ALLOCATE_PROJECT) {
						System.out.println("You have already sent a request to be allocated to a project!");
						break;
					}
				}
				Request request = studentAssignProjectMenu();
				studentRequestInformation(request);
				sc.nextLine();
				break;
				
			case 6:
				//Request project title change
				studentProjectMenu(student.isAssigned());
				Request req = studentChangeTitleMenu();
				studentRequestInformation(req);
				sc.nextLine();
				break;
			case 7:
				//Request FYP deregistration
				sc.nextLine();
				studentDeregisterMenu();
				break;
			case 8:
				System.out.println("Thank you for using the FYP Management System. ");
				break;
			default:
				System.out.println("Invalid input. Please only enter 1-8. ");
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
	
	public void availableProjectMenu(boolean assigned) {
		//ProjectDB projDB = new ProjectDB();
		if (assigned) {
			System.out.println("You have already been assigned a project!");
		}
		else {
			System.out.println("\u001B[1m\u001B[4mList of Projects Available\u001B[0m");
			ArrayList<Project> projects = ProjectDB.findProjectByStatus(ProjectStatus.AVAILABLE);
			for (Project project : projects) {
				System.out.println("Project ID: " + project.getProjectID());
				System.out.println("Project Title: " +project.getProjectTitle());
				String supervisorName = SupervisorDB.findUserByID(project.getSupervisorID()).getName();
				System.out.println("Supervisor Name: " + supervisorName);
				System.out.println("Supervisor ID: " + project.getSupervisorID());
				System.out.println();
			}
		}
	}
	
	public void studentProjectMenu(boolean assigned) {
		//ProjectDB projDB = new ProjectDB();
		if (!assigned) {
			System.out.println("You do not have any project assigned!\n");
		}
		else {
			System.out.println("\u001B[1m\u001B[4mProject Information\u001B[0m");
			Project project = ProjectDB.findProjectByStudentID(student.getUserID());
			
			System.out.println("Project ID: " + project.getProjectID());
			System.out.println("Project Title: " +project.getProjectTitle());
			System.out.println("Supervisor ID: "+project.getSupervisorID());
			System.out.println("Supervisor Name: " + project.getSupervisor().getName());
			System.out.println("Supervisor Email: "+project.getSupervisor().getEmail());
			System.out.println();
		}
	}
	
	public void studentRequestHistory() {
		System.out.println("\u001B[1m\u001B[4mRequest History\u001B[0m");
		if (student.getRequests()==null /*&& RequestDB.findRequestBySender(student).isEmpty()*/) {
			System.out.println("You do not have any request history. ");
			System.out.println();
		}
		else {
			//requestID|requestDate|projectID|senderID|receiverID|requestDesc|requestStatus|
			ArrayList<Request> req = student.getRequests();
			for (Request request : req) {
				System.out.println("Request ID: " + request.getRequestID());
				System.out.println("Timestamp: "+request.getRequestTime());
				System.out.println("Project ID: " +request.getProjectID());			
				System.out.println("Sender ID: " + request.getSender().getUserID());
				System.out.println("Receiver ID: " + request.getReceiver().getUserID());
				System.out.println("Request Description: " + request.getRequestDesc());
				System.out.println("Request Status: " + request.getRequestStatus());
				System.out.println("Project ID: " + request.getProjectID());
				System.out.println("Project Title: " + ProjectDB.findProjectByID(request.getProjectID()).getProjectTitle());
				if (request.getRequestDesc()==Request.RequestDesc.CHANGE_TITLE)
					System.out.println("New Requested Title: "+((RequestTitleChange)request).getNewTitle());
				System.out.println();
			}
		}
	}
	
	public void studentRequestInformation(Request request) {
		if (request!=null) {
			System.out.println("\nRequest created successfully. ");
			System.out.println("\u001B[1m\u001B[4mRequest Information\u001B[0m");
			System.out.println("Request ID: " + request.getRequestID());
			System.out.println("Request Description: " +request.getRequestDesc());
			System.out.println("Sender Name: " + request.getSender().getName());
			
			System.out.println("Receiver Name: " + request.getReceiver().getName());
			System.out.println("Request Status: "+ request.getRequestStatus());
			System.out.println("Project ID: " + request.getProjectID());
			System.out.println("Project Title: " + ProjectDB.findProjectByID(request.getProjectID()).getProjectTitle());
			if (request.getRequestDesc()==Request.RequestDesc.CHANGE_TITLE)
				System.out.println("New Requested Title: "+((RequestTitleChange)request).getNewTitle());
			System.out.println();
		}
		else {
			return;
		}
	}
	
	public Request studentAssignProjectMenu() {
		if (student.isAssigned()) {
			return null;
		}
		Request request = null;	
		int projID;
		do {
			System.out.print("Enter the project ID you want to be assigned to: ");
			projID = sc.nextInt();
			if (ProjectDB.findProjectByID(projID)==null || ProjectDB.findProjectByID(projID).getProjectStatus()!=ProjectStatus.AVAILABLE)
				System.out.println("Please enter a valid Project ID. \n");
		}while (ProjectDB.findProjectByID(projID)==null || ProjectDB.findProjectByID(projID).getProjectStatus()!=ProjectStatus.AVAILABLE);
		//String supervisorID = ProjectDB.findProjectByID(projID).getSupervisorID();
		Supervisor fypCoord = SupervisorDB.findUserByID(fypCoordinator);
		
		try {
			request = student.createRequest(RequestDesc.ALLOCATE_PROJECT, projID, fypCoord, null, null);
		} catch (IOException e) {
			System.out.println("Error. Request File not found. ");
		}
		return request;
	}
	
	public Request studentChangeTitleMenu() {
		if (!student.isAssigned()) {
			return null;
		}
		Request request =null;
		System.out.print("Enter the new project title you would like to propose: ");
		sc.nextLine();
		String newTitle = sc.nextLine();
		int projID = student.getProject().getProjectID();
		String supervisorID = ProjectDB.findProjectByID(projID).getSupervisorID();
		Supervisor supervisor = SupervisorDB.findUserByID(supervisorID);
		
		try {
			request = student.createRequest(RequestDesc.CHANGE_TITLE, projID, supervisor, newTitle, null);
		} catch (IOException e) {
			System.out.println("Error. Request File not found. ");
		}
		return request;
	}
	
	public boolean studentDeregisterMenu() {
		if (student.isDeregistered()) {
			System.out.println("You are already deregistered from the FYP. ");
			return false;
		}
		if (!student.isAssigned()) {
			System.out.println("You are not assigned to any project. ");
			return false;
		}
		String deregChoice;
		do {
			System.out.println("Are you sure you would like to \u001B[4;31;1mDEREGISTER\u001B[0m from the FYP?");
			System.out.println("Press the Enter key to go back. If not, enter '\u001B[4;31;1mYES\u001B[0m' to proceed. ");
			deregChoice = sc.nextLine();
			
			if (deregChoice.equals("YES")) {
				int projID = student.getProject().getProjectID();
				String supervisorID = ProjectDB.findProjectByID(projID).getSupervisorID();
				Supervisor fypCoord = SupervisorDB.findUserByID(fypCoordinator);
				try {
					student.createRequest(RequestDesc.DEREGISTER_FYP, projID, fypCoord, null, null);
				} catch (IOException e) {
					System.out.println("Error. Request File not found. ");
				}
				break;
			}
			else if (deregChoice.isEmpty()) {
				return false;
			}
			else {
				System.out.println("Invalid input. Please only press Enter or 'YES'. ");
				System.out.println();
			}
		}while (!deregChoice.equals("YES") || !deregChoice.equals(""));
		return true;
	}

}
