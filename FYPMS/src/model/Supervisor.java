package model;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;

import database.ProjectDB;
import database.RequestDB;
import model.Project.ProjectStatus;
import model.Request.RequestDesc;
import model.Request.RequestStatus;

public class Supervisor extends User{

	private int numSupervised;
    private ArrayList<Project> projects;
    private ArrayList<Request> requests;
    
    public Supervisor(String userID, String name, String email, int numSupervised, ArrayList<Project> projects, ArrayList<Request> requests) {
        super(userID, name, email, requests);
        this.numSupervised=numSupervised;
        this.projects = projects;
        this.requests=requests;
    }
    
	public int getNumSupervised() {
		return numSupervised;
	}
	
	public ArrayList<Project> getProjects() {
		return projects;
	}
	
	//increase numSupervised when:
	//1. the request to assign project is approved
	//2. the request to transfer student is approved
	public Project createProject(String projectTitle) throws IOException {
		// read String from text file  
		//projID is increased!
		ArrayList projectList = ProjectDB.readFile(ProjectDB.fileName);
		Project project = new Project(ProjectDB.getNextProjectID(), projectTitle, ProjectStatus.AVAILABLE, this, null);
		//projects.add(project) ;
		//setProjects(projects);
		addProject(project);
		projectList.add(project);
		project.setSupervisorID(getUserID());
		project.setSupervisor(this);
		project.setStudentID("empty");
		project.setStudent(null);
		ProjectDB.saveFile(ProjectDB.fileName, projectList);
		return project;
	}
	
	public void setProjects(ArrayList<Project> projects) {
		this.projects = projects;
	}
	public void addProject(Project project) {
        projects.add(project);
    }
	public void updateProjects(int index, Project project) {
		projects.set(index, project);
	}

    public void removeProject(Project project) {
        projects.remove(project);
    }
	public void setNumSupervised(int numSupervised) {
		this.numSupervised = numSupervised;
	}
	
	public Project modifyProjectTitle(int projectID, String newTitle) throws IOException {
		ArrayList projectList = ProjectDB.readFile(ProjectDB.fileName);
		
		for (Project project : projects) {
			if (project.getProjectID() == projectID) {
				project.setProjectTitle(newTitle);
				projectList.set(projectID-1, project);
				ProjectDB.saveFile(ProjectDB.fileName, projectList);
				return project;
			}
		}
		return null;
	}

	@Override
	public Request createRequest(RequestDesc reqDesc, int projectID, User receiver, String newTitle, User newSupervisor) throws IOException {
		//read String from text file  
		ArrayList requestList = RequestDB.readFile(RequestDB.fileName);
		
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
		String dateString = dateFormat.format(timestamp);
		Request request = null;
		if (reqDesc == RequestDesc.CHANGE_TITLE) {
			request = new RequestTitleChange(RequestDB.getNextRequestID(), dateString, projectID, this, receiver, newTitle, RequestStatus.PENDING);
		}
		else if (reqDesc == RequestDesc.TRANSFER_STUDENT) {
			request = new RequestTransferStudent(RequestDB.getNextRequestID(), dateString, projectID, this, receiver, newSupervisor, RequestStatus.PENDING);
		}
		//requestID|requestDate|projectID|sender|receiver|requestDesc|requestStatus|
		//projects.add(project) ;
		//setProjects(projects);
		addRequest(request);
		request.setSenderID(getUserID());
		request.setSender(this);
		//edit accordingly
		request.setReceiverID(receiver.getUserID());
		request.setReceiver(receiver);
		requestList.add(request);
		RequestDB.saveFile(RequestDB.fileName, requestList);
		return request;
	}
	
	public void updateRequests(Request request) {

        for (Request req:requests) {
        	if(req.getRequestID()==request.getRequestID()) {
        		req.setRequestStatus(request.getRequestStatus());
        		break;
        	}
        }
        setRequests(requests);
       
    }
	
	public void addRequests(Request request) {
        // Add the request to the Supervisor's list of requests
        // This list can be an instance variable in the Supervisor class
        // For example, you can have an ArrayList to store the requests

        // Assuming you have an instance variable named "requests" in the Supervisor class
        this.requests.add(request);
    }
}
