package client_gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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
 * 
 * UserRegisterController is a class that handles the registration of a user to
 * a customer.
 * 
 * It implements the Initializable interface, which allows it to be initialized
 * with data when the corresponding
 * 
 * FXML file is loaded.
 * 
 * The class contains a start method, which displays the User Register Page, and
 * an initialize method, which sets the prompt
 * 
 * text of certain text fields to the current user's information.
 * 
 * The class also contains a clickOnBackButton method, which is called when the
 * back button is clicked and takes the user
 * 
 * back to the User Dashboard page.
 * 
 * The registerUserToCustomer method is called when the register button is
 * clicked, and it sends a transaction to the server
 * 
 * with the user's information to register them as a customer. If the
 * registration is unsuccessful, an error label will be
 * 
 * displayed. If it is successful, a confirmation page will be displayed.
 */
public class UserRegisterController implements Initializable {

	@FXML
	private Button registerBtn;
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
	 * Displays the User Register page.
	 *
	 * @param primaryStage the primary stage for this application
	 */
	public void start(Stage primaryStage) {
		generalMethods.displayScreen(primaryStage, getClass(), "/client_fxml/UserRegisterPage.fxml",
				"Ekurt Register User");
	}

	/**
	 * Handles the event of clicking the back button and navigates to the User
	 * Dashboard page.
	 *
	 * @param event MouseEvent
	 */
	@FXML
	void clickOnBackButton(MouseEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
		Stage primaryStage = new Stage();
		UserDashboardController page = new UserDashboardController();
		page.start(primaryStage);
	}

	/**
	 * Initializes the User Register page and sets the prompt text for the text
	 * fields.
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
	}

	/**
	 * Registers the user as a customer by sending a transaction to the server and
	 * handling the response.
	 *
	 * @param event ActionEvent
	 * @throws Exception
	 */
	@FXML
	void registerUserToCustomer(ActionEvent event) {

		List<String> inputQuery = new ArrayList<>();
		String date = expMonthTxt.getText() + "/" + expYearTxt.getText();
		inputQuery.addAll(Arrays.asList(String.valueOf(ClientUtils.currUser.getId()), creditCardTxt.getText(),
				cvvTxt.getText(), date));
		Transaction t = new Transaction(Action.REGISTER_USER_TO_CUSTOMER, null, inputQuery);
		ClientUI.chat.accept(t);
		t = ClientUI.chat.getObj();
		if (t.getResponse() == Response.REGISTER_USER_TO_CUSTOMER_UNSUCCESSFULLY) {
			errorLabel.setVisible(true);
			errorLabel.setStyle(Constants.TEXT_NOT_VALID_STYLE);
			errorLabel.setTextFill(Color.RED);
			errorLabel.setText("Register user to customer is faild.");
		} else {
			errorLabel.setVisible(true);
			errorLabel.setStyle(Constants.TEXT_FIELD_VALID_STYLE);
			errorLabel.setTextFill(Color.GREEN);
			errorLabel.setText("The request was sent.\nThe region manager need to\naprrove your request.");
			((Node) event.getSource()).getScene().getWindow().hide();
			RequestConfirmPageController page = new RequestConfirmPageController();
			Stage primaryStage = new Stage();
			page.start(primaryStage);
		}
	}
}
