package dataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import common.Transaction;

/**
 * 
 * dbController class is responsible for handling the database connection and
 * executing queries.
 * 
 * It also acts as a singleton class, which means only one instance of the class
 * is created throughout the application.
 * 
 * This class also contains the parsingToData method which is responsible for
 * passing the data to the ActionAnalyze class to be handled.
 * 
 * It also contains the connectToDB method which is used to establish a
 * connection to the database.
 */
public class dbController {
	private static dbController databaseController;
	public static Connection conn;
	
	/**
	 * 
	 * Returns the current connection to the database.
	 * 
	 * @return Connection object
	 */
	public static Connection getConn() {
		return conn;
	}
	/**
	 * 
	 * This method is used to create a singleton instance of the class.
	 * 
	 * @return dbController instance
	 */
	public static dbController getInstance() {
		if (databaseController == null) {
			databaseController = new dbController();
		}
		return databaseController;
	}

	/**
	 * @param query SELECT query
	 * @return ResultSet
	 */
	public ResultSet executeQuery(String query) {
		Statement stmt;
		try {
			stmt = conn.createStatement();
			return stmt.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param query UPDATE/INSERT/DELETE queries
	 * @return Number of values that has been changed
	 */
	public int executeUpdate(String query) {
		PreparedStatement prepareStatement = null;

		try {
			prepareStatement = conn.prepareStatement(query);
			return prepareStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	/**
	 * 
	 * This method is used to parse the data to the ActionAnalyze class
	 * 
	 * @param obj Transaction object that contains the data to be parsed
	 */
	public static void parsingToData(Transaction obj) {
		ActionAnalyze.actionAnalyzeServer(obj, conn);

	}
	/**
	 * 
	 * This method is used to establish a connection to the database.
	 * 
	 * @param data List of strings that contains the necessary information to
	 *             connect to the database.
	 * @return true if the connection is established successfully, false otherwise.
	 */
	public static boolean connectToDB(List<String> data) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			System.out.println("Driver definition succeed");
		} catch (Exception ex) {
			/* handle the error */
			System.out.println("Driver definition failed");
		}

		try {

			conn = DriverManager.getConnection(data.get(0), data.get(1), data.get(2));
			data.clear();

			System.out.println("SQL connection succeed");

		} catch (SQLException ex) {/* handle any errors */
			data.clear();
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			return false;
		}
		return true;
	}

}
