package client_gui;

import java.net.URL;
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
import enums.RegionEnum;
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
 * This class represents the controller for the customer reports screen. It
 * handles the actions for the buttons on the screen and updates the bar chart
 * to display the selected customer report.
 */
public class CustomerReportsPageFxmlController implements Initializable {

	@FXML
	private ComboBox<Year> yearComboBox;
	@FXML
	private ImageView backBtn;
	@FXML
	private Label errorLabel = new Label();
	@FXML
	private Label lowLabel = new Label();
	@FXML
	private Label middleLabel = new Label();
	@FXML
	private Label highLabel = new Label();
	@FXML
	private Button showReportBtn;
	@FXML
	private Button exportReportBtn;
	@FXML
	CategoryAxis monthsAxis = new CategoryAxis();
	@FXML
	NumberAxis numOfCustomerAxis = new NumberAxis();
	@FXML
	BarChart<String, Number> customerReportBarChart = new BarChart<>(monthsAxis, numOfCustomerAxis);
	@FXML
	List<XYChart.Series<String, Number>> seriesList = new ArrayList<>();

	/**
	 * Displays the customer reports screen and sets the title of the window.
	 * 
	 * @param primaryStage the primary stage of the application
	 */
	public void start(Stage primaryStage) {
		generalMethods.displayScreen(primaryStage, getClass(), "/client_fxml/CustomerReportPage.fxml",
				"Ekurt Customer Report Page");
	}

	/**
	 * Event handler for the "Back" button. Hides the current window and displays
	 * the manager reports screen.
	 * 
	 * @param event the MouseEvent that triggers the handler
	 */
	@FXML
	void clickOnBackButton(MouseEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide();
		Stage primaryStage = new Stage();
		ManagerReportsFxmlController menuPage = new ManagerReportsFxmlController();
		menuPage.start(primaryStage);
	}

	/**
	 * Initializes the customer reports screen by adding the current year and the
	 * past 7 years to the yearComboBox, and setting the visibility of certain
	 * labels to false.
	 * 
	 * @param location  the URL location
	 * @param resources the ResourceBundle
	 */
	public void initialize(URL location, ResourceBundle resources) {
		Year currentYear = Year.now();
		monthsAxis.setTickLabelsVisible(false);
		numOfCustomerAxis.setTickUnit(1);
		customerReportBarChart.setMinWidth(10);
		for (int year = currentYear.getValue(); year >= currentYear.getValue() - 7; year--) {
			yearComboBox.getItems().add(Year.of(year));
		}
		errorLabel.setVisible(false);
		lowLabel.setVisible(false);
		middleLabel.setVisible(false);
		highLabel.setVisible(false);
	}

