package database;

import java.util.Scanner;

import model.User;

public class PasswordManager {

	public static void changePassword(User user) {
		String newPassword1, newPassword;
		
		Scanner sc = new Scanner(System.in);
		do {
	        System.out.print("Enter your new password: ");
	        newPassword1 = sc.next();
	        System.out.print("Reenter your new password: ");
	        newPassword = sc.next();
	        
	        if (!newPassword.equals(newPassword1)) {
	            System.out.println("\nPasswords do not match. Please try again.");
	        }
	    } while (!newPassword.equals(newPassword1));
        
        
        // Save the changes to the user object in the database/file
        UserDB userDB = new UserDB();
        User newUser = userDB.findUserByID(user.getUserID());
        newUser.setPassword(newPassword);
        if (userDB.saveUser(newUser)) {
        	System.out.println("\nSuccessfully changed password.");
        	System.out.println("Please login again with your new password.\n");
        }
    }
}
