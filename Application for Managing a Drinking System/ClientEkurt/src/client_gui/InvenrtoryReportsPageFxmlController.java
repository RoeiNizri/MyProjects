package client_gui;

import java.io.IOException;
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
import javafx.beans.binding.Bindings;
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
 * InvenrtoryReportsPageFxmlController is a class that displays the inventory
 * reports page and provides functionality for interacting with the page. It
 * also implements the Initializable interface to initialize the page when it is
 * first displayed.
 */
public class InvenrtoryReportsPageFxmlController implements Initializable {

	public ComboBox<String> getMachineComboBox() {
		return machineComboBox;
	}

	public void setMachineComboBox(ComboBox<String> machineComboBox) {
		this.machineComboBox = machineComboBox;
	}

	@FXML
	private TableView<ProductsInMachine> productsTable = new TableView<ProductsInMachine>();
	@FXML
	private TableColumn<ProductsInMachine, String> productIdCol;
	@FXML
	private TableColumn<ProductsInMachine, String> productNameCol;
	@FXML
	private TableColumn<ProductsInMachine, String> quantityCol;
	@FXML
	private TableColumn<ProductsInMachine, String> priceCol;
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
	private Button showReportBtn;
	@FXML
	private Button exportReportBtn;
	@FXML
	private Button UpdateProductLimitBtn;
	@FXML
	private TextField limitTxt;
	private int limit;
	private ObservableList<ProductsInMachine> listView = FXCollections.observableArrayList();

	/**
	 * This method is used to start the inventory report page.
	 * 
	 * @param primaryStage the primary stage of the GUI
	 */
	public void start(Stage primaryStage) {
		generalMethods.displayScreen(primaryStage, getClass(), "/client_fxml/InventoryReportPage.fxml",
				"Ekurt Report Page");
	}

	/**
	 * This method is used to handle the event of clicking on the back button.
	 * 
	 * @param event the event that triggered this method
	 */
	@FXML
	void clickOnBackButton(MouseEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
		Stage primaryStage = new Stage();
		ManagerReportsFxmlController menuPage = new ManagerReportsFxmlController();
		menuPage.start(primaryStage);
	}

	/**
	 * This method is used to initialize the elements on the inventory report page.
	 * 
	 * @param location  the location of the FXML file
	 * @param resources the resources used in the FXML file
	 */
	@SuppressWarnings("unchecked")
	public void initialize(URL location, ResourceBundle resources) {
		productIdCol.setCellValueFactory(new PropertyValueFactory<ProductsInMachine, String>("productId"));
		productNameCol.setCellValueFactory(new PropertyValueFactory<ProductsInMachine, String>("productName"));
		quantityCol.setCellValueFactory(new PropertyValueFactory<ProductsInMachine, String>("quantity"));
		priceCol.setCellValueFactory(new PropertyValueFactory<ProductsInMachine, String>("price"));
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
	}

	/**
	 * This method is used to view the inventory report for a specific machine.
	 * 
	 * @param event the event that triggered this method
	 */
	@FXML
	void viewInventoryReport(ActionEvent event) {
		listView.clear();
		errorLabel.setVisible(false);
		if (machineComboBox.getValue() == null) {
			setErrorNotValid("Please choose machine.");
		} else {
			String machine = machineComboBox.getValue();
			Transaction transaction = new Transaction(Action.GET_INVENTORY_REPORT, null, machine);
			ClientUI.chat.accept(transaction);
			transaction = ClientUI.chat.getObj();
			productsTable.setEditable(true);
			@SuppressWarnings("unchecked")
			List<ProductsInMachine> temp = (List<ProductsInMachine>) transaction.getData();
			if (transaction.getResponse() == Response.GETINVENTORYREPORT_SUCCESSFULLY) {
				for (int i = 0; i < temp.size(); i++) {
					listView.add(temp.get(i));
				}
				productsTable.setItems(listView);
			} else {
				setErrorNotValid("There is not available reports in the system");
			}
			transaction = new Transaction(Action.GET_LIMIT_BY_MACHINE, null, machineComboBox.getValue());
			ClientUI.chat.accept(transaction);
			transaction = ClientUI.chat.getObj();
			String limit = (String) transaction.getData();
			int limitValue = Integer.parseInt(limit);
			limitTxt.setText(limit);
			quantityCol.setCellFactory(tc -> {
				TableCell<ProductsInMachine, String> cell = new TableCell<>();
				cell.textProperty().bind(cell.itemProperty());
				cell.textFillProperty().bind(Bindings.createObjectBinding(() -> {
					if (limit != null) {
						try {
							int quantity = Integer.parseInt(cell.getItem());
							if (quantity < limitValue) {
								return Color.RED;
							}
						} catch (NumberFormatException e) {
							e.printStackTrace();
						}
					}
					return Color.GREEN;
				}, cell.itemProperty()));
				return cell;
			});
		}
	}

