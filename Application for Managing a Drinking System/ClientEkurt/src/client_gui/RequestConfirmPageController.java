package client_gui;

import Utils.generalMethods;
import clientUtil.ClientUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
/**
 * The RequestConfirmPageController class is a JavaFX controller class for a "Request Confirmation" page, 
 * which appears after a user has submitted a request (such as an order).
 */
public class RequestConfirmPageController {

	@FXML
	private Button okBtn;

	@FXML
	private Label orderCode = new Label();
	/**
	* The start method is used to display the Request Confirmation page by calling the
	* generalMethods.displayScreen() method, passing in the primary stage, the class, the FXML
	* file location and the title of the page as arguments.
	*
	*/
	public void start(Stage primaryStage) {
		generalMethods.displayScreen(primaryStage, getClass(), "/client_fxml/RequestConfirmaionPage.fxml",
				"Request Confirmation");
	}
	/**
	* This method is called when the user clicks on the "OK" button. It hides the current window and
	* opens the appropriate dashboard based on the user's role.
	*
	* @param event the event that triggered the method call
	*/
	@FXML
	void clickOnOkBtn(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide();
		switch (ClientUtils.currUser.getRole()) {
		case USER: {
			new UserDashboardController().start(new Stage());
			break;
		}
		case CUSTOMER: {
			new MainDashboradController().start(new Stage());
			break;
		}
		case SUBSCRIBER: {
			new MainDashboradController().start(new Stage());
			break;
		}
		default:
			break;
		}
	}
}
