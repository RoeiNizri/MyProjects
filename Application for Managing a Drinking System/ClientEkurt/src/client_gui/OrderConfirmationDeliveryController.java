package client_gui;

import Utils.generalMethods;
import clientUtil.ClientUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;
/**
 * This class represents the OrderConfirmationDelivery and shows
 * the message that the client is reciving after making the delivery order. .
 */
public class OrderConfirmationDeliveryController {

    @FXML
    private Button okBtn;
    
	public void start (Stage primaryStage) {
		generalMethods.displayScreen(primaryStage, getClass(), "/client_fxml/OrderConfirmationDelivery.fxml", "Order Confirmation");
	}
	 /**
	    * Handles the event of clicking on the OK button.
	    * This method is called when the user clicks the OK button.
	    * It hides the current window and opens a new window based on the user's role.
	    * @param event the event that triggered the method call (a button click in this case)
	    */
    @FXML
    void clickOnOk(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide();
		switch (ClientUtils.currUser.getRole()) {
		case CUSTOMER: {
			new MainDashboradController().start(new Stage());
			break;
		}

		case SUBSCRIBER: {
			new MainDashboradController().start(new Stage());
			break;
		}
		case CEO: {
			new CeoDashboardController().start(new Stage());
			break;
		}
		}
    }

}
