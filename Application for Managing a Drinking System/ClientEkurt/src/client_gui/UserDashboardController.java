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
 * UserDashboardController is a class that displays the user dashboard page and
 * provides functionality for interacting with the page. It also implements the
 * Initializable interface to initialize the page when it is first displayed.
 */
public class UserDashboardController implements Initializable {

	@FXML
	private Button registerBtn;
	@FXML
	private Button LogoutBtn;
	@FXML
	private Label usernameLabel;

	/**
	 * Displays the User Dashboard page.
	 *
	 * @param primaryStage the primary stage for this application
	 */
	public void start(Stage primaryStage) {
		generalMethods.displayScreen(primaryStage, getClass(), "/client_fxml/UserDashboard.fxml", "Ekurt User's Menu");
	}

	/**
	 * Handles the event of clicking the "Go to Register" button and navigates to
	 * the User Register page.
	 *
	 * @param event ActionEvent
	 */
	@FXML
	void goToRegister(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		Stage primaryStage = new Stage();
		UserRegisterController Page = new UserRegisterController();
		Page.start(primaryStage);
	}

	/**
	 * Handles the event of clicking the "Logout" button and logs the user out and
	 * navigates to the login page.
	 *
	 * @param event ActionEvent
	 * @throws Exception
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
		usernameLabel.setText(ClientUtils.currUser.getFirstName() + " " + ClientUtils.currUser.getLastName());
	}

}