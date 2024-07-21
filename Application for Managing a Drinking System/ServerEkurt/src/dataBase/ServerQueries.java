package dataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServerQueries {

	/**
	 * This method imports users from an external system and inserts them into the
	 * local database.
	 * 
	 * @param conn a Connection object used to connect to the database and execute
	 *             the query
	 * @return boolean value indicating whether the import was successful or not
	 */
	public static boolean importUsersFromExternalSystem(Connection conn) {
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		boolean isWork = true;
		try {
			ps = conn.prepareStatement("SELECT * FROM externalusermanagmentsystem.users");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				try {
					ps1 = conn.prepareStatement(
							"INSERT INTO ekurt.users (id, username, password, is_logged_in, firstName, lastName, email, telephone, role, status, region) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
				} catch (SQLException e1) {
					System.out.println("Statement importUsersFromExternalSystem failure.");
					isWork = false;
				}
				try {
					ps1.setString(1, rs.getString("id"));
					ps1.setString(2, rs.getString("username"));
					ps1.setString(3, rs.getString("password"));
					ps1.setString(4, rs.getString("is_logged_in"));
					ps1.setString(5, rs.getString("firstName"));
					ps1.setString(6, rs.getString("lastName"));
					ps1.setString(7, rs.getString("email"));
					ps1.setBoolean(8, rs.getBoolean("telephone"));
					ps1.setString(9, rs.getString("role"));
					ps1.setString(10, rs.getString("status"));
					ps1.setString(11, rs.getString("region"));
					ps1.executeUpdate();
				} catch (Exception e) {
					System.out.println(e.getMessage());
					System.out.println("Executing importUsersFromExternalSystem failed!");
					isWork = false;
				}
			}
		} catch (Exception e) {
			System.out.println("importUsersFromExternalSystem has failed");
			isWork = false;
		}
		return isWork;
	}

	/**
	 *
	 * 
	 * This method logs out all users in the database by setting their is_logged_in
	 * field to 0.
	 * 
	 * @param conn a Connection object used to connect to the database and execute
	 *             the query
	 */
	public static void logoutAll(Connection conn) {
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement("UPDATE ekurt.users SET is_logged_in = 0;");
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
