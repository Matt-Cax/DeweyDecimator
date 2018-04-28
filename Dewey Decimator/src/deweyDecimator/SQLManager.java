package deweyDecimator;

public class SQLManager {
	
	//Yay for flags
	public static final int PATRON = -1;
	public static final int LIBRARIAN = -2;
	public static final int ADMIN = -3;
	public static final int ERROR = -4;
	
	public int verifyLogin(String loginID) {
		// TODO
		//SELECT type FROM LibraryUsers WHERE userID==loginID
		//if query returns empty, return ERROR
		//else return type
		return LIBRARIAN;
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
