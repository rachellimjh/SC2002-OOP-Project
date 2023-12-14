package model;

import java.util.ArrayList;

import database.ProjectDB;
import database.RequestDB;
import database.StudentDB;
import database.SupervisorDB;
import model.Project.ProjectStatus;
import model.Request.RequestStatus;

public class RequestTransferStudent extends Request {
    private User newSupervisor;
    private String newSupervisorID; 
    
    public RequestTransferStudent(int requestID, String requestTime, int projectID, User sender, User receiver, User newSupervisor, RequestStatus reqStatus) {
        super(requestID, requestTime, projectID, sender, receiver, RequestDesc.TRANSFER_STUDENT, reqStatus);
        this.newSupervisor = newSupervisor;
    }

    public User getNewSupervisor() {
        return newSupervisor;
    }

	public String getNewSupervisorID() {
		return newSupervisorID;
	}

	public void setNewSupervisorID(String newSupervisorID) {
		this.newSupervisorID = newSupervisorID;
	}

	public void setNewSupervisor(User newSupervisor) {
		this.newSupervisor = newSupervisor;
	}

	@Override
	public boolean approve(Supervisor supervisor) {
		//transfer student to another supervisor
		//1. Update Project Supervisor
		////3. Update Old supervisor numSupervised, update New supervisor numSupervised
		////4. Update Request Status
		
		this.setRequestStatus(RequestStatus.APPROVED);
		
		Project proj = ProjectDB.findProjectByID(getProjectID());
		proj.setSupervisorID(getNewSupervisorID());
		
		Student student = StudentDB.findUserByID(getSenderID());
		student.setProject(proj);
		
		Supervisor newSupervisor = SupervisorDB.findUserByID(newSupervisorID);
		supervisor.setNumSupervised(supervisor.getNumSupervised()-1);
		newSupervisor.setNumSupervised(newSupervisor.getNumSupervised()+1);
		newSupervisor.updateProjects(getProjectID()-1, proj);
		supervisor.updateRequests(this);
		
		if (ProjectDB.saveProject(proj) && StudentDB.saveStudent(student) && SupervisorDB.saveSupervisor(supervisor) && SupervisorDB.saveSupervisor(newSupervisor) && RequestDB.saveRequest(this)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean reject(Supervisor supervisor) {
		this.setRequestStatus(RequestStatus.REJECTED);
		
		supervisor.updateRequests(this);
		
		if (SupervisorDB.saveSupervisor(supervisor) && RequestDB.saveRequest(this)) {
			return true;
		}
		return false;
	}
}