	/**
	 * Event handler for the "Show Report" button. Retrieves the customer report for
	 * the selected year and region, and displays it in the bar chart.
	 * 
	 * @param event the ActionEvent that triggers the handler
	 */
	@SuppressWarnings("unchecked")
	public void viewCustomerReport(ActionEvent event) {
		seriesList.clear();
		errorLabel.setVisible(false);
		Transaction transaction = null;
		List<String> activityList = null;
		List<String> queryInputs = new ArrayList<>();
		String region = ClientUtils.currUser.getRegion().toString();
		queryInputs.add(region);
		queryInputs.add(yearComboBox.getValue().toString());
		String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September",
				"October", "November", "December" };
		customerReportBarChart.getData().clear();
		if (yearComboBox.getValue() == null) {
			setErrorNotValid("Inorder to view report\n you must choose a 'YEAR'!");
		} else {
			int lowActivityCounter = 0, middleActivityCounter = 0, highActivityCounter = 0;
			if (ClientUtils.currUser.getRegion() == RegionEnum.WORLDWIDE) {
				transaction = new Transaction(Action.GET_CUSTOMER_REPORT, null, yearComboBox.getValue().toString());
				ClientUI.chat.accept(transaction);
				transaction = ClientUI.chat.getObj();
				activityList = (List<String>) transaction.getData();
			} else {
				transaction = new Transaction(Action.GET_CUSTOMER_REPORT_BY_REGION, null, queryInputs);
				ClientUI.chat.accept(transaction);
				transaction = ClientUI.chat.getObj();
				activityList = (List<String>) transaction.getData();
			}
			if (activityList.isEmpty()) {
				setErrorNotValid("There is not available reports\n in the system.");
				return;
			}
			if (transaction.getResponse() == Response.GETCUSTOMERREPORT_SUCCESSFULLY
					|| transaction.getResponse() == Response.GETCUSTOMERREPORT_BY_REGION_SUCCESSFULLY) {
				lowLabel.setVisible(true);
				middleLabel.setVisible(true);
				highLabel.setVisible(true);
				for (int j = 1; j <= 12; j++) {
					for (int i = 0; i < activityList.size(); i = i + 2) {
						if (Integer.parseInt(activityList.get(i)) == j) {
							if (Integer.parseInt(activityList.get(i + 1)) < 3) {
								lowActivityCounter++;
							} else if (Integer.parseInt(activityList.get(i + 1)) >= 3
									&& Integer.parseInt(activityList.get(i + 1)) <= 7) {
								middleActivityCounter++;
							} else
								highActivityCounter++;
						}
					}
					seriesList.add(new XYChart.Series<>());
					seriesList.get(j - 1).setName(months[j - 1]);
					seriesList.get(j - 1).getData().add(new XYChart.Data<>("Low Activity", lowActivityCounter));
					seriesList.get(j - 1).getData().add(new XYChart.Data<>("Middle Activity", middleActivityCounter));
					seriesList.get(j - 1).getData().add(new XYChart.Data<>("High Activity", highActivityCounter));
					customerReportBarChart.getData().add(seriesList.get(j - 1));
					lowActivityCounter = 0;
					middleActivityCounter = 0;
					highActivityCounter = 0;
				}
			} else {
				setErrorNotValid("There is not available reports\n in the system");
			}
		}
	}

	/**
	 * 
	 * Event handler for the "Export Report to Xsl" button. Retrieves the customer
	 * report for the selected year and region, and exports it to XSL file. If the
	 * user hasn't selected a year it will show an error label with a message. If
	 * the transaction response is successful, it will open the folder where the
	 * report is exported. If the transaction response is not successful, it will
	 * show an error label with a message.
	 * 
	 * @param event the ActionEvent that triggers the handler
	 * @throws Exception
	 */
	@FXML
	void exportReportToXsl(ActionEvent event) throws Exception {
		errorLabel.setVisible(false);
		Transaction transaction = null;
		List<String> queryInputs = new ArrayList<>();
		String region = ClientUtils.currUser.getRegion().toString();
		queryInputs.add(region);
		if (yearComboBox.getValue() == null) {
			setErrorNotValid("Inorder to view report\n you must choose a 'YEAR'!");
		} else {
			queryInputs.add(yearComboBox.getValue().toString());
			if (ClientUtils.currUser.getRegion() == RegionEnum.WORLDWIDE) {
				transaction = new Transaction(Action.GET_CUSTOMER_REPORT_TO_XLS, null,
						yearComboBox.getValue().toString());
				ClientUI.chat.accept(transaction);
				transaction = ClientUI.chat.getObj();
			} else {
				transaction = new Transaction(Action.GET_CUSTOMER_REPORT_BY_REGION_TO_XLS, null, queryInputs);
				ClientUI.chat.accept(transaction);
				transaction = ClientUI.chat.getObj();
			}
			if (transaction.getResponse() == Response.GET_CUSTOMER_REPORT_BY_REGION_TO_XLS_SUCCESSFULLY
					|| transaction.getResponse() == Response.GET_CUSTOMER_REPORT_TO_XLS_SUCCESSFULLY) {
				setErrorValid("The Xsl Report is exported.");

				String folderPath = "C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\";
				Runtime.getRuntime().exec("explorer.exe /e, /root," + folderPath);
			} else {
				setErrorNotValid("The Xsl Report isn't exported:\\nthe report was already exported.");
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