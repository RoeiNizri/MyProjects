package dataBase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import common.Response;
import common.Transaction;
import enums.OrderStatusEnum;
import enums.RegionEnum;
import enums.SupplyMethodEnum;
import logic.DeliveryOrder;

public class DeliveryOperatorQuaries {
	/**
	 * 
	 * Get all the orders that match the following conditions: The region of the
	 * user who placed the order is the same as the region specified in the input
	 * The supply method of the order is "DELIVERY" The status of the order is not
	 * "COMPLETE"
	 * 
	 * @param msg a Transaction object that contains the region of the user who
	 *            placed the orders
	 */
	public static void getOrders(Transaction msg) {
		List<String> Alist = new ArrayList<>();
		if (msg instanceof Transaction) {
			Object obj = msg.getData();
			RegionEnum region = RegionEnum.valueOf(obj.toString());
			String method = SupplyMethodEnum.DELIVERY.toString();
			try {
				ResultSet rs = dbController.getInstance().executeQuery(
						"select * from orders where (client_id IN (select id from ekurt.users where region = '" + region + "')) AND supply_method='" + method + "' AND status != 'COMPLETE'");				if (!(rs.next())) {
					msg.setResponse(Response.FAILED_TO_GET_ORDERS);
					return;
				}
				rs.previous();
				List<DeliveryOrder> orders = new ArrayList<>();
				while (rs.next()) {
					String orderId = rs.getString("order_code");
					String clientId = rs.getString("client_id");
					Timestamp OrderDate = rs.getTimestamp("order_date");
					Duration duration = Duration.ofHours(3);
					OrderDate = Timestamp.from(OrderDate.toInstant().plus(duration));
					duration = Duration.ofMinutes(30);
					OrderDate = Timestamp.from(OrderDate.toInstant().plus(duration));
					Timestamp supplyDate = rs.getTimestamp("supply_date");
					if(supplyDate!=null) {
					supplyDate = Timestamp.from(supplyDate.toInstant().plus(duration));
					duration = Duration.ofMinutes(30);
					supplyDate = Timestamp.from(supplyDate.toInstant().plus(duration));
					}
					float price = rs.getFloat("total_price");
					String enumValue = rs.getString("status");
					OrderStatusEnum suppMethod = OrderStatusEnum.valueOf(enumValue);
					DeliveryOrder order = new DeliveryOrder(orderId, clientId, OrderDate, supplyDate, price,suppMethod);
					orders.add(order);
				}
				msg.setResponse(Response.FOUND_ORDERS);
				msg.setData(orders);
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
				msg.setResponse(Response.FAILED_TO_GET_ORDERS);
				return;
			}
		} else
			msg.setResponse(Response.FAILED_TO_GET_ORDERS);
	}
	
	/**
	 * Approve an order, the method will update the order status to "ON_THE_WAY" if
	 * the order status is "WAIT_APPROVAL" and order code is matched.
	 *
	 * @param msg Transaction object that contains a DeliveryOrder object as data.
	 */
	public static void ApproveOrder(Transaction msg) {
	if (msg instanceof Transaction) {
		DeliveryOrder obj = (DeliveryOrder) msg.getData();
		int rowsAffected = dbController.getInstance().executeUpdate(
                "UPDATE orders SET status = 'ON_THE_WAY' WHERE "
                + "order_code ='"+obj.getOrderId()+"' AND status = 'WAIT_APPROVAL'");
		if (rowsAffected ==1) {
			msg.setResponse(Response.APPROVED_ORDER_SUCCSSEFULLY);
			return;
	}
		else {
			msg.setResponse(Response.FAILED_TO_APPROVE_ORDER);
			return;
		}
	}else
			msg.setResponse(Response.FAILED_TO_APPROVE_ORDER);
	}
	/**
	 * 
	 * The CompleteOrder method is used to mark a delivery order as complete in the
	 * database. It takes in a Transaction object as an argument and checks if it is
	 * an instance of the Transaction class. If it is, it retrieves the
	 * DeliveryOrder object from the transaction data and updates the status of the
	 * order in the database to 'COMPLETE' using the order code. The method then
	 * sets the response of the transaction object to indicate if the update was
	 * successful.
	 * 
	 * @param msg - A Transaction object containing the delivery order information
	 */
	public static void CompleteOrder(Transaction msg) {
		if (msg instanceof Transaction) {
			DeliveryOrder obj = (DeliveryOrder) msg.getData();
			int rowsAffected = dbController.getInstance().executeUpdate(
	                "UPDATE orders SET status = 'COMPLETE' WHERE "
	                + "order_code ='"+obj.getOrderId()+"' AND status = 'RECEIVED'");
			if (rowsAffected ==1) {
				msg.setResponse(Response.COMPLETED_ORDER_SUCCSSEFULLY);
				return;
		}
			else {
				msg.setResponse(Response.FAILED_TO_COMPLETE_ORDER);
				return;
			}
		}else
				msg.setResponse(Response.FAILED_TO_COMPLETE_ORDER);
		}
}