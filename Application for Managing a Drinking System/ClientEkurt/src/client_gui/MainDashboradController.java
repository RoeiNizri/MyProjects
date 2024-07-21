package client_gui;

import java.net.URL;
import java.util.ResourceBundle;
import Utils.generalMethods;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import clientUtil.ClientUtils;
import common.Action;
import common.Transaction;
import enums.RoleEnum;
import client.PopUpWindowController;
/**
 * This class represents the controller for the maindashboard screen. It handles
 * the actions for the buttons on the screen.
 * it shows the order options that the user can choose from
 */
public class MainDashboradController implements Initializable{

    @FXML
    private Button LogoutBtn;

    @FXML
    private Button deliveryBtn;

    @FXML
    private Button manageDeliveryBtn;

    @FXML
    private Button pickUpBtn;

    @FXML
    private Button regAsSubBtn;

    @FXML
    private Label usernameLabel;
    public static PopUpWindowController PopUpWindowController;
	/**
	This method is an event handler for when the "pickup" button is clicked by the user.
	It hides the current window, and opens up the ChooseMachinePickupController window for the user to select a machine for pickup.
	It also retrieves the list of machines from the server via a Transaction message.
	@param event the ActionEvent that triggered the method call
	*/
	@FXML
    void clickOnPickup(ActionEvent event) {
		Transaction machineGetter = new Transaction(Action.GET_MACHINES_LIST, null);
        ClientUI.chat.accept(machineGetter); 
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		new ChooseMachinePickupController().start(new Stage());
	}
	/**
	 * Displays the MainDashboard screen and sets the title of the window to Main menu.
	 * 
	 * @param primaryStage the primary stage of the application
	 */
	public void start(Stage primaryStage) {
		generalMethods.displayScreen(primaryStage, getClass(), "/client_fxml/MainDashboard.fxml", "Main menu");
	}
	
	@FXML
    void clickOnlogout(ActionEvent event) throws Exception {
		LoginController.logout(ClientUtils.currUser.getUsername());
		PopUpWindowController.stop();
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		new LoginController().start(new Stage());
	}
	 /**
    This method handles the event when the "BecomeASubscriber" button is clicked.
    It hides the current window and opens the "SubscriberRegisterController" window.
    @param event the event that triggers the method call.
    @throws Exception if an error occurs while opening the new window.
    */
    @FXML
    void clickOnBecomeASubscriber(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		new SubscriberRegisterController().start(new Stage());
    }
    /**
    This method handles the event when the "OnDeliver" button is clicked.
    It hides the current window and opens the "EnterDeliAddController" window.
    @param event the event that triggers the method call.
    @throws Exception if an error occurs while opening the new window.
    */
    @FXML
    void clickOnDelivery(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		new EnterDeliAddController().start(new Stage());
    }
    /**
    This method handles the event when the "Manage Deliveries" button is clicked.
    It hides the current window and opens the "CustomerReceivePage" window.
    @param event the event that triggers the method call.
    @throws Exception if an error occurs while opening the new window.
    */
    @FXML
    void clickOnManageDeliveries(ActionEvent event) throws Exception {
    	((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		new CustomerReceivePageController().start(new Stage());
    }
    /**
	Initialize method for setting the user's name and role on the top of the dashboard.
	If the user's role is SUBSCRIBER, the "Register as Subscriber" button is not visible.
	Also, starts a task that runs every minute using the PopUpWindowController.
	@param arg0 URL, used to initialize the controller.
	@param arg1 ResourceBundle, used to initialize the controller.
	*/
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		usernameLabel.setText(ClientUtils.currUser.getFirstName()+ " " + ClientUtils.currUser.getLastName());
		if(ClientUtils.currUser.getRole().equals(RoleEnum.SUBSCRIBER)) {
			regAsSubBtn.setVisible(false);
		}
		 PopUpWindowController = new PopUpWindowController();
		 PopUpWindowController.EveryMinuteTask();
	}
	
}