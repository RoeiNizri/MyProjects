package client_gui;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Utils.generalMethods;
import client.ClientUI;
import clientUtil.ClientUtils;
import common.Action;
import common.Transaction;
import enums.OrderStatusEnum;
import enums.RoleEnum;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import logic.LocalOrder;
/**
 * 
 * This class represents the EkWelcomePageController, it's responsible
 * for displaying the options that the customer has while handling the user interactions
 */
public class EkWelcomePageController implements Initializable {

    @FXML
    private Button LogoutBtn;

    @FXML
    private Label nameLabel;

    @FXML
    private Button newOrder;

    @FXML
    private Button pickupOrder;
	/**
	 * Displays the customer EkWelcomePageand sets the title of the window to Welcome to Ekrut .
	 * 
	 * @param primaryStage the primary stage of the application
	 */
    public void start(Stage primaryStage) {
		generalMethods.displayScreen(primaryStage, getClass(), "/client_fxml/EkWelcomePage.fxml", "Welcome to Ekrut");
	}
	
    @FXML
   
    void clickOnNewOrder(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		if(ClientUtils.currUser.getRole().equals(RoleEnum.CUSTOMER)) {
			ekConfiguration();
			CustomerCategoriesController customerCategory = new CustomerCategoriesController();
			Platform.runLater(() -> {
				customerCategory.start(new Stage());
			});
		}else {
			ekConfiguration();
			SubscriberCategoriesPageController subCategory = new SubscriberCategoriesPageController();
			Platform.runLater(() -> {
				subCategory.start(new Stage());
			});
		}
    }
    /**
	 * Event handler for the clickOnPickup" button. Hides the current window and
	 * displays the PickUpPageController screen.
	 * 
	 * @param event the ActionEvent that triggers the handler
	 */
    @FXML
    void clickOnPickup(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		new PickUpPageController().start(new Stage());
    }
    /**
   	 * Event handler for the clickOnlogout" button. Hides the current window and
   	 * displays the LoginController screen based on the user that currUser has.
   	 * 
   	 * @param event the ActionEvent that triggers the handler
   	 */
    @FXML
    void clickOnlogout(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
    	new LoginController().logout(ClientUtils.currUser.getUsername());
    	new LoginController().start(new Stage());
    }
    /**
	 *  Perform the ek configuration for the current user. It initializes a new LocalOrder for the current user, 
	 *  Then it sends a GET_AVAILABLE_PRODUCTS_IN_MACHINE message to the server with the machine name to get the available products in the machine
	 *  and a GET_NOT_AVAILABLE_PRODUCTS_IN_MACHINE message to the server with the machine name to get the not available products in the machine.
	 *  Finally it sets the cartDisplayFlag to true.
	 */
	void ekConfiguration() {
		ClientUtils.localOrderInProcess = new LocalOrder(ClientUtils.currUser, ClientUtils.machine,
				Timestamp.valueOf(LocalDateTime.now()), new ArrayList<>(), OrderStatusEnum.IN_PROCESS);
		Transaction msg1 = new Transaction(Action.GET_AVAILABLE_PRODUCTS_IN_MACHINE,
				ClientUtils.localOrderInProcess.getMachineName());
		ClientUI.chat.accept(msg1);

		Transaction msg2 = new Transaction(Action.GET_NOT_AVAILABLE_PRODUCTS_IN_MACHINE,
				ClientUtils.localOrderInProcess.getMachineName());
		ClientUI.chat.accept(msg2);

		ClientUtils.cartDisplayFlag = true;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		nameLabel.setText(ClientUtils.currUser.getFirstName()+" "+ clientUtil.ClientUtils.currUser.getLastName());
	}

}
