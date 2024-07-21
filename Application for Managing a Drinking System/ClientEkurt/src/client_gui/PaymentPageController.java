package client_gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Utils.Constants;
import Utils.generalMethods;
import client.ClientUI;
import clientUtil.ClientUtils;
import common.Action;
import common.Transaction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class PaymentPageController implements Initializable{
	/**
	 * This is a JavaFX controller class for a PaymentPage that is used in a client application. 
	 * The class contains methods for handling events such as button clicks for the "back" and 
	 * "place order" buttons, as well as for handling user input in the text fields for the credit 
	 * card information (cvv, month, year, creditCardText, emailText, and idNumText).
	 *  It also has a start method that is used to display the PaymentPage.fxml on the primary stage.
	 */
	

    @FXML
    private Button backBtn;

    @FXML
    private Button placeOrderBtn;
    
    @FXML
    private TextField cvv;

    @FXML
    private TextField month;

    @FXML
    private TextField year;
    
    @FXML
    private Label errorLabel;
    
    @FXML
    private TextField creditCardText = new TextField();

    @FXML
    private TextField emailText = new TextField();

    @FXML
    private TextField idNumText = new TextField();
    
    private ArrayList<String> paymentDetails = new ArrayList<>();
    private String monthValue;
    private String yearValue;
    private String cvvValue;
    /**
   	 * Displays the PaymentPage screen and sets the title of the window for Payment.
   	 * 
   	 * @param primaryStage the primary stage of the application
   	 */
       
    
	public void start (Stage primaryStage) {
		generalMethods.displayScreen(primaryStage, getClass(), "/client_fxml/PaymentPage.fxml", "Payment");
	}
	/**
	 * Event handler for the "Back" button. Hides the current window and displays
	 * @param event the MouseEvent that triggers the handler
	 */
    @FXML
    void clickOnBackBtn(ActionEvent event) {
    	((Node) event.getSource()).getScene().getWindow().hide();
    }
    /**
   	 *The clickOnPlaceOrder method is used to handle the
   	 * event when the place order button is clicked, it checks if the input of cvv,
   	 *  month and year match the expected values and if so, 
   	 *it sends a message to the server with the order details. If the input is incorrect it displays an error message..
   	 * 
   	 * @param event the MouseEvent that triggers the handler
   	 */
    @FXML
    void clickOnPlaceOrder(ActionEvent event) {
    	if(cvv.getText().equals(cvvValue) && month.getText().equals(monthValue) && year.getText().equals(yearValue)) {
    		if(ClientUtils.localOrderInProcess != null) { //if local
    		Transaction msg = new Transaction(Action.PLACE_LOCAL_ORDER, ClientUtils.localOrderInProcess);
        	ClientUI.chat.accept(msg);
        	((Node) event.getSource()).getScene().getWindow().hide();
        	Stage.getWindows().get(0).hide();
        	new OrderConfirmationLocalController().start(new Stage());
    		}
    		else if(ClientUtils.pickupOrderInProcess != null) { //if pickup
    		Transaction msg = new Transaction(Action.PLACE_PICKUP_ORDER, ClientUtils.pickupOrderInProcess);
        	ClientUI.chat.accept(msg);
        	((Node) event.getSource()).getScene().getWindow().hide();
        	Stage.getWindows().get(0).hide();
        	new OrderConfirmationPickUpController().start(new Stage());
    		}
    		else { //if delivery
        		Transaction msg = new Transaction(Action.PLACE_DELIVERY_ORDER, ClientUtils.deliveryOrderInProcess);
            	ClientUI.chat.accept(msg);
            	((Node) event.getSource()).getScene().getWindow().hide();
            	Stage.getWindows().get(0).hide();
            	new OrderConfirmationDeliveryController().start(new Stage());
    		}
    	}else {
    		errorLabel.setVisible(true);
    		cvv.setStyle(Constants.TEXT_FIELD_NOT_VALID_STYLE);
    		year.setStyle(Constants.TEXT_FIELD_NOT_VALID_STYLE);
    		month.setStyle(Constants.TEXT_FIELD_NOT_VALID_STYLE);
    	}
    }
    /**
	 *The clickOnCvv, clickOnMonth, clickOnYear method is used to clear
	 * the error label when the user clicks on cvv, month, year text field.
	 * @param event the MouseEvent that triggers the handler
	 */
    @FXML
    void clickOnCvv(MouseEvent event) {
    	clearTextField();
    }

    @FXML
    void clickOnMonth(MouseEvent event) {
    	clearTextField();
    }
    

    @FXML
    void clickOnyear(MouseEvent event) {
    	clearTextField();
    }
    /**
  	 *The class also implements Initializable interface, which is used to
  	 * initialize the controller class. This is used to perform any 
  	 * initialization that needs to happen before the class is used, such as setting up
  	 * the paymentDetails arraylist, and initializing 
  	 * the monthValue, yearValue, and cvvValue variables.
  	 * 
  	 * @param event the MouseEvent that triggers the handler
  	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		errorLabel.setVisible(false);
		Transaction msg = new Transaction(Action.GET_PAYMENT_DETAILS, ClientUtils.currUser.getId());
    	ClientUI.chat.accept(msg);
		msg = ClientUI.chat.getObj();
		if(msg.getData() instanceof ArrayList<?>) {
			paymentDetails = ArrayList.class.cast(msg.getData());
			emailText.setText(paymentDetails.get(0));
			creditCardText.setText(paymentDetails.get(1));
			idNumText.setText(String.valueOf(ClientUtils.currUser.getId()));
			monthValue = paymentDetails.get(3).substring(0, 2);
			yearValue = paymentDetails.get(3).substring(3, 5);
			cvvValue = paymentDetails.get(2);
		}else {
			
		}
	}
	
	public void clearTextField () {
		errorLabel.setVisible(false);
		cvv.setStyle(Constants.TEXT_FIELD_NOT_VALID_STYLE);
		year.setStyle(Constants.TEXT_FIELD_NOT_VALID_STYLE);
		month.setStyle(Constants.TEXT_FIELD_NOT_VALID_STYLE);
	}

}
