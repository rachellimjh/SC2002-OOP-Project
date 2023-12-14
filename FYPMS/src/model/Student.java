package model;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import database.ProjectDB;
import database.RequestDB;
import database.StudentDB;
import database.SupervisorDB;
import model.Project.ProjectStatus;
import model.Request.RequestDesc;
import model.Request.RequestStatus;

public class Student extends User{

	private Project project;
	
	boolean assigned, deregistered;
	public Student(String userID, String name, String email, boolean assigned, boolean deregistered, Project project, ArrayList<Request> requests) {
		super(userID, name, email, requests);
		this.assigned=assigned;
		this.deregistered=deregistered;
		this.project=project;
	}
	public boolean isAssigned() {
		return assigned;
	}
	public boolean isDeregistered() {
		return deregistered;
	}

	
	public Project getProject() {
        return project;
    }
	
	public void setProject(Project project) {
        this.project = project;
        this.assigned=true;
    }
	
	//student is not allowed to select project again later after deregistering. 
	//check this when the student selects "request project allocation"
	//check this when the student selects "view all available project"
	
	public void deregister() {
		this.project=null;
		this.assigned=false;
		this.deregistered=true;
		
	}
	
	@Override
	public Request createRequest(RequestDesc reqDesc, int projectID, User receiver, String newTitle, User newSupervisor) throws IOException {
		//read String from text file  
		//projID is increased!
		ArrayList requestList = RequestDB.readFile(RequestDB.fileName);
		ArrayList projectList = ProjectDB.readFile(ProjectDB.fileName);
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
		String dateString = dateFormat.format(timestamp);
		Request request = null;
		if (reqDesc == RequestDesc.ALLOCATE_PROJECT) {
			request = new RequestProjectAllocation(RequestDB.getNextRequestID(), dateString, projectID, this, receiver, RequestStatus.PENDING);
		}
		else if (reqDesc == RequestDesc.CHANGE_TITLE) {
			request = new RequestTitleChange(RequestDB.getNextRequestID(), dateString, projectID, this, receiver, newTitle, RequestStatus.PENDING);
		}
		else if (reqDesc == RequestDesc.DEREGISTER_FYP) {
			request = new RequestDeregisterFYP(RequestDB.getNextRequestID(), dateString, projectID, this, receiver, RequestStatus.PENDING);
		}
		//requestID|requestDate|projectID|sender|receiver|requestDesc|requestStatus|
		
		
		request.setSenderID(getUserID());
		request.setSender(this);
		//edit accordingly
		request.setReceiverID(receiver.getUserID());
		request.setReceiver(receiver);
		requestList.add(request);
		RequestDB.saveFile(RequestDB.fileName, requestList);
		
		//update the project status to reserved since a request is created
		Project project = ProjectDB.findProjectByID(projectID);
		project.setProjectStatus(ProjectStatus.RESERVED);
		ProjectDB.saveProject(project);
		
		Supervisor supervisor = (Supervisor) receiver;
		supervisor.addRequests(request);
		SupervisorDB.saveSupervisor(supervisor);
		
		addRequest(request);
		StudentDB.saveStudent(this);
		return request;		
		
	}
	
}
