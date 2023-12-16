package model;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import database.ProjectDB;
import database.RequestDB;
import model.Project.ProjectStatus;
import model.Request.RequestDesc;
import model.Request.RequestStatus;

public class User {

	private String userID;
	private String password;
	private String name;
	private String email;
	private UserType UserType;
	private ArrayList<Request> requests;
	public enum UserType {
		STUDENT,
		SUPERVISOR,
		FYPCOORDINATOR;
	}
	
	public User(String userID, String password, String name, String email, UserType userType2) {
		this.userID= userID;
		this.password=password;
		this.name=name;
		this.email=email;
		this.UserType=userType2;
	}
	public User(String userID, String name, String email, ArrayList<Request> requests) {
		this.userID= userID;
		this.name=name;
		this.email=email;
		this.requests=requests;
	}
	public String getUserID() {
		return userID;
	}

	public String getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public UserType getUserType() {
		return UserType;
	}
	
	public void setPassword(String password) {
        this.password = password;
    }

	public void addRequest(Request request) {
        requests.add(request);
    }
	public void setRequests(ArrayList<Request> requests) {
		this.requests = requests;
	}
	public ArrayList<Request> getRequests() {
		return requests;
	}
	
	public Request createRequest(Request.RequestDesc reqDesc, int projectID, User receiver, String newTitle, User newSupervisor) throws IOException{
		return null;
	}
	
}
