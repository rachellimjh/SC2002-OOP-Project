package model;

import database.ProjectDB;
import database.RequestDB;
import database.StudentDB;
import database.SupervisorDB;
import model.Project.ProjectStatus;
import model.Request.RequestStatus;

public class RequestDeregisterFYP extends Request {
    public RequestDeregisterFYP(int requestID, String requestTime, int projectID, User sender, User receiver, RequestStatus reqStatus) {
        super(requestID, requestTime, projectID, sender, receiver, RequestDesc.DEREGISTER_FYP, reqStatus);
    }

	@Override
	public boolean approve(Supervisor supervisor) {
		//1. Update Project Status, Project Student
		////2. Update Student assigned
		////3. Update Supervisor numSupervised
		////4. Update Request Status
		
		this.setRequestStatus(RequestStatus.APPROVED);
		
		Project proj = ProjectDB.findProjectByID(getProjectID());
		proj.setProjectStatus(ProjectStatus.AVAILABLE);
		proj.setStudentID("empty");
		proj.setSupervisorID(getReceiverID());
		
		Student student = StudentDB.findUserByID(getSenderID());
		student.deregister();
		student.setProject(null);
		
		Supervisor sup = SupervisorDB.findUserByID(proj.getSupervisorID());
		sup.setNumSupervised(sup.getNumSupervised()-1);
		supervisor.updateRequests(this);
		
		if (ProjectDB.saveProject(proj) && StudentDB.saveStudent(student) && SupervisorDB.saveSupervisor(sup) && SupervisorDB.saveSupervisor(supervisor)&& RequestDB.saveRequest(this)) {
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
