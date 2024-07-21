package client_gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import logic.Offer;

public class PromoteOffersFXController implements Initializable {
	/**
	The class PromoteOffersFXController is a controller class for the PromoteOffersPage
	.fxml file in a JavaFX application. It implements the Initializable interface which is used to indicate that the class
	 can be initialized by the FXML loader. 
	This class sets up all the necessary parameters for the Promote Offers page.
	this class used to Promote offers in the system
	The class has several instance variables, including several buttons (backBtn, getOffersBtn,
	 PromoteOfferBtn, and StopOffersBtn), a TableView object (table) and several TableColumn objects (IdColTbl,
	  NameColTbl, PriceColTbl, DiscountColTbl, and StatusColTbl), and a Label object (errorLabel). It also has an ObservableList of Offer 
	objects (listView) and an instance of the generalMethods class (gm) and an Offer object (selectedOffer).
	* 
	* @param primaryStage the primary stage of the application
	*/
	//@FXML
	//private ComboBox<String> RegionComboBox;
	@FXML
	private Button getOffersBtn;
	@FXML
	private Button PromoteOfferBtn;
	@FXML
	private Button StopOffersBtn;
	@FXML
	private TableView<Offer> table = new TableView<Offer>(); //changed to static
	@FXML
	private TableColumn<Offer, String> IdColTbl;
	@FXML
	private TableColumn<Offer, String> NameColTbl;
	@FXML
	private TableColumn<Offer, String> PriceColTbl;
	@FXML
	private TableColumn<Offer, String> DiscountColTbl;
	@FXML
	private TableColumn<Offer, String> StatusColTbl;
    @FXML
    private ImageView backBtn;
	@FXML
	private Label errorLabel;
	private ObservableList<Offer> listView = FXCollections.observableArrayList();
	private generalMethods gm = new generalMethods();
	private Offer selectedOffer;
	/**
	 * Displays the PromoteOffersPage screen and sets the title of the window for Promote Offers Page.
	 * 
	 * @param primaryStage the primary stage of the application
	 */
	@SuppressWarnings("static-access")
	public void start(Stage primaryStage) throws Exception {
		gm.displayScreen(primaryStage, getClass(), "/client_fxml/PromoteOffersPage.fxml", "Promote Offers Page");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		IdColTbl.setCellValueFactory(new PropertyValueFactory<Offer, String>("productID"));
		NameColTbl.setCellValueFactory(new PropertyValueFactory<Offer, String>("productName"));
		PriceColTbl.setCellValueFactory(new PropertyValueFactory<Offer, String>("productPrice"));
		DiscountColTbl.setCellValueFactory(new PropertyValueFactory<Offer, String>("productDiscount"));
		StatusColTbl.setCellValueFactory(new PropertyValueFactory<Offer, String>("IsActive"));
		//ObservableList<String> regions = FXCollections.observableArrayList("South","North","UAE");
		//RegionComboBox.setItems(regions);
		errorLabel.setVisible(false);
		table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
	    // Update the selectedOffer variable with the new selected Offer object
	    selectedOffer = newValue;
		});
		setColors();
	}
	/**
	 * Event handler for the "Back" button. Hides the current window and displays
	 * the suitable fxml file 
	 * if the Customer was logged in, it will open the CustomerCategoriesController page.
	 * if the Subscriber was logged in, it will open the SubscriberCategoriesPageController page.
	 * 
	 * @param event the MouseEvent that triggers the handler
	 */
	 
	@FXML
	void Back(MouseEvent event) throws Exception {
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding window
		Stage primaryStage = new Stage();
		MarketingWorkerDashboardFXController menuPage = new MarketingWorkerDashboardFXController();
		menuPage.start(primaryStage);
	}
	/**
	  The method GetOffers is an event handler for a button click, and is annotated with @FXML, indicating that it is associated with a specific element in the GUI.

When the button is clicked, the method is executed and performs the following actions:

Hides an error label, which is likely used to display error messages to the user.
Creates a new Transaction object, msg, with the action "GET_OFFERS" and the region of the current user.
Sends the transaction to a server through a "chat" object.
Retrieves a response message from the server.
If the response is "FAILED_TO_GET_OFFERS", it clears the list view and displays an error message.
If the response is successful, it clears the list view and sets it to the list of offers returned from the
server, then it displays the offers in the table.
Enables the StopOffersBtn and PromoteOfferBtn buttons.
	 */
	@FXML
	void GetOffers(ActionEvent event) throws Exception {
		errorLabel.setVisible(false);
		Transaction msg;
		//if(RegionComboBox.getValue() == null)
			//errorLabel.setVisible(true);
		msg = new Transaction(Action.GET_OFFERS,ClientUtils.currUser.getRegion());
	    ClientUI.chat.accept(msg);
		msg = ClientUI.chat.getObj(); 
		if(msg.getResponse()==Response.FAILED_TO_GET_OFFERS) {
			listView.clear();
			errorLabel.setVisible(true);
		}else {
		listView.clear();
		errorLabel.setVisible(false);
		@SuppressWarnings("unchecked")
		List<Offer> Alist = (List<Offer>) msg.getData();
		for (Offer offer : Alist) {
			  listView.add(offer);
			}
			listView.forEach(Offer -> System.out.println(Offer));
			table.setEditable(true);
			table.setItems(listView);
			StopOffersBtn.setDisable(false);
			PromoteOfferBtn.setDisable(false);

		}
	}
	/**
	 This code is written in Java and appears to be part of a graphical user interface (GUI) application. The method PromoteOffers is an event handler for a button click, and is annotated with @FXML, indicating that it is associated with a specific element in the GUI.

When the button is clicked, the method is executed and performs the following actions:

Hides an error label, which is likely used to display error messages to the user.
Sets the region of the selected offer to the region of the current user.
If no offer is selected, it displays an error message asking the user to select an offer.
If the selected offer is already active, it displays an error message.
Creates a new Transaction object, msg, with the action "PROMOTE_OFFER" and the selected offer.
Sends the transaction to a server through a "chat" object.
Retrieves a response message from the server.
If the response is "OFFER_PROMOTED_UNSUCCESSFULLY", it displays an error message to the user.
If the response is successful, it sets the status of the selected offer to "ON" and updates the table.
	 */
	@FXML
	void PromoteOffers(ActionEvent event) throws Exception {
		errorLabel.setVisible(false);
		Transaction msg;
		selectedOffer.setRegion(ClientUtils.currUser.getRegion());		
		if(selectedOffer==null) {
			errorLabel.setText("Please choose an offer");
			errorLabel.setVisible(true);
			return;
		}
		if(selectedOffer.getIsActive().equalsIgnoreCase("ON")) {
			errorLabel.setText("The offer is active already!");
			errorLabel.setVisible(true);
			return;
		}
		msg = new Transaction(Action.PROMOTE_OFFER,selectedOffer);
	    ClientUI.chat.accept(msg);
		msg = ClientUI.chat.getObj(); 
		if(msg.getResponse()==Response.OFFER_PROMOTED_UNSUCCESSFULLY) {
			errorLabel.setText("Failed to promote offer pleast try again");
			errorLabel.setVisible(true);
		}
		else {
		listView.get(listView.indexOf(selectedOffer)).setIsActive("ON");
		setColors();
		table.refresh();
		}
	}
	/**
	Hides an error label, which is likely used to display error messages to the user.
Sets the region of the selected offer to the region of the current user.
If no offer is selected, it displays an error message asking the user to select an offer.
If the selected offer is already inactive, it displays an error message.
Creates a new Transaction object, msg, with the action "STOP_OFFER" and the selected offer.
Sends the transaction to a server through a "chat" object.
Retrieves a response message from the server.
If the response is "OFFER_PROMOTED_UNSUCCESSFULLY", it displays an error message to the user.
If the response is successful, it sets the status of the selected offer to "OFF" and updates the table.
	 */
	@FXML
	void stopOffer(ActionEvent event) throws Exception {
		errorLabel.setVisible(false);
		Transaction msg;
		selectedOffer.setRegion(ClientUtils.currUser.getRegion());
		
		if(selectedOffer==null) {
			errorLabel.setText("Please choose an offer");
			errorLabel.setVisible(true);
			return;
		}
		if(selectedOffer.getIsActive().equalsIgnoreCase("OFF")) {
			errorLabel.setText("The offer is not active already!");
			errorLabel.setVisible(true);
			return;
		}
		msg = new Transaction(Action.STOP_OFFER,selectedOffer);
	    ClientUI.chat.accept(msg);
		msg = ClientUI.chat.getObj(); 
		if(msg.getResponse()==Response.OFFER_PROMOTED_UNSUCCESSFULLY) {
			errorLabel.setText("Failed to stop offer pleast try again");
			errorLabel.setVisible(true);
		}
		else {
		listView.get(listView.indexOf(selectedOffer)).setIsActive("OFF");
		setColors();
		table.refresh();
		}
	}
	/**
	The setColors() method is used to set the background color of each cell of the table view. 
	The Back() method is used to navigate back to the previous screen. The getOffers() method is used to get all the offers from the server.
	 The PromoteOffer() method is used to promote the selected offer. 

	* 
	* @param primaryStage the primary stage of the application
	*/
	void setColors() {
		// Get the columns you want to compare
		TableColumn<Offer, String> statusColumn = StatusColTbl;
		// Create a cell factory that sets the text color to green if the value in the status column is "ON", and red otherwise
		statusColumn.setCellFactory(tc -> {
		    TableCell<Offer, String> cell = new TableCell<>();
		    cell.textProperty().bind(cell.itemProperty());
		    cell.itemProperty().addListener((obs, oldText, newText) -> {
		        if (newText != null) {
		            if (newText.equals("ON")) {
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
