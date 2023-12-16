package view;

import java.io.IOException;
import java.util.ArrayList;

import database.PasswordManager;
import database.ProjectDB;
import database.RequestDB;
import database.StudentDB;
import database.SupervisorDB;
import database.UserDB;
import model.FYPCoordinator;
import model.Project;
import model.Request;
import model.Request.RequestDesc;
import model.Request.RequestStatus;
import model.RequestTitleChange;
import model.RequestTransferStudent;
import model.Student;
import model.Supervisor;
import model.User;
import model.Project.ProjectStatus;

public class SupervisorController extends UserController{
	
	Supervisor supervisor;
	public SupervisorController(Supervisor user) {
		this.supervisor=user;
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
			System.out.println("\u001B[1mRequest\u001B[0m");
			System.out.println("5. View/Approve/Reject PENDING requests");
			System.out.println("6. Request to transfer student");
			System.out.println("7. View my request history");
			System.out.println("8. Exit");
			
			System.out.print("\nEnter your choice: ");
			choice = sc.nextInt();
			sc.nextLine(); //consume the new line character if the previous input was an integer
			System.out.println();
			switch(choice)
			{
			case 1:
				PasswordManager.changePassword(supervisor);
				start();
				break;
			case 2:
				//Create new project
				ArrayList <Project> al = new ArrayList<>();
				Project project = supervisorCreateProjectView();
				al.add(project);
				if (project!=null) {
					System.out.println("\nProject created successfully. ");
					supervisorProjectMenu(al);
				}
				else {
					System.out.println("Error occurred. Please try again. ");
				}
				break;
			case 3:
				//Modify project title
				//To display the project information again after modifying
				ArrayList <Project> alr = new ArrayList<>();
				Project proj = supervisorModifyProjectTitleView();
				alr.add(proj);
				System.out.println("\nProject title modified successfully.\n");
				supervisorProjectMenu(alr);
				break;
			case 4:
				//View submitted projects
				supervisorProjectMenu(supervisor.getProjects());
				break;
			case 5:
				//View/Approve/Reject pending requests
				supervisorPendingRequestView();
				break;
			case 6: 
				//Request to transfer student
				if(!supervisorAssignedProjectMenu(supervisor.getProjects()))
					break;
				Request req = supervisorTransferStudentMenu();
				supervisorRequestInformation(req);
				System.out.println("Request created successfully. ");
				break;
			case 7:
				//View my request history
				supervisorRequestMenu();
				break;
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
	
	public void supervisorProjectMenu(ArrayList<Project> proj) {
		
		System.out.println("\u001B[1m\u001B[4mProject Information\u001B[0m");
		
	    if (proj == null || proj.isEmpty()) {
	        System.out.println("You do not have any projects.");
	    }
		//ArrayList<Project> proj = projDB.findProjectBysupervisorID(supervisor.getUserID());
		for (Project project : proj) {
			System.out.println("Project ID: " + project.getProjectID());
			System.out.println("Project Title: " +project.getProjectTitle());
			System.out.println("Supervisor Name: " + project.getSupervisor().getName());
			System.out.println("Student Name: " + project.getStudentID());
			System.out.println("Project Status: "+ project.getProjectStatus());
			System.out.println();
		}
	}
	
	public boolean supervisorAssignedProjectMenu(ArrayList<Project> proj) {
		
		System.out.println("\u001B[1m\u001B[4mProject Information\u001B[0m");
		if (proj == null || proj.isEmpty()) {
	        System.out.println("You do not have any projects.");
	        return false;
	    }
		ArrayList<Project> projList = new ArrayList<>();
		for (Project project : proj) {
			//even the projects with student assigned is null
			if (project.getStudent()!=null)
				projList.add(project);
		}
		if (projList == null || projList.isEmpty()) {
	        System.out.println("You do not have any projects assigned to a student.");
	        return false;
	    }
		for (Project project : projList) {
			System.out.println("Project ID: " + project.getProjectID());
			System.out.println("Project Title: " +project.getProjectTitle());
			System.out.println("Supervisor Name: " + project.getSupervisor().getName());
			System.out.println("Student Name: " + project.getStudentID());
			System.out.println("Project Status: "+ project.getProjectStatus());
			System.out.println();
		}
		return true;
	}
	public Request supervisorTransferStudentMenu() {
		Request request = null;	
		int projID;
		String newSupID;
		Supervisor newSup;
		User fypCoord= UserDB.findUserByID(fypCoordinator); 
		do {
			System.out.print("Enter the project ID to transfer your student: ");
			projID = sc.nextInt();
			if(ProjectDB.findProjectByID(projID)==null || !((ProjectDB.findProjectByID(projID).getSupervisorID()).equals(supervisor.getUserID())))
				System.out.println("Please enter a valid Project ID. \n");
		}while (ProjectDB.findProjectByID(projID)==null || !((ProjectDB.findProjectByID(projID).getSupervisorID()).equals(supervisor.getUserID())));
		do {
			System.out.print("Enter the replacement Supervisor ID: ");
			newSupID=sc.next().toUpperCase();
			newSup = SupervisorDB.findUserByID(newSupID);
			if (newSup==null)
				System.out.println("Please enter a valid replacement Supervisor ID. \n");
		}while(newSup==null);
		
		try {
			//RequestDesc reqDesc, int projectID, User receiver, String newTitle, User newSupervisor
			request = supervisor.createRequest(RequestDesc.TRANSFER_STUDENT, projID,fypCoord, null, newSup);
		} catch (IOException e) {
			System.out.println("Error. Request File not found. ");
		}
		return request;
	}

	public Project supervisorCreateProjectView() {
		if(supervisor.getNumSupervised()>=2) {
			System.out.println("Sorry! You are already supervising 2 projects.");
			return null;
		}
		System.out.print("Enter your project title: ");
		String projectTitle = sc.nextLine();
		Project project=null;
		try {
			project = supervisor.createProject(projectTitle);
			//((Supervisor) user).addProject(project);
            //project.setSupervisor((Supervisor) user);
		} catch (IOException e) {
			System.out.println("Error. Project file not found.");
		}
		return project;
	}
	public Project supervisorModifyProjectTitleView() {
		if (supervisor.getProjects()==null) {
			System.out.println("You do not have projects. ");
			return null;
		}
		int projID;
		String newTitle;
		supervisorProjectMenu(supervisor.getProjects());
		Project project=null;
		do {
			System.out.print("Enter the project ID: ");
			projID = sc.nextInt();
			System.out.print("Enter the new project title: ");
			sc.nextLine(); // Consume the newline character
		    newTitle = sc.nextLine(); // Read the entire line of input
			try {
				project = supervisor.modifyProjectTitle(projID, newTitle);
			} catch (IOException e) {
				System.out.println("Error occurred. ");
			}
			if (project!=null) {
				break;
			}
			else {
				System.out.println("Please enter a valid project ID! ");
			}
		}while (project==null);
		
		return project;
	}
	
	public void supervisorRequestMenu() {
		
		System.out.println("\u001B[1m\u001B[4mRequest History\u001B[0m");
		ArrayList<Request> req = this.supervisor.getRequests();
		if (req == null || req.isEmpty()) {
			System.out.println("You do not have any request history. ");
			System.out.println();
		}
		else {
			
			//requestID|requestDate|projectID|senderID|receiverID|requestDesc|requestStatus|
			for (Request request : req) {
				supervisorRequestInformation(request);
			}
		}
	}
	
	public void supervisorRequestInformation(Request request) {
		if (request!=null) {
			System.out.println("\u001B[1m\u001B[4mRequest Information\u001B[0m");
			System.out.println("Request ID: " + request.getRequestID());
			System.out.println("Request Description: " +request.getRequestDesc());
			String senderName = UserDB.findUserByID(request.getSenderID()).getName();
			System.out.println("Sender Name: " + senderName);
			String receiverName = UserDB.findUserByID(request.getReceiverID()).getName();
			System.out.println("Receiver Name: " + receiverName);
			System.out.println("Project ID: " + request.getProjectID());
			System.out.println("Project Title: " + ProjectDB.findProjectByID(request.getProjectID()).getProjectTitle());
			if(request.getRequestDesc()==Request.RequestDesc.CHANGE_TITLE) {
				System.out.println("Requested New Title: " + ((RequestTitleChange) request).getNewTitle());
			}
			if (request.getRequestDesc()==Request.RequestDesc.TRANSFER_STUDENT) {
				System.out.println("Replacement Supervisor ID: "+ ((RequestTransferStudent) request).getNewSupervisorID());
			}
			System.out.println("Request Status: "+ request.getRequestStatus());
			System.out.println();
		}
		else {
			return;
		}
	}
	
	public void supervisorPendingRequestView() {
		int numRequests=0;
		//the request status is still pending in this attribute of getRequests
		for (Request req: supervisor.getRequests()) {
			if (req.getRequestStatus()==RequestStatus.PENDING && !req.getSenderID().equals(supervisor.getUserID())) {
				supervisorRequestInformation(req);
				numRequests++;
			}
		}
		if(numRequests==0) {
			System.out.println("You do not have any PENDING requests. ");
			return;
		}
		String input=null;
		int reqID;
		Request req=null;
		do {
			System.out.println("Press Enter to go back. ");
			System.out.print("Enter the Request ID you would like to Approve/Reject: ");
			
			input=sc.nextLine();
			if(input.isEmpty() || input=="\n") {
				break;
			}
			reqID = Integer.parseInt(input);
			//instance of req in reqDB created
			req = RequestDB.findRequestByReqID(reqID);
			if (req==null) {
				System.out.println("Invalid Request ID. Please try again. \n");
			}
			else {
				char input1 = 0;
				do {
					System.out.print("Enter 'A' to Approve/ 'R' to Reject: ");
					input1=sc.next().charAt(0);
					sc.nextLine();
					if (input1=='A') {
						if (req.approve(supervisor)) {
							System.out.println("Request approve successfully.");
							System.out.println();
						}
						break;
					}
					else if (input1=='R') {
						if(req.reject(supervisor)) {
							System.out.println("Request rejected successfully. ");
						}
						break;
					}
					else {
						System.out.println("Invalid input. Please only enter 'A' or 'R'. ");
					}
				}while(input1!='A'||input1!='R');
				break;
			}
		}while (!input.isEmpty() || req!=null);
		
	}
}
