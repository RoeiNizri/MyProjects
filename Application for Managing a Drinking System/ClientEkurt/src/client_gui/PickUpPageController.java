package client_gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Utils.Constants;
import Utils.generalMethods;
import client.ClientUI;
import clientUtil.ClientUtils;
import common.Action;
import common.Response;
import common.Transaction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import logic.ProductInGrid;
/**
 * The PickUpPageController is a controller class for the PickUpPage.fxml file in a JavaFX application.
 *  It has several instance variables such as backBtn of
 *   type ImageView, okBtn and errorLabel of type Button and Label respectively, 
 *   pickupcodeText of type TextField, pickupCode and userId of type int and String respectively, pickUpList of type List<Integer>,
 *   and a boolean variable wrongPickup to check if the
 *    pickup code entered is incorrect.
 * 
 * @param primaryStage the primary stage of the application
 */
public class PickUpPageController {
	@FXML
	private ImageView backBtn;

	@FXML
	private Button okBtn;
	@FXML
	private Label errorLabel;
	@FXML
	private TextField pickupcodeText;
	int pickupCode = 0;
	String userId = null;
	List<Integer> pickUpList;
	private boolean wrongPickup = false;
	/**
	 * Displays the pickUpPage screen and sets the title of the window for PickUp Page.
	 * 
	 * @param primaryStage the primary stage of the application
	 */
	public void start(Stage primaryStage) {
		generalMethods.displayScreen(primaryStage, getClass(), "/client_fxml/pickUpPage.fxml", "PickUp Page");
	}
	/**
	 * Clear errorlabel,pickupcodeTexts and sets wrongPickup to 0
	 * 
	 * @param event the MouseEvent that triggers the handler
	 */
	@FXML
	void clearTextField(MouseEvent event) {
		if (wrongPickup) {
			errorLabel.setText("");
			;
			pickupcodeText.setStyle("");
			wrongPickup = false;
		}
	}
	/**
	 * Event handler for the "Back" button. Hides the current window and displays
	 * @param event the MouseEvent that triggers the handler
	 */
	@FXML
	void clickOnBackButton(MouseEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		new EkWelcomePageController().start(new Stage());
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
	void clickOnOkBtn(ActionEvent event) {
		if (pickupcodeText.getText().equals("")) {
			pickupcodeText.setStyle(Constants.TEXT_FIELD_NOT_VALID_STYLE);
			pickupcodeText.clear();
			errorLabel.setText("Please supply a PickUp code");
			wrongPickup = true;
		} else {
			pickupCode = Integer.valueOf(pickupcodeText.getText());
			userId = String.valueOf(ClientUtils.currUser.getId());
			Transaction msg = new Transaction(Action.GET_PICKUP_CODE, userId);
			ClientUI.chat.accept(msg);
			msg = (Transaction) ClientUI.chat.getObj();
			if (msg.getData() instanceof ArrayList<?>) {
				pickUpList = ArrayList.class.cast(msg.getData());
			}
			if (!pickUpList.contains(pickupCode)) {
				pickupcodeText.setStyle(Constants.TEXT_FIELD_NOT_VALID_STYLE);
				pickupcodeText.clear();
				errorLabel.setText("Wrong pickUp code - Please try again");
				wrongPickup = true;
			} else {
				HashMap<Integer, String> pickupANDuserId = new HashMap<>();
				pickupANDuserId.put(pickupCode, userId);
				Transaction msg2 = new Transaction(Action.UPDATE_STATUS, pickupANDuserId);
				ClientUI.chat.accept(msg2);
				msg2 = (Transaction) ClientUI.chat.getObj();
				if (msg2.getResponse().equals(Response.UPDATE_STATUS_SUCC)) {
					((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
					new PickUpCompletedController().start(new Stage());
				}
			}

		}

	}

}