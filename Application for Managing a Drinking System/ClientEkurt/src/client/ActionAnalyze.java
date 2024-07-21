package client;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import clientUtil.ClientUtils;
import client_gui.ChooseMachinePickupController;
import client_gui.OrderConfirmationPickUpController;
import common.Transaction;
import logic.*;

public class ActionAnalyze {
	/**
	 * Analyzes a transaction and performs the appropriate action.
	 *
	 * @param msg The transaction to be analyzed.
	 * @return A boolean indicating whether the action was successful.
	 * @throws Exception If an error occurs while analyzing the transaction.
	 */
	public static boolean actionAnalyzeClient(Transaction msg) throws Exception {
		ClientController.setObj(msg);
		switch (msg.getResponse()) {
		case FOUND_SUBSCRIBERS: {
			// TODO: Add relevant action for found subscribers
			break;
		}
		case DIDNT_FOUND_SUBSCRIBERS: {
			// TODO: Add relevant action for not found subscribers
			break;
		}
		case UPDATE_SUBSCRIBERS_SUCCESS: {
			// TODO: Add relevant action for successful subscriber update
			break;
		}
		case UPDATE_SUBSCRIBERS_FAILD: {
			// TODO: Add relevant action for failed subscriber update
			break;
		}
		case FAILED: {
			// insert relevant method from the clientQuaries on the future
			// TODO: Add relevant action for failed transaction
			break;
		}
		case ALREADY_LOGGED_IN: {
			// insert relevant method from the clientQuaries on the future
			break;
		}
		case LOGGED_IN_SUCCESS: {
			
			break;
		}
		case INCORRECT_VALUES: {
			// insert relevant method from the clientQuaries on the future
			break;
		}

		case FOUND_MACHINE_NAMES: {
			ChooseMachinePickupController.setMachineNames((List<String>)msg.getData());
			break;
		}
		case FOUND_PRODUCTS_FOR_DISPLAY: {
			if (msg.getData() instanceof ArrayList<?>) {
				ArrayList<ProductInGrid> list = ArrayList.class.cast(msg.getData());
				if (ClientUtils.localOrderInProcess != null) {
					ClientUtils.localOrderInProcess.getProductsForDisplay().addAll(list);
				} else if (ClientUtils.pickupOrderInProcess != null) {
					ClientUtils.pickupOrderInProcess.getProductsForDisplay().addAll(list);
				} else {
					ClientUtils.deliveryOrderInProcess.getProductsForDisplay().addAll(list);
				}
			}
			break;
		}
		case FOUND_OFFERS:{
			//PromoteOffersFXController.setTableScreen(msg);
			break;
		}
		case FAILED_TO_GET_OFFERS:{
			break;
		}
		case FOUND_ORDERS:{
			break;
		}
		case FAILED_TO_GET_ORDERS:{
			break;
		}
		case FOUND_CUR_STOCK: {
			
			break;
		}
		case FOUND_PAYMENT_DETAILS: {
			
			break;
		}
		case ORDER_PLACED_SUCCESSFULLY: {
			if (ClientUtils.pickupOrderInProcess != null) {
				if (msg.getData() instanceof Integer) { // when pickup
					int pickupCode = (int) msg.getData();
					new OrderConfirmationPickUpController().setPickupCode(pickupCode);
				}
			} else if (ClientUtils.deliveryOrderInProcess != null) {
				if (msg.getData() instanceof Timestamp) {
					Timestamp estimatedDelivery = (Timestamp) msg.getData();
					ClientUtils.deliveryOrderInProcess.setEstimatedDelivery(estimatedDelivery);
				}
			}
			break;
		}
		case GET_CREDIT_CARD_BY_ID_SUCCESSFULLY: {
			break;
		}
		case GET_CREDIT_CARD_BY_ID_UNSUCCESSFULLY: {
			break;
		}
		case APPROVE_REGISTER_REQUEST_UNSUCCESSFULLY : {
			break;
		}
		case APPROVE_REGISTER_REQUEST_SUCCESSFULLY : {
			break;
		}
		case FOUND_SUB_ID_LIST: {
		//	ConnectToServerController.setSubIdList((List<String>)msg.getData());
			break;
		}
		case FOUND_ALL_IDS: {
			break;
		}
		case CHECK_ID_SUCC:{	
			break;
		}
		case CHECK_ID_UNSUCC:{
			
			break;
		}
		case GET_PICKUP_LIST_SUCC:{
			break;
		}
		case UPDATE_STATUS_SUCC:{
			break;
		}
	
	}
		return false;
	}
}
