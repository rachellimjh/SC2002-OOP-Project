package database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import model.Request;
import model.Student;
import model.Supervisor;
import model.User;

public class StudentDB extends DataAccess{
	public static String fileName = "./src/database/StudentList.txt";
	
	private ArrayList<Student> studentList;

    public StudentDB() {
        studentList = readFile(fileName);
    }
	public static ArrayList readFile(String fileName){
		// read String from text file  
		ArrayList stringArray = null;
		try {
			stringArray = (ArrayList)read(fileName);
		} catch (IOException e) {
			System.out.println("Error in reading Student File. ");
		}    
		ArrayList alr = new ArrayList() ;// to store Students data    
 
        for (int i = 0 ; i < stringArray.size() ; i++) {   
				String st = (String)stringArray.get(i); 
				// get individual 'fields' of the string separated by SEPARATOR
				StringTokenizer star = new StringTokenizer(st , SEPARATOR);	// pass in the string to the string tokenizer using delimiter ","

				String userID = star.nextToken().trim();
				String name = star.nextToken().trim();
				String email = star.nextToken().trim(); 
				boolean assigned = Boolean.parseBoolean(star.nextToken().trim());
				boolean deregistered = Boolean.parseBoolean(star.nextToken().trim());
				 ArrayList<Request> requests = new ArrayList<>();
				Student student = new Student(userID, name, email, assigned, deregistered, null, requests);
				// add to Student list
				alr.add(student) ;
			}
		return alr ;
	}

	public static boolean saveFile(String fileName, List al){
		List alw = new ArrayList() ;// to store Students data

	  for (int i = 0 ; i < al.size() ; i++) {
				Student student = (Student)al.get(i);
				StringBuilder st =  new StringBuilder() ;
				st.append(student.getUserID().trim());
				st.append(SEPARATOR);
				st.append(student.getName().trim());
				st.append(SEPARATOR);
				st.append(student.getEmail().trim());
				st.append(SEPARATOR); 
				st.append(Boolean.toString(student.isAssigned()).trim());
				st.append(SEPARATOR); 
				st.append(Boolean.toString(student.isDeregistered()).trim());
				st.append(SEPARATOR); 
				alw.add(st.toString()) ;
			} 
		try {
			write(fileName,alw);
		} catch (IOException e) {
			System.out.println("Error in saving Student File. ");
			return false;
		}
		return true;
	}
	
	public static Student findUserByID(String userID){
		ArrayList<Student> studentList = readFile(fileName);
        for (Student student : studentList) {
            if (student.getUserID().equals(userID)) {
                return student; // Found the student with the matching userID
            }
        }
        return null; // Student not found
    }
	
	public static boolean saveStudent(Student student) {
        // Find the user in the userList and update its data
        ArrayList<Student> studentList = readFile(StudentDB.fileName);
        
        for (int i = 0; i < studentList.size(); i++) {
            if (studentList.get(i).getUserID().equals(student.getUserID())) {
                studentList.set(i, student);
                if (saveFile(StudentDB.fileName, studentList)) {
                    return true;
                }
            }
        }
        return false;
    }
	
}
