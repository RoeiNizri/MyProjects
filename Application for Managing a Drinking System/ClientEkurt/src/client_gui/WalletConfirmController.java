package client_gui;

import java.net.URL;
import java.util.ResourceBundle;
import Utils.generalMethods;
import client.ClientUI;
import clientUtil.ClientUtils;
import common.Action;
import common.Response;
import common.Transaction;
import enums.RegionEnum;
import enums.RoleEnum;
import enums.StatusEnum;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import logic.Product;
/**
 *This class represents the controller class for a GUI screen that allows a user
 * to confirm a wallet transaction.
 */
public class WalletConfirmController implements Initializable{

    @FXML
    private Button backBtn;

    @FXML
    private Button placeOrderBtn;

    @FXML
    private Label totalPrice = new Label();

    @FXML
    private Label outOfStock = new Label();
    Product ProOutOfStock;
    /**
	 * Displays the WalletConfirmPage screen and sets the title of the window for Add to wallet
	 * 
	 * @param primaryStage the primary stage of the application
	 */
	public void start(Stage primaryStage) {
		generalMethods.displayScreen(primaryStage, getClass(), "/client_fxml/WalletConfirmPage.fxml",
				"Add to wallet");
	}
	/**
	 * Event handler for the "Back" button. Hides the current window.
	 * @param event the MouseEvent that triggers the handler
	 */
    @FXML
    void clickOnBack(ActionEvent event) {
    	((Node) event.getSource()).getScene().getWindow().hide();
    }
    /**
	 * Event handler for the button click-When the button is clicked, 
	 * it checks if a local, pickup, or delivery order is in process.if a local order is in process, 
	 * it applies a discount for the first order, creates a new Transaction object with the Action 
	 * "PLACE_LOCAL_ORDER" and the order, sends the transaction through the chat, 
	 * closes the current window, and opens a new window for order confirmation. The same process 
	 * is repeated for pickup and delivery orders, with the appropriate Action and confirmation window.
	 * 
	 * @param event the ActionEvent that triggers the handler
	 */
	@FXML
	void clickOnPlaceOrder(ActionEvent event) {
		if (ClientUtils.localOrderInProcess != null) { // if local
			if(ClientUtils.currUser.getStatus().equals(StatusEnum.FIRST_ORDER)) {
				ClientUtils.localOrderInProcess.setTotalToPay(ClientUtils.localOrderInProcess.getFirstOrderDiscount());
			}
			Transaction msg = new Transaction(Action.PLACE_LOCAL_ORDER, ClientUtils.localOrderInProcess);
			ClientUI.chat.accept(msg);
			msg = (Transaction) ClientUI.chat.getObj();
			
				((Node) event.getSource()).getScene().getWindow().hide();
				Stage.getWindows().get(0).hide();
				new OrderConfirmationLocalController().start(new Stage());
			
		}
		else if (ClientUtils.pickupOrderInProcess != null) {
			if(ClientUtils.currUser.getStatus().equals(StatusEnum.FIRST_ORDER)) {
				ClientUtils.pickupOrderInProcess.setTotalToPay(ClientUtils.pickupOrderInProcess.getFirstOrderDiscount());
			}
			Transaction msg = new Transaction(Action.PLACE_PICKUP_ORDER, ClientUtils.pickupOrderInProcess);
			ClientUI.chat.accept(msg);
			
				((Node) event.getSource()).getScene().getWindow().hide();
				Stage.getWindows().get(0).hide();
				new OrderConfirmationPickUpController().start(new Stage());
		} else {
			if(ClientUtils.currUser.getStatus().equals(StatusEnum.FIRST_ORDER)) {
				ClientUtils.deliveryOrderInProcess.setTotalToPay(ClientUtils.deliveryOrderInProcess.getFirstOrderDiscount());
			}
			Transaction msg = new Transaction(Action.PLACE_DELIVERY_ORDER, ClientUtils.deliveryOrderInProcess);
			ClientUI.chat.accept(msg);
			((Node) event.getSource()).getScene().getWindow().hide();
			Stage.getWindows().get(0).hide();
			new OrderConfirmationDeliveryController().start(new Stage());
		}
	}
	/**
     * This method initializes the total price for an order. It checks the status of the current user
     *(if it's their first order or not) and checks which type of order is in process (local, pickup or delivery).
     * Depending on the status and type of order, it sets the total price to either the first order discount or the
     * total amount to pay. The total price is then set to a text field "totalPrice".
     * 
	 * @param arg0 URL
	 * @param arg1 ResourceBundle
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		if(ClientUtils.currUser.getStatus().equals(StatusEnum.FIRST_ORDER)) {
			if(ClientUtils.localOrderInProcess != null) {
				totalPrice.setText(String.valueOf(ClientUtils.localOrderInProcess.getFirstOrderDiscount())+" ¤");
			}else if(ClientUtils.pickupOrderInProcess != null) {
				totalPrice.setText(String.valueOf(ClientUtils.pickupOrderInProcess.getFirstOrderDiscount())+" ¤");
			}else {
				totalPrice.setText(String.valueOf(ClientUtils.deliveryOrderInProcess.getFirstOrderDiscount())+" ¤");
			}
		}else {
		if(ClientUtils.localOrderInProcess != null) {
			totalPrice.setText(String.valueOf(ClientUtils.localOrderInProcess.getTotalToPay())+" ¤");
		}else if(ClientUtils.pickupOrderInProcess != null) {
			totalPrice.setText(String.valueOf(ClientUtils.pickupOrderInProcess.getTotalToPay())+" ¤");
		}else {
			totalPrice.setText(String.valueOf(ClientUtils.deliveryOrderInProcess.getTotalToPay())+" ¤");
		}
	}
	}

}
