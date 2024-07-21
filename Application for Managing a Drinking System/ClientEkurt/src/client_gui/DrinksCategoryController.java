package client_gui;

import java.net.URL;
import java.util.ResourceBundle;
import Utils.generalMethods;
import clientUtil.ClientUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The  DrinksCategoryController class present all the drink we have in our Ekrut.
 * in this page we can choose what drink we want and the amount.
 */
public class DrinksCategoryController implements Initializable {

    @FXML
    private ImageView backBtn;

    @FXML
    private Button checkoutBtn;

    @FXML
    private GridPane gridPane;

    @FXML
    private ImageView viewCartBtn;
    
    @FXML
    private Label numOfProductsInCartLabel = new Label();
	public Label getNumOfProductsInCartLabel() {
		return numOfProductsInCartLabel;
	}
	
	public void setNumOfProductsInCartLabel(String numOfProductsInCart) {
		 numOfProductsInCartLabel.setText(numOfProductsInCart);
	}
	/**
	 * Displays the  DrinksCategory screen and sets the title of the window for Drinks.
	 * 
	 * @param primaryStage the primary stage of the application
	 */
	public void start (Stage primaryStage) {
		generalMethods.displayScreen(primaryStage, getClass(), "/client_fxml/DrinksCategory.fxml", "Drinks");
		
	}
	/**
	 *The code checks if there is a local order, pickup order, or delivery order in process and loads the appropriate products into the grid
	pane. If the product is in stock, it loads the "ProductInGrid" FXML file, and if not, it loads the "ProductNotInStock" FXML file. 
	 * 
	 * @param arg0 a URL object used to specify the location of the FXML file
	 * @param arg1 a ResourceBundle object used to provide localized resources, such 
	 */

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		int columns = 0;
		int row = 1;
		
		try {
			if(ClientUtils.localOrderInProcess != null) {
			for(int i=0 ; i<ClientUtils.localOrderInProcess.getDrinksCategoryProducts().size();i++) {
				VBox box;
				if(ClientUtils.localOrderInProcess.getDrinksCategoryProducts().get(i).getStock() > 0) {
					FXMLLoader fxmlLoader = new FXMLLoader();
					fxmlLoader.setLocation(getClass().getResource("/client_fxml/ProductInGrid.fxml"));
					box = fxmlLoader.load();
					ProductInGridController productInGridController = fxmlLoader.getController();
					productInGridController.setData(ClientUtils.localOrderInProcess.getDrinksCategoryProducts().get(i));
				}
				else {
					FXMLLoader fxmlLoader = new FXMLLoader();
					fxmlLoader.setLocation(getClass().getResource("/client_fxml/ProductNotInStock.fxml"));
					box = fxmlLoader.load();
					ProductInGridController productInGridController = fxmlLoader.getController();
					productInGridController.setData(ClientUtils.localOrderInProcess.getDrinksCategoryProducts().get(i));
				}
				if(columns == 4) {
					columns = 0;
					++row;
				}
				gridPane.add(box, columns++, row);
				gridPane.setMargin(box, new Insets(8));
				ClientUtils.categoryProducts.add(box);
			}
			}else if(ClientUtils.pickupOrderInProcess != null) {
			for(int i=0 ; i<ClientUtils.pickupOrderInProcess.getDrinksCategoryProducts().size();i++) {
				VBox box;
				if(ClientUtils.pickupOrderInProcess.getDrinksCategoryProducts().get(i).getStock() > 0) {
					FXMLLoader fxmlLoader = new FXMLLoader();
					fxmlLoader.setLocation(getClass().getResource("/client_fxml/ProductInGrid.fxml"));
					box = fxmlLoader.load();
					ProductInGridController productInGridController = fxmlLoader.getController();
					productInGridController.setData(ClientUtils.pickupOrderInProcess.getDrinksCategoryProducts().get(i));
				}
				else {
					FXMLLoader fxmlLoader = new FXMLLoader();
					fxmlLoader.setLocation(getClass().getResource("/client_fxml/ProductNotInStock.fxml"));
					box = fxmlLoader.load();
					ProductInGridController productInGridController = fxmlLoader.getController();
					productInGridController.setData(ClientUtils.pickupOrderInProcess.getDrinksCategoryProducts().get(i));
				}
				if(columns == 4) {
					columns = 0;
					++row;
				}
				gridPane.add(box, columns++, row);
				gridPane.setMargin(box, new Insets(8));
				ClientUtils.categoryProducts.add(box);
			}
			}else {
				for(int i=0 ; i<ClientUtils.deliveryOrderInProcess.getDrinksCategoryProducts().size();i++) {
					VBox box;
					if(ClientUtils.deliveryOrderInProcess.getDrinksCategoryProducts().get(i).getStock() > 0) {
						FXMLLoader fxmlLoader = new FXMLLoader();
						fxmlLoader.setLocation(getClass().getResource("/client_fxml/ProductInGrid.fxml"));
						box = fxmlLoader.load();
						ProductInGridController productInGridController = fxmlLoader.getController();
						productInGridController.setData(ClientUtils.deliveryOrderInProcess.getDrinksCategoryProducts().get(i));
					}
					else {
						FXMLLoader fxmlLoader = new FXMLLoader();
						fxmlLoader.setLocation(getClass().getResource("/client_fxml/ProductNotInStock.fxml"));
						box = fxmlLoader.load();
						ProductInGridController productInGridController = fxmlLoader.getController();
						productInGridController.setData(ClientUtils.deliveryOrderInProcess.getDrinksCategoryProducts().get(i));
					}
					if(columns == 4) {
						columns = 0;
						++row;
					}
					gridPane.add(box, columns++, row);
					gridPane.setMargin(box, new Insets(8));
					ClientUtils.categoryProducts.add(box);
			}
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Event handler for the "Back" button. Hides the current window and displays
	 * the suitable fxml file based on the currUser log in.
	 * if the Customer was logged in, it will open the CustomerCategoriesController page.
	 * if the Subscriber was logged in, it will open the SubscriberCategoriesPageController page.
	 * 
	 * @param event the MouseEvent that triggers the handler
	 */
    @FXML
    void clickOnBackButton(MouseEvent event) {
    	((Node) event.getSource()).getScene().getWindow().hide();
    	switch (ClientUtils.currUser.getRole()) {
		case CUSTOMER: {
	    	new CustomerCategoriesController().start(new Stage());
			break;
		}
		case SUBSCRIBER: {
			new SubscriberCategoriesPageController().start(new Stage());
			break;
		}
    	}	
    }
    /**
	 * Will open the CartPageController page and set the flag to 0.
	 * 
	 * @param event the ActionEvent that triggers the handler
	 */
    @FXML
    void clickOnCheckout(ActionEvent event) {
    	ClientUtils.cartFlag = false;
    	new CartPageController().start(new Stage());
    }
    /**
	 *Will open the CartPageController page and set the flag to 1.
	 * 
	 * @param event the MouseEvent that triggers the handler
	 */
    @FXML
    void clickOnViewCart(MouseEvent event) {
    	ClientUtils.cartFlag = true;
    	new CartPageController().start(new Stage());
    }
}
