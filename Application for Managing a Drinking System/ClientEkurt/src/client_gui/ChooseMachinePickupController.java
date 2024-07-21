package client_gui;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import Utils.generalMethods;
import client.ClientUI;
import clientUtil.ClientUtils;
import common.Action;
import common.Transaction;
import enums.OrderStatusEnum;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import logic.Machine;
import logic.PickUpOrder;
/**
* This class represents the controller for the "ChooseMachinePickup" Controller.
* It handles the logic for the user to select a machine from a list and continue to the next step.
* @author 
*/
public class ChooseMachinePickupController {
	
	public static int flag = 0;
	@FXML
	private ImageView backBtn;

	@FXML
	private ComboBox<String> chooseMachine = new ComboBox<>();

	@FXML
	private Button continueBtn;

	private static List<String> machineNames;
	private static List<Machine> machineList;


	public static List<Machine> getMachineList() {
		return machineList;
	}

	public static List<String> getMachineNames() {
		return machineNames;
	}

	public static void setMachineNames(List<String> list) {
			machineNames = list;
	}

	public void start(Stage primaryStage) {
		generalMethods.displayScreen(primaryStage, getClass(), "/client_fxml/ChooseMachinePickup.fxml",
				"choose machine");
	}
	/**
	 * Event handler for the "Back" button. Hides the current window and displays
	 * the right option.
	 * 
	 * @param event the MouseEvent that triggers the handler
	 */
	@FXML
	void clickOnBackButton(MouseEvent event) {
		ClientUtils.pickupOrderInProcess = null;
		((Node) event.getSource()).getScene().getWindow().hide();
		switch (ClientUtils.currUser.getRole()) {
		case SUBSCRIBER: {
			new MainDashboradController().start(new Stage());
			break;
		}
		case CUSTOMER: {
			new MainDashboradController().start(new Stage());
			break;
		}
		}
	}
	/**
	* This method is called when the user clicks on the continue button. It creates a new PickUpOrder 
	* object with the current user, chosen machine, current time stamp and empty list of products, and 
	* sets the status to IN_PROCESS. It also sends two messages to the server to get the available and 
	* not available products in the chosen machine. Then, it hides the current window and opens the 
	* appropriate Categories page based on the role of the current user.
	*
	* @param event the ActionEvent that triggers this method.
	*/
	@FXML
	void clickOnContinue(ActionEvent event) {
		ClientUtils.pickupOrderInProcess = new PickUpOrder(ClientUtils.currUser, chooseMachine.getValue(),
				Timestamp.valueOf(LocalDateTime.now()), new ArrayList<>(), OrderStatusEnum.IN_PROCESS);
		Transaction msg1 = new Transaction(Action.GET_AVAILABLE_PRODUCTS_IN_MACHINE,
				ClientUtils.pickupOrderInProcess.getMachineName());
		ClientUI.chat.accept(msg1);
		
		Transaction msg2 = new Transaction(Action.GET_NOT_AVAILABLE_PRODUCTS_IN_MACHINE,
				ClientUtils.pickupOrderInProcess.getMachineName());
		ClientUI.chat.accept(msg2);

		((Node) event.getSource()).getScene().getWindow().hide();
		ClientUtils.cartDisplayFlag = true;
		switch (ClientUtils.currUser.getRole()) {
		case SUBSCRIBER: {
			new SubscriberCategoriesPageController().start(new Stage());
			break;
		}
		case CUSTOMER: {
			new CustomerCategoriesController().start(new Stage());
			break;
		}
		}
	}
	/**
	 * Initializes the ChooseMachinePickUpController screen by getting all the machines name that
	 *  we have in our database  to the machineComboBox.
	 * labels to false.
	 */
	public void initialize() {
		chooseMachine.getItems().addAll(machineNames);
	}

}
