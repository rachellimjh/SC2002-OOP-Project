package fypms;

import java.util.NoSuchElementException;
import java.util.Scanner;

import database.StudentDB;
import database.SupervisorDB;
import database.UserDB;
import model.Student;
import model.Supervisor;
import model.User;
import model.User.UserType;
import view.FYPCoordinatorController;
import view.StudentController;
import view.SupervisorController;
import view.UserController;

public class FYPMSApp {

	public static void main(String[] args) {
		UserController user = new UserController();
		while (true) {
			user.start();
		}
		//System.out.println(((Student) user).isAssigned());
		//we are trying to use a function in the subclass
		//we need to downcast
	}

	
}
