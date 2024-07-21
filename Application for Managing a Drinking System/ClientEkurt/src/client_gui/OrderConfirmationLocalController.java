package client_gui;

import Utils.generalMethods;
import clientUtil.ClientUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;
/**
 * This class represents the OrderConfirmationLocalController and shows
 * the message that the client is reciving after making the Local order. .
 */
public class OrderConfirmationLocalController {

    @FXML
    private Button okBtn;
	/**
	 * Displays OrderConfirmationLocal and sets the title of the window to Order Confirmation .
	 * 
	 * @param primaryStage the primary stage of the application
	 */
	public void start (Stage primaryStage) {
		generalMethods.displayScreen(primaryStage, getClass(), "/client_fxml/OrderConfirmationLocal.fxml", "Order Confirmation");
	}
	/**
     * Handles the event of clicking on the OK button.
     * This method is called when the user clicks the OK button.
     * It hides the current window and opens a new window based on the currUser.
     * @param event the event that triggered the method call (a button click in this case)
     */
    @FXML
    void clickOnOkBtn(ActionEvent event) {
    	((Node) event.getSource()).getScene().getWindow().hide();
    		LoginController.logout(ClientUtils.currUser.getUsername());
    		new LoginController().start(new Stage());
    }

}
