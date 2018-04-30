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
	
	public String find(String findcol, String table, String searchcol, String testval) {
		String query = "SELECT " + findcol + " FROM " + table + " WHERE " + searchcol + " = " + testval;
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
	
	public void set(String table, String setcol, String setval, String searchcol, String searchval) {
		String update = "UPDATE " +  table + " SET " + setcol + "=" + setval + " WHERE " + searchcol + "=" + searchval;
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
		int ID = getUnique("userID", "LibraryUser");
		
		// find unique card number
		int CN = getUnique("cardNumber", "LibraryUser");
		
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
			System.out.println("add User failed");
		}
		
		return idcn;
		
	}
	
	public ArrayList<String> getCurrent(String col, String table) {
		ArrayList<String> current = new ArrayList<String>();
		try {
			String query = "SELECT " + col + " FROM " + table;
			Statement stmt = sql.createStatement();
			ResultSet results = stmt.executeQuery(query);
			
			ResultSetMetaData rsmd = results.getMetaData();
			int numCols = rsmd.getColumnCount();
			
			while(results.next()) {
				for(int i=1; i<= numCols; i++) {
					String nextVal = results.getString(i);
					current.add(nextVal);
				}
			}
			
		} catch (SQLException e) {
			System.out.println("get current" + col + " failed");
			//nothing
		}
		
		return current;
	}
	
	public int getUnique(String col, String table) {
		
		ArrayList<String> A = getCurrent(col, table);
		
		int max=0;
		for(int i=0; i<A.size(); i++) {
			if(Integer.parseInt(A.get(i)) > max) {
				max = Integer.parseInt(A.get(i));
			}
		}
		
		return max + 1;
	}
	
	public void addCard(int cn) {
		String insert = "INSERT INTO Card VALUES (" + cn + ", " + "0.00, '2018-01-01')";
		//System.out.println("Trying: " + insert);
		try {
			Statement stmt = sql.createStatement();
			stmt.executeUpdate(insert);
			
			System.out.println("User and Card added");
			
		} catch (SQLException e) {
			System.out.println("Add card failed");
			System.out.println(e.getMessage());
		}
	}
	
	public void createLoan(String resourceID, String cardNumber) {
		//get unique loanid
		int loanid = getUnique("loanNumber", "Loan");
		
		String insert = "INSERT INTO Loan VALUES (" + loanid + ", " + resourceID + ", " + cardNumber 
				+ ",  '2018-01-01', '2018-07-01', 0.00)";
		
		try {
			Statement stmt = sql.createStatement();
			stmt.executeUpdate(insert);
			
			System.out.println("Loan added");
		} catch (SQLException e) {
			System.out.println("Failed to create loan");
			e.printStackTrace();
		}
	}
	
	public void delete(String table, String searchcol, String searchval) {
		String deletestring = "DELETE FROM " + table + " WHERE " + searchcol + "=" + searchval;
		
		try {
			sql.setAutoCommit(false);
			Statement deletestatement = sql.createStatement();
			deletestatement.executeUpdate(deletestring);
			
			//commit
			sql.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
}
 