package client_gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * 
 * This class represents the OrdersReportPageFxmlController, it's responsible
 * for displaying the order reports page,
 * 
 * handling the user interactions and displaying the orders report.
 */
public class OrderReportsPageFxmlController implements Initializable {

	@FXML
	private ComboBox<String> monthComboBox;
	@FXML
	private ComboBox<Year> yearComboBox;
	@FXML
	private ComboBox<String> regionComboBox;
	@FXML
	private ImageView backBtn;
	@FXML
	private Label errorLabel;
	@FXML
	private Button showReportBtn;
	@FXML
	private Button exportReportBtn;
	@FXML
	CategoryAxis machinesAxis = new CategoryAxis();
	@FXML
	NumberAxis numOfOrdersAxis = new NumberAxis();
	@FXML
	BarChart<String, Number> ordersBarChart = new BarChart<>(machinesAxis, numOfOrdersAxis);
	@FXML
	List<XYChart.Series<String, Number>> seriesList = new ArrayList<>();

	/**
	 * 
	 * This method is used to display the OrdersReportPage.fxml file and set the
	 * title of the window as "Ekurt Order Report Page".
	 * 
	 * @param primaryStage The main window of the application
	 */
	public void start(Stage primaryStage) {
		generalMethods.displayScreen(primaryStage, getClass(), "/client_fxml/OrdersReportPage.fxml",
				"Ekurt Order Report Page");
	}

	/**
	 * 
	 * This method is used to go back to the ManagerReportsFxmlController window and
	 * hide the current window.
	 * 
	 * @param event A mouse event that is triggered when the user clicks on the back
	 *              button
	 */
	@FXML
	void clickOnBackButton(MouseEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding window
		Stage primaryStage = new Stage();
		ManagerReportsFxmlController menuPage = new ManagerReportsFxmlController();
		menuPage.start(primaryStage);
	}

	/**
	 * 
	 * Initializes the report page by setting the tick labels of the machinesAxis to
	 * not visible, the tick unit of the numOfOrdersAxis to 1, the min width of the
	 * ordersBarChart to 10, and filling the year, month, and region ComboBoxes with
	 * their respective options. The error label is also set to not visible
	 * initially.
	 * 
	 * @param location  The location of the FXML file
	 * @param resources The resources used to localize the root object
	 */
	public void initialize(URL location, ResourceBundle resources) {
		Year currentYear = Year.now();
		machinesAxis.setTickLabelsVisible(false);
		numOfOrdersAxis.setTickUnit(1);
		ordersBarChart.setMinWidth(10);
		ObservableList<String> region = null;
		for (int year = currentYear.getValue(); year >= currentYear.getValue() - 7; year--) {
			yearComboBox.getItems().add(Year.of(year));
		}
		switch (ClientUtils.currUser.getRegion()) {
		case WORLDWIDE:
			region = FXCollections.observableArrayList("North", "South", "UAE");
			break;
		case NORTH:
			region = FXCollections.observableArrayList("North");
			break;
		case SOUTH:
			region = FXCollections.observableArrayList("South");
			break;
		case UAE:
			region = FXCollections.observableArrayList("UAE");
			break;
		}
		ObservableList<String> months = FXCollections.observableArrayList("January", "February", "March", "April",
				"May", "June", "July", "August", "September", "October", "November", "December");
		this.monthComboBox.setItems(months);
		this.regionComboBox.setItems(region);
		errorLabel.setVisible(false);
	}

