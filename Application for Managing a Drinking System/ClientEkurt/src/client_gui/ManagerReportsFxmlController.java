package client_gui;

import javafx.scene.control.ComboBox;
import java.net.URL;
import java.util.ResourceBundle;
import Utils.generalMethods;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * ManagerReportsFxmlController is a class that displays the manager reports
 * page and provides functionality for interacting with the page. It also
 * implements the Initializable interface to initialize the page when it is
 * first displayed.
 */
public class ManagerReportsFxmlController implements Initializable {

	@FXML
	private ComboBox<String> reportTypeComboBox;
	@FXML
	private Button showReportBtn;
	@FXML
	private ImageView backBtn;
	@FXML
	private Label errorLabel;

	/**
	 * This method is used to display the Manager Reports page.
	 * 
	 * @param primaryStage the stage that this page is displayed on
	 */
	public void start(Stage primaryStage) {
		generalMethods.displayScreen(primaryStage, getClass(), "/client_fxml/ManagerReportsPage.fxml",
				"Ekurt Report's Menu");
	}

	/**
	 * This method is used to handle the back button click, returning the user to
	 * the CEO dashboard page.
	 * 
	 * @param event the event that triggered this method
	 */
	@FXML
	void clickOnBackButton(MouseEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
		Stage primaryStage = new Stage();
		CeoDashboardController menuPage = new CeoDashboardController();
		menuPage.start(primaryStage);
	}

	/**
	 * This method is used to initialize the Manager Reports page and populate the
	 * report type combo box with options.
	 * 
	 * @param arg0 the URL location of the FXML file
	 * @param arg1 the resource bundle for localization
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ObservableList<String> types = FXCollections.observableArrayList("Orders Report", "Inventory report",
				"Customer report");
		this.reportTypeComboBox.setItems(types);
		errorLabel.setVisible(false);
	}

	/**
	 * This method is used to handle the view report button click, determining the
	 * selected report type and displaying the corresponding report page.
	 * 
	 * @param event the event that triggered this method
	 */
	@FXML
	void viewReport(ActionEvent event) {
		Stage primaryStage = new Stage();
		if (reportTypeComboBox.getValue() == null) {
			errorLabel.setVisible(true);
			errorLabel.setText("Inorder to view report you must choose\nReport Type!");
		} else {
			switch (reportTypeComboBox.getValue()) {
			case "Orders Report":
				((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
				OrderReportsPageFxmlController orderReportPage = new OrderReportsPageFxmlController();
				orderReportPage.start(primaryStage);
				break;
			case "Inventory report":
				((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
				InvenrtoryReportsPageFxmlController inventoryReportPage = new InvenrtoryReportsPageFxmlController();
				inventoryReportPage.start(primaryStage);
				break;
			case "Customer report":
				((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
				CustomerReportsPageFxmlController CustomerReportPage = new CustomerReportsPageFxmlController();
				CustomerReportPage.start(primaryStage);
				break;
			}
		}
	}
}