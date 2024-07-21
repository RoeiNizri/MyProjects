package client_gui;

import java.net.URL;
import java.util.ResourceBundle;

import Utils.generalMethods;
import client.TimeMeasureThread;
import clientUtil.ClientUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
/**
The class PopUpWindowFXController is a controller class for the PopUpWindow.fxml file in a JavaFX application.
 It implements the Initializable 
interface which is used to indicate that the class can be initialized by the FXML loader.
* 
* @param primaryStage the primary stage of the application
*/
public class PopUpWindowFXController implements Initializable {

    @FXML
    private Button okBtn;
    @FXML
    private Label lbl;
    @FXML
    private Label ApprovedLbl;
    /**
	 * Displays the PopUpWindow screen and sets the title of the window for Order Approved!.
	 * 
	 * @param primaryStage the primary stage of the application
	 */
    @FXML
	public void start(Stage primaryStage) throws Exception {
		generalMethods.displayScreen(primaryStage, getClass(), "/client_fxml/PopUpWindow.fxml",
				"Order Approved!");
	}
    /**
   	 * hides the current window
   	 * 
   	 * @param event the MouseEvent that triggers the handler
   	 */
    @FXML
    void clickOnOkBtn(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide();	
    }
    /**
  	 *The class also implements Initializable interface, which is used to
  	 * initialize the controller class. This is used to perform any 
  	 * initialization that needs to happen before the class is used, such as setting up
  	 * lbl with the correct user values
  	 * 
  	 * @param event the MouseEvent that triggers the handler
  	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	Platform.runLater(() -> {
			try {
				lbl.setText("Dear customer order ID: "+ClientUtils.Popmsg.getOrderId()+" is on the way!\nSMS will be sent to: "+ClientUtils.Popmsg.getTelephone()+""
		    			+ "\n Email will be sent to: "+ClientUtils.Popmsg.getEmail() +"\nThe estimated delivery time is: "+ClientUtils.Popmsg.getSuppDate().toString());
		    	lbl.setVisible(true);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
    	
		
	}
}