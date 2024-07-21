package client_gui;

import Utils.generalMethods;
import clientUtil.ClientUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
/**
 * The class PickUpCompletedController
 *  is a controller class for the PickUpCompleted.fxml file in a JavaFX application.
 *  It has a single method start(Stage primaryStage)
 *   which is used to display the PickUpCompleted.fxml file on the primary stage.
 * 
 * @param primaryStage the primary stage of the application
 */
public class PickUpCompletedController {

    @FXML
    private Label close;
    /**
   	 * Displays the PickUpCompleted screen and sets the title of the window for PickUp Page.
   	 * 
   	 * @param primaryStage the primary stage of the application
   	 */
    public void start(Stage primaryStage) {
		generalMethods.displayScreen(primaryStage, getClass(), "/client_fxml/PickUpCompleted.fxml", "PickUp Page");
	}
    /**
   	 * Event handler for the "ClickOnClose" button. Hides the current window and displays
   	 * the suitable fxml.
   	* and logout the current user runs the LoginController screen
   	 * 
   	 * @param event the MouseEvent that triggers the handler
   	 */
    @FXML
    void clickOnClose(MouseEvent event) {
    	((Node) event.getSource()).getScene().getWindow().hide();
		LoginController.logout(ClientUtils.currUser.getUsername());
		new LoginController().start(new Stage());
    }

}
