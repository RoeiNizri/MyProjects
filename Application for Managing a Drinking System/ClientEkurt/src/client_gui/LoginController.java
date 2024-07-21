package client_gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import Utils.Constants;
import Utils.generalMethods;
import clientUtil.ClientUtils;
import client.ClientController;
import client.ClientUI;
import clientInterfaces.IClientController;
import clientInterfaces.IFxml;
import common.Action;
import common.Response;
import common.Transaction;
import enums.RoleEnum;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import logic.User;

/**
 * This class handles the Item methods to be sent and received from the server
 * for the relevant controllers
 */
public class LoginController {//implements Initializable {

	@FXML
	private Button loginButton;

	@FXML
	private Label loginErrorLabel;

	@FXML
	private Label ektLoginErrorLabel;

	@FXML
	private PasswordField passwordField;

	@FXML
	private TextField userNameField;

	private boolean labelFlag = false;

	@FXML
	//private ComboBox<String> idComboBox = new ComboBox<>();
	private List<String> userIds = new ArrayList<>();

	// For Tests
	ClientController chat;
	IFxml ifxml;
	IClientController iClientController;

	public LoginController(IFxml ifxml, IClientController iClientConsole) {
		this.ifxml = ifxml;
		this.iClientController = iClientConsole;
	}
	public LoginController() {
	}

	/**
	 * Displays the CEO LoginPage screen and sets the title of the window for Login.
	 * 
	 * @param primaryStage the primary stage of the application
	 */
	@FXML
	public void start(Stage primaryStage) {
		generalMethods.displayScreen(primaryStage, getClass(), "/client_fxml/LoginPage.fxml", "Login");
	}

