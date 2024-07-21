package client_gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import Utils.Constants;
import Utils.generalMethods;
import client.ClientUI;
import common.Action;
import common.Response;
import common.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import logic.*;

/**
 * The CustomerApprovingController class is a controller class for the
 * CustomerApprovingPage.fxml file. It handles the customer's registration
 * approval process, including displaying the customer's registration requests
 * in a table, and approving the selected customer's request.
 * 
 * @author [Roei Nizri] & [Ran Polac]
 * @version [1.0]
 * @since [29/12/2022]
 */
public class CustomerApprovingController implements Initializable {

	@FXML
	private ImageView backBtn;
	@FXML
	private Button approveRequestBtn;
	@FXML
	private TableView<User> requestCustomerToRegisterTbl = new TableView<User>();
	@FXML
	private TableColumn<User, String> idCol;
	@FXML
	private TableColumn<User, String> nameCol;
	@FXML
	private TableColumn<User, String> lastNameCol;
	@FXML
	private TableColumn<User, String> phoneCol;
	@FXML
	private TableColumn<User, String> emailCol;
	@FXML
	private TableColumn<User, String> regionCol;
	@FXML
	private Label errorLabel;
	@FXML
	private ObservableList<User> listView = FXCollections.observableArrayList();

	/**
	 * This method is used to start the customer's registration approval process by
	 * displaying the CustomerApprovingPage.fxml file on the primary stage.
	 * 
	 * @param primaryStage The stage on which the CustomerApprovingPage.fxml file
	 *                     will be displayed.
	 * @throws Exception
	 */
	public void start(Stage primaryStage) {
		generalMethods.displayScreen(primaryStage, getClass(), "/client_fxml/CustomerApprovingPage.fxml",
				"Ekrut Customer Approving Page");
	}

	/**
	 * This method is called when the FXML file is loaded. It initializes the table
	 * columns and sets the error label to be invisible. It also calls the
	 * displayTable() method to display the customer's registration requests in the
	 * table.
	 * 
	 * @param location  The location used to resolve relative paths for the root
	 *                  object, or null if the location is not known.
	 * @param resources The resources used to localize the root object, or null if
	 *                  the root object was not localized.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		idCol.setCellValueFactory(new PropertyValueFactory<User, String>("id"));
		nameCol.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
		lastNameCol.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
		phoneCol.setCellValueFactory(new PropertyValueFactory<User, String>("telephone"));
		emailCol.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
		regionCol.setCellValueFactory(new PropertyValueFactory<User, String>("region"));
		errorLabel.setVisible(false);
		displayTable();
	}

	/**
	 * This method is called when the back button is clicked. It hides the current
	 * window and opens the CEO dashboard page.
	 * 
	 * @param event The mouse event that triggers this method.
	 */
	@FXML
	void clickOnBackButton(MouseEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
		Stage primaryStage = new Stage();
		CeoDashboardController menuPage = new CeoDashboardController();
		menuPage.start(primaryStage);
	}

	/**
	 * This method is used to approve a customer's registration request.
	 * 
	 * @param event the event that triggered this method
	 * @throws Exception if an error occurs while communicating with the server
	 */
	@FXML
	void approveRequest(ActionEvent event) {
		int userId = requestCustomerToRegisterTbl.getSelectionModel().getSelectedItem().getId();
		Transaction transaction = new Transaction(Action.APPROVE_REGISTER_REQUEST, null, userId);
		ClientUI.chat.accept(transaction);
		transaction = ClientUI.chat.getObj();
		if (transaction.getResponse() == Response.APPROVE_REGISTER_REQUEST_SUCCESSFULLY) {
			listView.clear();
			errorLabel.setStyle(Constants.TEXT_FIELD_VALID_STYLE);
			errorLabel.setTextFill(Color.GREEN);
			errorLabel.setText("The User Approved.");
			errorLabel.setVisible(true);
			displayTable();
		}
	}

	/**
	 * This method is used to display the customer registration request table.
	 */
	void displayTable() {
		Transaction transaction = new Transaction(Action.GET_USER_REGISTER_REQUEST, null, null);
		ClientUI.chat.accept(transaction);
		transaction = ClientUI.chat.getObj();
		requestCustomerToRegisterTbl.setEditable(true);
		@SuppressWarnings("unchecked")
		List<User> temp = (List<User>) transaction.getData();
		if (transaction.getResponse() == Response.GET_USER_REGISTER_REQUEST_SUCCESSFULLY) {
			for (int i = 0; i < temp.size(); i++) {
				listView.add(temp.get(i));
			}
			requestCustomerToRegisterTbl.setItems(listView);
		}
	}
}
