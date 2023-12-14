package view;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import database.ProjectDB;
import database.RequestDB;
import database.StudentDB;
import database.SupervisorDB;
import database.UserDB;
import model.FYPCoordinator;
import model.Project;
import model.Request;
import model.Student;
import model.Supervisor;
import model.User;
import model.User.UserType;
import view.FYPCoordinatorController;
import view.StudentController;
import view.SupervisorController;
import view.UserController;

public class UserController {
	
	public int choice;
	public final static String fypCoordinator = "ASFLI";
	UserController account = null;
	Scanner sc = new Scanner(System.in);
	
	public User login() {
		String userID = null, password=null;
		User loginUser=null;
		User user=null;
		
		UserDB users = new UserDB();
		do{
			try {
		        System.out.print("UserID: ");
		        userID = sc.next().toUpperCase();
		        System.out.print("Password: ");
		        password = sc.next();
		        System.out.println();
		        loginUser = users.authenticateUser(userID, password);
		        
		        if (loginUser == null) {
		            System.out.println("Invalid userID or password. Please try again.");
		        }
		    } catch (NoSuchElementException e) {
		        // Handle any potential input errors or exceptions
		        System.out.println("An error occurred while reading input. Please try again.");
		        sc.nextLine(); // Clear the scanner's buffer
		    }
			
		}while (loginUser==null);
		
		return loginUser;
	}
	
	public void headerView () {
		System.out.println("\u001B[1m\u001B[4m\u001B[34mFYP Management System\u001B[0m");
	}
	//contract such that all the subclasses must provide implementation for these views
	public void mainMenu() {
		System.out.println("This is a generic user view");
	};
	
	 public void start() {
		ProjectDB projDB = new ProjectDB(); 
		RequestDB reqDB = new RequestDB();
		User user = login();
			if (user.getUserType()==UserType.STUDENT) {
				StudentDB students= new StudentDB();
				user=students.findUserByID(user.getUserID());
				account = new StudentController((Student) user);
				Project project = projDB.findProjectByStudentID(user.getUserID());
				ArrayList<Request> requestsSent = reqDB.findRequestBySenderID(user.getUserID());
				ArrayList<Request> requestsReceived = reqDB.findRequestByReceiverID(user.getUserID());
				if (project!=null) {
					//establishing the association between student and project
					((Student) user).setProject(project);
	                project.setStudent((Student) user);
				}
				if (requestsSent !=null) {
					for (Request request : requestsSent) {
						((Student) user).addRequest(request);
		                request.setSender(user);
					}
				}
				if (requestsReceived !=null) {
					for (Request request : requestsReceived) {
						((Student) user).addRequest(request);
		                request.setReceiver(user);
					}
				}
				
			}
			else {
				SupervisorDB supervisors = new SupervisorDB();
				if(user.getUserType()==UserType.SUPERVISOR) {
					user=supervisors.findUserByID(user.getUserID());
					account = new SupervisorController((Supervisor) user);
					ArrayList<Project> projects = projDB.findProjectBySupervisorID(user.getUserID());
					ArrayList<Request> requestsSent = reqDB.findRequestBySenderID(user.getUserID());
					ArrayList<Request> requestsReceived = reqDB.findRequestByReceiverID(user.getUserID());
					if (projects!=null) {
						for (Project project : projects) {
							//establishing the association between supervisor and project
							((Supervisor) user).addProject(project);
			                project.setSupervisor((Supervisor) user);
			                ProjectDB.saveProject(project);
			            }
					}
					
					if (requestsSent !=null) {
						for (Request request : requestsSent) {
							((Supervisor) user).addRequest(request);
			                request.setSender(user);
			                request.setReceiver(request.getReceiver());
			                //RequestDB.saveRequest(request);
						}
					}
					if (requestsReceived !=null) {
						for (Request request : requestsReceived) {
							((Supervisor) user).addRequest(request);
							request.setSender(request.getSender());
			                request.setReceiver(user);
			                //RequestDB.saveRequest(request);
						}
					}
					
				}
				if(user.getUserType()==UserType.FYPCOORDINATOR) {
					user=supervisors.findUserByID(user.getUserID());
					account = new FYPCoordinatorController((FYPCoordinator)user); 
					ArrayList<Project> projects = projDB.findProjectBySupervisorID(user.getUserID());
					ArrayList<Request> requestsSent = reqDB.findRequestBySenderID(user.getUserID());
					ArrayList<Request> requestsReceived = reqDB.findRequestByReceiverID(user.getUserID());
					if (projects!=null) {
						for (Project project : projects) {
							//establishing the association between supervisor and project
							((Supervisor) user).addProject(project);
			                project.setSupervisor((Supervisor) user);
			                ProjectDB.saveProject(project);
			            }
					}
					
					if (requestsSent !=null) {
						for (Request request : requestsSent) {
							((Supervisor) user).addRequest(request);
			                request.setSender(user);
			                request.setReceiver(request.getReceiver());
			                //RequestDB.saveRequest(request);
						}
					}
					if (requestsReceived !=null) {
						for (Request request : requestsReceived) {
							((Supervisor) user).addRequest(request);
							request.setSender(request.getSender());
			                request.setReceiver(user);
			                //RequestDB.saveRequest(request);
						}
					}
				}
			}
		account.mainMenu();
	   }

}
