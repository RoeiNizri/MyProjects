package dataBase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import common.Response;
import common.Transaction;
import enums.OrderStatusEnum;
import enums.RegionEnum;
import enums.RoleEnum;
import enums.StatusEnum;
import enums.SupplyMethodEnum;
import logic.*;

/**
 * This class defines the methods that send query's to the DB and receive the
 * data back from the DB and return it to the relevant controllers and classes
 * that need to use that information
 */
public class OrderQueries {
	/**
	 * Get machine information from the database, and store it in the obj.data
	 * parameter.
	 *
	 * @param obj A Transaction object
	 * @param con A Connection object to connect to the database
	 */
	public static void getMachineList(Transaction obj) {
		ResultSet rs = dbController.getInstance().executeQuery("SELECT * FROM ekurt.machines");
		if (rs == null)
			obj.setResponse(Response.FAILED);
		else {
			List<Machine> machines = new ArrayList<>(Machine.createMachineListFromResultSet(rs));
			List<String> machineNames = new ArrayList<>();
			for (int i = 0; i < machines.size(); i++) {
				machineNames.add(machines.get(i).getMachine_name());
			}
			obj.setData(machineNames);
			obj.setResponse(Response.FOUND_MACHINE_NAMES);
		}
	}
	/**
	 * The getProductCodesInMachine method is used to retrieve the products that are
	 * currently in stock in a specific vending machine. It takes in a Transaction
	 * object as a parameter. It retrieves the name of the chosen machine from the
	 * data field of the Transaction object and uses it to execute a SQL query to
	 * retrieve the necessary information. If the query is successful, it sets the
	 * response of the Transaction object to indicate success and stores the
	 * retrieved information in the data field of the Transaction object.
	 * 
	 * @param obj
	 */
	public static void getProductCodesInMachine(Transaction obj) {
		String chosenMachine = (String) obj.getData();
		ResultSet rs = dbController.getInstance().executeQuery(
				"SELECT pro_code, stock, is_in_sale FROM ekurt.productinmachine where machine_code = (SELECT machine_code FROM ekurt.machines where machine_name='"
						+ chosenMachine + "') AND (stock > 0);");
		if (rs == null)
			obj.setResponse(Response.FAILED);
		else {
			List<ProductInGrid> products = new ArrayList<>();
			products = getProduct(rs);
			obj.setData(products);
			obj.setResponse(Response.FOUND_PRODUCTS_FOR_DISPLAY);
		}
	}
	/**
	 * The getProductCodesInMachineNotInStock method is used to retrieve the
	 * products that are currently not in stock in a specific vending machine. It
	 * takes in a Transaction object as a parameter. It retrieves the name of the
	 * chosen machine from the data field of the Transaction object and uses it to
	 * execute a SQL query to retrieve the necessary information. If the query is
	 * successful, it sets the response of the Transaction object to indicate
	 * success and stores the retrieved information in the data field of the
	 * Transaction object.
	 * 
	 * @param obj
	 */
	public static void getProductCodesInMachineNotInStock(Transaction obj) {
		String chosenMachine = (String) obj.getData();
		ResultSet rs = dbController.getInstance().executeQuery(
				"SELECT pro_code, stock, is_in_sale FROM ekurt.productinmachine where machine_code = (SELECT machine_code FROM ekurt.machines where machine_name='"
						+ chosenMachine + "') AND (stock = 0);");
		if (rs == null)
			obj.setResponse(Response.FAILED);
		else {
			List<ProductInGrid> products = new ArrayList<>();
			products = getProduct(rs);
			obj.setData(products);
			obj.setResponse(Response.FOUND_PRODUCTS_FOR_DISPLAY);
		}
	}
	/**
	 * The getProductsFromWarehous method is used to retrieve the products that are
	 * currently in stock in a specific warehouse. It takes in a Transaction object
	 * as a parameter. It retrieves the region of the chosen warehouse from the data
	 * field of the Transaction object and uses it to execute a SQL query to
	 * retrieve the necessary information. If the query is successful, it sets the
	 * response of the Transaction object to indicate success and stores the
	 * retrieved information in the data field of the Transaction object.
	 * 
	 * @param obj
	 */
	public static void getProductsFromWarehous(Transaction obj) {
		RegionEnum region = (RegionEnum) obj.getData();
		ResultSet rs = dbController.getInstance().executeQuery(
				"SELECT pro_code, stock, is_in_sale from ekurt.productinwarehouse where region = '" + region + "';");
		if (rs == null)
			obj.setResponse(Response.FAILED);
		else {
			List<ProductInGrid> products = new ArrayList<>();
			products = getProduct(rs);
			obj.setData(products);
			obj.setResponse(Response.FOUND_PRODUCTS_FOR_DISPLAY);
		}
	}
	/**
	 * 
	 * This method retrieves a list of products from a ResultSet containing product
	 * codes and stock levels. It also retrieves additional information about each
	 * product, such as whether it is currently on sale and the offer name (if
	 * applicable). The information is added to a list of ProductInGrid objects,
	 * which are returned by the method.
	 * 
	 * @param productCodesAndStock the ResultSet containing product codes and stock
	 *                             levels
	 * @return a list of ProductInGrid objects containing the retrieved product
	 *         information
	 */
	public static List<ProductInGrid> getProduct(ResultSet productCodesAndStock) {
		List<ProductInGrid> products = new ArrayList<>();
		String pro_code;
		int stock;
		boolean is_in_sale;
		String offerName = null;
		try {
			while (productCodesAndStock.next()) {
				pro_code = productCodesAndStock.getString("pro_code");
				stock = productCodesAndStock.getInt("stock");
				is_in_sale = productCodesAndStock.getBoolean("is_in_sale");
				ResultSet rs = dbController.getInstance()
						.executeQuery("SELECT * FROM ekurt.products where pro_code = '" + pro_code + "';");

				if (is_in_sale) {
					offerName = getOfferName(pro_code);
				}
				products.add(ProductInGrid.getProductFromResultSet(rs, stock, is_in_sale, offerName));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return products;

	}
	/**
	 * 
	 * This method retrieves the offer name for a specific product from the offers
	 * table in the database.
	 * 
	 * @param pro_code the product code of the product to retrieve the offer name
	 *                 for
	 * @return the offer name of the product, or null if no offer is found
	 */
	public static String getOfferName(String pro_code) {
		String offerName = null;
		ResultSet rs = dbController.getInstance()
				.executeQuery("SELECT discount FROM  ekurt.offers WHERE pro_code='" + pro_code + "';");
		try {
			if (rs.next()) {
				offerName = rs.getString("discount");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return offerName;

	}

	public static void getProductInMachineStock(Transaction obj) {
		if (obj.getData() instanceof ArrayList<?>) {
			ArrayList<String> list = ArrayList.class.cast(obj.getData());

			int stock = 0;
			ResultSet rs = dbController.getInstance().executeQuery(
					"SELECT stock FROM ekurt.productinmachine WHERE pro_code = (SELECT pro_code FROM ekurt.products WHERE pro_name = '"
							+ list.get(1)
							+ "') AND (machine_code = (SELECT machine_code FROM ekurt.machines where machine_name='"
							+ list.get(0) + "') );");
			if (rs == null)
				obj.setResponse(Response.FAILED);
			else {
				try {
					if (rs.next()) {
						stock = rs.getInt("stock");
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				obj.setData(stock);
				obj.setResponse(Response.FOUND_CUR_STOCK);
			}
		}
	}
	/**
	 * 
	 * This method retrieves the payment details of a user by their ID and stores
	 * the information in a list of strings. It sets the Transaction object's data
	 * to the list of payment details and response to FOUND_PAYMENT_DETAILS if
	 * successful.
	 * 
	 * @param obj the Transaction object containing the user ID and response
	 */
	public static void getPaymentDetails(Transaction obj) {
		if (obj.getData() instanceof Integer) {
			int userId = (int) obj.getData();
			ResultSet rs = dbController.getInstance().executeQuery(
					"SELECT email, CreditCardNum, CVV, ExpDate FROM ekurt.users , ekurt.paymentdetails WHERE users.id ='"
							+ userId + "' AND paymentdetails.Id = '" + userId + "';");
			if (rs == null)
				obj.setResponse(Response.FAILED);
			else {

				List<String> paymentDetails = new ArrayList<>();
				try {
					if (rs.next()) {
						paymentDetails.add(rs.getString("email"));
						paymentDetails.add(rs.getString("CreditCardNum"));
						paymentDetails.add(rs.getString("CVV"));
						paymentDetails.add(rs.getString("ExpDate"));

					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				obj.setData(paymentDetails);
				obj.setResponse(Response.FOUND_PAYMENT_DETAILS);
			}

		} else {
			System.out.println("Not Integer");
		}
	}
	/**
	 * 
	 * This method places a local order by inserting the order information into the
	 * orders table and the machine_order table in the database. It also retrieves
	 * the product codes for the products in the order and inserts the information
	 * into the order_product table. If the insertion is successful, the response in
	 * the Transaction object is set to SUCCESS. Otherwise, it is set to FAILED.
	 * 
	 * @param obj the Transaction object containing the LocalOrder object and
	 *            response
	 */
	public static void placeLocalOrder(Transaction obj) {
		LocalOrder order = (LocalOrder) obj.getData();
		int affectedRows;
		HashMap<Integer, Product> productAndQuantity = new HashMap<>();
		String machine_code = null;
		int order_code = 0;
		affectedRows = dbController.getInstance().executeUpdate(
					"insert into ekurt.orders (client_id, Order_date, supply_date, supply_method, total_price, status) values('"
							+ order.getClient().getId() + "', '" + order.getDate() + "', '" + LocalDateTime.now()
							+ "', '" + SupplyMethodEnum.LOCAL + "'," + order.getTotalToPay() + ",'"
							+ OrderStatusEnum.COMPLETE + "');");
			if (affectedRows == 0)
				obj.setResponse(Response.FAILED);
			ResultSet rs = dbController.getInstance().executeQuery(
					"SELECT MAX(ekurt.orders.order_code), machine_code FROM ekurt.orders,ekurt.machines WHERE  machine_name='"
							+ order.getMachineName() + "'; ");
			try {
				if (rs.next()) {
					machine_code = rs.getString("machine_code");
					order_code = rs.getInt("MAX(ekurt.orders.order_code)");
					affectedRows = insertIntoMachineOrder(order_code, machine_code);
					if (affectedRows != 1)
						obj.setResponse(Response.FAILED);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

			for (int i = 0; i < order.getProducts().size(); i++) {
				String proName = order.getProducts().get(i).getName();

				rs = dbController.getInstance()
						.executeQuery("SELECT pro_code FROM ekurt.products WHERE pro_name='" + proName + "';");
				try {
					if (rs.next())
						order.getProducts().get(i).setProduct_code(rs.getString("pro_code"));
					productAndQuantity.put(order_code, order.getProducts().get(i));

					affectedRows = updateProStock(productAndQuantity, machine_code);
					if (affectedRows != 1)
						obj.setResponse(Response.FAILED);

					affectedRows = insertIntoProductsInOrder(productAndQuantity);
					if (affectedRows != 1)
						obj.setResponse(Response.FAILED);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (order.getClient().getStatus().equals(StatusEnum.FIRST_ORDER)) {
				affectedRows = upadteUserStatus(order.getClient().getId());
				if (affectedRows != 1)
					obj.setResponse(Response.FAILED);
			}
			if (order.getClient().getRole().equals(RoleEnum.SUBSCRIBER)) {
				affectedRows = dbController.getInstance()
						.executeUpdate("UPDATE ekurt.subscriberwallet SET balance = balance+" + order.getTotalToPay()
								+ "WHERE id='" + order.getClient().getId() + "';");
				if (affectedRows != 1)
					obj.setResponse(Response.FAILED);
			}
			obj.setResponse(Response.ORDER_PLACED_SUCCESSFULLY);
	}

	public static void placePickupOrder(Transaction obj) {
		PickUpOrder order = (PickUpOrder) obj.getData();
		int affectedRows;
		HashMap<Integer, Product> productAndQuantity = new HashMap<>();
		String machine_code = null;
		int order_code = 0;
		affectedRows = dbController.getInstance().executeUpdate(
				"insert into ekurt.orders (client_id, Order_date, supply_date, supply_method, total_price, status) values('"
						+ order.getClient().getId() + "', '" + order.getDate() + "', null, '" + SupplyMethodEnum.PICKUP
						+ "'," + order.getTotalToPay() + ",'" + OrderStatusEnum.WAIT_PICKUP + "');");
		if (affectedRows != 1)
			obj.setResponse(Response.FAILED);
		ResultSet rs = dbController.getInstance().executeQuery(
				"SELECT MAX(ekurt.orders.order_code), machine_code FROM ekurt.orders,ekurt.machines WHERE  machine_name='"
						+ order.getMachineName() + "'; ");
		try {
			if (rs.next()) {
				machine_code = rs.getString("machine_code");
				order_code = rs.getInt("MAX(ekurt.orders.order_code)");
				System.out.println(machine_code + " and " + order_code);
				affectedRows = insertIntoMachineOrder(order_code, machine_code);
				if (affectedRows != 1)
					obj.setResponse(Response.FAILED);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < order.getProducts().size(); i++) {
			String proName = order.getProducts().get(i).getName();

			rs = dbController.getInstance()
					.executeQuery("SELECT pro_code FROM ekurt.products WHERE pro_name='" + proName + "';");
			try {
				if (rs.next())
					order.getProducts().get(i).setProduct_code(rs.getString("pro_code"));
				productAndQuantity.put(order_code, order.getProducts().get(i));

				affectedRows = updateProStock(productAndQuantity, machine_code);
				if (affectedRows != 1)
					obj.setResponse(Response.FAILED);

				affectedRows = insertIntoProductsInOrder(productAndQuantity);
				if (affectedRows != 1)
					obj.setResponse(Response.FAILED);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (order.getClient().getStatus().equals(StatusEnum.FIRST_ORDER)) {
			affectedRows = upadteUserStatus(order.getClient().getId());
			if (affectedRows != 1)
				obj.setResponse(Response.FAILED);
		}
		if (order.getClient().getRole().equals(RoleEnum.SUBSCRIBER)) {
			affectedRows = dbController.getInstance()
					.executeUpdate("UPDATE ekurt.subscriberwallet SET balance = balance+" + order.getTotalToPay()
							+ "WHERE id='" + order.getClient().getId() + "';");
			if (affectedRows != 1)
				obj.setResponse(Response.FAILED);
		}

		Random rand = new Random();
		int pickupCode = 100000 + rand.nextInt(900000);

		affectedRows = insertIntoPickupCodes(pickupCode, order_code);
		if (affectedRows != 1)
			obj.setResponse(Response.FAILED);
		obj.setData(pickupCode);
		obj.setResponse(Response.ORDER_PLACED_SUCCESSFULLY);

	}

	public static void placeDeliveryOrder(Transaction obj) {
		DeliveryOrder order = (DeliveryOrder) obj.getData();
		int affectedRows;
		HashMap<Integer, Product> productAndQuantity = new HashMap<>();
		int order_code = 0;
		affectedRows = dbController.getInstance().executeUpdate(
				"insert into ekurt.orders (client_id, Order_date, supply_date, supply_method, total_price, status) values('"
						+ order.getClient().getId() + "', '" + order.getOrderDate() + "', null, '"
						+ SupplyMethodEnum.DELIVERY + "'," + order.getTotalToPay() + ",'"
						+ OrderStatusEnum.WAIT_APPROVAL + "');");
		if (affectedRows != 1)
			obj.setResponse(Response.FAILED);
		ResultSet rs = dbController.getInstance()
				.executeQuery("SELECT MAX(ekurt.orders.order_code) FROM ekurt.orders;");
		try {
			if (rs.next()) {
				order_code = rs.getInt("MAX(ekurt.orders.order_code)");
				affectedRows = insertIntoDeliveryOrders(order_code, order.getClient().getRegion());
				if (affectedRows != 1)
					obj.setResponse(Response.FAILED);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < order.getProducts().size(); i++) {
			String proName = order.getProducts().get(i).getName();
			rs = dbController.getInstance()
					.executeQuery("SELECT pro_code FROM ekurt.products WHERE pro_name='" + proName + "';");
			try {
				if (rs.next())
					order.getProducts().get(i).setProduct_code(rs.getString("pro_code"));
				productAndQuantity.put(order_code, order.getProducts().get(i));

				affectedRows = updateProStockDelivery(productAndQuantity, order.getClient().getRegion());
				if (affectedRows != 1)
					obj.setResponse(Response.FAILED);

				affectedRows = insertIntoProductsInOrder(productAndQuantity);
				if (affectedRows != 1)
					obj.setResponse(Response.FAILED);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (order.getClient().getStatus().equals(StatusEnum.FIRST_ORDER)) {
			affectedRows = upadteUserStatus(order.getClient().getId());
			if (affectedRows != 1)
				obj.setResponse(Response.FAILED);
		}
		if (order.getClient().getRole().equals(RoleEnum.SUBSCRIBER)) {
			affectedRows = dbController.getInstance()
					.executeUpdate("UPDATE ekurt.subscriberwallet SET balance = balance+" + order.getTotalToPay()
							+ "WHERE id='" + order.getClient().getId() + "';");
			if (affectedRows != 1)
				obj.setResponse(Response.FAILED);
		}

		affectedRows = dbController.getInstance()
				.executeUpdate("INSERT INTO ekurt.addresses VALUES('" + order.getClient().getId() + "','"
						+ order.getCity() + "','" + order.getStreetName() + "'," + order.getHouseNum() + ");");
		if (affectedRows != 1)
			obj.setResponse(Response.FAILED);

		// for simulating estimated delivery time
		Duration load = Duration.ofDays(1);
		Duration distance = Duration.ofDays(1);
		Duration droneAvailability = Duration.ofDays(1);

		switch (order.getClient().getRegion()) {
		case NORTH:
			obj.setData(order.getOrderDate().getTime() + load.toMillis() + distance.toMillis()
					+ droneAvailability.toMillis());
			obj.setResponse(Response.ORDER_PLACED_SUCCESSFULLY);
			break;
		case SOUTH:
			obj.setData(order.getOrderDate().getTime() + load.toMillis() + distance.toMillis()
					+ droneAvailability.toMillis());
			obj.setResponse(Response.ORDER_PLACED_SUCCESSFULLY);
			break;
		case UAE:
			obj.setData(order.getOrderDate().getTime() + load.toMillis() + distance.toMillis()
					+ droneAvailability.toMillis());
			obj.setResponse(Response.ORDER_PLACED_SUCCESSFULLY);
			break;
		}
	}

	public static void getPickupCodes(Transaction obj) {
		String currId = (String) obj.getData();
		List<Integer> pickUpList = new ArrayList<>();
		ResultSet rs = dbController.getInstance().executeQuery(
				"select pickup_code from pickupcodes where order_code IN (select order_code from orders where client_id='"
						+ currId + "' AND status='WAIT_PICKUP');");
		if (rs == null)
			obj.setResponse(Response.FAILED);
		else {
			try {
				while (rs.next()) {
					pickUpList.add(rs.getInt("pickup_code"));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			obj.setData(pickUpList);
			obj.setResponse(Response.GET_PICKUP_LIST_SUCC);
		}
	}

	public static void upDateStatus(Transaction obj) {
		HashMap<Integer, String> args = (HashMap<Integer, String>) obj.getData();
		String currId = null;
		int pickupCode = 0;
		for (Map.Entry<Integer, String> current : args.entrySet()) {
			pickupCode = current.getKey();
			currId = current.getValue();
		}
		int affectedRows;
		affectedRows = dbController.getInstance().executeUpdate(
				"UPDATE ekurt.orders SET status ='COMPLETE' WHERE order_code=(select order_code from pickupcodes where pickup_code="
						+ pickupCode + ") AND client_id='" + currId + "';");
		if (affectedRows != 1)
			obj.setResponse(Response.FAILED);
		else {
			obj.setResponse(Response.UPDATE_STATUS_SUCC);
		}

	}

	public static void GetReceiveOrder(Transaction msg) {
		List<String> Alist = new ArrayList<>();
		if (msg instanceof Transaction) {
			Object obj = ((Transaction) msg).getData();
			String clientID = obj.toString();

			try {
				ResultSet rs = dbController.getInstance()
						.executeQuery("select order_code, client_id, order_date, status from orders where client_id = '"
								+ clientID + "' AND (status='WAIT_APPROVAL' OR status='ON_THE_WAY');");
				if (!(rs.next())) {
					msg.setResponse(Response.FAILED_TO_GET_RECEIVED_ORDERS);
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
					String enumValue = rs.getString("status");
					OrderStatusEnum suppMethod = OrderStatusEnum.valueOf(enumValue);
					DeliveryOrder order = new DeliveryOrder(orderId, clientId, OrderDate, null, 0, suppMethod);
					orders.add(order);
				}
				msg.setResponse(Response.GOT_ORDERS_SUCCSSEFULLY);
				msg.setData(orders);
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
				msg.setResponse(Response.FAILED_TO_GET_RECEIVED_ORDERS);
				return;
			}
		} else
			msg.setResponse(Response.FAILED_TO_GET_RECEIVED_ORDERS);
	}

	public static void RecieveOrder(Transaction msg) {
		if (msg instanceof Transaction) {
			DeliveryOrder obj = (DeliveryOrder) msg.getData();
			int rowsAffected = dbController.getInstance().executeUpdate("UPDATE orders SET status = 'RECEIVED' WHERE "
					+ "order_code ='" + obj.getOrderId() + "' AND status = 'ON_THE_WAY'");
			rowsAffected = dbController.getInstance()
					.executeUpdate("UPDATE orders SET supply_date = '" + Timestamp.valueOf(LocalDateTime.now())
							+ "' WHERE " + "order_code ='" + obj.getOrderId() + "' AND status = 'RECEIVED'");
			if (rowsAffected == 1) {
				msg.setResponse(Response.SET_RECEIVED);
				return;
			} else {
				msg.setResponse(Response.FAILED_TO_SET_RECEIVED);
				return;
			}
		} else
			msg.setResponse(Response.FAILED_TO_SET_RECEIVED);
	}

	public static void PopUpWindow(Transaction msg) {
		PopUpMessage pop = new PopUpMessage();
		if (msg instanceof Transaction) {
			User obj = (User) msg.getData();
			ResultSet rs = dbController.getInstance()
					.executeQuery("SELECT email, telephone FROM ekurt.users WHERE id ='" + obj.getId() + "';");
			try {
				while (rs.next()) {
					String email = rs.getString("email");
					String telephone = rs.getString("telephone");
					pop.setTelephone(telephone);
					pop.setEmail(email);
				}
			} catch (SQLException e) {
				e.printStackTrace();
				msg.setResponse(Response.NO_POPUP);

			}
			ResultSet rs1 = dbController.getInstance()
					.executeQuery("SELECT order_code,order_date from ekurt.orders WHERE client_id ='" + obj.getId()
							+ "' AND (status='ON_THE_WAY' AND Pop_UP=0) LIMIT 1;");
			try {
				while (rs1.next()) {
					String ordercode = rs1.getString("order_code");
					Timestamp estimated = rs1.getTimestamp("order_date");
					Duration duration = Duration.ofHours(3);
					estimated = Timestamp.from(estimated.toInstant().plus(duration));
					duration = Duration.ofMinutes(30);
					estimated = Timestamp.from(estimated.toInstant().plus(duration));
					if (obj.getRegion() == RegionEnum.NORTH) {
						estimated = Timestamp.from(estimated.toInstant().plus(duration));
					} else if (obj.getRegion() == RegionEnum.SOUTH) {
						estimated = Timestamp.from(estimated.toInstant().plus(Duration.ofMinutes(45)));
					} else {
						estimated = Timestamp.from(estimated.toInstant().plus(Duration.ofMinutes(60)));
					}
					pop.setOrderId(ordercode);
					pop.setSuppDate(estimated);
				}
			} catch (SQLException e) {
				msg.setResponse(Response.NO_POPUP);
				e.printStackTrace();
			}
			try {
				int orderCode = Integer.parseInt(pop.getOrderId());
				PreparedStatement stmt = dbController.getInstance().getConn()
						.prepareStatement("UPDATE ekurt.orders SET Pop_UP = 1 WHERE order_code = ?");
				stmt.setInt(1, orderCode);
				int rowsAffected = stmt.executeUpdate();
				if (rowsAffected == 1) {
					msg.setResponse(Response.POPUP);
					msg.setData(pop);
					return;
				} else {
					msg.setResponse(Response.NO_POPUP);
					return;
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid input");
			} catch (SQLException e) {
				msg.setResponse(Response.NO_POPUP);
				e.printStackTrace();
			}

		} else
			msg.setResponse(Response.NO_POPUP);
	}

	public static int insertIntoPickupCodes(int pickupCode, int order_code) {
		return dbController.getInstance()
				.executeUpdate("INSERT INTO ekurt.pickupcodes VALUES(" + pickupCode + "," + order_code + ")");
	}

	public static int upadteUserStatus(int id) {

		return dbController.getInstance()
				.executeUpdate("UPDATE ekurt.users SET `status` = 'DISCOUNT_APPLIED' WHERE(id = '" + id + "');");
	}

	public static int insertIntoProductsInOrder(HashMap<Integer, Product> productAndQuantity) {
		int row = 0;
		for (Map.Entry<Integer, Product> current : productAndQuantity.entrySet()) {
			return dbController.getInstance()
					.executeUpdate("insert into ekurt.productsinorder values(" + current.getKey() + ", '"
							+ current.getValue().getProduct_code() + "'," + current.getValue().getAmount() + ","
							+ current.getValue().getFinalPrice() + ");");
		}
		return row;
	}

	public static int insertIntoMachineOrder(int order_code, String machine_code) {
		return dbController.getInstance()
				.executeUpdate("INSERT INTO ekurt.machine_orders VALUES(" + order_code + ",'" + machine_code + "');");
	}

	public static int updateProStock(HashMap<Integer, Product> map, String machine_code) {
		int row = 0;
		for (Map.Entry<Integer, Product> current : map.entrySet()) {
			return dbController.getInstance()
					.executeUpdate("UPDATE ekurt.productinmachine SET stock=stock-" + current.getValue().getAmount()
							+ " WHERE pro_code='" + current.getValue().getProduct_code() + "' AND machine_code='"
							+ machine_code + "';");
		}
		return 0;
	}

	public static int insertIntoDeliveryOrders(int order_code, RegionEnum region) {
		return dbController.getInstance()
				.executeUpdate("INSERT INTO ekurt.delivery_orders VALUES(" + order_code + ",'" + region + "');");
	}

	public static int updateProStockDelivery(HashMap<Integer, Product> map, RegionEnum region) {
		int row = 0;
		for (Map.Entry<Integer, Product> current : map.entrySet()) {
			return dbController.getInstance()
					.executeUpdate("UPDATE ekurt.productinwarehouse SET stock=stock-" + current.getValue().getAmount()
							+ " WHERE pro_code='" + current.getValue().getProduct_code() + "' AND region='" + region
							+ "';");
		}
		return 0;
	}
}
