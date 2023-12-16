package model;

import java.util.Date;

import database.RequestDB;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public abstract class Request {

	public enum RequestDesc{
		ALLOCATE_PROJECT,
		CHANGE_TITLE,
		TRANSFER_STUDENT,
		DEREGISTER_FYP
	}
	public enum RequestStatus{
		APPROVED,
		REJECTED,
		PENDING
	}
	
	RequestDB reqDB;
	
	private int requestID;
    private int projectID;
    private User sender;
    private String senderID;
    private String receiverID;
    private User receiver;
    private RequestDesc requestDesc;
    private String requestTime;
    private RequestStatus requestStatus;
    
    public Request(int requestID, String requestTime, int projectID, User sender, User receiver, RequestDesc requestDesc, RequestStatus requestStatus) {
        this.requestID = requestID;
        this.projectID = projectID;
        this.sender = sender;
        this.receiver = receiver;
        this.requestDesc = requestDesc;
        this.requestTime = requestTime;
        this.requestStatus = requestStatus;
    }

	
	public RequestDB getReqDB() {
		return reqDB;
	}


	public void setReqDB(RequestDB reqDB) {
		this.reqDB = reqDB;
	}


	public int getRequestID() {
		return requestID;
	}


	public void setRequestID(int requestID) {
		this.requestID = requestID;
	}


	public int getProjectID() {
		return projectID;
	}


	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}


	public User getSender() {
		return sender;
	}


	public void setSender(User sender) {
		this.sender = sender;
	}


	public String getSenderID() {
		return senderID;
	}


	public void setSenderID(String senderID) {
		this.senderID = senderID;
	}


	public String getReceiverID() {
		return receiverID;
	}


	public void setReceiverID(String receiverID) {
		this.receiverID = receiverID;
	}


	public User getReceiver() {
		return receiver;
	}


	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}


	public RequestDesc getRequestDesc() {
		return requestDesc;
	}


	public void setRequestDesc(RequestDesc requestDesc) {
		this.requestDesc = requestDesc;
	}


	public String getRequestTime() {
		return requestTime;
	}


	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}


	public RequestStatus getRequestStatus() {
		return requestStatus;
	}


	public void setRequestStatus(RequestStatus requestStatus) {
		this.requestStatus = requestStatus;
	}

	public abstract boolean approve(Supervisor supervisor);
	public abstract boolean reject(Supervisor supervisor);
}
