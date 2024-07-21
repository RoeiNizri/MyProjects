package client_gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import Utils.Constants;
import Utils.generalMethods;
import client.ClientUI;
import clientUtil.ClientUtils;
import common.Action;
import common.Response;
import common.Transaction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * SubscriberRegisterController is a class that displays the subscriber
 * registration page and provides functionality for interacting with the page.
 * It also implements the Initializable interface to initialize the page when it
 * is first displayed.
 */
public class SubscriberRegisterController implements Initializable {

	@FXML
	private Button requestBtn;
    @FXML
    private ImageView backBtn;
	@FXML
	private TextField firstNameTxt;
	@FXML
	private TextField lastNameTxt;
	@FXML
	private TextField idNumberTxt;
	@FXML
	private TextField emailTxt;
	@FXML
	private TextField phoneTxt;
	@FXML
	private TextField creditCardTxt;
	@FXML
	private TextField expMonthTxt;
	@FXML
	private TextField expYearTxt;
	@FXML
	private TextField cvvTxt;
	@FXML
	private Label errorLabel;

	/**
	 * Displays the Subscriber Register page.
	 *
	 * @param primaryStage the primary stage for this application
	 */
	public void start(Stage primaryStage) {
		generalMethods.displayScreen(primaryStage, getClass(), "/client_fxml/SubscriberRegisterPage.fxml",
				"Ekurt To Be Subscriber");
	}

	/**
	 * Handles the event of clicking the back button and navigates to the User
	 * Dashboard page.
	 *
	 * @param event ActionEvent
	 */
	@FXML
	void Back(MouseEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
		Stage primaryStage = new Stage();
		UserDashboardController page = new UserDashboardController(); // customerDashboardcontroller
		page.start(primaryStage);
	}

	/**
	 * Initializes the Subscriber Register page and sets the prompt text for the
	 * text fields.
	 *
	 * @param arg0 URL
	 * @param arg1 ResourceBundle
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		firstNameTxt.setPromptText(ClientUtils.currUser.getFirstName());
		lastNameTxt.setPromptText(ClientUtils.currUser.getLastName());
		idNumberTxt.setPromptText(String.valueOf(ClientUtils.currUser.getId()));
		emailTxt.setPromptText(ClientUtils.currUser.getEmail());
		phoneTxt.setPromptText(ClientUtils.currUser.getTelephone());
		errorLabel.setVisible(false);
		Transaction transaction = new Transaction(Action.GET_CREDIT_CARD_BY_ID, null,
				String.valueOf(ClientUtils.currUser.getId()));
		ClientUI.chat.accept(transaction);
		transaction = ClientUI.chat.getObj();
		@SuppressWarnings("unchecked")
		List<String> paymentDetails = (List<String>) transaction.getData();
		creditCardTxt.setPromptText(paymentDetails.get(0));
		cvvTxt.setPromptText(paymentDetails.get(1));
		String date = paymentDetails.get(2);
		String[] splitDate = date.split("/");
		String month = splitDate[0];
		String year = splitDate[1];
		expMonthTxt.setPromptText(month);
		expYearTxt.setPromptText(year);

	}

	/**
	 * Sends a request to register the customer as a subscriber and handles the
	 * response.
	 *
	 * @param event ActionEvent
	 */
	@FXML
	void requestToSubscriber(ActionEvent event) {
		Transaction transaction = new Transaction(Action.REGISTER_CUSTOMER_TO_SUBSCRIBER, null,
				String.valueOf(ClientUtils.currUser.getId()));
		ClientUI.chat.accept(transaction);
		transaction = ClientUI.chat.getObj();
		if (transaction.getResponse() == Response.REGISTER_CUSTOMER_TO_SUBSCRIBER_UNSUCCESSFULLY) {
			errorLabel.setVisible(true);
			errorLabel.setStyle(Constants.TEXT_NOT_VALID_STYLE);
			errorLabel.setTextFill(Color.RED);
			errorLabel.setText("Request is faild.");
		} else {
			errorLabel.setVisible(true);
			errorLabel.setStyle(Constants.TEXT_FIELD_VALID_STYLE);
			errorLabel.setTextFill(Color.GREEN);
			errorLabel.setText("The request was sent.\nThe service worker need \nto aprrove your request.");
			((Node) event.getSource()).getScene().getWindow().hide();
			RequestConfirmPageController page = new RequestConfirmPageController();
			Stage primaryStage = new Stage();
			page.start(primaryStage);
		}
	}
}
