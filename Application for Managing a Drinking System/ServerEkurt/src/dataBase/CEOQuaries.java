package dataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import common.Response;
import common.Transaction;
import enums.RegionEnum;
import logic.*;

/**
 * CEOQuaries is a class that is responsible for performing various quaries and
 * calculations related to the CEO's and managers needs. It contains methods to
 * perform quaries such as getting the total revenue, total profit, and other
 * financial statistics.
 */
public class CEOQuaries {

	/**
	 * Get machine information from the database, and store it in the obj.data
	 * parameter.
	 *
	 * @param obj A Transaction object
	 * @param con A Connection object to connect to the database
	 */
	public static void getMachine(Transaction obj, Connection con) {
		if (obj instanceof Transaction) {
			List<String> list = new ArrayList<>();
			Statement stmt;
			try {
				stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT machine_name FROM machines;");
				while (rs.next()) {
					StringBuilder tmpOrder = new StringBuilder();
					tmpOrder.append(rs.getString(1));
					list.add(tmpOrder.toString());
				}

				obj.setData(list);
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
				obj.setResponse(Response.GETMACHINE_UNSUCCESSFULLY);
				return;
			}
			obj.setResponse(Response.GETMACHINE_SUCCESSFULLY);
		} else
			obj.setResponse(Response.GETMACHINE_UNSUCCESSFULLY);
	}

