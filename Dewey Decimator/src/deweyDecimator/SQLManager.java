package deweyDecimator;

import java.sql.*;
import java.util.ArrayList;

public class SQLManager {
	
	// login information
	private static String url = "jdbc:mysql://localhost:3306/deweydecimator?autoReconnect=true&useSSL=false";
	private static String user = "root";
	private static String pass = "toor";
	
	// establish connection to server
	private static SQLConnection connection = new SQLConnection(url, user, pass);
	
	//Yay for flags
	public static final int PATRON = -1;
	public static final int LIBRARIAN = -2;
	public static final int ADMIN = -3;
	public static final int ERROR = -4;
	
	public int verifyLogin(String loginID) {
		System.out.println("verifying login");
		String type = connection.findUserType(loginID);
		System.out.printf("User Type: %s\n", type);
		switch(type) {
		case "Patron":
			return PATRON;
		case "Librarian":
			return LIBRARIAN;
		case "Administrator":
			return ADMIN; 
				
		}
		return ERROR;
	}
	
	public String getFines(String cardNumber) {
		return connection.getFines(cardNumber);
	}
	
	public void setFines(String cardNumber, String fines) {
		connection.setFines(cardNumber, fines);
	}
	
	public void addUser(String f, String l, String a, String p, String level) {
		//add user
		ArrayList<Integer> idcn = connection.addUser(f,l,a,p,level);
		int ID = idcn.get(0);
		int CN = idcn.get(1);
		
		//create corresponding card
		connection.addCard(CN);
		
	}
	public void addAdmin(String firstName, String lastName) {
		// TODO
		// Should also insert a new userID, I don't know how to check current DB for highest number tho to add 1 to it
		// INSERT INTO LibraryUsers(userID,name,userType) VALUES (oldID+1, firstName + " " + lastName, 'Administrator')
	}
	
	public void addLib(String firstName, String lastName) {
		// TODO
		// Should also insert a new userID, I don't know how to check current DB for highest number tho to add 1 to it
		// INSERT INTO LibraryUsers(userID,name,userType) VALUES (oldID+1, firstName + " " + lastName, 'Librarian')
	}
	
	public void addPatron(String firstName, String lastName, String address, String phoneNum) {
		// TODO
		// Should also insert a new userID, I don't know how to check current DB for highest number tho to add 1 to it
		// INSERT INTO LibraryUsers(userID,name,address,phone,userType) VALUES (oldID+1, firstName + " " + lastName, address, phoneNum, 'Patron')
	}
	
	public void addFines(int cardNumber, double fines) {
		// TODO
		// Should also insert a new loanNumber, I don't know how to check current DB for highest number tho to add 1 to it
		// INSER INTO Loans(loanNumber, cardNumber, fine) VALUES (oldLN+1, cardNumber, fine)
	}
	
	public void searchCard() {
		
	}
}
