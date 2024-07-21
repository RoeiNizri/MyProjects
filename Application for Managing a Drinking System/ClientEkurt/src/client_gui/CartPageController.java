package client_gui;

import java.net.URL;
import java.util.ResourceBundle;
import Utils.generalMethods;
import clientUtil.ClientUtils;
import enums.StatusEnum;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * This class represents the controller for the CartPage screen. It handles
 * the actions for the buttons on the screen and .
 */
public class CartPageController implements Initializable {
	/**
	 * Displays the CEO CartPage screen and sets the title of the window to Shopping Cart .
	 * 
	 * @param primaryStage the primary stage of the application
	 */
	public void start(Stage primaryStage) {
		generalMethods.displayScreen(primaryStage, getClass(), "/client_fxml/CartPage.fxml", "Shopping Cart");
	}
	
	@FXML
	private Button clearBtn;

	@FXML
	private GridPane cartGridPane;

	public GridPane getCartGridPane() {
		return cartGridPane;
	}

	public void setCartGridPane(GridPane cartGridPane) {
		this.cartGridPane = cartGridPane;
	}

	@FXML
	private Button confirmBtn;

	@FXML
	private Label pageTitle;

	@FXML
	private Label totalToPayLable = new Label();

	@FXML
	private Label discountLabel = new Label();

