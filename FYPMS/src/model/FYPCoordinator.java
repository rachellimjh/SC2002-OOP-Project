package model;

import java.util.ArrayList;

public class FYPCoordinator extends Supervisor{

	public FYPCoordinator(String userID, String name, String email, int numSupervised, ArrayList<Project> projects, ArrayList<Request> requests) {
		super(userID, name, email, numSupervised, projects, requests);
	}
	
	
	//approve request will call this method
	public boolean transferStudent(int projectID, String newSupID) {
		//if successful
		return true;
	}

}