	/**
	 * This method is used to update the product limit in a specific machine.
	 * 
	 * @param event the event that triggered this method
	 */
	@FXML
	void UpdateProductLimit(ActionEvent event) {
		List<String> queryInputs = new ArrayList<>();
		queryInputs.add(limitTxt.getText());
		limit = Integer.parseInt(limitTxt.getText());
		queryInputs.add(machineComboBox.getValue());
		Transaction transaction = new Transaction(Action.UPDATE_LIMIT_QUANTITY_IN_MACHINE, null, queryInputs);
		ClientUI.chat.accept(transaction);
		transaction = ClientUI.chat.getObj();
		if (limitTxt.getText().isEmpty()) {
			limitTxt.setStyle(Constants.TEXT_FIELD_NOT_VALID_STYLE);
			setErrorNotValid("Please enter Limit.");
		} else {
			// Create a TableColumn and set the cellFactory
			quantityCol.setCellFactory(tc -> {
				// Create a new TableCell and bind the textFill property to a value in the data
				// model
				TableCell<ProductsInMachine, String> cell = new TableCell<>();
				cell.textProperty().bind(cell.itemProperty());
				cell.textFillProperty().bind(Bindings.when(Bindings.createBooleanBinding(() -> {
					// Parse the cell's value as an integer and return true if it's less than 3
					int quantity = Integer.parseInt(cell.getItem());
					return quantity < limit;
				}, cell.itemProperty())).then(Color.RED).otherwise(Color.GREEN));
				return cell;
			});

			// Add the column to the TableView
			productsTable.getColumns().remove(quantityCol);
			productsTable.getColumns().add(quantityCol);
		}
	}

	/**
	 * This method is used to update the quantity status for products in a specific
	 * machine.
	 * 
	 * @param event the event that triggered this method
	 */
	@FXML
	void updateQuantityStatus(ActionEvent event) {
		listView.clear();
		if (machineComboBox.getValue() == null) {
			setErrorNotValid("Please choose machine.");
		} else {
			String machine = machineComboBox.getValue().toString();
			Transaction transaction = new Transaction(Action.UPDATE_QUANTITY_STATUS, null, machine);
			ClientUI.chat.accept(transaction);
			transaction = ClientUI.chat.getObj();
			productsTable.setEditable(true);
			if (transaction.getResponse() == Response.UPDATE_QUANTITY_STATUS_SUCCESSFULLY) {
				viewInventoryReport(event);
			} else {
				setErrorNotValid("There is not updates in this table.");
				viewInventoryReport(event);
			}
		}
	}

	/**
	 * This method is used to export the inventory report to an xls file for a
	 * specific machine.
	 * 
	 * @param event the event that triggered this method
	 * @throws IOException if there is an error creating the xls file
	 */
	@FXML
	void exportReportToXsl(ActionEvent event) throws IOException {
		if (machineComboBox.getValue() == null) {
			setErrorNotValid("Please choose machine.");
		} else {
			String machine = machineComboBox.getValue();
			Transaction transaction = new Transaction(Action.GET_INVENTORY_REPORT_TO_XLS, null, machine);
			ClientUI.chat.accept(transaction);
			transaction = ClientUI.chat.getObj();
			if (transaction.getResponse() == Response.GET_INVENTORY_REPORT_TO_XLS_SUCCESSFULLY) {
				setErrorValid("The Xsl Report is exported.");
				String folderPath = "C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\";
				Runtime.getRuntime().exec("explorer.exe /e, /root," + folderPath);
			} else {
				setErrorNotValid(
						"The Xsl Report isn't exported:\nThere is not available reports\n in the system or the report\n was already exported.");
			}
		}
	}

	private void setErrorNotValid(String error) {
		errorLabel.setTextFill(Color.RED);
		errorLabel.setStyle(Constants.TEXT_NOT_VALID_STYLE);
		errorLabel.setText(error);
		errorLabel.setVisible(true);
	}

	private void setErrorValid(String error) {
		errorLabel.setTextFill(Color.GREEN);
		errorLabel.setStyle(Constants.TEXT_FIELD_VALID_STYLE);
		errorLabel.setVisible(true);
		errorLabel.setText(error);
	}

	public TableView<ProductsInMachine> getProductsTable() {
		return productsTable;
	}

	public TableColumn<ProductsInMachine, String> getProductIdCol() {
		return productIdCol;
	}

	public TableColumn<ProductsInMachine, String> getProductNameCol() {
		return productNameCol;
	}

	public TableColumn<ProductsInMachine, String> getQuantityCol() {
		return quantityCol;
	}

	public TableColumn<ProductsInMachine, String> getPriceCol() {
		return priceCol;
	}

	public TableColumn<ProductsInMachine, String> getQuantityStatusCol() {
		return quantityStatusCol;
	}

	public TableCell<ProductsInMachine, String> getQuantityCell() {
		return quantityCell;
	}

	public ImageView getBackBtn() {
		return backBtn;
	}

	public Label getErrorLabel() {
		return errorLabel;
	}

	public Button getShowReportBtn() {
		return showReportBtn;
	}

	public Button getExportReportBtn() {
		return exportReportBtn;
	}

	public Button getUpdateProductLimitBtn() {
		return UpdateProductLimitBtn;
	}

	public TextField getLimitTxt() {
		return limitTxt;
	}

	public int getLimit() {
		return limit;
	}

	public ObservableList<ProductsInMachine> getListView() {
		return listView;
	}
}