	@FXML
	private Label subTotalLabel = new Label();
    @FXML
    private Label discountMsg;
    private float firstOrderDiscount = (float) 0.8;
	/**

	This method is called when the "Clear" button is clicked.
	clears the products, total to pay, and first order discount for the local, pickup, or delivery order in process
	 (as determined by the values in the ClientUtils class). It also hides the current window and opens a new CartPage.
	
	@param event the ActionEvent object passed in when the button is clicked
	*/
	@FXML
	void clickOnClearBtn(ActionEvent event) {
		subTotalLabel.setText("0.00 ¤");
		if (ClientUtils.localOrderInProcess != null) {
			for (int i = ClientUtils.localOrderInProcess.getProducts().size() - 1; i >= 0; i--) {
				ClientUtils.localOrderInProcess.getProducts().remove(i);
				
			}
			ClientUtils.localOrderInProcess.setTotalToPay(0);
			ClientUtils.localOrderInProcess.setFirstOrderDiscount(0);

			((Node) event.getSource()).getScene().getWindow().hide();
			new CartPageController().start(new Stage());
		} else if (ClientUtils.pickupOrderInProcess != null) {
			for (int i = ClientUtils.pickupOrderInProcess.getProducts().size() - 1; i >= 0; i--) {
				ClientUtils.pickupOrderInProcess.getProducts().remove(i);
			}
			ClientUtils.pickupOrderInProcess.setTotalToPay(0);
			ClientUtils.pickupOrderInProcess.setFirstOrderDiscount(0);
			((Node) event.getSource()).getScene().getWindow().hide();
			new CartPageController().start(new Stage());
		} else {
			for (int i = ClientUtils.deliveryOrderInProcess.getProducts().size() - 1; i >= 0; i--) {
				ClientUtils.deliveryOrderInProcess.getProducts().remove(i);
			}
			ClientUtils.deliveryOrderInProcess.setTotalToPay(0);
			ClientUtils.deliveryOrderInProcess.setFirstOrderDiscount(0);

			((Node) event.getSource()).getScene().getWindow().hide();
			new CartPageController().start(new Stage());
		}
	}
	/**

	This method is called when the "Clear" button is clicked.
	clears the products, total to pay, and first order discount for the local, pickup, or delivery order in process
	 (as determined by the values in the ClientUtils class). It also hides the current window and opens a new CartPage.
	
	@param event the ActionEvent object passed in when the button is clicked
	*/
	@FXML
	void clickOnConfirm(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide();
		switch (ClientUtils.currUser.getRole()) {
		case CUSTOMER: {
			new PaymentPageController().start(new Stage());
			break;
		}
		case SUBSCRIBER: {
			new WalletConfirmController().start(new Stage());
			break;
		}
		}
	}
	/**

	This method is called when the CartPage or OrderSummaryPage is initialized.
	 It sets the visibility of the discount message and the page title based on the value of the "cartFlag" variable in the ClientUtils class.
	  It also populates the cartGridPane with productInCart.fxml for each product in the local order in process.
	   If the local order in process has no products, the "Clear" and "Confirm" buttons are set to invisible.
	@param arg0 the URL of the location of the root object
	@param arg1 the ResourceBundle used to localize the root object
	*/
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		discountMsg.setVisible(false);
		if(ClientUtils.cartFlag)
			pageTitle.setText("Shopping Cart");
		else {
			pageTitle.setText("Order Summary");
		}
		int row = 1;
		if(ClientUtils.localOrderInProcess != null) {
		if(ClientUtils.localOrderInProcess.getProducts().size() == 0) {
			clearBtn.setVisible(false);
			confirmBtn.setVisible(false);
		}
		try {
			for (int i = 0; i < ClientUtils.localOrderInProcess.getProducts().size(); i++) {
				FXMLLoader fxmlLoader = new FXMLLoader();
				fxmlLoader.setLocation(getClass().getResource("/client_fxml/productInCart.fxml"));
				HBox box = fxmlLoader.load();
				ProductInCartController productInCartController = fxmlLoader.getController();
				productInCartController.setData(ClientUtils.localOrderInProcess.getProducts().get(i));
				cartGridPane.add(box, 0, row++);
				cartGridPane.setMargin(box, new Insets(1));
				ClientUtils.cartProducts.add(box);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ProductInCartController.cartGridPane = cartGridPane;

		float subTotal = 0;
		float total = 0;
		for (int i = 0; i < ClientUtils.localOrderInProcess.getProducts().size(); i++) {
			for (int j = 0; j < ClientUtils.localOrderInProcess.getProductsForDisplay().size(); j++) {
				if (ClientUtils.localOrderInProcess.getProductsForDisplay().get(j).getPro_name()
						.equals(ClientUtils.localOrderInProcess.getProducts().get(i).getName())) {
					subTotal += (ClientUtils.localOrderInProcess.getProductsForDisplay().get(j).getPrice() *
							ClientUtils.localOrderInProcess.getProducts().get(i).getAmount());
					break;
				}
			}
		}
		if(ClientUtils.currUser.getStatus().equals(StatusEnum.FIRST_ORDER)) {
			discountMsg.setVisible(true);
			totalToPayLable.setText(String.format("%.2f", ClientUtils.localOrderInProcess.getFirstOrderDiscount()) + " ¤");
			float discount = subTotal - ClientUtils.localOrderInProcess.getFirstOrderDiscount();
			discountLabel.setText(String.format("%.2f",discount) + " ¤");
		}else {
			totalToPayLable.setText(String.format("%.2f", ClientUtils.localOrderInProcess.getTotalToPay()) + " ¤");
			float discount = subTotal - ClientUtils.localOrderInProcess.getTotalToPay();
			discountLabel.setText(String.format("%.2f",discount) + " ¤");
		}
		subTotalLabel.setText(String.format("%.2f", subTotal) + " ¤");
		
		
		}else if(ClientUtils.pickupOrderInProcess != null) {
		if(ClientUtils.pickupOrderInProcess.getProducts().size() == 0) {
			clearBtn.setVisible(false);
			confirmBtn.setVisible(false);
		}
		try {
			for (int i = 0; i < ClientUtils.pickupOrderInProcess.getProducts().size(); i++) {
				FXMLLoader fxmlLoader = new FXMLLoader();
				fxmlLoader.setLocation(getClass().getResource("/client_fxml/productInCart.fxml"));
				HBox box = fxmlLoader.load();
				ProductInCartController productInCartController = fxmlLoader.getController();
				productInCartController.setData(ClientUtils.pickupOrderInProcess.getProducts().get(i));
				cartGridPane.add(box, 0, row++);
				cartGridPane.setMargin(box, new Insets(1));
				ClientUtils.cartProducts.add(box);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ProductInCartController.cartGridPane = cartGridPane;
		float subTotal = 0;
		float total = 0;
		for (int i = 0; i < ClientUtils.pickupOrderInProcess.getProducts().size(); i++) {
			for (int j = 0; j < ClientUtils.pickupOrderInProcess.getProductsForDisplay().size(); j++) {
				if (ClientUtils.pickupOrderInProcess.getProductsForDisplay().get(j).getPro_name()
						.equals(ClientUtils.pickupOrderInProcess.getProducts().get(i).getName())) {
					subTotal += (ClientUtils.pickupOrderInProcess.getProductsForDisplay().get(j).getPrice() * 
					ClientUtils.pickupOrderInProcess.getProducts().get(i).getAmount());
					break;
				}
			}
		}
		if(ClientUtils.currUser.getStatus().equals(StatusEnum.FIRST_ORDER)) {
			discountMsg.setVisible(true);
			totalToPayLable.setText(String.format("%.2f", ClientUtils.pickupOrderInProcess.getFirstOrderDiscount()) + " ¤");
			float discount = subTotal - ClientUtils.pickupOrderInProcess.getFirstOrderDiscount();
			discountLabel.setText(String.format("%.2f",discount) + " ¤");
		}else {
			totalToPayLable.setText(String.format("%.2f", ClientUtils.pickupOrderInProcess.getTotalToPay()) + " ¤");
			float discount = subTotal - ClientUtils.pickupOrderInProcess.getTotalToPay();
			discountLabel.setText(String.format("%.2f",discount) + " ¤");
		}
		subTotalLabel.setText(String.format("%.2f", subTotal) + " ¤");
				
		}else {
			if(ClientUtils.deliveryOrderInProcess.getProducts().size() == 0) {
				clearBtn.setVisible(false);
				confirmBtn.setVisible(false);
			}
			try {
				for (int i = 0; i < ClientUtils.deliveryOrderInProcess.getProducts().size(); i++) {
					FXMLLoader fxmlLoader = new FXMLLoader();
					fxmlLoader.setLocation(getClass().getResource("/client_fxml/productInCart.fxml"));
					HBox box = fxmlLoader.load();
					ProductInCartController productInCartController = fxmlLoader.getController();
					productInCartController.setData(ClientUtils.deliveryOrderInProcess.getProducts().get(i));
					cartGridPane.add(box, 0, row++);
					cartGridPane.setMargin(box, new Insets(1));
					ClientUtils.cartProducts.add(box);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			
			ProductInCartController.cartGridPane = cartGridPane;
			float subTotal = 0;
			float total = 0;
			for (int i = 0; i < ClientUtils.deliveryOrderInProcess.getProducts().size(); i++) {
				for (int j = 0; j < ClientUtils.deliveryOrderInProcess.getProductsForDisplay().size(); j++) {
					if (ClientUtils.deliveryOrderInProcess.getProductsForDisplay().get(j).getPro_name()
							.equals(ClientUtils.deliveryOrderInProcess.getProducts().get(i).getName())) {
						subTotal += (ClientUtils.deliveryOrderInProcess.getProductsForDisplay().get(j).getPrice() *
								ClientUtils.deliveryOrderInProcess.getProducts().get(i).getAmount());
						break;
					}
				}
			}
			if(ClientUtils.currUser.getStatus().equals(StatusEnum.FIRST_ORDER)) {
				discountMsg.setVisible(true);
				totalToPayLable.setText(String.format("%.2f", ClientUtils.deliveryOrderInProcess.getFirstOrderDiscount()) + " ¤");
				float discount = subTotal - ClientUtils.deliveryOrderInProcess.getFirstOrderDiscount();
				discountLabel.setText(String.format("%.2f",discount) + " ¤");
			}else {
				totalToPayLable.setText(String.format("%.2f", ClientUtils.deliveryOrderInProcess.getTotalToPay()) + " ¤");
				float discount = subTotal - ClientUtils.deliveryOrderInProcess.getTotalToPay();
				discountLabel.setText(String.format("%.2f",discount) + " ¤");
			}
			subTotalLabel.setText(String.format("%.2f", subTotal) + " ¤");
			
			}
		
	}

}
