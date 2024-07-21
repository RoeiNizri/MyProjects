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
 * This class represents the controller for the CEO dashboard screen. It handles
 * the actions for the buttons on the screen and updates the username label.
 */
public class CeoDashboardController implements Initializable {

	// FXML buttons
	@FXML
	private Button LogoutBtn;
	@FXML
	private Button viewReportsBtn;
	@FXML
	private Button showReportBtn;
	@FXML
	private Button aprroveCustomersBtn;
	@FXML
	private Label usernameLabel;

	/**
	 * Displays the CEO dashboard screen and sets the title of the window.
	 * 
	 * @param primaryStage the primary stage of the application
	 */
	public void start(Stage primaryStage) {
		generalMethods.displayScreen(primaryStage, getClass(), "/client_fxml/CeoDashboard.fxml",
				"Ekurt Manager's Menu");
	}

	/**
	 * Event handler for the "View Reports" button. Hides the current window and
	 * displays the manager reports screen.
	 * 
	 * @param event the ActionEvent that triggers the handler
	 * @throws Exception if an exception occurs
	 */
	@FXML
	void viewReports(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		Stage primaryStage = new Stage();
		ManagerReportsFxmlController getPage = new ManagerReportsFxmlController();
		getPage.start(primaryStage);
	}

	/**
	 * Event handler for the "Approve Customers" button. Hides the current window
	 * and displays the customer approving screen.
	 * 
	 * @param event the ActionEvent that triggers the handler
	 * @throws Exception if an exception occurs
	 */
	@FXML
	void aprroveCustomers(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		Stage primaryStage = new Stage();
		CustomerApprovingController page = new CustomerApprovingController();
		page.start(primaryStage);
	}

	/**
	 * Event handler for the "Logout" button. Calls the logout method in the
	 * LoginController class and displays the login screen.
	 * 
	 * @param event the ActionEvent that triggers the handler
	 * @throws Exception if an exception occurs
	 */
	@FXML
	void logout(ActionEvent event) {
		LoginController.logout(ClientUtils.currUser.getUsername());
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		new LoginController().start(new Stage());

	}

	/**
	 * Initializes the User Dashboard page and sets the text for the username label.
	 *
	 * @param arg0 URL
	 * @param arg1 ResourceBundle
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Display the current user's name in the username label
		usernameLabel.setText(ClientUtils.currUser.getFirstName() + " " + ClientUtils.currUser.getLastName());
	}

}