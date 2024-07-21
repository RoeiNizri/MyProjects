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

public class DeliveryOperatorDashboardController implements Initializable {
	/**
	 * This class represents the controller for the  DeliveryOperatorDashboard. 
	 * it shows the options that the DeliveryOperator has
	 * in our case he has the option to manage orders.
	 */
	@FXML
	private Button ManagerOrdersBtn;
	@FXML
	private Button LogoutBtn;
	@FXML
	private Label lbl = new Label();
	//private generalMethods gm = new generalMethods();
	/**
	 * Displays the DeliveryOperatorDashboard screen and sets the title of the window.
	 * 
	 * @param primaryStage the primary stage of the application
	 */
	public void start(Stage primaryStage) {
		generalMethods.displayScreen(primaryStage, getClass(), "/client_fxml/DeliveryOperatorDashboard.fxml", "Delivery Operator window");
	}
	/**
	 * Event handler for the "ManagerOrders" button. Hides the current window and displays
	 * the ManageOrdersFXController.
	 * 
	 * @param event the MouseEvent that triggers the handler
	 */
	@FXML
	void ManagerOrders(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		Stage primaryStage = new Stage();
		ManageOrdersFXController getPage = new ManageOrdersFXController();
		getPage.start(primaryStage);
		}
	/**
	 * Event handler for the "logout" button. Hides the current window and bring us back to
	 * the LoginController page.
	 * 
	 * @param event the MouseEvent that triggers the handler
	 */
	@FXML
	void logout(ActionEvent event) throws Exception {
		LoginController.logout(ClientUtils.currUser.getUsername());
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		new LoginController().start(new Stage());
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		lbl.setText("Welcome Back\n"+ClientUtils.currUser.getFirstName()+ " " + ClientUtils.currUser.getLastName());

	}
	
}