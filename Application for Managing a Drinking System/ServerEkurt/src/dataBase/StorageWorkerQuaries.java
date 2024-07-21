package dataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import common.Response;
import common.Transaction;
import logic.ProductsInMachine;
/**
*
* The StorageWorkerQuaries is a class that contains methods for making database
* queries related to the storage worker functionality.
* 
* @author Alex & Alin
*
*/
public class StorageWorkerQuaries {
	/**
	 * The getRefillStock method is used to retrieve the products that need to be
	 * refilled in a specific vending machine. It takes in a Transaction object and
	 * a Connection to the database as parameters. If the input is valid, it
	 * executes a SQL query to retrieve the necessary information and sets the
	 * response of the Transaction object to indicate success. * @param obj A
	 * Transaction object
	 * 
	 * @param obj A Transaction object
	 * @param con A Connection object to connect to the database
	 */
	public static void getRefillStock(Transaction obj, Connection con) {
		if (obj instanceof Transaction) {
			String machine = (String) obj.getData();
			ArrayList<ProductsInMachine> list = new ArrayList<ProductsInMachine>();
			PreparedStatement pstmt;
			try {
				if (machine.equals("")) {
					obj.setResponse(Response.GETSTOCK_UNSUCCESSFULLY);
					return;
				}
				pstmt = con.prepareStatement("SELECT t2.pro_code, t3.pro_name, t2.stock,  t1.quantity_limit, t2.status_stock"
						+ " FROM machines t1" + " JOIN productinmachine t2 ON t1.machine_code = t2.machine_code"
						+ " JOIN products t3 ON t2.pro_code = t3.pro_code" + " WHERE t1.machine_name = ? AND t2.status_stock = 'REFILL_REQUEST'");
				pstmt.setString(1, machine);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					list.add(new ProductsInMachine(rs.getString("pro_code"), rs.getString("pro_name"),
							rs.getString("stock"), rs.getInt("quantity_limit"), rs.getString("status_stock")));
				}
				obj.setData(list);
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
				obj.setResponse(Response.GETSTOCK_UNSUCCESSFULLY);
				return;
			}
			obj.setResponse(Response.GETSTOCK_SUCCESSFULLY);
		} else
			obj.setResponse(Response.GETSTOCK_UNSUCCESSFULLY);
	}
	/**
	 * The UpdateQuantityInMachine method is used to update the stock of a product
	 * in a specific vending machine. It takes in a Transaction object and a
	 * Connection to the database as parameters. If the input is valid, it executes
	 * a SQL query to update the stock and sets the response of the Transaction
	 * object to indicate success.
	 *
	 * @param obj A Transaction object
	 * @param con A Connection object to connect to the database
	 */
	public static void UpdateQuantityInMachine(Transaction obj, Connection con) {
		if (obj instanceof Transaction) {
			List<String> queryInputs = new ArrayList<>();
			queryInputs = (List<String>) obj.getData();
			String quantity = (String) queryInputs.get(0);
			String machine = (String) queryInputs.get(1);
			String prodID = (String) queryInputs.get(2);
			PreparedStatement pstmt;
			try {
				for (String a : queryInputs) {
					if (a.equals("")) {
						obj.setResponse(Response.UPDATE_QUANTITY_IN_MACHINE_UNSUCCESSFULLY);
						return;
					}
					}
				pstmt = con.prepareStatement("UPDATE productinmachine SET stock=?, status_stock='IN_STOCK' WHERE machine_code=(SELECT machine_code FROM machines WHERE machine_name=?) AND pro_code=?;");
				pstmt.setString(1, quantity);
				pstmt.setString(2, machine);
				pstmt.setString(3, prodID);
				if (pstmt.executeUpdate() == 0) {
					obj.setResponse(Response.UPDATE_QUANTITY_IN_MACHINE_UNSUCCESSFULLY);
					return;
				}

			} catch (SQLException e) {
				e.printStackTrace();
				obj.setResponse(Response.UPDATE_QUANTITY_IN_MACHINE_UNSUCCESSFULLY);
				return;
			}
			obj.setResponse(Response.UPDATE_QUANTITY_IN_MACHINE_SUCCESSFULLY);
		}
	}
}
