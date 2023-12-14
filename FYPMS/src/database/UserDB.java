package database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import model.Student;
import model.User;

public class UserDB extends DataAccess{

	public static String fileName = "./src/database/UserList.txt";
	private static ArrayList<User> userList;

    public UserDB() {
        userList = readFile(fileName);
    }
    
	public static ArrayList readFile(String fileName){
		// read String from text file  
		ArrayList stringArray = null;
		try {
			stringArray = (ArrayList)read(fileName);
		} catch (IOException e) {
			System.out.println("Error in reading User File. ");
		}    
		ArrayList alr = new ArrayList() ;// to store Students data    
 
        for (int i = 0 ; i < stringArray.size() ; i++) {   
				String st = (String)stringArray.get(i); 
				// get individual 'fields' of the string separated by SEPARATOR
				StringTokenizer star = new StringTokenizer(st , SEPARATOR);	// pass in the string to the string tokenizer using delimiter ","

				String userID = star.nextToken().trim();
				String password = star.nextToken().trim();
				String name = star.nextToken().trim();
				String email = star.nextToken().trim(); 
				String userType = star.nextToken().trim();
				User.UserType userType2 = User.UserType.valueOf(userType);
				User user = new User(userID, password, name, email, userType2);
				// add to Student list
				alr.add(user) ;
			}
		return alr ;
	}

	public static void saveFile(String fileName, List al){
		List alw = new ArrayList() ;// to store Students data

      for (int i = 0 ; i < al.size() ; i++) {
				User user = (User)al.get(i);
				StringBuilder st =  new StringBuilder() ;
				st.append(user.getUserID().trim());
				st.append(SEPARATOR);
				st.append(user.getPassword().trim());
				st.append(SEPARATOR);
				st.append(user.getName().trim());
				st.append(SEPARATOR);
				st.append(user.getEmail().trim());
				st.append(SEPARATOR); 
				st.append(user.getUserType().toString().trim());
				st.append(SEPARATOR); 
				alw.add(st.toString()) ;
			} 
			try {
				write(fileName,alw);
			} catch (IOException e) {
				System.out.println("Error in saving User File. ");
			}
	}

	public User authenticateUser(String userID, String password) {
        for (User user : userList) {
            if (user.getUserID().equals(userID) && user.getPassword().equals(password)) {
                return user; // Authentication successful, return the user object
            }
        }
        return null; // Authentication failed
    }

	public static User findUserByID(String userID) {
		ArrayList<User> userList = readFile(UserDB.fileName);
        for (User user : userList) {
            if (user.getUserID().equals(userID)) {
                return user; // Found the user with the matching userID
            }
        }
        return null; // User not found
    }
	
	public boolean saveUser(User user) {
        // Find the user in the userList and update its data
		ArrayList<User> userList = readFile(UserDB.fileName);
		userList.set(userList.indexOf(user), user);
        
        saveFile(fileName, userList);
        return true;
    }

}
