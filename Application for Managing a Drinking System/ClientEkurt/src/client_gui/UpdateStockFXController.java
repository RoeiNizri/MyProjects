package client_gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import Utils.Constants;
import Utils.generalMethods;
import client.ClientUI;
import clientUtil.ClientUtils;
import common.Action;
import common.Response;
import common.Transaction;
import enums.RegionEnum;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import logic.*;
/**
 *UpdateStockFXcontroller is a class for a stock management.it updates the stock of products in the machines.
 *
 * It implements the Initializable interface, which is used to initialize the controller class.
 * 
 *It uses FXML to define the layout and components of the GUI and the @FXML annotation to link the code with the 
 *corresponding elements in the FXML file.
 *
 *It contains methods that handle button clicks, e.g. Back() method that goes back to the StorageWorkerDashboard and
 *initialize() method that sets up the TableView with the products in the machine, the ComboBox with the machines, and other widgets.
 *It also contains methods that handle displaying error messages, updating the product quantity, and displaying requests for products.
 *It also uses a Transaction object to send requests to the server for getting machines and other data and uses ClientUI.chat.accept()
 *to send the transaction to the server.
 */
public class UpdateStockFXController implements Initializable {

	@FXML
	private TableView<ProductsInMachine> productsTable = new TableView<ProductsInMachine>();
	@FXML
	private TableColumn<ProductsInMachine, String> productIdCol;
	@FXML
	private TableColumn<ProductsInMachine, String> productNameCol;
	@FXML
	private TableColumn<ProductsInMachine, String> quantityCol;
	@FXML
	private TableColumn<ProductsInMachine, Integer> LimitCol;
	@FXML
	private TableColumn<ProductsInMachine, String> quantityStatusCol;
	@FXML
	private TableCell<ProductsInMachine, String> quantityCell = new TableCell<>();
	@FXML
	private ComboBox<String> machineComboBox;
    @FXML
    private ImageView backBtn;
	@FXML
	private Label errorLabel;
	@FXML
	private Button showRequestsBtn;
	@FXML
	private Button UpdateProductQuantityBtn;
	@FXML
	private TextField QuantityTxt;
	
	private ObservableList<ProductsInMachine> listView = FXCollections.observableArrayList();
	private static ProductsInMachine selectedProduct;
	
	public void start(Stage primaryStage) {
		generalMethods.displayScreen(primaryStage, getClass(), "/client_fxml/UpdateStockPage.fxml",
				"Update stock Page");
	}
	
