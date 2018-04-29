package deweyDecimator;

import java.sql.*;

public class SQLConnection {
	Connection sql;
	
	// Default constructor
	public SQLConnection(String url, String user, String pass) {
		// tries to create an instance of the database connection
		try {
			this.sql = DriverManager.getConnection(url, user, pass);
		}
		catch(SQLException e) {
			System.out.println("ERROR: CONNECTION TO DATABASE NOT ESTABLISHED");
			System.out.println(e.getMessage());
		}
	}
	
	// Select Statements
	public void printAll(String table) {
		try {
			String query = "SELECT * FROM " + table;
			//String query = "SELECT * FROM LibraryUser WHERE userID = 101";
			
			//SQL statement object
			Statement stmt = sql.createStatement();
			
			//get results
			ResultSet results = stmt.executeQuery(query);
			
			// set metadata (note indexing starts at 1)
			ResultSetMetaData rsmd = results.getMetaData();
			int numCols = rsmd.getColumnCount();
			
			// print column labels
			for(int i=1; i<= numCols; i++) {
				if (i == numCols) {
					System.out.println(rsmd.getColumnLabel(i));
				} else {
					System.out.print(rsmd.getColumnLabel(i) + ", ");
				}
			}
			
			// print query results
			while(results.next()) {
				for(int i=1; i<= numCols; i++) {
					String nextVal = results.getString(i);
					if(i==numCols) {
						System.out.println(nextVal);
					} else {
						System.out.print(nextVal + ", ");
					}
				}
			}
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public String findUserType(String userID) {
		String query = "SELECT userType FROM LibraryUsers WHERE userID=" + userID;
		Statement stmt;
		try {
			stmt = sql.createStatement();
			ResultSet results = stmt.executeQuery(query);
			
			System.out.printf("User Type: %s", results.getString("userType"));
			
			return results.getString("userType");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}
 