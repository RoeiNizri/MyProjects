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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import logic.DeliveryOrder;

/**
 * This class represents the controller for the CEO EnterDeliAdd. 
 *  in this class the user who wants to make a delivery order
 *  insert his city, street name and house number 
 */
public class EnterDeliAddController implements Initializable {

    @FXML
    private TextField city=new TextField();

    @FXML
    private Button continueBtn;

    @FXML
    private TextField houseNum = new TextField();

    @FXML
    private TextField streetName= new TextField();
    
    @FXML
    private ImageView backBtn;
    
    @FXML
    private Label addressLabel = new Label();
    
	public void start(Stage primaryStage) {
		generalMethods.displayScreen(primaryStage, getClass(), "/client_fxml/EnterDeliveryAddress.fxml", "Enter Address");
	}
	/**
     * Handles the click event on the "Continue" button on the delivery address page.
     * It checks if the city, street name and house number fields are filled, if not it shows an error message and highlights the empty fields,
     * otherwise it sets the delivery order's city, street name, and house number from the input fields, 
     * sends a GET_PRODUCTS_FOR_DELIVERY message to the server, 
     * hides the current window and opens the product categories page for the current user's role (subscriber or customer).
     *
     * @param event The action event that triggers this method
     */
    @FXML
    void clickOnContinue(ActionEvent event) {
    	ClientUtils.deliveryOrderInProcess = new DeliveryOrder(ClientUtils.currUser, Timestamp.valueOf(LocalDateTime.now()), new ArrayList<>(), OrderStatusEnum.IN_PROCESS);
		Transaction msg = new Transaction(Action.GET_PRODUCTS_FOR_DELIVERY,
				ClientUtils.deliveryOrderInProcess.getClient().getRegion());
		ClientUI.chat.accept(msg);
		if(city.getText().equals("") || streetName.getText().equals("") || houseNum.getText().equals("")) {
			addressLabel.setText("Incorrect details - Please try again");
			addressLabel.setVisible(true);
    		city.setStyle("-fx-border-color: #EB5234; -fx-border-width: 1px 1px 1px 1px;");
    		streetName.setStyle("-fx-border-color: #EB5234; -fx-border-width: 1px 1px 1px 1px;");
    		houseNum.setStyle("-fx-border-color: #EB5234; -fx-border-width: 1px 1px 1px 1px;");
		}else {
    	ClientUtils.deliveryOrderInProcess.setCity(city.getText());
    	ClientUtils.deliveryOrderInProcess.setStreetName(streetName.getText());
    	ClientUtils.deliveryOrderInProcess.setHouseNum(Integer.valueOf(houseNum.getText()));
		
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
    }
    @FXML
    void clickOnCity(MouseEvent event) {
    	clearTextField();
    }

    @FXML
    void clickOnHouseNum(MouseEvent event) {
    	clearTextField();
    }

    @FXML
    void clickOnStreetName(MouseEvent event) {
    	clearTextField();
    }
    
    
	/**
	 * Event handler for the "Back" button. Hides the current window and displays
	 * the suitable page  based on the currUser log in.
	 * if the Customer was logged in, it will open the MainDashboradController page.
	 * if the Subscriber was logged in, it will open the MainDashboradController page.
	 * 
	 * @param event the MouseEvent that triggers the handler
	 */
	@FXML
	void clickOnBackButton(MouseEvent event) {
		ClientUtils.deliveryOrderInProcess = null;
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
	 * function that clear the text field
	 */
	public void clearTextField () {
		addressLabel.setVisible(false);
		city.setStyle("-fx-border-color: transparent; -fx-border-width: 0px 0px 0px 0px;");
		streetName.setStyle("-fx-border-color: transparent; -fx-border-width: 0px 0px 0px 0px;");
		houseNum.setStyle("-fx-border-color: transparent; -fx-border-width: 0px 0px 0px 0px;");
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		addressLabel.setVisible(false);
		city.setStyle("-fx-border-color: transparent; -fx-border-width: 0px 0px 0px 0px;");
		streetName.setStyle("-fx-border-color: transparent; -fx-border-width: 0px 0px 0px 0px;");
		houseNum.setStyle("-fx-border-color: transparent; -fx-border-width: 0px 0px 0px 0px;");
	}

}