package model;

import database.ProjectDB;
import database.RequestDB;
import database.StudentDB;
import database.SupervisorDB;
import model.Request.RequestStatus;

public class RequestTitleChange extends Request {
    private String newTitle;

    public RequestTitleChange(int requestID, String requestTime, int projectID, User sender, User receiver, String newTitle, RequestStatus requestStatus) {
        super(requestID, requestTime, projectID, sender, receiver, RequestDesc.CHANGE_TITLE, requestStatus);
        this.newTitle = newTitle;
    }


	public String getNewTitle() {
        return newTitle;
    }

	@Override
	public boolean approve(Supervisor supervisor) {
		//change project title
		//1. Update Project Title
		//2. Update Request Status
		
		this.setRequestStatus(RequestStatus.APPROVED);
		
		Project proj = ProjectDB.findProjectByID(getProjectID());
		proj.setProjectTitle(newTitle);
		
		supervisor.updateRequests(this);
		
		if (ProjectDB.saveProject(proj)&& SupervisorDB.saveSupervisor(supervisor) && RequestDB.saveRequest(this)) {
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

