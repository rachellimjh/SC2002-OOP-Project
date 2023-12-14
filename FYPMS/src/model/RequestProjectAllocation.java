package model;

import java.io.IOException;
import java.util.ArrayList;

import database.ProjectDB;
import database.RequestDB;
import database.StudentDB;
import database.SupervisorDB;
import model.Project.ProjectStatus;
import model.Request.RequestStatus;

public class RequestProjectAllocation extends Request{

	public RequestProjectAllocation(int requestID, String requestTime, int projectID, User sender, User receiver, RequestStatus requestStatus) {
		super(requestID, requestTime, projectID, sender, receiver, RequestDesc.ALLOCATE_PROJECT, requestStatus);
	}
	
	@Override
	public boolean approve(Supervisor supervisor){
		//assign student to the project
		//1. Update Project Status, Project Student
		////2. Update Student assigned
		////3. Update Supervisor numSupervised
		////4. Update Request Status
		
		this.setRequestStatus(RequestStatus.APPROVED);
		
		Project proj = ProjectDB.findProjectByID(getProjectID());
		proj.setProjectStatus(ProjectStatus.ALLOCATED);
		proj.setStudentID(getSenderID());
		proj.setSupervisorID(getReceiverID());
		
		Student student = StudentDB.findUserByID(getSenderID());
		student.setProject(proj);
		
		supervisor.setNumSupervised(supervisor.getNumSupervised()+1);
		supervisor.updateProjects(getProjectID()-1, proj);
		supervisor.updateRequests(this);
		
		if (supervisor.getNumSupervised()==2) {
			ArrayList<Project> projs = supervisor.getProjects();
			for (Project project:projs) {
				if (project.getProjectStatus()==ProjectStatus.AVAILABLE || project.getProjectStatus()==ProjectStatus.RESERVED) {
					project.setProjectStatus(ProjectStatus.UNAVAILABLE);
					ProjectDB.saveProject(project);
				}
			}
		}
		if (ProjectDB.saveProject(proj) && StudentDB.saveStudent(student) && SupervisorDB.saveSupervisor(supervisor) && RequestDB.saveRequest(this)) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean reject(Supervisor supervisor){
		this.setRequestStatus(RequestStatus.REJECTED);
		
		Project proj = ProjectDB.findProjectByID(getProjectID());
		proj.setProjectStatus(ProjectStatus.AVAILABLE);
		
		supervisor.updateRequests(this);
		
		if (ProjectDB.saveProject(proj) && SupervisorDB.saveSupervisor(supervisor) && RequestDB.saveRequest(this)) {
			return true;
		}
		return false;
	}
}
