package model;

public class Project {
	
	public enum ProjectStatus {
		AVAILABLE,
		UNAVAILABLE,
		RESERVED,
		ALLOCATED;
	}

	private int projectID;
	private String projectTitle;
	private ProjectStatus projectStatus;
	private String supervisorID;
	private String studentID;
	private Supervisor supervisor;
	private Student student;
	
	public Project(int projectID, String projectTitle, ProjectStatus projectStatus, Supervisor supervisor, Student student) {
		this.projectID=projectID;
		this.projectTitle=projectTitle;
		this.projectStatus=projectStatus;
		this.supervisor=supervisor;
		this.student=student;
	}

	public int getProjectID() {
		return projectID;
	}

	public String getProjectTitle() {
		return projectTitle;
	}

	public ProjectStatus getProjectStatus() {
		return projectStatus;
	}

	public Supervisor getSupervisor() {
		return supervisor;
	}

	public Student getStudent() {
		return student;
	}

	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}

	public void setProjectTitle(String projectTitle) {
		this.projectTitle = projectTitle;
	}

	public void setProjectStatus(ProjectStatus projectStatus) {
		this.projectStatus = projectStatus;
	}

	public void setSupervisor(Supervisor supervisor) {
		this.supervisor = supervisor;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public void setSupervisorID(String supervisorID) {
		this.supervisorID = supervisorID;
	}
	
	public void setStudentID(String studentID) {
		this.studentID=studentID;
	}

	public String getSupervisorID() {
		return this.supervisorID;
	}
	
	public String getStudentID() {
		return this.studentID;
	}

}
