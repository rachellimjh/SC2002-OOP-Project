package database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import model.Project;
import model.Project.ProjectStatus;
import model.Student;
import model.Supervisor;
import model.User;

public class ProjectDB extends DataAccess {
	
	private static ArrayList<Project> projectList;
	public static final String fileName = "./src/database/ProjectList.txt";
	
	public ProjectDB() {
	    projectList = readFile(fileName);
	}

	public static ArrayList<Project> readFile(String fileName){
		// read String from text file  
		ArrayList stringArray = null;
		try {
			stringArray = (ArrayList)read(fileName);
		} catch (IOException e) {
			System.out.println("Error in reading Project File. ");
		}    
		ArrayList alr = new ArrayList() ;// to store Students data    
 
        for (int i = 0 ; i < stringArray.size() ; i++) {   
				String st = (String)stringArray.get(i); 
				// get individual 'fields' of the string separated by SEPARATOR
				StringTokenizer star = new StringTokenizer(st , SEPARATOR);	// pass in the string to the string tokenizer using delimiter ","
				//projectID|projectTitle|projectStatus|supervisorID|studentID|
				int projectID = Integer.parseInt(star.nextToken().trim());
				String projectTitle = star.nextToken().trim();
				String projectStatus = star.nextToken().trim(); 
				Project.ProjectStatus projStatus2 = Project.ProjectStatus.valueOf(projectStatus);
				String supervisorID = star.nextToken().trim();
				String studentID = star.nextToken().trim();
				Supervisor sup=null;
				Student student=null;
				if (supervisorID!="empty") 
					sup = SupervisorDB.findUserByID(supervisorID);
				if (studentID!="empty")
					student = StudentDB.findUserByID(studentID);
				Project project = new Project(projectID, projectTitle, projStatus2, sup, student);
				project.setStudentID(studentID);
				project.setSupervisorID(supervisorID);
				alr.add(project) ;
			}
		return alr ;
	}

	public static boolean saveFile(String fileName, List al){
		List alw = new ArrayList() ;// to store Students data

	      for (int i = 0 ; i < al.size() ; i++) {
					Project project = (Project)al.get(i);
					StringBuilder st =  new StringBuilder() ;
					st.append(Integer.toString(project.getProjectID()));
					st.append(SEPARATOR);
					st.append(project.getProjectTitle().trim());
					st.append(SEPARATOR);
					st.append(project.getProjectStatus().toString().trim());
					st.append(SEPARATOR);
					st.append(project.getSupervisorID().trim());
					st.append(SEPARATOR); 
					st.append(project.getStudentID().trim());
					st.append(SEPARATOR); 
					alw.add(st.toString()) ;
		} 
		try {
			write(fileName,alw);
		} catch (IOException e) {
			System.out.println("Error in saving Project File. ");
			return false;
		}
		return true;
	}
	
	public static Project findProjectByID(int projectID) {
		//ArrayList<Project> projectList = readFile(ProjectDB.fileName);
		for (Project project : projectList) {
            if (project.getProjectID()==projectID) {
                return project; // Found the user with the matching userID
            }
        }
        return null; // User not found
	}

	public static ArrayList<Project> findProjectBySupervisorID(String supervisorID) {
		ArrayList<Project> projectsBySupervisor = new ArrayList<>();
		//ArrayList<Project> projectList = readFile(ProjectDB.fileName);
		
	    for (Project project : projectList) {
	        if (project.getSupervisorID().equals(supervisorID)) {
	            projectsBySupervisor.add(project); // Add projects supervised by the given supervisorID
	        }
	    }

	    return projectsBySupervisor;
	}
	
	public static Project findProjectByStudentID(String studentID) {
		Project projectByStudent = null;
		//ArrayList<Project> projectList = readFile(ProjectDB.fileName);
		
	    for (Project project : projectList) {
	        if (project.getStudentID().equals(studentID)) {
	        	projectByStudent = project;
	        	break; // Add projects supervised by the given supervisorID
	        }
	    }

	    return projectByStudent;
	}
	
	public static ArrayList<Project> findProjectBysupervisor(Supervisor supervisor) {
		ArrayList<Project> projectsBySupervisor = new ArrayList<>();
		//ArrayList<Project> projectList = readFile(ProjectDB.fileName);
		
	    for (Project project : projectList) {
	        if (project.getSupervisor().equals(supervisor)) {
	            projectsBySupervisor.add(project); // Add projects supervised by the given supervisorID
	        }
	    }

	    return projectsBySupervisor;
	}
	
	//one project to one student
	public static Project findProjectByStudent(Student student) {
		Project projectByStudent = null;
		//ArrayList<Project> projectList = readFile(ProjectDB.fileName);
		
	    for (Project project : projectList) {
	        if (project.getStudent().equals(student)) {
	        	projectByStudent = project;
	        	break; // Add projects supervised by the given supervisorID
	        }
	    }
		return projectByStudent;
	}
	
	public static ArrayList<Project> findProjectByStatus(ProjectStatus status) {
		ArrayList<Project> projectsByStatus = new ArrayList<>();
		//ArrayList<Project> projectList = readFile(ProjectDB.fileName);
		
	    for (Project project : projectList) {
	        if (project.getProjectStatus().equals(status)) {
	        	projectsByStatus.add(project); // Add projects supervised by the given supervisorID
	        }
	    }
	    return projectsByStatus;
	}

	public static int getNextProjectID() {
		//projectList = readFile(fileName);
		return projectList.size()+1;
	}
	
	public static boolean saveProject(Project project) {
        // Find the user in the userList and update its data
		ArrayList<Project> projectList = readFile(ProjectDB.fileName);
		projectList.set(project.getProjectID()-1, project);
        
        if (saveFile(fileName, projectList)) {
        	return true;
        }
        return false;
    }

}
