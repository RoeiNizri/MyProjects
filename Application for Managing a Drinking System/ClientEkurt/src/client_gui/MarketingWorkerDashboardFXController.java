package client_gui;

import java.net.URL;
import java.util.ResourceBundle;

import Utils.generalMethods;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import clientUtil.ClientUtils;

/**
 * This class represents the controller for the  MarketingWorkerDashboard. 
 * it shows the options that the MarketingWorker has
 * in our case he has the option to promote offers.
 */
public class MarketingWorkerDashboardFXController implements Initializable{

	@FXML
	private Button PromoteOffers;
	@FXML
	private Button LogoutBtn;
	@FXML
	private Label lbl = new Label();
	//private generalMethods gm = new generalMethods();
	/**
	 * Displays theMarketingWorkerDashboardscreen and sets the title of the window to Ekurt marketing worker Menu .
	 * 
	 * @param primaryStage the primary stage of the application
	 */
	public void start(Stage primaryStage) {
		generalMethods.displayScreen(primaryStage, getClass(), "/client_fxml/MarketingWorkerDashboard.fxml", "Ekurt marketing worker Menu");
	}
	/**
	* Handles the event of promoting offers.
	* This method is called when the user clicks the "Promote Offers" button.
	* It hides the current window and opens a new window for promoting offers.
	* @param event the event that triggered the method call (a button click in this case)
	* @throws Exception if an error occurs while opening the new window
	*/
	@FXML
	void PromoteOffers(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		Stage primaryStage = new Stage();
		PromoteOffersFXController PromotePage = new PromoteOffersFXController();
		PromotePage.start(primaryStage);
	}
	/**
	 * Event handler for the "Logout" button. Calls the logout method in the
	 * LoginController class and displays the login screen.
	 * 
	 * @param event the ActionEvent that triggers the handler
	 * @throws Exception if an exception occurs
	 */
	@FXML
	void logout(ActionEvent event) throws Exception {
		LoginController.logout(ClientUtils.currUser.getUsername());
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		new LoginController().start(new Stage());
	}
	/**
	* Initializes the controller class.
	* This method is automatically called after the FXML file has been loaded.
	*
	* @param arg0 the URL of the FXML file that was used to create the controller
	* @param arg1 the resource bundle that was used to localize the root object
	*/
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Platform.runLater(() -> {
			try {
				lbl.setText("Welcome Back\n"+ClientUtils.currUser.getFirstName()+ " " + ClientUtils.currUser.getLastName());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
}