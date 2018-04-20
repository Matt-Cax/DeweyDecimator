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

}
