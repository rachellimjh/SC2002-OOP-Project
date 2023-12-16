package database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import model.FYPCoordinator;
import model.Project;
import model.Request;
import model.Student;
import model.Supervisor;
import model.User;

public class SupervisorDB extends DataAccess{
	public static String fileName = "./src/database/SupervisorList.txt";
	
	private static ArrayList<Supervisor> supervisorList;
	public final static String fypCoordinator = "ASFLI";
	
	public SupervisorDB() {
        supervisorList = readFile(fileName);
    }

	public static ArrayList<Supervisor> readFile(String fileName){
		ArrayList stringArray = null;
		try {
			stringArray = (ArrayList)read(fileName);
		} catch (IOException e) {
			System.out.println("Error in reading Supervisor File. ");
		}    
		ArrayList alr = new ArrayList() ;// to store Students data    

		 for (int i = 0 ; i < stringArray.size() ; i++) {   
			String st = (String)stringArray.get(i); 
            StringTokenizer tokenizer = new StringTokenizer(st, SEPARATOR);
            String userID = tokenizer.nextToken().trim();
            String name = tokenizer.nextToken().trim();
            String email = tokenizer.nextToken().trim();
            int numSupervised = Integer.parseInt(tokenizer.nextToken().trim());
            ArrayList<Project> projects = new ArrayList<>();
            ArrayList<Request> requests = new ArrayList<>();
            FYPCoordinator fypCoor = null;
            if (userID.equals(fypCoordinator)) {
            	fypCoor = new FYPCoordinator(userID, name, email, numSupervised, projects, requests);
            	alr.add(fypCoor);
            	continue;
            }
            Supervisor supervisor = new Supervisor(userID, name, email, numSupervised, projects, requests);
            alr.add(supervisor);
        }

        return alr;
    }

	public static boolean saveFile(String fileName, List al){
		List alw = new ArrayList() ;// to store Students data

		  for (int i = 0 ; i < al.size() ; i++) {
					Supervisor supervisor = (Supervisor)al.get(i);
					StringBuilder st =  new StringBuilder() ;
					st.append(supervisor.getUserID().trim());
					st.append(SEPARATOR);
					st.append(supervisor.getName().trim());
					st.append(SEPARATOR);
					st.append(supervisor.getEmail().trim());
					st.append(SEPARATOR); 
					st.append(Integer.toString(supervisor.getNumSupervised()));
					st.append(SEPARATOR); 
					alw.add(st.toString()) ;
			} 
			try {
				write(fileName,alw);
			} catch (IOException e) {
				System.out.println("Error in saving Supervisor File. ");
				return false;
			}
		return true;
	}

	public static Supervisor findUserByID(String userID) {
		//supervisorList = readFile(SupervisorDB.fileName);
		ArrayList<Supervisor> supervisorList = readFile(fileName);
		for (Supervisor supervisor : supervisorList) {
            if (supervisor.getUserID().equals(userID)) {
                return supervisor; // Found the user with the matching userID
            }
        }
        return null; // User not found
    }

	
	public static boolean saveSupervisor(Supervisor supervisor) {
		// Find the user in the userList and update its data
        ArrayList<Supervisor> supervisorList = readFile(SupervisorDB.fileName);
        
        for (int i = 0; i < supervisorList.size(); i++) {
            if (supervisorList.get(i).getUserID().equals(supervisor.getUserID())) {
            	supervisorList.set(i, supervisor);
                if (saveFile(SupervisorDB.fileName, supervisorList)) {
                    return true;
                }
            }
        }
        return false;
    }
}