	/**
	 * getMachineByRegion method is responsible for getting all the machines in a
	 * specific region, it takes a Transaction object and a Connection object as a
	 * parameter, the data field of the Transaction object should contain the region
	 * name.
	 * 
	 * @param obj a Transaction object that contains the region name
	 * @param con a Connection object that connects to the database
	 */
	public static void getMachineByRegion(Transaction obj, Connection con) {
		if (obj instanceof Transaction) {
			String region = (String) obj.getData();
			List<String> list = new ArrayList<>();
			PreparedStatement pstmt;
			try {
				pstmt = con.prepareStatement("SELECT machine_name FROM machines WHERE region=?;");
				pstmt.setString(1, region);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					list.add(rs.getString("machine_name"));
				}
				obj.setData(list);
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
				obj.setResponse(Response.GET_MACHINE_BY_REGION_UNSUCCESSFULLY);
				return;
			}
			obj.setResponse(Response.GET_MACHINE_BY_REGION_SUCCESSFULLY);
		} else
			obj.setResponse(Response.GET_MACHINE_BY_REGION_UNSUCCESSFULLY);
	}

	/**
	 * getOrderReport method is responsible for getting a report of all the orders
	 * in a specific month and year, it takes a Transaction object and a Connection
	 * object as a parameter, the data field of the Transaction object should
	 * contain the region name, year, and month.
	 * 
	 * @param obj a Transaction object that contains the region name, year, and
	 *            month
	 * @param con a Connection object that connects to the database
	 */
	public static void getOrderReport(Transaction obj, Connection con) {
		if (obj instanceof Transaction) {
			PreparedStatement pstmt;
			@SuppressWarnings("unchecked")
			List<String> list = (List<String>) obj.getData();
			List<String> list2 = new ArrayList<>();
			String yearString = list.get(1);
			String monthName = list.get(2);
			String dateString;
			Month month = Month.valueOf(monthName.toUpperCase());
			int monthNumber = month.getValue();
			if (monthNumber < 9) {
				dateString = yearString + "-0" + monthNumber + "-01";
			} else {
				dateString = yearString + "-" + monthNumber + "-01";
			}
			String matchingDate = dateString;
			matchingDate = matchingDate.substring(0, 8);
			if (monthNumber == 2) {
				matchingDate = matchingDate + "28";
			} else {
				matchingDate = matchingDate + "30";
			}
			try {
				pstmt = con.prepareStatement("SELECT t1.machine_name, count(t2.order_code) AS numOfOrders "
						+ "FROM machines t1 " + "INNER JOIN machine_orders t2 ON t1.machine_code = t2.machine_code "
						+ "INNER JOIN orders t3 ON t3.order_code=t2.order_code "
						+ "WHERE t1.region=? AND t3.order_date BETWEEN ? AND ? " + "GROUP BY t1.machine_code");
				pstmt.setString(1, list.get(0));
				pstmt.setString(2, dateString);
				pstmt.setString(3, matchingDate);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					list2.add(rs.getString("machine_name"));
					list2.add(rs.getString("numOfOrders"));
				}
				obj.setData(list2);
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
				obj.setResponse(Response.GETORDERREPORT_UNSUCCESSFULLY);
				return;
			}
			obj.setResponse(Response.GETORDERREPORT_SUCCESSFULLY);
		} else
			obj.setResponse(Response.GETORDERREPORT_UNSUCCESSFULLY);
	}
	/**
	 * This method is used to retrieve the inventory report of a specific vending
	 * machine. The method takes in a Transaction object and a Connection object as
	 * parameters. The Transaction object should contain the name of the machine for
	 * which the inventory report is needed. The Connection object is used to
	 * connect to the database. The method will return a list of ProductsInMachine
	 * objects, containing the product code, name, stock, price, and status of stock
	 * for each product in the machine. If the method is unable to retrieve the
	 * inventory report, it will set the response in the Transaction object to
	 * GETINVENTORYREPORT_UNSUCCESSFULLY. If the method is successful in retrieving
	 * the inventory report, it will set the response in the Transaction object to
	 * GETINVENTORYREPORT_SUCCESSFULLY.
	 * 
	 * @param obj - A Transaction object containing the name of the machine for
	 *            which the inventory report is needed.
	 * @param con - A Connection object used to connect to the database.
	 */
	public static void getInventoryReport(Transaction obj, Connection con) {
		if (obj instanceof Transaction) {
			String machine = (String) obj.getData();
			ArrayList<ProductsInMachine> list = new ArrayList<ProductsInMachine>();
			PreparedStatement pstmt;
			try {
				if (machine.equals("")) {
					obj.setResponse(Response.GETINVENTORYREPORT_UNSUCCESSFULLY);
					return;
				}
				pstmt = con.prepareStatement("SELECT t2.pro_code, t3.pro_name, t2.stock,  t3.price, t2.status_stock"
						+ " FROM machines t1" + " JOIN productinmachine t2 ON t1.machine_code = t2.machine_code"
						+ " JOIN products t3 ON t2.pro_code = t3.pro_code" + " WHERE t1.machine_name = ?");
				pstmt.setString(1, machine);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					list.add(new ProductsInMachine(rs.getString("pro_code"), rs.getString("pro_name"),
							rs.getString("stock"), rs.getString("price"), rs.getString("status_stock")));
				}
				obj.setData(list);
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
				obj.setResponse(Response.GETINVENTORYREPORT_UNSUCCESSFULLY);
				return;
			}
			obj.setResponse(Response.GETINVENTORYREPORT_SUCCESSFULLY);
		} else
			obj.setResponse(Response.GETINVENTORYREPORT_UNSUCCESSFULLY);
	}

	/**
	 * This method is used to retrieve the customer activity report for a specific
	 * year. The method takes in a Transaction object and a Connection object as
	 * parameters. The Transaction object should contain the year for which the
	 * customer activity report is needed. The Connection object is used to connect
	 * to the database. The method will return a list of integers, containing the
	 * number of orders and the corresponding month for each client. If the method
	 * is unable to retrieve the customer activity report, it will set the response
	 * in the Transaction object to GETCUSTOMERREPORT_UNSUCCESSFULLY. If the method
	 * is successful in retrieving the customer activity report, it will set the
	 * response in the Transaction object to GETCUSTOMERREPORT_SUCCESSFULLY.
	 * 
	 * @param obj - A Transaction object containing the year for which the customer
	 *            activity report is needed.
	 * @param con - A Connection object used to connect to the database.
	 */

	public static void getCustomerActivityReport(Transaction obj, Connection con) {
		if (obj instanceof Transaction) {
			PreparedStatement pstmt;
			List<String> list = new ArrayList<>();
			String yearString = (String) obj.getData();
			String startString = yearString + "-01" + "-01";
			String endString = yearString + "-12" + "-30";
			try {
				pstmt = con.prepareStatement("SELECT COUNT(*) as num_orders, EXTRACT(MONTH FROM order_date) as month "
						+ "FROM orders " + "WHERE order_date BETWEEN ? AND ? " + "GROUP BY client_id, month");
				pstmt.setString(1, startString);
				pstmt.setString(2, endString);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					list.add(rs.getString("month"));
					list.add(rs.getString("num_orders"));
				}
				obj.setData(list);
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
				obj.setResponse(Response.GETCUSTOMERREPORT_UNSUCCESSFULLY);
				return;
			}
			obj.setResponse(Response.GETCUSTOMERREPORT_SUCCESSFULLY);
		} else
			obj.setResponse(Response.GETCUSTOMERREPORT_UNSUCCESSFULLY);
	}

	/**
	 * This method is used to retrieve the customer activity report for a specific
	 * year and region. The method takes in a Transaction object and a Connection
	 * object as parameters. The Transaction object should contain the year, month,
	 * and region for which the customer activity report is needed. The Connection
	 * object is used to connect to the database. The method will return a list of
	 * integers, containing the number of orders and the corresponding month for
	 * each client. If the method is unable to retrieve the customer activity
	 * report, it will set the response in the Transaction object to
	 * GETCUSTOMERREPORT_BY_REGION_UNSUCCESSFULLY. If the method is successful in
	 * retrieving the customer activity report, it will set the response in the
	 * Transaction object to GETCUSTOMERREPORT_BY_REGION_SUCCESSFULLY.
	 * 
	 * @param obj - A Transaction object containing the year, month, and region for
	 *            which the customer activity report is needed.
	 * @param con - A Connection object used to connect to the database.
	 */

	@SuppressWarnings("unchecked")
	public static void getCustomerActivityReportByRegion(Transaction obj, Connection con) {
		if (obj instanceof Transaction) {
			PreparedStatement pstmt;
			List<String> list = new ArrayList<>();
			List<String> queryInputs = new ArrayList<>();
			queryInputs = (List<String>) obj.getData();
			String region = queryInputs.get(0);
			String yearString = queryInputs.get(1);
			String startString = yearString + "-01" + "-01";
			String endString = yearString + "-12" + "-30";
			try {
				pstmt = con.prepareStatement("SELECT COUNT(*) as num_orders, EXTRACT(MONTH FROM order_date) as month "
						+ "FROM orders " + "JOIN users ON orders.client_id = users.id "
						+ "WHERE order_date BETWEEN ? AND ? AND users.region = ?" + "GROUP BY orders.client_id, month");
				pstmt.setString(1, startString);
				pstmt.setString(2, endString);
				pstmt.setString(3, region);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					list.add(rs.getString("month"));
					list.add(rs.getString("num_orders"));
				}
				obj.setData(list);
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
				obj.setResponse(Response.GETCUSTOMERREPORT_BY_REGION_UNSUCCESSFULLY);
				return;
			}
			obj.setResponse(Response.GETCUSTOMERREPORT_BY_REGION_SUCCESSFULLY);
		} else
			obj.setResponse(Response.GETCUSTOMERREPORT_BY_REGION_UNSUCCESSFULLY);
	}

	/**
	 * Sets the quantity limit for a specific machine in the database.
	 *
	 * @param obj The Transaction object containing the data for the update
	 * @param con The connection to the database
	 */
	@SuppressWarnings("unchecked")
	public static void setLimitQuantityInMachine(Transaction obj, Connection con) {
		if (obj instanceof Transaction) {
			List<String> queryInputs = new ArrayList<>();
			queryInputs = (List<String>) obj.getData();
			String quantityLimit = (String) queryInputs.get(0);
			String machine = (String) queryInputs.get(1);
			PreparedStatement pstmt;
			try {
				for (String a : queryInputs) {
					if (a.equals("")) {
						obj.setResponse(Response.UPDATE_LIMIT_QUANTITY_IN_MACHINE_UNSUCCESSFULLY);
						return;
					}
				}
				pstmt = con.prepareStatement("UPDATE machines SET quantity_limit=? WHERE machine_name=?;");
				pstmt.setString(1, quantityLimit);
				pstmt.setString(2, machine);
				if (pstmt.executeUpdate() == 0) {
					obj.setResponse(Response.UPDATE_LIMIT_QUANTITY_IN_MACHINE_UNSUCCESSFULLY);
					return;
				}

			} catch (SQLException e) {
				e.printStackTrace();
				obj.setResponse(Response.UPDATE_LIMIT_QUANTITY_IN_MACHINE_UNSUCCESSFULLY);
				return;
			}
			obj.setResponse(Response.UPDATE_LIMIT_QUANTITY_IN_MACHINE_SUCCESSFULLY);
		}
	}

	/**
	 * Sets the stock status for a specific machine in the database based on the
	 * quantity of products in the machine.
	 *
	 * @param obj The Transaction object containing the data for the update
	 * @param con The connection to the database
	 */
	public static void setStatusStockByQuantity(Transaction obj, Connection con) {
		if (obj instanceof Transaction) {
			String machine = (String) obj.getData();
			PreparedStatement pstmt;
			try {
				pstmt = con.prepareStatement("UPDATE productinmachine p " + "SET p.status_stock = 'REFILL_REQUEST' "
						+ "WHERE EXISTS (" + "SELECT 1 FROM machines m "
						+ "WHERE m.machine_name = ? AND m.machine_code = p.machine_code AND m.quantity_limit > p.stock)");
				pstmt.setString(1, machine);
				if (pstmt.executeUpdate() == 0) {
					obj.setResponse(Response.UPDATE_QUANTITY_STATUS_UNSUCCESSFULLY);
					return;
				}

			} catch (SQLException e) {
				e.printStackTrace();
				obj.setResponse(Response.UPDATE_QUANTITY_STATUS_UNSUCCESSFULLY);
				return;
			}
			obj.setResponse(Response.UPDATE_QUANTITY_STATUS_SUCCESSFULLY);
		}
	}

	/**
	 * Retrieves all user registration requests from the database.
	 *
	 * @param obj The Transaction object containing the data for the update
	 * @param con The connection to the database
	 */
	public static void getUserRegisterRequest(Transaction obj, Connection con) {
		if (obj instanceof Transaction) {
			Statement stmt;
			ArrayList<User> list = new ArrayList<User>();
			try {
				stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT id, firstName, lastName, telephone, email, region "
						+ "FROM users " + "WHERE users.role = 'USER' AND status='REQ_TO_REG'");
				while (rs.next()) {
					list.add(new User(rs.getInt("id"), rs.getString("firstName"), rs.getString("lastName"),
							rs.getString("telephone"), rs.getString("email"),
							RegionEnum.valueOf(rs.getString("region"))));

				}
				obj.setData(list);
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
				obj.setResponse(Response.GET_USER_REGISTER_REQUEST_UNSUCCESSFULLY);
				return;
			}
			obj.setResponse(Response.GET_USER_REGISTER_REQUEST_SUCCESSFULLY);
		} else
			obj.setResponse(Response.GET_USER_REGISTER_REQUEST_UNSUCCESSFULLY);
	}

	/**
	 * Updating approved registration request to the database.
	 *
	 * @param obj The Transaction object containing the data for the update
	 * @param con The connection to the database
	 */
	public static void approveRegisterRequest(Transaction obj, Connection con) {
		if (obj instanceof Transaction) {
			int userId = (int) obj.getData();
			PreparedStatement pstmt;
			try {
				pstmt = con
						.prepareStatement("UPDATE users SET role='CUSTOMER', status= 'REGISTER_APPROVED' WHERE id=?;");
				pstmt.setInt(1, userId);
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
	 * Getting low limit quantity in machine.
	 *
	 * @param obj The Transaction object containing the data for the update
	 * @param con The connection to the database
	 */
	public static void getLimitByMachine(Transaction obj, Connection con) {
		if (obj instanceof Transaction) {
			String machine = (String) obj.getData();
			String limit = "";
			PreparedStatement pstmt;
			try {
				pstmt = con.prepareStatement("SELECT quantity_limit FROM machines WHERE machine_name = ?");
				pstmt.setString(1, machine);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					limit = rs.getString("quantity_limit");
				}
				obj.setData(limit);
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
				obj.setResponse(Response.GET_LIMIT_BY_MACHINE_UNSUCCESSFULLY);
				return;
			}
			obj.setResponse(Response.GET_LIMIT_BY_MACHINE_SUCCESSFULLY);
		} else
			obj.setResponse(Response.GET_LIMIT_BY_MACHINE_UNSUCCESSFULLY);
	}

	/**
	 * 
	 * This method generates an order report in XLS format for a given month and
	 * year, and writes it to a specified file location.
	 * 
	 * @param obj a Transaction object containing the necessary data for generating
	 *            the report
	 * @param con a Connection object used to connect to the database and execute
	 *            the query
	 */
	public static void getOrderReportToXsl(Transaction obj, Connection con) {
		if (obj instanceof Transaction) {
			PreparedStatement pstmt;
			@SuppressWarnings("unchecked")
			List<String> list = (List<String>) obj.getData();
			String yearString = list.get(1);
			String monthName = list.get(2);
			String dateString;
			Month month = Month.valueOf(monthName.toUpperCase());
			int monthNumber = month.getValue();
			if (monthNumber < 9) {
				dateString = yearString + "-0" + monthNumber + "-01";
			} else {
				dateString = yearString + "-" + monthNumber + "-01";
			}
			String matchingDate = dateString;
			matchingDate = matchingDate.substring(0, 8);
			if (monthNumber == 2) {
				matchingDate = matchingDate + "28";
			} else {
				matchingDate = matchingDate + "30";
			}
			try {
				pstmt = con.prepareStatement("SELECT t1.machine_name, count(t2.order_code) AS numOfOrders "
						+ "FROM machines t1 " + "INNER JOIN machine_orders t2 ON t1.machine_code = t2.machine_code "
						+ "INNER JOIN orders t3 ON t3.order_code=t2.order_code "
						+ "WHERE t1.region=? AND t3.order_date BETWEEN ? AND ? "
						+ "GROUP BY t1.machine_code INTO OUTFILE "
						+ "'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/Order_" + monthName + "_" + yearString
						+ "_Report.xls'");
				pstmt.setString(1, list.get(0));
				pstmt.setString(2, dateString);
				pstmt.setString(3, matchingDate);
				pstmt.executeQuery();
			} catch (SQLException e) {
				e.printStackTrace();
				obj.setResponse(Response.GETORDERREPORT_TO_XLS_UNSUCCESSFULLY);
				return;
			}
			obj.setResponse(Response.GETORDERREPORT_TO_XLS_SUCCESSFULLY);
		} else
			obj.setResponse(Response.GETORDERREPORT_TO_XLS_UNSUCCESSFULLY);
	}

	/**
	 * 
	 * This method generates an inventory report in XLS format for a specific
	 * machine, and writes it to a specified file location.
	 * 
	 * @param obj a Transaction object containing the necessary data for generating
	 *            the report (machine name)
	 * @param con a Connection object used to connect to the database and execute
	 *            the query
	 */
	public static void getInventoryReportToXsl(Transaction obj, Connection con) {
		if (obj instanceof Transaction) {
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String currentDate = dateFormat.format(calendar.getTime());
			String machine = (String) obj.getData();
			PreparedStatement pstmt;
			try {
				if (machine.equals("")) {
					obj.setResponse(Response.GET_INVENTORY_REPORT_TO_XLS_UNSUCCESSFULLY);
					return;
				}
				pstmt = con.prepareStatement("SELECT t2.pro_code, t3.pro_name, t2.stock,  t3.price, t2.status_stock"
						+ " FROM machines t1" + " JOIN productinmachine t2 ON t1.machine_code = t2.machine_code"
						+ " JOIN products t3 ON t2.pro_code = t3.pro_code"
						+ " WHERE t1.machine_name = ? INTO OUTFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/Inventory_"
						+ machine + "_" + currentDate + "_Report.xls'");
				pstmt.setString(1, machine);
				pstmt.executeQuery();
			} catch (SQLException e) {
				e.printStackTrace();
				obj.setResponse(Response.GET_INVENTORY_REPORT_TO_XLS_UNSUCCESSFULLY);
				return;
			}
			obj.setResponse(Response.GET_INVENTORY_REPORT_TO_XLS_SUCCESSFULLY);
		} else
			obj.setResponse(Response.GET_INVENTORY_REPORT_TO_XLS_UNSUCCESSFULLY);
	}

	/**
	 * 
	 * This method generates a customer activity report in XLS format for a specific
	 * year, and writes it to a specified file location.
	 * 
	 * @param obj a Transaction object containing the necessary data for generating
	 *            the report (year)
	 * @param con a Connection object used to connect to the database and execute
	 *            the query
	 */
	public static void getCustomerReportToXsl(Transaction obj, Connection con) {
		if (obj instanceof Transaction) {
			PreparedStatement pstmt;
			String yearString = (String) obj.getData();
			String startString = yearString + "-01" + "-01";
			String endString = yearString + "-12" + "-30";
			try {
				pstmt = con.prepareStatement(
						"SELECT COUNT(*) as num_orders, EXTRACT(MONTH FROM order_date) as month, client_id as 'Client ID' "
								+ "FROM orders WHERE order_date BETWEEN ? AND ? GROUP BY client_id, month INTO OUTFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/CustomerActivity_"
								+ yearString + "_Report.xls'");
				pstmt.setString(1, startString);
				pstmt.setString(2, endString);
				pstmt.executeQuery();
			} catch (SQLException e) {
				e.printStackTrace();
				obj.setResponse(Response.GET_CUSTOMER_REPORT_TO_XLS_UNSUCCESSFULLY);
				return;
			}
			obj.setResponse(Response.GET_CUSTOMER_REPORT_TO_XLS_SUCCESSFULLY);
		} else
			obj.setResponse(Response.GET_CUSTOMER_REPORT_TO_XLS_UNSUCCESSFULLY);
	}

	/**
	 * 
	 * This method generates a customer activity report in XLS format for a specific
	 * region and year, and writes it to a specified file location.
	 * 
	 * @param obj a Transaction object containing the necessary data for generating
	 *            the report (region, year)
	 * @param con a Connection object used to connect to the database and execute
	 *            the query
	 */
	@SuppressWarnings("unchecked")
	public static void getCustomerReportToXslByRegion(Transaction obj, Connection con) {
		if (obj instanceof Transaction) {
			PreparedStatement pstmt;
			List<String> queryInputs = new ArrayList<>();
			queryInputs = (List<String>) obj.getData();
			String region = queryInputs.get(0);
			String yearString = queryInputs.get(1);
			String startString = yearString + "-01" + "-01";
			String endString = yearString + "-12" + "-30";
			try {
				pstmt = con.prepareStatement(
						"SELECT COUNT(*) as num_orders, EXTRACT(MONTH FROM order_date) as month , users.region as Region, client_id as 'Client ID' "
								+ "FROM orders JOIN users ON orders.client_id = users.id "
								+ "WHERE order_date BETWEEN ? AND ? AND users.region = ? GROUP BY orders.client_id, month INTO OUTFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/CustomerActivity_"
								+ yearString + "_Report.xls'");
				pstmt.setString(1, startString);
				pstmt.setString(2, endString);
				pstmt.setString(3, region);
				pstmt.executeQuery();
			} catch (SQLException e) {
				e.printStackTrace();
				obj.setResponse(Response.GET_CUSTOMER_REPORT_BY_REGION_TO_XLS_UNSUCCESSFULLY);
				return;
			}
			obj.setResponse(Response.GET_CUSTOMER_REPORT_BY_REGION_TO_XLS_SUCCESSFULLY);
		} else
			obj.setResponse(Response.GET_CUSTOMER_REPORT_BY_REGION_TO_XLS_UNSUCCESSFULLY);
	}
}