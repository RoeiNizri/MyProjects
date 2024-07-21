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

*The StorageWorkerDashboardController class is a controller class that handles the actions and logic
*behind the Storage Worker Dashboard GUI. It implements the Initializable interface and provides
*the functionality for the Storage Worker to update stock, logout, and display a welcome message
*when the GUI is opened.
*
*/
public class StorageWorkerDashboardController implements Initializable {

	@FXML
	private Button UpdateStockBtn;
	@FXML
	private Button LogoutBtn;
	@FXML
	private Label lbl = new Label();
	//private generalMethods gm = new generalMethods();
	//private generalMethods gm = new generalMethods();

	/**
	*The start method is used to display the Storage worker window.
	*@param primaryStage The primary stage on which the Storage worker window is displayed.
	*/
	public void start(Stage primaryStage) {
		generalMethods.displayScreen(primaryStage, getClass(), "/client_fxml/StorageWorkerDashboard.fxml", "Storage worker window");
	}
	/**

	*This method handles updating a stock item by hiding the current window, creating a new stage, and displaying the UpdateStockFXController page.
	*@param event the ActionEvent that triggers the method
	*@throws Exception if there is an issue with creating or displaying the new stage
	*/
	@FXML
	void UpdateStock(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		Stage primaryStage = new Stage();
		UpdateStockFXController getPage = new UpdateStockFXController();
		getPage.start(primaryStage);
		}
	/**

	*This method handles logging out the current user by calling the logout method in LoginController class for the current user, hiding the current window and displaying the login page.
	*@param event the ActionEvent that triggers the method
	*@throws Exception if there is an issue with creating or displaying the new stage
	*/
	@FXML
	void logout(ActionEvent event) throws Exception {
		LoginController.logout(ClientUtils.currUser.getUsername());
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		new LoginController().start(new Stage());
	}
	/**

	This method initializes the scene by running a task on the JavaFX Application Thread, setting the text of a label to display a welcome message for the current user.
	@param arg0 URL of the location of the FXML file that initializes this controller
	@param arg1 ResourceBundle for the localized strings used in the scene
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