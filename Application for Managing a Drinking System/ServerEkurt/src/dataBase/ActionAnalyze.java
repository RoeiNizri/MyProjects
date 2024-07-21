package dataBase;

import common.Transaction;
import java.sql.Connection;
import java.util.ArrayList;

public class ActionAnalyze {
	public static ArrayList<String> list = new ArrayList<String>();

	/**
	 * This method analyzes the received action from the client and performs the
	 * corresponding action on the server.
	 *
	 * @param obj the Transaction object
	 * @param con the Connection object
	 */
	public static void actionAnalyzeServer(Transaction obj, Connection con) {

		switch (obj.getAction()) {
	    case LOGIN_USERNAME_PASSWORD:{
	    	LoginQuaries.loginByUsernameAndPassword(obj);
	    break;
		}
	    case LOGOUT_USER: {
	    	LoginQuaries.logoutUsername(obj);
	    break;
	    }
	    case GET_MACHINES_LIST: {
	    	OrderQueries.getMachineList(obj);
	    break;
	    }
	    case GET_AVAILABLE_PRODUCTS_IN_MACHINE: {
	    		OrderQueries.getProductCodesInMachine(obj);
	    break;
	    }
	    case GET_NOT_AVAILABLE_PRODUCTS_IN_MACHINE: {
	    	OrderQueries.getProductCodesInMachineNotInStock(obj);
	    break;
	    }
	    case GET_CUR_STOCK: {
	    	OrderQueries.getProductInMachineStock(obj);
	    break;
	    }
	    case GET_PAYMENT_DETAILS: {
	    	OrderQueries.getPaymentDetails(obj);
	    break;
	    }
	    case PLACE_PICKUP_ORDER: {
	    	OrderQueries.placePickupOrder(obj);
	    break;
	    }
	    case PLACE_DELIVERY_ORDER: {
	    	OrderQueries.placeDeliveryOrder(obj);
	    break;
	    }
	    case PLACE_LOCAL_ORDER: {
	    	OrderQueries.placeLocalOrder(obj);
	    	break;
	    }
	    case GET_PRODUCTS_FOR_DELIVERY: {
	    	OrderQueries.getProductsFromWarehous(obj);
	    break;
	    }
	    case GET_CREDIT_CARD_BY_ID : {
	    	UserQuaries.getCreditCardById(obj, con);
	    	break;
	    }
	    case GET_ID_LIST: {
	    	UserQuaries.getSubscribersId(obj);
	    	break;
	    }
		case GET_MACHINE: {
			CEOQuaries.getMachine(obj, con);
			break;
		}
		case GET_ORDERS_REPORT: {
			CEOQuaries.getOrderReport(obj, con);
			break;
		}
		case GET_INVENTORY_REPORT: {
			CEOQuaries.getInventoryReport(obj, con);
			break;
		}
		case GET_CUSTOMER_REPORT: {
			CEOQuaries.getCustomerActivityReport(obj, con);
			break;
		}
		case GET_MACHINE_BY_REGION: {
			CEOQuaries.getMachineByRegion(obj, con);
			break;
		}
		case GET_CUSTOMER_REPORT_BY_REGION: {
			CEOQuaries.getCustomerActivityReportByRegion(obj, con);
			break;
		}
		case UPDATE_LIMIT_QUANTITY_IN_MACHINE: {
			CEOQuaries.setLimitQuantityInMachine(obj, con);
			break;
		}
		case UPDATE_QUANTITY_STATUS: {
			CEOQuaries.setStatusStockByQuantity(obj, con);
			break;
		}
		case GET_USER_REGISTER_REQUEST: {
			CEOQuaries.getUserRegisterRequest(obj, con);
			break;
		}
		case APPROVE_REGISTER_REQUEST: {
			CEOQuaries.approveRegisterRequest(obj, con);
			break;
		}
		case REGISTER_USER_TO_CUSTOMER: {
			UserQuaries.registerUserToCustomerWithCreditCard(obj, con);
			break;
		}
		case REGISTER_CUSTOMER_TO_SUBSCRIBER: {
			UserQuaries.subscriberRequest(obj, con);
			break;
		}
		case GET_SUBSCRIBER_REQUESTS_TO_SERVICE_WORKER: {
			ServiceWorkerQuaries.getSubscriberRequestsToServiceWorker(obj, con);
			break;
		}
		case APPROVE_SUBSCRIBER_REQUEST: {
			ServiceWorkerQuaries.approveSubscriberRequest(obj, con);
			break;
		}
		case GET_LIMIT_BY_MACHINE: {
			CEOQuaries.getLimitByMachine(obj, con);
			break;
		}
	    case GET_OFFERS:{
	    	OffersQuaries.getOffers(obj);
	    break;
	    }
	    case GET_ORDERS:{
	    	DeliveryOperatorQuaries.getOrders(obj);
	    break;
	    }
	    case APPROVE_ORDER:{
	    	DeliveryOperatorQuaries.ApproveOrder(obj);
	    	 break;
	    }
	    case COMPLETE_ORDER:{
	    	DeliveryOperatorQuaries.CompleteOrder(obj);
	    	 break;
	    }
	    case GET_INVENTORY_STOCK:{
	    	StorageWorkerQuaries.getRefillStock(obj, con);
	    	break;
	    }
	    case UPDATE_QUANTITY_IN_MACHINE:{
	    	StorageWorkerQuaries.UpdateQuantityInMachine(obj, con);
	    	break;
	    }
	    case PROMOTE_OFFER:{
	    	OffersQuaries.PromoteOffer(obj);
	    	break;
	    }
	    case STOP_OFFER:{
	    	OffersQuaries.StopOffer(obj);
	    	break;
	    }
	    case GET_ALL_USER_ID: {
	    	LoginQuaries.getAllUserId(obj);
	    	break;
	    }
	    case CHECK_ID:{
	    	LoginQuaries.checkId(obj);
			break;
		}
	    case GET_PICKUP_CODE: {
	    	OrderQueries.getPickupCodes(obj);
	    	break;
	    }
	    case UPDATE_STATUS:{
	    	OrderQueries.upDateStatus(obj);
		break;
	    }
	    case GET_ORDERS_REPORT_TO_XLS: {
			CEOQuaries.getOrderReportToXsl(obj, con);
			break;
		}
		case GET_INVENTORY_REPORT_TO_XLS: {
			CEOQuaries.getInventoryReportToXsl(obj, con);
			break;
		}
		case GET_CUSTOMER_REPORT_TO_XLS: {
			CEOQuaries.getCustomerReportToXsl(obj, con);
			break;
		}
		case GET_CUSTOMER_REPORT_BY_REGION_TO_XLS: {
			CEOQuaries.getCustomerReportToXslByRegion(obj, con);
			break;
		}
	    case GET_ORDERS_TO_RECEIVE:{
	    	OrderQueries.GetReceiveOrder(obj);
	    	break;
	    }
	    case RECEIVE_ORDER:{
	    	OrderQueries.RecieveOrder(obj);
	    	break;
	    }
	    case POP_UP:{
	    	OrderQueries.PopUpWindow(obj);
	    	break;
	    }
		}
	}
}
