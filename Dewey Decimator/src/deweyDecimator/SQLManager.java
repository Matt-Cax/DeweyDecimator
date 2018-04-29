package deweyDecimator;

import java.sql.*;

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
		String type = connection.findUserType(loginID);
		System.out.printf("User Type: %s", type);
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
}
