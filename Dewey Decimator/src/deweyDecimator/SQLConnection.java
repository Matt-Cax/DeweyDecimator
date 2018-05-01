package deweyDecimator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
	
	//Searches a table for a certain attribute based on search criteria
	public String find(String findcol, String table, String searchcol, String testval) {
		//Generate query
		String query = "SELECT " + findcol + " FROM " + table + " WHERE " + searchcol + " = " + testval;

		//Attempt to execute query
		Statement stmt;
		try {
			stmt = sql.createStatement();
			ResultSet results = stmt.executeQuery(query);
			
			results.next();
			
			return results.getString(1);
		} catch (SQLException e) {
			System.out.println("Find from " + table + " failed");
			//e.printStackTrace();
		}
		
		return null;
	}

	//Returns media corresponding to a patrons query
	public String searchBooks(String searchType, String searchTerm)
	{
		String query = "SELECT * FROM Book WHERE " + searchType + " LIKE '%" + searchTerm + "%';";
		// String query = SELECT * FROM BOOK WHERE author LIKE '%Dickens%';
		//System.out.println(query);
		try {
			//SQL statement object
			Statement stmt = sql.createStatement();
			
			//get results
			ResultSet results = stmt.executeQuery(query);
		
			StringBuilder builder = new StringBuilder();
			int columnCount = results.getMetaData().getColumnCount();
			
			//For each tuple
			while(results.next())
			{
				//For eachc attribute
				for(int i = 0; i < columnCount;)
				{
					//Add attribute to string delimited by ; 
					builder.append(results.getString(i + 1));
					if(++i < columnCount)
					{
						builder.append("; ");
					}
				}
				//Add ; between tuples
				builder.append("; ");
			}
			return builder.toString();
			
		} 
		catch(SQLException e)
		{
			System.out.println("search failed");
			//e.printStackTrace();
		}
		
		return null;
	}
	
	public void set(String table, String setcol, String setval, String searchcol, String searchval) {
		String update = "UPDATE " +  table + " SET " + setcol + "=" + setval + " WHERE " + searchcol + "=" + searchval;
		Statement stmt;
		//System.out.println("Trying: " + update);
		
		try {
			stmt = sql.createStatement();
			stmt.executeUpdate(update);
		} catch (SQLException e) {
			System.out.println("Failed to edit " + table);
			//System.out.println(e.getMessage());
		}
	}
	
	//Adds a user to the databse
	public ArrayList<Integer> addUser(String f, String l, String a, String p, String level) {
		// find unique UserID
		int ID = getUnique("userID", "LibraryUser");
		
		// find unique card number
		int CN = getUnique("cardNumber", "LibraryUser");
		
		ArrayList<Integer> idcn = new ArrayList<Integer>();
		
		//Insert user into db
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

	//Get size of table
	public ArrayList<String> getCurrent(String col, String table) {
		ArrayList<String> current = new ArrayList<String>();
		//Execute query
		try {
			String query = "SELECT " + col + " FROM " + table;
			Statement stmt = sql.createStatement();
			ResultSet results = stmt.executeQuery(query);
			
			ResultSetMetaData rsmd = results.getMetaData();
			int numCols = rsmd.getColumnCount();
			
			//Add each tuple to array
			while(results.next()) {
				for(int i=1; i<= numCols; i++) {
					String nextVal = results.getString(i);
					current.add(nextVal);
				}
			}
			
		} catch (SQLException e) {
			System.out.println("get current" + col + " failed");
		}
		
		return current;
	}
	
	//Returns the next id that isn't in use
	public int getUnique(String col, String table) {
		
		ArrayList<String> A = getCurrent(col, table);
		
		//find the max id
		int max=0;
		for(int i=0; i<A.size(); i++) {
			if(Integer.parseInt(A.get(i)) > max) {
				max = Integer.parseInt(A.get(i));
			}
		}
		
		//return next id
		return max + 1;
	}
	
	//Add card to db
	public void addCard(int cn) {
		String insert = "INSERT INTO Card VALUES (" + cn + ", " + "0.00, '2018-01-01')";
		
		try {
			Statement stmt = sql.createStatement();
			stmt.executeUpdate(insert);
			
			System.out.println("User and Card added");
			
		} catch (SQLException e) {
			System.out.println("Add card failed");
			//System.out.println(e.getMessage());
		}
	}
	
	//Add loan to db
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
			//e.printStackTrace();
		}
	}
	
	//Delete tuple 
	public void delete(String table, String searchcol, String searchval) {
		String deletestring = "DELETE FROM " + table + " WHERE " + searchcol + "=" + searchval;
		
		try {
			sql.setAutoCommit(false);
			Statement deletestatement = sql.createStatement();
			deletestatement.executeUpdate(deletestring);
			
			//commit
			sql.commit();
		} catch (SQLException e) {
			System.out.println("Delete from " + table + " failed");
			//e.printStackTrace();
		}
	}
	
	//Add media to db
	public void addMedia(String la, String isbn, String t, String a, String p, String pd, String e, String b, String m, String g) {
		//get unique resourceID
		int resourceID = getUnique("resourceID", "Book");
		String insert = "INSERT INTO Book VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement is = sql.prepareStatement(insert);
			
			is.setInt(1, resourceID);
			is.setString(2, la);
			is.setString(3, isbn);
			is.setString(4, t);
			is.setString(5, a);
			is.setString(6, p);
			is.setString(7, pd);
			is.setString(8, e);
			is.setString(9, b);
			is.setString(10, m);
			is.setString(11, g);
			
			is.executeUpdate();
		} catch (SQLException ee) {
			System.out.println("Failed to add media");
			//ee.printStackTrace();
		}
	}
	
}
 