	/**
	 * 
	 * Handles the "View Report" button press by first clearing the series list and
	 * the bar chart data, setting the error label to not visible, and checking if
	 * the user has selected a month, year, and region. If not, an error message is
	 * displayed. If all options are selected, a transaction is sent to the server
	 * to retrieve the report data, which is then parsed and displayed in a bar
	 * chart.
	 * 
	 * @param event The button press event
	 */
	public void viewOrderReport(ActionEvent event) {
		seriesList.clear();
		errorLabel.setVisible(false);
		ordersBarChart.getData().clear();
		if (monthComboBox.getValue() == null || yearComboBox.getValue() == null || regionComboBox.getValue() == null) {
			setErrorNotValid("Inorder to view report\n you must choose\n'YEAR', 'MONTH' and 'REGION'!");
		} else {
			List<String> list = new ArrayList<String>();
			int j = 0;
			list.add(regionComboBox.getValue());
			list.add(yearComboBox.getValue().toString());
			list.add(monthComboBox.getValue());
			Transaction transaction = new Transaction(Action.GET_ORDERS_REPORT, null, list);
			ClientUI.chat.accept(transaction);
			transaction = ClientUI.chat.getObj();
			@SuppressWarnings("unchecked")
			List<String> orderList = (List<String>) transaction.getData();
			if (orderList.isEmpty()) {
				setErrorNotValid("There is not available reports\n in the system");
			}
			if (transaction.getResponse() == Response.GETORDERREPORT_SUCCESSFULLY) {
				for (int i = 0; i < orderList.size(); i = i + 2) {
					seriesList.add(new XYChart.Series<>());
					seriesList.get(j).setName(orderList.get(i));
					j++;
				}
				j = 1;
				for (int i = 0; i < orderList.size() / 2; i++) {
					int num = Integer.parseInt(orderList.get(j));
					seriesList.get(i).getData().add(new XYChart.Data<>(seriesList.get(i).getName(), num));
					ordersBarChart.getData().add(seriesList.get(i));
					j = j + 2;
				}
			} else {
				setErrorNotValid("There is not available reports\n in the system");
			}
		}

	}

	/**
	 * 
	 * Handles the export of the report to a .xls file when the "Export to XLS"
	 * button is pressed. Displays an error message if the user has not selected a
	 * month, year, and region to view the report. If all options are selected, the
	 * method checks if the selected month and year are the current month and year,
	 * and if so, displays an error message asking the user to choose a completed
	 * month. If the selected month and year are not the current month and year, a
	 * transaction is sent to the server to retrieve the report data and export it
	 * to a .xls file. The exported file can be found in the specified folder path.
	 * 
	 * @author [Roei Nizri]
	 * @param event The button press event throws Exception
	 */
	@FXML
	void exportReportToXsl(ActionEvent event) throws Exception {
		errorLabel.setVisible(false);
		LocalDate localDate = LocalDate.now();
		int month = localDate.getMonthValue();
		int year = localDate.getYear();
		Month monthEnum = Month.of(month);
		String monthString = monthEnum.toString();
		if (monthComboBox.getValue() == null || yearComboBox.getValue() == null || regionComboBox.getValue() == null) {
			setErrorNotValid("Inorder to export report\n you must choose\n'YEAR', 'MONTH' and 'REGION'!");
		} else {
			if (yearComboBox.getValue().toString().equals(Integer.toString(year))
					&& monthComboBox.getValue().toUpperCase().equals(monthString)) {
				setErrorNotValid("Inorder to export report to xsl \n you must choose a completed\n month please.");
				return;
			} else {
				List<String> list = new ArrayList<String>();
				list.add(regionComboBox.getValue());
				list.add(yearComboBox.getValue().toString());
				list.add(monthComboBox.getValue());
				Transaction transaction = new Transaction(Action.GET_ORDERS_REPORT_TO_XLS, null, list);
				ClientUI.chat.accept(transaction);
				transaction = ClientUI.chat.getObj();
				if (transaction.getResponse() == Response.GETORDERREPORT_TO_XLS_SUCCESSFULLY) {
					setErrorValid("The Xsl Report is exported.");
					String folderPath = "C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\";
					Runtime.getRuntime().exec("explorer.exe /e, /root," + folderPath);
				} else {
					setErrorNotValid(
							"The Xsl Report isn't exported:\\nThere is not available reports\\n in the system or the report\\n was already exported.\\n Please Check it here.\"");
				}
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
}
