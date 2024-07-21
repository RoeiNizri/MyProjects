package dataBase;

import logic.User;
import common.*;
import enums.RegionEnum;
import enums.RoleEnum;
import enums.StatusEnum;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class defines the methods that send query's to the DB and receive the
 * data back from the DB and return it to the relevant controllers and classes
 * that need to use that information
 */
public class LoginQuaries {
    public static void loginByUsernameAndPassword(Transaction msg) {
        HashMap<String, String> args = (HashMap<String, String>) msg.getData();
        String username = args.get("username");
        String password = args.get("password");
        User currentUser = null;
        ResultSet rs = dbController.getInstance().executeQuery("SELECT * FROM ekurt.users where username='" + username + "' and password='" + password + "'");
		if (rs == null)
			msg.setResponse(Response.FAILED);
		else {
			try {
				if (rs.next()) {
					currentUser = new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"),
							rs.getBoolean("is_logged_in"), rs.getString("firstname"), rs.getString("lastname"),
							rs.getString("email"), rs.getString("telephone"), RoleEnum.valueOf(rs.getString("role")),
							StatusEnum.valueOf(rs.getString("status")), RegionEnum.valueOf(rs.getString("region")));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (currentUser == null) {
				msg.setResponse(Response.INCORRECT_VALUES);
			}else if(!currentUser.isLoggedIn()) {
				updateIsLoggedInColumn(username, password, 1);
				currentUser.setLoggedIn(true);
				msg.setData(currentUser);
				msg.setResponse(Response.LOGGED_IN_SUCCESS);
			} else if (currentUser.isLoggedIn()) {
				msg.setResponse(Response.ALREADY_LOGGED_IN);
			} else {
				msg.setResponse(Response.INCORRECT_VALUES);
			}
		}
	}

	/**
	 * Set 'is_logged_in' column in DB to 0
	 * 
	 * @param msg contains HashMap with 'username'
	 */
	public static void logoutUsername(Transaction msg) {
		@SuppressWarnings("unchecked")
		HashMap<String, String> args = (HashMap<String, String>) msg.getData();
		String username = args.get("username");
		updateIsLoggedInColumn(username, null, 0);
		msg.setResponse(Response.LOGGEDOUT_SUCCESSFULLY);
	}
	
	public static void getAllUserId(Transaction msg) {
		List<String> ids = new ArrayList<>();
        ResultSet rs = dbController.getInstance().executeQuery("SELECT id FROM ekurt.users;");
		if (rs == null)
			msg.setResponse(Response.FAILED);
		else {
			try {
				while(rs.next()) {
					ids.add(rs.getString("id"));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			msg.setData(ids);
			msg.setResponse(Response.FOUND_ALL_IDS);
		}
	}

	/**
	 * Check if a user is already flagged as 'logged in', in the DB
	 * 
	 * @param rs
	 * @return True if logged in, False otherwise
	 */
	@SuppressWarnings("unused")
	private static boolean isAlreadyLoggedIn(ResultSet rs) {
		try {
			if (rs.first()) {
				return rs.getBoolean("is_logged_in");
			}
			rs.beforeFirst();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Update 'is_logged_in' column in DB to 'value' parameter
	 *
	 * @param username to update his 'is_logged_in' column
	 * @param value    0 / 1
	 * @param password
	 * @return
	 */
	private static boolean updateIsLoggedInColumn(String username, String password, int value) {
		if (value != 0 && value != 1)
			throw new IllegalArgumentException("'is_logged_in' column can be 0 or 1!");
		String query;
		if (password == null) {
			query = "UPDATE ekurt.users SET is_logged_in = " + value + " WHERE username = '" + username + "'";
        }else {
        	query = "UPDATE ekurt.users SET is_logged_in = " + value + " WHERE username = '" + username + "' AND password = '" + password + "'";
        }
        int affectedRows = dbController.getInstance().executeUpdate(query);
        System.out.println("affected rows" + affectedRows);
        return (affectedRows == 1);
    }
	/**
	 * 
	 * The checkId method is used to retrieve user information from the database and
	 * verify the role of the user. It takes in a Transaction object as an argument
	 * and retrieves the user ID from the transaction data. The method then queries
	 * the database for the user's username, password, and role using the user ID.
	 * If the query returns a result, the method checks the role of the user. If the
	 * role is SUBSCRIBER, it sets the transaction data to be a HashMap containing
	 * the username and password, and sets the response to indicate a successful
	 * check. If the role is not SUBSCRIBER, it sets the response to indicate an
	 * unsuccessful check. If the query does not return a result, the method sets
	 * the response to indicate a failure.
	 * 
	 * @param obj - A Transaction object containing the user ID
	 */
	public static void checkId(Transaction obj) {
		String id = (String)obj.getData();
		RoleEnum role = null;
		HashMap<String,String> details=new HashMap<>();
		ResultSet rs = dbController.getInstance().executeQuery("SELECT username, password, role FROM ekurt.users where id='" + id + "';");
		if (rs == null)
			obj.setResponse(Response.FAILED);
		else {
				try {
					if (rs.next()) {
						details.put("username",rs.getString("username"));
						details.put("password",rs.getString("password"));
						role=RoleEnum.valueOf(rs.getString("role"));
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(role.equals(RoleEnum.SUBSCRIBER)) {
					obj.setData(details);
					obj.setResponse(Response.CHECK_ID_SUCC);
				}
				else {
					obj.setResponse(Response.CHECK_ID_UNSUCC);
				}
		}
	}
	
	
}