package client_gui;

import java.net.URL;
import java.sql.Timestamp;
import java.util.List;
import java.util.ResourceBundle;
import Utils.generalMethods;
import client.ClientUI;
import clientUtil.ClientUtils;
import common.Action;
import common.Response;
import common.Transaction;
import enums.OrderStatusEnum;
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
import javafx.stage.Stage;
import logic.DeliveryOrder;

public class CustomerReceivePageController implements Initializable {
	/**
	 * This class represents the controller for the customer CustomerReceivePage. It
	 * handles the actions for the buttons on the screen and updates the Table
	 * to display the selected orders that are waiting to be approval.
	 */
	private static final OrderStatusEnum WAIT_APPROVAL = null;
	@FXML
	private Button ShowOrdersBtn;
	@FXML
	private TableView<DeliveryOrder> table = new TableView<DeliveryOrder>(); //changed to static
	@FXML
	private TableColumn<DeliveryOrder, String> OrderIdColTbl;
	@FXML
	private TableColumn<DeliveryOrder, String> ClientIDColTbl;
	@FXML
	private TableColumn<DeliveryOrder, Timestamp> OrderDateColTbl;
	@FXML
	private TableColumn<DeliveryOrder, Enum> StatusColTbl;
	@FXML
	private Label noOrdersErrorLabel;
	@FXML
	private Label NoSelectedOrderErrorLabel;
	@FXML
	private Label FailUpdateStatusLabel;
    @FXML
    private ImageView backBtn;
	
	private ObservableList<DeliveryOrder> listView = FXCollections.observableArrayList();
	private generalMethods gm = new generalMethods();
	private DeliveryOrder selectedOrder;
	
	@SuppressWarnings("static-access")
	public void start(Stage primaryStage) throws Exception {
		gm.displayScreen(primaryStage, getClass(), "/client_fxml/CustomerReceivePage.fxml", "Manage your Orders Page");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		OrderIdColTbl.setCellValueFactory(new PropertyValueFactory<DeliveryOrder, String>("OrderId"));
		ClientIDColTbl.setCellValueFactory(new PropertyValueFactory<DeliveryOrder, String>("ClientId"));
		OrderDateColTbl.setCellValueFactory(new PropertyValueFactory<DeliveryOrder, Timestamp>("OrderDate"));
		StatusColTbl.setCellValueFactory(new PropertyValueFactory<DeliveryOrder, Enum>("status"));
		noOrdersErrorLabel.setVisible(false);
		NoSelectedOrderErrorLabel.setVisible(false);
		FailUpdateStatusLabel.setVisible(false);
		table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
	    // Update the selectedOffer variable with the new selected Offer object
	    selectedOrder = newValue;
		});
	}
	/**
	 * Event handler for the "Back" button. Hides the current window and displays
	 * the suitable fxml file based on the currUser log in.
	 * if the Customer was logged in, it will open the MainDashboradController page.
	 * if the Subscriber was logged in, it will open the MainDashboradController page.
	 * 
	 * @param event the ActionEvent that triggers the handler
	 */
	@FXML
	void Back(MouseEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide();
		switch (ClientUtils.currUser.getRole()) {
		case SUBSCRIBER: {
			new MainDashboradController().start(new Stage());
			break;
		}
		case CUSTOMER: {
			new MainDashboradController().start(new Stage());
			break;
		}
		}
		/**
		 It creates a new Transaction with current user's ID, and sends it to the server using the chat object from the ClientUI class.
	 	 If it is a failure, it clears the listView and sets the "noOrdersErrorLabel" text.
		If it is a success, it clears the listView, ,and adds the received DeliveryOrder objects to the listView.
		@param event the ActionEvent object passed in when the button is clicked.
		@throws Exception any exception that may be thrown during the execution of this method
		*/
	}
	@FXML
	void GetOrders(ActionEvent event) throws Exception {
		Transaction msg;
		msg = new Transaction(Action.GET_ORDERS_TO_RECEIVE,ClientUtils.currUser.getId());
	    ClientUI.chat.accept(msg);
		msg = ClientUI.chat.getObj();
		if(msg.getResponse()==Response.FAILED_TO_GET_RECEIVED_ORDERS) {
			listView.clear();
			noOrdersErrorLabel.setText("There are currently no orders available for you!");
			noOrdersErrorLabel.setVisible(true);
		}else {
		listView.clear();
		noOrdersErrorLabel.setVisible(false);
		@SuppressWarnings("unchecked")
		List<DeliveryOrder> Alist = (List<DeliveryOrder>) msg.getData();
		for (DeliveryOrder order : Alist) {
			  listView.add(order);
			}
			listView.forEach(Offer -> System.out.println(Offer));
			table.setEditable(true);
			table.setItems(listView);
		}
	}
	/**
 	first checks if a DeliveryOrder object has been selected in the table, and if not, it sets the "NoSelectedOrderErrorLabel" to be visible.
	Next it checks if the selected order's status is "WAIT_APPROVAL" and if so, it sets the "noOrdersErrorLabel" text to "Cannot receive, needs to be approved first" and makes it visible.
	If both of the above checks passed, it creates a new Transaction object with the action "RECEIVE_ORDER" and the selected order, and sends it to the server using the chat object from the ClientUI class.
	It then receives the server's response and checks if it is a success or failure. If it is a failure, it sets the "FailUpdateStatusLabel" to be visible.
	If it is a success, it updates the status of the selected order in the listView to "RECEIVED" and refreshes the table.
	@param event the ActionEvent object passed in when the button is clicked
	@throws Exception any exception that may be thrown during the execution of this method
	*/
	@FXML
	void ReceiveOrders(ActionEvent event) throws Exception {
		if(selectedOrder==null) {
			NoSelectedOrderErrorLabel.setVisible(true);
			return;
		}
		else NoSelectedOrderErrorLabel.setVisible(false);
		FailUpdateStatusLabel.setVisible(false);
		if(selectedOrder.getStatus()==OrderStatusEnum.valueOf("WAIT_APPROVAL")) {
			noOrdersErrorLabel.setText("Cannot receive, needs to be approved first");
			noOrdersErrorLabel.setVisible(true);
			return;
		}
		Transaction msg;
		System.out.println(selectedOrder);
		msg = new Transaction(Action.RECEIVE_ORDER,selectedOrder);
		ClientUI.chat.accept(msg);
		msg = ClientUI.chat.getObj();
		if(msg.getResponse()==Response.FAILED_TO_SET_RECEIVED) {
			FailUpdateStatusLabel.setVisible(true);
			return;
		}
		listView.get(listView.indexOf(selectedOrder)).setStatus(OrderStatusEnum.RECEIVED);
		table.refresh();
		selectedOrder=null;
	}

}
