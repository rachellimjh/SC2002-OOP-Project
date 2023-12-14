package database;

import java.io.IOException;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import model.Project;
import model.Request;
import model.Request.RequestDesc;
import model.RequestDeregisterFYP;
import model.RequestProjectAllocation;
import model.RequestTitleChange;
import model.RequestTransferStudent;
import model.Student;
import model.User;

public class RequestDB extends DataAccess{

	private static ArrayList<Request> requestList = new ArrayList<>();
	public static final String fileName = "./src/database/RequestList.txt";
	
	public RequestDB() {
        requestList = readFile(fileName);
    }
	
	public static ArrayList<Request> readFile(String fileName){
		// read String from text file  
		ArrayList stringArray = null;
		try {
			stringArray = (ArrayList)read(fileName);
		} catch (IOException e) {
			System.out.println("Error in reading Request File. ");
		}    
		ArrayList alr = new ArrayList() ;// to store Requests data    
 
        for (int i = 0 ; i < stringArray.size() ; i++) {   
				String st = (String)stringArray.get(i); 
				// get individual 'fields' of the string separated by SEPARATOR
				StringTokenizer star = new StringTokenizer(st , SEPARATOR);	// pass in the string to the string tokenizer using delimiter ","
				
				int requestID = Integer.parseInt(star.nextToken().trim());
				//date
				String timestamp = star.nextToken().trim();
				int projectID = Integer.parseInt(star.nextToken().trim());
				String senderID = star.nextToken().trim();
				String receiverID = star.nextToken().trim();
				User sender =UserDB.findUserByID(senderID);
				User receiver = UserDB.findUserByID(receiverID);
				String requestDesc = star.nextToken().trim(); 
				Request.RequestDesc reqDesc = Request.RequestDesc.valueOf(requestDesc);
				String requestStatus = star.nextToken().trim(); 
				Request.RequestStatus reqStatus = Request.RequestStatus.valueOf(requestStatus);
				String newTitleOrSupId = star.nextToken().trim();
				String newTitle = null;
				String newSupervisorID = null;
				User newSup=null;
				if (newTitleOrSupId!="empty") {
					if (reqDesc == RequestDesc.CHANGE_TITLE) {
						newTitle = newTitleOrSupId;
					}
				    if (reqDesc == RequestDesc.TRANSFER_STUDENT) {
				    	newSupervisorID = newTitleOrSupId;
				    	newSup = UserDB.findUserByID(newSupervisorID);
				    }
				}
				Request request;
		        switch (reqDesc) {
		            case ALLOCATE_PROJECT:
		                request = new RequestProjectAllocation(requestID, timestamp, projectID, sender, receiver, reqStatus);
		                request.setSenderID(senderID);
		                request.setReceiverID(receiverID);
		                break;
		            case CHANGE_TITLE:
		                request = new RequestTitleChange(requestID, timestamp, projectID, sender, receiver, newTitle, reqStatus);
		                request.setSenderID(senderID);
		                request.setReceiverID(receiverID);
		                break;
		            case TRANSFER_STUDENT:
		                request = new RequestTransferStudent(requestID, timestamp, projectID, sender, receiver, newSup, reqStatus);
		                request.setSenderID(senderID);
		                request.setReceiverID(receiverID);
		                ((RequestTransferStudent) request).setNewSupervisorID(newSupervisorID);
		                ((RequestTransferStudent) request).setNewSupervisor(newSup);
		                break;
		            case DEREGISTER_FYP:
		                request = new RequestDeregisterFYP(requestID, timestamp, projectID, sender, receiver, reqStatus);
		                request.setSenderID(senderID);
		                request.setReceiverID(receiverID);
		                break;
		            default:
		                System.out.println("Unknown Request Type!");
		                request = null;
		                break;
		        }

	            alr.add(request);
			}
		return alr ;
	}