	/**
	 * Event handler for "Back button".
	 * The method hides the current window, creates a new stage, creates a new instance of the
	 * "StorageWorkerDashboardController" class, and then calls the "start" method on that class, 
	 * passing the new primary stage as a parameter. This changes the current scene being 
	 * displayed to the user to a new scene associated with the "StorageWorkerDashboardController" class.
	 * 
	 * @param event the MouseEvent that triggers the handler.
	 */
    @FXML
    void Back(MouseEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
		Stage primaryStage = new Stage();
		StorageWorkerDashboardController menuPage = new StorageWorkerDashboardController();
		menuPage.start(primaryStage);
    }
    /**
	 *This method initializes the productIdCol, productNameCol, quantityCol, LimitCol and quantityStatusCol columns of the productsTable,
	 *sets the errorLabel to be not visible, retrieves a list of machines based on the current user's region, and populates the machineComboBox
	 *with the retrieved list of machines. It also adds a listener to the productsTable's selectedItemProperty, which updates the
	 *selectedProduct variable with the newly selected product.
	 *@param location - the location of the FXML file
	 *@param resources - the resource bundle for localization
	 */
	public void initialize(URL location, ResourceBundle resources) {
		productIdCol.setCellValueFactory(new PropertyValueFactory<ProductsInMachine, String>("productId"));
		productNameCol.setCellValueFactory(new PropertyValueFactory<ProductsInMachine, String>("productName"));
		quantityCol.setCellValueFactory(new PropertyValueFactory<ProductsInMachine, String>("quantity"));
		LimitCol.setCellValueFactory(new PropertyValueFactory<ProductsInMachine, Integer>("productLimit"));
		quantityStatusCol.setCellValueFactory(new PropertyValueFactory<ProductsInMachine, String>("quantityStatus"));
		errorLabel.setVisible(false);
		String region = ClientUtils.currUser.getRegion().toString();
		List<String> machines;
		Transaction transaction;
		if (ClientUtils.currUser.getRegion() == RegionEnum.WORLDWIDE) {
			transaction = new Transaction(Action.GET_MACHINE, null, null);
			ClientUI.chat.accept(transaction);
			transaction = ClientUI.chat.getObj();
			machines = (List<String>) transaction.getData();
		} else {
			transaction = new Transaction(Action.GET_MACHINE_BY_REGION, null, region);
			ClientUI.chat.accept(transaction);
			transaction = ClientUI.chat.getObj();
			machines = (List<String>) transaction.getData();
		}
		for (int i = 0; i < machines.size(); i++) {
			machines.get(i).split("\\s+");
		}
		if (transaction.getResponse() == Response.GETMACHINE_SUCCESSFULLY
				|| transaction.getResponse() == Response.GET_MACHINE_BY_REGION_SUCCESSFULLY) {
			machineComboBox.setItems(FXCollections.observableList(machines));
		}
		productsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
		    // Update the selectedOffer variable with the new selected Offer object
			selectedProduct = newValue;
			});
	}
	/**
	 * Event handler method for the "Show Requests" button.
	 * When the button is clicked, the method retrieves the inventory stock data for the selected machine
	 * and displays it in the productsTable and listView. It also sets the color of quantity column to red.
	 *
	 * @param event the ActionEvent that triggered the method call
	 */
	@FXML
	void ShowRequests(ActionEvent event) {
		listView.clear();
		QuantityTxt.setText(null);
		String machine = machineComboBox.getValue();
		if (machine.equals("")) {
			errorLabel.setText("Please choose a machine.");
			errorLabel.setVisible(true);
		}
		Transaction transaction = new Transaction(Action.GET_INVENTORY_STOCK, null, machine);
		ClientUI.chat.accept(transaction);
		transaction = ClientUI.chat.getObj();
		productsTable.setEditable(true);
		List<ProductsInMachine> temp = (List<ProductsInMachine>) transaction.getData();
		if (transaction.getResponse() == Response.GETSTOCK_SUCCESSFULLY) {
			for (int i = 0; i < temp.size(); i++) {
				listView.add(temp.get(i));
			}
			productsTable.setItems(listView);
			//setColors();
		} else {
			errorLabel.setText("There is not available reports in the system");
			errorLabel.setVisible(true);
		}
		TableColumn<ProductsInMachine, String> column = quantityCol;

		// Create a cell factory that sets the text color to red for cells in the specified column
		column.setCellFactory(tc -> {
		    TableCell<ProductsInMachine, String> cell = new TableCell<>();
		    cell.textProperty().bind(cell.itemProperty());
		    cell.itemProperty().addListener((obs, oldText, newText) -> {
		        if (newText != null) {
		            cell.setTextFill(Color.RED);
		        }
		    });
		    return cell;
		});
	}
	/**
	 * Event handler method for the "Update Quantity" button.
	 * When the button is clicked, the method updates the quantity of the selected product in the selected machine.
	 * It also updates the quantity status of the selected product to "IN_STOCK"
	 * 
	 * @param event the ActionEvent that triggered the method call
	 */
	@FXML
	void UpdateQuantity(ActionEvent event) {
		if(selectedProduct==null) {
			errorLabel.setText("Please Choose a product");
			errorLabel.setVisible(true);
			return;
		}
		if (QuantityTxt.getText()==null) {
			QuantityTxt.setStyle(Constants.TEXT_FIELD_NOT_VALID_STYLE);
			errorLabel.setText("Please enter Quantity.");
			errorLabel.setVisible(true);
			return;
		}
		else {
			List<String> queryInputs = new ArrayList<>();
			queryInputs.add(QuantityTxt.getText());
			queryInputs.add(machineComboBox.getValue());
			queryInputs.add(selectedProduct.getProductId());
			Transaction transaction = new Transaction(Action.UPDATE_QUANTITY_IN_MACHINE, null, queryInputs);
			ClientUI.chat.accept(transaction);
			transaction = ClientUI.chat.getObj();
			listView.get(listView.indexOf(selectedProduct)).setQuantity(QuantityTxt.getText());
			listView.get(listView.indexOf(selectedProduct)).setQuantityStatus("IN_STOCK");
			setColors();
		}
	}
	/**
	 * This method sets the text color of the quantityCol column to green if the value in the quantity column
	 *  is greater than the value in the LimitCol column, and red otherwise.
	 */

	void setColors() {
		// Get the columns you want to compare
		TableColumn<ProductsInMachine, String> quantityColumn = quantityCol;
		TableColumn<ProductsInMachine, Integer> limitColumn = LimitCol;

		// Create a cell factory that sets the text color to green if the value in the quantity column is greater than the value in the limit column, and red otherwise
		quantityColumn.setCellFactory(tc -> {
		    TableCell<ProductsInMachine, String> cell = new TableCell<>();
		    cell.textProperty().bind(cell.itemProperty());
		    cell.itemProperty().addListener((obs, oldText, newText) -> {
		        if (newText != null) {
		            // Get the row index for the current cell
		            int rowIndex = cell.getIndex();

		            // Get the value in the limit column for the current row
		            Integer limitValue = limitColumn.getCellData(rowIndex);

		            // Compare the values and set the text color accordingly
		            if (Integer.parseInt(newText) > limitValue) {
		                cell.setTextFill(Color.GREEN);
		            } else {
		                cell.setTextFill(Color.RED);
		            }
		        }
		    });
		    return cell;
		});
	}
	

	
}