	/**
	 * Handles the click event on the "Login" button. It checks if the username and
	 * password fields are filled, if not it shows an error message, otherwise it
	 * sends a LOGIN_USERNAME_PASSWORD message to the server with the username and
	 * password entered, receives a response from the server, and based on the
	 * response it either shows an error message, sets the current user, hides the
	 * current window and opens the dashboard page for the current user's role
	 * (subscriber or customer).
	 *
	 * @param event The action event that triggers this method
	 */
	@FXML
	void onButtonPressLogin(ActionEvent event) {
		if (userNameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
			setLabelStyleForIncorrectValues("Please supply both username and password");
		} else {
			HashMap<String, String> args = new HashMap<>();
			args.put("username", userNameField.getText());
			args.put("password", passwordField.getText());
			Transaction msg = new Transaction(Action.LOGIN_USERNAME_PASSWORD, args);
			ClientUI.chat.accept(msg);
			msg = (Transaction) ClientUI.chat.getObj();
			if (msg.getResponse().equals(Response.ALREADY_LOGGED_IN)) {
				setLabelStyleForIncorrectValues("User is already logged in");
			} else if (msg.getResponse().equals(Response.INCORRECT_VALUES)) {
				setLabelStyleForIncorrectValues("Wrong username or password - Please try again");
			} else if (msg.getResponse().equals(Response.LOGGED_IN_SUCCESS)) {
				if (msg.getData() instanceof User) {
					ClientUtils.currUser = (User) msg.getData();
				}
				try {
					((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
					openDashboardByRole(ClientUtils.currUser.getRole());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	// A function independent of the GUI
	public static String onButtonPressLogin_Test(String userName, String lastName, Response respsone) {
		String errorLabel = null;
		if (userName.isEmpty() || lastName.isEmpty()) {
			errorLabel = "Please supply both username and password";
		} else {
			HashMap<String, String> args = new HashMap<>();
			args.put("username", userName);
			args.put("password", lastName);
			Transaction msg = new Transaction(Action.LOGIN_USERNAME_PASSWORD, args);
			msg.setResponse(respsone);
			if (msg.getResponse().equals(Response.ALREADY_LOGGED_IN)) {
				errorLabel = "User is already logged in";
			} else if (msg.getResponse().equals(Response.INCORRECT_VALUES)) {
				errorLabel = "Wrong username or password - Please try again";
			} else if (msg.getResponse().equals(Response.LOGGED_IN_SUCCESS)) {
				errorLabel = "Successful Login";
			}
		}
		return errorLabel;
	}

	/**
	 * Logs out the current user from the application. Sends a LOGOUT_USER message
	 * to the server with the username of the user to log out, sets the current user
	 * to null and closes the current window.
	 *
	 * @param the username of the user that wants to log out.
	 */
	public static void logout(String username) {
		ClientUtils.currUser = null;
		HashMap<String, String> args = new HashMap<>();
		args.put("username", username);
		Transaction msg = new Transaction(Action.LOGOUT_USER, args);
		ClientUI.chat.accept(msg);
	}

	/**
	 * makes our password field to be empty
	 * 
	 * @param event the MouseEvent that triggers the handler
	 */
	void setLabelStyleForIncorrectValues(String string) {
		loginErrorLabel.setText(string);
		loginErrorLabel.setVisible(true);
		labelFlag = true;
		userNameField.setStyle(Constants.TEXT_FIELD_NOT_VALID_STYLE);
		passwordField.setStyle(Constants.TEXT_FIELD_NOT_VALID_STYLE);
		userNameField.clear();
		passwordField.clear();
	}

	/**
	 * makes our password field to be empty
	 * 
	 * @param event the MouseEvent that triggers the handler
	 */
	@FXML
	void clickOnPasswordField(MouseEvent event) {
		if (labelFlag) {
			loginErrorLabel.setVisible(false);
			userNameField.setStyle("");
			passwordField.setStyle("");
			labelFlag = false;
		}
	}

	/**
	 * makes our username field to be empty
	 * 
	 * @param event the MouseEvent that triggers the handler
	 */
	@FXML
	void clickOnUsernameField(MouseEvent event) {
		if (labelFlag) {
			loginErrorLabel.setVisible(false);
			userNameField.setStyle("");
			passwordField.setStyle("");
			labelFlag = false;
		}
	}

	/**
	 * Opens the appropriate dashboard page for the given user role.
	 *
	 * @param roleEnum The user's role (subscriber, customer, CEO, etc.)
	 * @throws Exception
	 */
	void openDashboardByRole(RoleEnum roleEnum) throws Exception {
		switch (roleEnum) {

		case CEO:
			CeoDashboardController CEODashboard = new CeoDashboardController();
			Platform.runLater(() -> {
				CEODashboard.start(new Stage());
			});
			break;
		case CUSTOMER:
			if (ClientUtils.configuration.equals("ek")) {
				new EkWelcomePageController().start(new Stage());
			} else if (ClientUtils.configuration.equals("ol")) {
				MainDashboradController CustomerODashboard = new MainDashboradController();
				Platform.runLater(() -> {
					CustomerODashboard.start(new Stage());
				});
			}
			break;
		case SUBSCRIBER:
			if (ClientUtils.configuration.equals("ek")) {
				new EkWelcomePageController().start(new Stage());
			} else if (ClientUtils.configuration.equals("ol")) {
				MainDashboradController subDashboard = new MainDashboradController();
				Platform.runLater(() -> {
					subDashboard.start(new Stage());
				});
			}
			break;
		case MARKETING_WORKER:
			MarketingWorkerDashboardFXController MWDashboard = new MarketingWorkerDashboardFXController();
			Platform.runLater(() -> {
				MWDashboard.start(new Stage());
			});
			break;
		case DELIVERY_OPERATOR:
			DeliveryOperatorDashboardController DODashboard = new DeliveryOperatorDashboardController();
			Platform.runLater(() -> {
				DODashboard.start(new Stage());
			});
			break;
		case USER:
			UserDashboardController userDashboard = new UserDashboardController();
			Platform.runLater(() -> {
				userDashboard.start(new Stage());
			});
			break;
		case REGION_MANAGER:
			CeoDashboardController regionManagerDashboard = new CeoDashboardController();
			Platform.runLater(() -> {
				regionManagerDashboard.start(new Stage());
			});
			break;
		case SERVICE_WORKER:
			ServiceWorkerDashboardController ServiceWorkerDashboard = new ServiceWorkerDashboardController();
			Platform.runLater(() -> {
				ServiceWorkerDashboard.start(new Stage());
			});
			break;
		case STORAGE_WORKER:
			StorageWorkerDashboardController StorageWorkerDashboard = new StorageWorkerDashboardController();
			Platform.runLater(() -> {
				StorageWorkerDashboard.start(new Stage());
			});
			break;
		default:
			break;
		}
	}

	/**
	 * Perform the ek configuration for the current user. It initializes a new
	 * LocalOrder for the current user, Then it sends a
	 * GET_AVAILABLE_PRODUCTS_IN_MACHINE message to the server with the machine name
	 * to get the available products in the machine and a
	 * GET_NOT_AVAILABLE_PRODUCTS_IN_MACHINE message to the server with the machine
	 * name to get the not available products in the machine. Finally it sets the
	 * cartDisplayFlag to true.
	 */
	/*@FXML
	void checkId(ActionEvent event) {
		ektLoginErrorLabel.setVisible(false);
		Transaction msg = new Transaction(Action.CHECK_ID, idComboBox.getValue());
		ClientUI.chat.accept(msg);
		msg = (Transaction) ClientUI.chat.getObj();
		if (msg.getResponse().equals(Response.CHECK_ID_SUCC)) {
			HashMap<String, String> details = (HashMap<String, String>) msg.getData();
			Transaction msg2 = new Transaction(Action.LOGIN_USERNAME_PASSWORD, details);
			ClientUI.chat.accept(msg2);
			msg2 = (Transaction) ClientUI.chat.getObj();
			if (msg2.getResponse().equals(Response.ALREADY_LOGGED_IN)) {
				ektLoginErrorLabel.setText("User is already logged in");
				ektLoginErrorLabel.setVisible(true);
			} else if (msg2.getResponse().equals(Response.LOGGED_IN_SUCCESS)) {
				if (msg2.getData() instanceof User) {
					ClientUtils.currUser = (User) msg2.getData();
				}
				try {
					((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
					openDashboardByRole(ClientUtils.currUser.getRole());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if (msg.getResponse().equals(Response.CHECK_ID_UNSUCC)) {
			ektLoginErrorLabel.setText("Only Subscribers are authorized");
			ektLoginErrorLabel.setVisible(true);

		}
	}*/

	/**
	 * This method hide our lable
	 * 
	 * @param @param event the MouseEvent that triggers the handler
	 */
	@FXML
	void hideLabel(MouseEvent event) {
		ektLoginErrorLabel.setVisible(false);
	}

	/**
	 * This method is called to initialize the FXML controller for EK login and it
	 * retrieves all the user ids from the server to populate the idComboBox.
	 * 
	 * @param arg0 URL location used to resolve relative paths for the root object,
	 *             or null if the location is not known.
	 * @param arg1 The resources used to localize the root object, or null if the
	 *             root object was not localized.
	 */
	/*@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Transaction msg = new Transaction(Action.GET_ALL_USER_ID, null);
		ClientUI.chat.accept(msg);
		msg = (Transaction) ClientUI.chat.getObj();
		userIds.addAll((List<String>) msg.getData());
		idComboBox.getItems().addAll(userIds);

	}*/

	public Label getLoginErrorLabel() {
		return loginErrorLabel;
	}

	public PasswordField getPasswordField() {
		return passwordField;
	}

	public TextField getUserNameField() {
		return userNameField;
	}

	public void setLoginErrorLabel(Label loginErrorLabel) {
		this.loginErrorLabel = loginErrorLabel;
	}

	public void setPasswordField(PasswordField passwordField) {
		this.passwordField = passwordField;
	}

	public void setUserNameField(TextField userNameField) {
		this.userNameField = userNameField;
	}

}
