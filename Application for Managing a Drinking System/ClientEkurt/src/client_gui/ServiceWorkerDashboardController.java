package client_gui;

import java.net.URL;
import java.util.ResourceBundle;

import Utils.generalMethods;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import clientUtil.*;

/**
 * 
 * This class is the controller for the service worker dashboard page,
 * 
 * it handles the functionality of the approve subscribers button and logout
 * button.
 * 
 * It also displays the current user's name on the page.
 * 
 * @author [Roei Nizri] & [Ran Polac]
 */
public class ServiceWorkerDashboardController implements Initializable {

	@FXML
	private Button approveSubscribersBtn;
	@FXML
	private Button LogoutBtn;
	@FXML
	private Label usernameLabel;

	/**
	 * 
	 * This method displays the service worker dashboard page
	 * 
	 * @param primaryStage the primary stage of the page
	 */
	public void start(Stage primaryStage) {
		generalMethods.displayScreen(primaryStage, getClass(), "/client_fxml/ServiceWorkerDashboard.fxml",
				"Ekurt Manager's Menu");
	}

	/**
	 * 
	 * This method handles the approve subscribers button press and redirects the
	 * user to the subscriber approving page
	 * 
	 * @param event the button press event
	 * @throws Exception
	 */
	@FXML
	void approveSubscribers(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		Stage primaryStage = new Stage();
		SubscriberApprovingController getPage = new SubscriberApprovingController();
		getPage.start(primaryStage);
	}

	/**
	 * 
	 * This method handles the logout button press and logs the user out and
	 * redirects the user to the login page.
	 *
	 * @param event the button press event
	 * @throws Exception
	 */
	@FXML
	void logout(ActionEvent event) {
		LoginController.logout(ClientUtils.currUser.getUsername());
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		new LoginController().start(new Stage());
	}

	/**
	 * This method is called when the page is loaded, it sets the current user's
	 * name on the page.
	 *
	 * @param arg0 URL
	 * @param arg1 Resource Bundle
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		usernameLabel.setText(ClientUtils.currUser.getFirstName() + " " + ClientUtils.currUser.getLastName());
	}
}