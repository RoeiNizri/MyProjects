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
 * 
 * This class is the controller for the subscriber approving page,
 * 
 * it handles the functionality of the approve button and back button.
 * 
 * It also displays a table of pending subscriber requests.
 * 
 * @author [Roei Nizri] & [Ran Polac]
 */
public class SubscriberApprovingController implements Initializable {

	@FXML
	private ImageView backBtn;
	@FXML
	private Button approveRequestBtn;
	@FXML
	private TableView<User> requestSubscriberToRegisterTbl = new TableView<User>();
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
	 * 
	 * This method displays the subscriber approving page
	 * 
	 * @param primaryStage the primary stage of the page
	 */
	public void start(Stage primaryStage) {
		generalMethods.displayScreen(primaryStage, getClass(), "/client_fxml/SubscriberApprovingPage.fxml",
				"Ekrut Subscriber Approving Page");
	}

	/**
	 * 
	 * This method is called when the page is loaded, it sets the table data and
	 * error label visibility.
	 * 
	 * @param location  URL
	 * @param resources Resource Bundle
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
	 * 
	 * clickOnBackButton is an event handler that listens to the back button, it is
	 * responsible for closing the current window and opening the service worker
	 * dashboard page.
	 * 
	 * @param event A semantic event which indicates that a mouse-button has been
	 *              pressed on a component.
	 */
	@FXML
	void clickOnBackButton(MouseEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide();
		Stage primaryStage = new Stage();
		ServiceWorkerDashboardController menuPage = new ServiceWorkerDashboardController();
		menuPage.start(primaryStage);
	}

	/**
	 * 
	 * approveRequest method is an event handler that listens to the approve button,
	 * it is responsible for approving the selected subscriber request.
	 * 
	 * @param event An event which indicates that a specific action occurred.
	 */
	@FXML
	void approveRequest(ActionEvent event) {
		int userId = requestSubscriberToRegisterTbl.getSelectionModel().getSelectedItem().getId();
		String id = String.valueOf(userId);
		Transaction transaction = new Transaction(Action.APPROVE_SUBSCRIBER_REQUEST, null, id);
		ClientUI.chat.accept(transaction);
		transaction = ClientUI.chat.getObj();
		if (transaction.getResponse() == Response.APPROVE_SUBSCRIBER_REQUEST_SUCCESSFULLY) {
			listView.clear();
			errorLabel.setStyle(Constants.TEXT_FIELD_VALID_STYLE);
			errorLabel.setTextFill(Color.GREEN);
			errorLabel.setText("The User Approved.");
			errorLabel.setVisible(true);
			displayTable();
		}
	}

	/**
	 * 
	 * This method displays the table of subscriber requests to the service worker.
	 * A Transaction object is created with the Action of
	 * GET_SUBSCRIBER_REQUESTS_TO_SERVICE_WORKER and passed to the chat. The chat
	 * then returns the transaction object containing the data of the subscriber
	 * requests. The table is made editable and the data is casted to a List of User
	 * objects. If the response of the transaction is
	 * GET_SUBSCRIBER_REQUESTS_TO_SERVICE_WORKER_SUCCESSFULLY, the data is added to
	 * the list view and displayed in the table.
	 */
	void displayTable() {
		Transaction transaction = new Transaction(Action.GET_SUBSCRIBER_REQUESTS_TO_SERVICE_WORKER, null, null);
		ClientUI.chat.accept(transaction);
		transaction = ClientUI.chat.getObj();
		requestSubscriberToRegisterTbl.setEditable(true);
		@SuppressWarnings("unchecked")
		List<User> temp = (List<User>) transaction.getData();
		if (transaction.getResponse() == Response.GET_SUBSCRIBER_REQUESTS_TO_SERVICE_WORKER_SUCCESSFULLY) {
			for (int i = 0; i < temp.size(); i++) {
				listView.add(temp.get(i));
			}
			requestSubscriberToRegisterTbl.setItems(listView);
		}
	}
}
