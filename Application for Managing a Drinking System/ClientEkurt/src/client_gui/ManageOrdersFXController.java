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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import logic.DeliveryOrder;

public class ManageOrdersFXController implements Initializable {
	
	private static final OrderStatusEnum WAIT_APPROVAL = null;
	@FXML
	private Button backBtn;
	@FXML
	private Button ShowOrdersBtn;
	@FXML
	private Button ApproveOrderBtn;
	@FXML
	private Button CompleteOrderBtn;
	@FXML
	private TableView<DeliveryOrder> table = new TableView<DeliveryOrder>(); //changed to static
	@FXML
	private TableColumn<DeliveryOrder, String> OrderIdColTbl;
	@FXML
	private TableColumn<DeliveryOrder, String> ClientIDColTbl;
	@FXML
	private TableColumn<DeliveryOrder, Timestamp> OrderDateColTbl;
	@FXML
	private TableColumn<DeliveryOrder, Timestamp> SupplyDateColTbl;
	@FXML
	private TableColumn<DeliveryOrder, Enum> StatusColTbl;
	@FXML
	private Label noOrdersErrorLabel;
	@FXML
	private Label NoSelectedOrderErrorLabel;
	@FXML
	private Label FailUpdateStatusLabel;
	
	private ObservableList<DeliveryOrder> listView = FXCollections.observableArrayList();
	private generalMethods gm = new generalMethods();
	private DeliveryOrder selectedOrder;
	
	@SuppressWarnings("static-access")
	public void start(Stage primaryStage) throws Exception {
		gm.displayScreen(primaryStage, getClass(), "/client_fxml/ManageOrdersPage.fxml", "Manage Orders Page");
	}
	/**
	This function initializes the delivery orders table, by setting the column values for the various attributes of a delivery order.
	It also sets the visibility of error labels to false, and adds a listener to the table selection model, so that the selected order can be stored in the 'selectedOrder' variable.
	*/
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		OrderIdColTbl.setCellValueFactory(new PropertyValueFactory<DeliveryOrder, String>("OrderId"));
		ClientIDColTbl.setCellValueFactory(new PropertyValueFactory<DeliveryOrder, String>("ClientId"));
		OrderDateColTbl.setCellValueFactory(new PropertyValueFactory<DeliveryOrder, Timestamp>("OrderDate"));
		SupplyDateColTbl.setCellValueFactory(new PropertyValueFactory<DeliveryOrder, Timestamp>("SuppDate"));
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
	 * This method is called when the back button is clicked. It hides the current
	 * window and opens the CEO DeliveryOperatorDashboardController page.
	 * 
	 * @param event the event that triggered this method.
	 */
	@FXML
	void Back(MouseEvent event) throws Exception {
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding window
		Stage primaryStage = new Stage();
		DeliveryOperatorDashboardController menuPage = new DeliveryOperatorDashboardController();
		menuPage.start(primaryStage);
	}
	/**
	This function is responsible for getting the orders for the current region of the logged in user.
	It sends a message to the server with the action of GET_ORDERS and the region of the current user.
	It then receives a message from the server with a list of the orders in that region.
	If there are no orders for the region, it displays an error message.
	If there are orders, it populates the table with the list of orders and enables the approve and complete order buttons.
	@param event The action event of clicking the 'Get Orders' button
	@throws Exception
	*/
	@FXML
	void GetOrders(ActionEvent event) throws Exception {
		Transaction msg;
		msg = new Transaction(Action.GET_ORDERS,ClientUtils.currUser.getRegion());
	    ClientUI.chat.accept(msg);
		msg = ClientUI.chat.getObj();
		if(msg.getResponse()==Response.FAILED_TO_GET_ORDERS) {
			listView.clear();
			noOrdersErrorLabel.setText("There are currently no orders for this region");
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
			ApproveOrderBtn.setDisable(false);
			CompleteOrderBtn.setDisable(false);
		}
	}
	/**
	This function is used to approve the selected order.
	The function first checks if any order is selected. If no order is selected, an error message is displayed to the user.
	If an order is selected, the function sends a request to the server to approve the selected order.
	If the order is successfully approved, the status of the order is updated in the table and the table is refreshed.
	@param event the event that triggered the function call
	@throws Exception the exception that may be thrown by the start() function
	*/
	@FXML
	void ApproveOrders(ActionEvent event) throws Exception {
		if(selectedOrder==null) {
			NoSelectedOrderErrorLabel.setVisible(true);
			return;
		}
		else NoSelectedOrderErrorLabel.setVisible(false);
		FailUpdateStatusLabel.setVisible(false);
		Transaction msg;
		System.out.println(selectedOrder);
		msg = new Transaction(Action.APPROVE_ORDER,selectedOrder);
		ClientUI.chat.accept(msg);
		msg = ClientUI.chat.getObj();
		if(msg.getResponse()==Response.FAILED_TO_APPROVE_ORDER) {
			FailUpdateStatusLabel.setVisible(true);
			return;
		}
		listView.get(listView.indexOf(selectedOrder)).setStatus(OrderStatusEnum.ON_THE_WAY);
		table.refresh();
		selectedOrder=null;
	}
	/**
	This method is used to complete a selected order.
	It first checks if an order has been selected, if not an error label is displayed.
	It then creates a new transaction with the action of COMPLETE_ORDER and the selected order as the data.
	It sends this transaction to the server and waits for a response.
	If the response is FAILED_TO_COMPLETE_ORDER it displays an error label.
	Else it updates the status of the selected order to COMPLETE and refreshes the table.
	@param event - the event that triggered the method, in this case clicking the CompleteOrder button
	@throws Exception
	*/
	@FXML
	void CompleteOrder(ActionEvent event) throws Exception {
		if(selectedOrder==null) {
			NoSelectedOrderErrorLabel.setVisible(true);
			return;
		}
		else NoSelectedOrderErrorLabel.setVisible(false);
		FailUpdateStatusLabel.setVisible(false);
		Transaction msg;
		System.out.println(selectedOrder);
		msg = new Transaction(Action.COMPLETE_ORDER,selectedOrder);
		ClientUI.chat.accept(msg);
		msg = ClientUI.chat.getObj();
		if(msg.getResponse()==Response.FAILED_TO_COMPLETE_ORDER) {
			FailUpdateStatusLabel.setVisible(true);
			return;
		}
		listView.get(listView.indexOf(selectedOrder)).setStatus(OrderStatusEnum.COMPLETE);
		table.refresh();
		selectedOrder=null;
	}
}
