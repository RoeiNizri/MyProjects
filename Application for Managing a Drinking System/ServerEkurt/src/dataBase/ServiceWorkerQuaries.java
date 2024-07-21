package dataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import common.Response;
import common.Transaction;
import enums.RegionEnum;
import logic.User;

public class ServiceWorkerQuaries {

	/**
	 * 
	 * This method retrieves a list of customer requests for subscription from the
	 * database and sets it in the given Transaction object. The requests are
	 * retrieved by querying the 'users' table for customers with the status
	 * 'REQ_TO_SUB'.
	 * 
	 * @param obj The Transaction object that holds the data retrieved from the
	 *            database and the response status
	 * @param con The database connection
	 */
	public static void getSubscriberRequestsToServiceWorker(Transaction obj, Connection con) {
		if (obj instanceof Transaction) {
			Statement stmt;
			ArrayList<User> list = new ArrayList<User>();
			try {
				stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT id, firstName, lastName, telephone, email, region "
						+ "FROM users " + "WHERE users.role = 'CUSTOMER' AND status='REQ_TO_SUB'");
				while (rs.next()) {
					list.add(new User(rs.getInt("id"), rs.getString("firstName"), rs.getString("lastName"),
							rs.getString("telephone"), rs.getString("email"),
							RegionEnum.valueOf(rs.getString("region"))));

				}
				obj.setData(list);
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
				obj.setResponse(Response.GET_SUBSCRIBER_REQUESTS_TO_SERVICE_WORKER_UNSUCCESSFULLY);
				return;
			}
			obj.setResponse(Response.GET_SUBSCRIBER_REQUESTS_TO_SERVICE_WORKER_SUCCESSFULLY);
		} else
			obj.setResponse(Response.GET_SUBSCRIBER_REQUESTS_TO_SERVICE_WORKER_UNSUCCESSFULLY);
	}

	/**
	 * Updating approved subscriber registration request to the database.
	 *
	 * @param obj The Transaction object containing the data for the update
	 * @param con The connection to the database
	 */
	public static void approveSubscriberRequest(Transaction obj, Connection con) {
		if (obj instanceof Transaction) {
			String userId = (String) obj.getData();
			System.out.println(userId);
			PreparedStatement pstmt;
			try {
				pstmt = con.prepareStatement(
						"UPDATE users SET role='SUBSCRIBER', status= 'SUBSCRIBER_APPROVED' WHERE id=?;");
				pstmt.setString(1, userId);
				if (pstmt.executeUpdate() == 0) {
					obj.setResponse(Response.APPROVE_SUBSCRIBER_REQUEST_UNSUCCESSFULLY);
					return;
				}
				pstmt = con.prepareStatement(
						"INSERT INTO subscriberwallet (id, balance) VALUES (? , 0);");
				pstmt.setString(1, userId);
				
				if (pstmt.executeUpdate() == 0) {
					obj.setResponse(Response.APPROVE_SUBSCRIBER_REQUEST_UNSUCCESSFULLY);
					return;
				}
			} catch (SQLException e) {
				e.printStackTrace();
				obj.setResponse(Response.APPROVE_SUBSCRIBER_REQUEST_UNSUCCESSFULLY);
				return;
			}
			obj.setResponse(Response.APPROVE_SUBSCRIBER_REQUEST_SUCCESSFULLY);
		}
	}
}
