package deweyDecimator;

import java.sql.*;
import java.util.ArrayList;

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
		String query = "SELECT userType FROM LibraryUser WHERE userID=" + userID;
		//String query = "SELECT userType FROM LibraryUser";
		//System.out.printf("Trying: %s\n", query);
		Statement stmt;
		try {
			stmt = sql.createStatement();
			ResultSet results = stmt.executeQuery(query);
			
			results.next();
			
			return results.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public String getFines(String cardNumber) {
		String query = "SELECT totalfines FROM Card WHERE cardNumber=" + cardNumber;
		Statement stmt;
		try {
			stmt = sql.createStatement();
			ResultSet results = stmt.executeQuery(query);
			
			results.next();
			
			return results.getString(1);
		} catch (SQLException e) {
			//e.printStackTrace();
		}
		
		return null;
		
	}
	
	public void setFines(String cardNumber, String fines) {
		String update = "UPDATE Card SET totalfines=" + fines + " WHERE cardNumber=" + cardNumber;
		Statement stmt;
		
		try {
			stmt = sql.createStatement();
			stmt.executeUpdate(update);
		} catch (SQLException e) {
			// nothing
		}
	}
	
	public ArrayList<Integer> addUser(String f, String l, String a, String p, String level) {
		// find unique UserID
		ArrayList<String> IDs = currentIDs();
		int maxi=0;
		for(int i=0; i<IDs.size(); i++) {
			if(Integer.parseInt(IDs.get(i)) > maxi) {
				maxi = Integer.parseInt(IDs.get(i));
			}
		}
		
		int ID = maxi + 1;
		
		// find unique card number
		ArrayList<String> CNs = currentCNs();
		int maxc=0;
		for(int i=0; i<CNs.size(); i++) {
			if(Integer.parseInt(CNs.get(i)) > maxc) {
				maxc = Integer.parseInt(CNs.get(i));
			}
		}
		
		int CN = maxc + 1;
		
		ArrayList<Integer> idcn = new ArrayList<Integer>();
		
		try {
			String insert = "INSERT INTO LibraryUser VALUES (?, ?, ?, ?, ?, ?)";
			PreparedStatement insertstatement = sql.prepareStatement(insert);
			
			insertstatement.setInt(1, ID);
			insertstatement.setString(2, f + " " + l);
			insertstatement.setString(3, a);
			insertstatement.setString(4, p);
			insertstatement.setString(5, level);
			insertstatement.setInt(6, CN);
			
			insertstatement.executeUpdate();
			
			// prep for return
			
			idcn.add(ID);
			idcn.add(CN);
			
		} catch (SQLException e) {
			//nothing
		}
		
		return idcn;
		
	}
	
	public ArrayList<String> currentIDs() {
		ArrayList<String> IDs = new ArrayList<String>();
		try {
			String query = "SELECT userID FROM LibraryUser";
			Statement stmt = sql.createStatement();
			ResultSet results = stmt.executeQuery(query);
			
			ResultSetMetaData rsmd = results.getMetaData();
			int numCols = rsmd.getColumnCount();
			
			while(results.next()) {
				for(int i=1; i<= numCols; i++) {
					String nextVal = results.getString(i);
					IDs.add(nextVal);
				}
			}
			
		} catch (SQLException e) {
			//nothing
		}
		
		return IDs;
	}
	
	public ArrayList<String> currentCNs() {
		ArrayList<String> CNs = new ArrayList<String>();
		try {
			String query = "SELECT cardNumber FROM LibraryUser";
			Statement stmt = sql.createStatement();
			ResultSet results = stmt.executeQuery(query);
			
			ResultSetMetaData rsmd = results.getMetaData();
			int numCols = rsmd.getColumnCount();
			
			while(results.next()) {
				for(int i=1; i<= numCols; i++) {
					String nextVal = results.getString(i);
					CNs.add(nextVal);
				}
			}
			
		} catch (SQLException e) {
			//nothing
		}
		
		return CNs;
	}
	
	public void addCard(int cn) {
		String insert = "INSERT INTO Card VALUES (" + cn + ", " + "0.00, '2018-01-01'";
		try {
			Statement stmt = sql.createStatement();
			stmt.executeQuery(insert);
			
		} catch (SQLException e) {
			System.out.println("Add card failed");
		}
	}
}
 