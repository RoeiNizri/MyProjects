package client_gui;

import Utils.generalMethods;
import client.ClientController;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
/**
 * This class represents the controller for the "ConnectToServer" view.
 * It handles the logic for the user to enter the server IP and port and connect to the server.
 * It has a label for the connection status, two text fields for the IP and port, and a confirm button.

 */
public class ConnectToServerController {
	private String ip, port;
	@FXML
	private Label ConnectStatusLabel;

	@FXML
	private TextField IpTxt;
	@FXML
	private Button ConfirmDetails;

	@FXML
	private TextField PortTxt;
	/**
	* The ConfirmClick method is called when the user clicks the "Confirm" button.
	* It retrieves the IP address and port number entered by the user, creates a new ClientController object with these values,
	* hides the current window, and opens the login window.
	*
	* @param event the ActionEvent object that triggers the method
	* @throws Exception if an error occurs when creating the new login window
	*/
	@FXML
	void ConfirmClick(ActionEvent event) throws Exception {
		ip = IpTxt.getText();
		port = PortTxt.getText();
		ClientUI.chat = new ClientController(ip, Integer.parseInt(port));
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
		new LoginController().start(new Stage());

	}
	/**
	 * Displays the ConnectToServer screen and sets the title of the window.
	 * 
	 * @param primaryStage the primary stage of the application
	 */
	public void start(Stage primaryStage) throws Exception {
		generalMethods.displayScreen(primaryStage, getClass(), "/client_fxml/ConnectToServer.fxml",
				"Ekrut Connect To Server");
	}
}