	public static boolean saveFile(String fileName, List al){
		List alw = new ArrayList() ;// to store Students data

		for (int i = 0 ; i < al.size() ; i++) {
				Request request = (Request)al.get(i);
				StringBuilder st =  new StringBuilder() ;
				//requestID|requestDate|projectID|senderID|receiverID|requestDesc|requestStatus|newTitleOrSupId
				
				st.append(Integer.toString(request.getRequestID()));
				st.append(SEPARATOR);
				st.append(request.getRequestTime().trim());
				st.append(SEPARATOR);
				st.append(Integer.toString(request.getProjectID()));
				st.append(SEPARATOR);
				st.append(request.getSenderID().trim());
				st.append(SEPARATOR); 
				st.append(request.getReceiverID().trim());
				st.append(SEPARATOR); 
				st.append(request.getRequestDesc().toString().trim());
				st.append(SEPARATOR);
				st.append(request.getRequestStatus().toString().trim());
				st.append(SEPARATOR);
				if (request.getRequestDesc()==Request.RequestDesc.CHANGE_TITLE) 
					st.append(((RequestTitleChange) request).getNewTitle().toString().trim());
				else if (request.getRequestDesc()==Request.RequestDesc.TRANSFER_STUDENT)
					st.append(((RequestTransferStudent) request).getNewSupervisorID().toString().trim());
				else
					st.append("empty");
				st.append(SEPARATOR);
				alw.add(st.toString()) ;
      	} 
		try {
			write(fileName,alw);
		} catch (IOException e) {
			System.out.println("Error in saving Request File. ");
			return false; 
		}
		return true;
	}
	
	public static Request findRequestByReqID(int requestID) {
		//ArrayList<Request> requestList = readFile(fileName);
        for (Request request : requestList) {
            if (request.getRequestID()==requestID) {
                return request; // Found the request with the matching reqID
            }
        }
        return null; // Request not found
    }

	public static ArrayList<Request> findRequestBySender(User sender) {
		ArrayList<Request> requestsBySender = new ArrayList<>();
		ArrayList<Request> requestList = readFile(RequestDB.fileName);
        for (Request request : requestList) {
            if (request.getSenderID()==sender.getUserID()) {
            	requestsBySender.add(request); // Found the request with the matching reqID
            }
        }
		return requestsBySender;
    }
	
	public static ArrayList<Request> findRequestByReceiver(User receiver) {
		ArrayList<Request> requestsByReceiver = new ArrayList<>();
        for (Request request : requestList) {
            if (request.getReceiver()==receiver) {
            	requestsByReceiver.add(request); // Found the request with the matching reqID
            }
        }
		return requestsByReceiver;
    }

	public static int getNextRequestID() {
		//ArrayList<Request> requestList = readFile(fileName);
		if (requestList.isEmpty()) {
			return 1;
		}
		return requestList.size()+1;
	}
	
	public static String generateTimestamp() {
	    Date date = new Date();
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	    return dateFormat.format(date);
	}
	
	public static boolean saveRequest(Request request) {
        // Find the user in the userList and update its data
		ArrayList<Request> requestList = readFile(RequestDB.fileName);
		requestList.set(request.getRequestID()-1, request);
        
        if(saveFile(fileName, requestList)) {
        	return true;
        }
        return false;
    }

	public static ArrayList<Request> findRequestBySenderID(String userID) {
		ArrayList<Request> reqsBySender = new ArrayList<>();
		ArrayList<Request> reqList = readFile(RequestDB.fileName);
		
	    for (Request req : reqList) {
	        if (req.getSenderID().equals(userID)) {
	        	reqsBySender.add(req); // Add projects supervised by the given supervisorID
	        }
	    }

	    return reqsBySender;
	}
	
	public static ArrayList<Request> findRequestByReceiverID(String userID) {
		ArrayList<Request> reqsByReceiver = new ArrayList<>();
		ArrayList<Request> reqList = readFile(RequestDB.fileName);
		
	    for (Request req : reqList) {
	        if (req.getReceiverID().equals(userID)) {
	        	reqsByReceiver.add(req); // Add projects supervised by the given supervisorID
	        }
	    }

	    return reqsByReceiver;
	}
}
