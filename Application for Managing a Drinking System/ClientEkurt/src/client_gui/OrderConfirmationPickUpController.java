package client_gui;

import java.net.URL;
import java.util.ResourceBundle;
import Utils.generalMethods;
import clientUtil.ClientUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
/**
 * This class represents the OrderConfirmationPickUpController and shows
 * the message that the client is reciving after making the Pickup order.
 */
public class OrderConfirmationPickUpController implements Initializable{
	
	public static int pickupCode;
    public static int getPickupCode() {
		return pickupCode;
	}

	public static void setPickupCode(int pickupCode) {
		OrderConfirmationPickUpController.pickupCode = pickupCode;
	}

	@FXML
    private Button okBtn;

    @FXML
    private Label orderCode = new Label();
    /**
   	 * Displays OrderConfirmationPickUp and sets the title of the window to Order Confirmation .
   	 * 
   	 * @param primaryStage the primary stage of the application
   	 */
	public void start (Stage primaryStage) {
		generalMethods.displayScreen(primaryStage, getClass(), "/client_fxml/OrderConfirmationPickUp.fxml", "Order Confirmation");
	}
	/**
	 * This method is called when the back button is clicked. It hides the current
	 * window and opens the CEO CeoDashboardController page if we logged in as a CEO
	 * opens the MainDashboradController if we logged in as a Customer or as a Subscriber.
	 * @param event The mouse event that triggers this method.
	 */
	@FXML
	void clickOnOkBtn(ActionEvent event) {
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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		orderCode.setText(String.valueOf(pickupCode));
	}

}
