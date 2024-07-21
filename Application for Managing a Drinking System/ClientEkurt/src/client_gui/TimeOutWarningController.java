package client_gui;

import Utils.generalMethods;
import client.TimeMeasureThread;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;
/**

 *This class is the controller for the TimeOutWarning FXML file. It contains a method to start the TimeOutWarning stage and a method
 *to handle the "OK" button press event.
 *The start method is used to display the TimeOutWarning screen, and the clickOnOkBtn method is used to handle the "OK" button press event.
 *When the OK button is pressed, the TimeOutWarning stage is closed, the start time is reset, and the flag is set to true.
 */
public class TimeOutWarningController {

    @FXML
    private Button okBtn;
    /**
	 * Displays the CEO dTimeOutWarning screen and sets the title of the window "Anybody's here?".
	 * 
	 * @param primaryStage the primary stage of the application
	 */
    @FXML
	public void start(Stage primaryStage) throws Exception {
		generalMethods.displayScreen(primaryStage, getClass(), "/client_fxml/TimeOutWarning.fxml",
				"Anybody's here?");
	}
    /**
 	 * Event handler for the "OK" button. Hides the current window and
 	 * the TimeOutWarning stage is closed, the start time is reset, and the flag is set to true.
 	 * 
 	 * @param event the ActionEvent that triggers the handler
 	 * @throws Exception if an exception occurs
 	 */
    @FXML
    void clickOnOkBtn(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide();
		TimeMeasureThread.resetStartTime();
		TimeMeasureThread.flag = true;	
    }
}
