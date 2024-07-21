package dataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import common.Response;
import common.Transaction;

public class UserQuaries {

	/**
	 * Register user to customer and adding a credit card to the database.
	 *
	 * @param obj The Transaction object containing the data for the update
	 * @param con The connection to the database
	 */
	public static void registerUserToCustomerWithCreditCard(Transaction obj, Connection con) {
		if (obj instanceof Transaction) {
			@SuppressWarnings("unchecked")
			List<String> inputQuery = (List<String>) obj.getData();
			PreparedStatement pstmt;
			try {
				pstmt = con.prepareStatement(
						"INSERT INTO paymentdetails (Id, CreditCardNum, CVV, ExpDate) " + "VALUES (?, ?, ?, ?);");
				pstmt.setString(1, inputQuery.get(0));
				pstmt.setString(2, inputQuery.get(1));
				pstmt.setString(3, inputQuery.get(2));
				pstmt.setString(4, inputQuery.get(3));
				if (pstmt.executeUpdate() == 0) {
					obj.setResponse(Response.REGISTER_USER_TO_CUSTOMER_UNSUCCESSFULLY);
					return;
				}
				pstmt = con.prepareStatement("UPDATE users SET status= 'REQ_TO_REG' WHERE id=?;");
				pstmt.setString(1, inputQuery.get(0));
				if (pstmt.executeUpdate() == 0) {
					obj.setResponse(Response.REGISTER_USER_TO_CUSTOMER_UNSUCCESSFULLY);
					return;
				}

			} catch (SQLException e) {
				e.printStackTrace();
				obj.setResponse(Response.REGISTER_USER_TO_CUSTOMER_UNSUCCESSFULLY);
				return;
			}
			obj.setResponse(Response.REGISTER_USER_TO_CUSTOMER_SUCCESSFULLY);
		}

	}

	/**
	 * Getting a credit card by user id from the database.
	 *
	 * @param obj The Transaction object containing the data for the update
	 * @param con The connection to the database
	 */
	public static void getCreditCardById(Transaction obj, Connection con) {
		if (obj instanceof Transaction) {
			String id = (String) obj.getData();
			List<String> list = new ArrayList<>();
			PreparedStatement pstmt;
			try {
				pstmt = con.prepareStatement("SELECT CreditCardNum, CVV, ExpDate FROM paymentdetails WHERE id = ?");
				pstmt.setString(1, id);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					list.add(rs.getString("CreditCardNum"));
					list.add(rs.getString("CVV"));
					list.add(rs.getString("ExpDate"));
				}
				obj.setData(list);
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
				obj.setResponse(Response.GET_CREDIT_CARD_BY_ID_UNSUCCESSFULLY);
				return;
			}
			obj.setResponse(Response.GET_CREDIT_CARD_BY_ID_SUCCESSFULLY);
		} else
			obj.setResponse(Response.GET_CREDIT_CARD_BY_ID_UNSUCCESSFULLY);
	}

	/**
	 * Customer request to be subscriber.
	 *
	 * @param obj The Transaction object containing the data for the update
	 * @param con The connection to the database
	 */
	public static void subscriberRequest(Transaction obj, Connection con) {
		if (obj instanceof Transaction) {
			String userId = (String) obj.getData();
			PreparedStatement pstmt;
			try {
				pstmt = con.prepareStatement("UPDATE users SET status= 'REQ_TO_SUB' WHERE id=?;");
				pstmt.setString(1, userId);
				if (pstmt.executeUpdate() == 0) {
					obj.setResponse(Response.APPROVE_REGISTER_REQUEST_UNSUCCESSFULLY);
					return;
				}

			} catch (SQLException e) {
				e.printStackTrace();
				obj.setResponse(Response.APPROVE_REGISTER_REQUEST_UNSUCCESSFULLY);
				return;
			}
			obj.setResponse(Response.APPROVE_REGISTER_REQUEST_SUCCESSFULLY);
		}

	}

	/**
	 * Getting all subscribers.
	 *
	 * @param obj The Transaction object containing the data for the update
	 * @param con The connection to the database
	 */
	public static void getSubscribersId(Transaction obj) {
		List<String> subId = new ArrayList<>();
		ResultSet rs = dbController.getInstance().executeQuery("SELECT id FROM ekurt.users WHERE role='SUBSCRIBER';");
		if (rs == null)
			obj.setResponse(Response.FAILED);
		else {
			try {
				while (rs.next()) {
					subId.add(rs.getString("id"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			obj.setData(subId);
			obj.setResponse(Response.FOUND_SUB_ID_LIST);
		}

	}
}
