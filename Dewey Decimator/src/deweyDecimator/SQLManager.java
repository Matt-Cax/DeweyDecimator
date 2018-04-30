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
		String type = connection.find("userType", "LibraryUser", "userID", loginID);
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
	// calls the SQLConnection searchBooks method, which returns all tuples with attribute type that includes the search term
	public String searchForBook(String type, String term)
	{
		return connection.searchBooks(type, term);
	}
	
	public String getFines(String cardNumber) {
		return connection.find("totalfines", "Card", "cardNumber", cardNumber);
	}
	
	public void setFines(String cardNumber, String fines) {
		//connection.setFines(cardNumber, fines);
		connection.set("Card", "totalfines", fines, "cardNumber", cardNumber);
	}
	
	public void addUser(String f, String l, String a, String p, String level) {
		//add user
		ArrayList<Integer> idcn = connection.addUser(f,l,a,p,level);
		int ID = idcn.get(0);
		int CN = idcn.get(1);
		
		//create corresponding card
		connection.addCard(CN);
		
	}
	
	public void checkOut(String resourceID, String patronID) {
		//get cardNumber from patronID
		String cardNumber = connection.find("cardNumber", "LibraryUser", "userID", patronID);
		
		//create loan
		connection.createLoan(resourceID, cardNumber);
		
		System.out.println("Checked out successfully");
	}
	
	public void checkIn(String resourceID, String patronID) {
		//get loanid
		String loanid = connection.find("loanNumber", "Loan", "resourceID", resourceID);
		
		//delete loan
		connection.delete("Loan", "loanNumber", loanid);
		
		System.out.println("Checked in successfully");
	}
	
	public void addMedia(String la, String isbn, String t, String a, String p, String pd, String e, String b, String m, String g) {
		connection.addMedia(la,isbn,t,a,p,pd,e,b,m,g);
	}
}
