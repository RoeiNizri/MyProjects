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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
* The SweetsCategoryController class is a JavaFX controller class for a sweets category screen in a client application.
* It implements the Initializable interface and has various FXML annotations for UI elements such as buttons and a grid pane.
* The class has a start method that is used to display the sweets category screen and a initialize method that is used to populate
* the grid pane with products from the localOrderInProcess or pickupOrderInProcess.
* The class also has event handlers such as the backBtn and checkoutBtn. The class loads different FXML files depending on the stock 
* of the product, if stock is more than 0 it loads ProductInGrid.fxml else it loads ProductNotInStock.fxml
*/
public class SweetsCategoryController implements Initializable {

    @FXML
    private ImageView backBtn;

    @FXML
    private Button checkoutBtn;

    @FXML
    private GridPane gridPane;
    @FXML
    private ImageView viewCartBtn;
    /**
     * The start method displays the sweets category screen by calling the displayScreen method of the generalMethods class.
     * It takes a Stage object as an argument, which represents the primary stage of the application.
     * The method also sets the title of the stage to "Sweets".
     *
     * @param primaryStage The primary stage of the application
     */

	public void start (Stage primaryStage) {
		generalMethods.displayScreen(primaryStage, getClass(), "/client_fxml/SweetsCategory.fxml", "Sweets");
	}
	/**
	* The initialize method is used to populate the grid pane with products from the localOrderInProcess or pickupOrderInProcess or deliveryOrderInProcess.
	* The method first checks if localOrderInProcess is not null and if so, it loops through the sweets category products of localOrderInProcess and for each product it checks the stock from db.
	* If the stock is greater than 0, it loads the ProductInGrid.fxml and sets the data for the product using the ProductInGridController.
	* If the stock is not greater than 0, it loads the ProductNotInStock.fxml and sets the data for the product using the ProductInGridController.
	* Then it adds the box to the gridPane and sets the margin for the box.
	* If localOrderInProcess is null then it checks for pickupOrderInProcess and if not null it does the same process as localOrderInProcess.
	* If pickupOrderInProcess is null then it checks for deliveryOrderInProcess and if not null it does the same process as localOrderInProcess.
	*
	* @param arg0 URL object passed by the initialize method
	* @param arg1 ResourceBundle object passed by the initialize method
	* @throws IOException if an error occurs while loading the FXML files
	*/
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		int columns = 0;
		int row = 1;
		
		try {
			if(ClientUtils.localOrderInProcess != null) {
			for(int i=0 ; i<ClientUtils.localOrderInProcess.getSweetsCategoryProducts().size();i++) {
				VBox box;
				if(ClientUtils.localOrderInProcess.getSweetsCategoryProducts().get(i).getStock() > 0) {
					FXMLLoader fxmlLoader = new FXMLLoader();
					fxmlLoader.setLocation(getClass().getResource("/client_fxml/ProductInGrid.fxml"));
					box = fxmlLoader.load();
					ProductInGridController productInGridController = fxmlLoader.getController();
					productInGridController.setData(ClientUtils.localOrderInProcess.getSweetsCategoryProducts().get(i));
				}
				else {
					FXMLLoader fxmlLoader = new FXMLLoader();
					fxmlLoader.setLocation(getClass().getResource("/client_fxml/ProductNotInStock.fxml"));
					box = fxmlLoader.load();
					ProductInGridController productInGridController = fxmlLoader.getController();
					productInGridController.setData(ClientUtils.localOrderInProcess.getSweetsCategoryProducts().get(i));
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
			for(int i=0 ; i<ClientUtils.pickupOrderInProcess.getSweetsCategoryProducts().size();i++) {
				VBox box;
				if(ClientUtils.pickupOrderInProcess.getSweetsCategoryProducts().get(i).getStock() > 0) {
					FXMLLoader fxmlLoader = new FXMLLoader();
					fxmlLoader.setLocation(getClass().getResource("/client_fxml/ProductInGrid.fxml"));
					box = fxmlLoader.load();
					ProductInGridController productInGridController = fxmlLoader.getController();
					productInGridController.setData(ClientUtils.pickupOrderInProcess.getSweetsCategoryProducts().get(i));
				}
				else {
					FXMLLoader fxmlLoader = new FXMLLoader();
					fxmlLoader.setLocation(getClass().getResource("/client_fxml/ProductNotInStock.fxml"));
					box = fxmlLoader.load();
					ProductInGridController productInGridController = fxmlLoader.getController();
					productInGridController.setData(ClientUtils.pickupOrderInProcess.getSweetsCategoryProducts().get(i));
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
				for(int i=0 ; i<ClientUtils.deliveryOrderInProcess.getSweetsCategoryProducts().size();i++) {
					VBox box;
					if(ClientUtils.deliveryOrderInProcess.getSweetsCategoryProducts().get(i).getStock() > 0) {
						FXMLLoader fxmlLoader = new FXMLLoader();
						fxmlLoader.setLocation(getClass().getResource("/client_fxml/ProductInGrid.fxml"));
						box = fxmlLoader.load();
						ProductInGridController productInGridController = fxmlLoader.getController();
						productInGridController.setData(ClientUtils.deliveryOrderInProcess.getSweetsCategoryProducts().get(i));
					}
					else {
						FXMLLoader fxmlLoader = new FXMLLoader();
						fxmlLoader.setLocation(getClass().getResource("/client_fxml/ProductNotInStock.fxml"));
						box = fxmlLoader.load();
						ProductInGridController productInGridController = fxmlLoader.getController();
						productInGridController.setData(ClientUtils.deliveryOrderInProcess.getSweetsCategoryProducts().get(i));
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
	    * The clickOnBackButton method is an event handler for the back button.
	    * It hides the current window and opens a new stage based on the role of the user.
	    * If the user is a customer it opens a new instance of CustomerCategoriesController and if the user is a subscriber it opens a new instance of SubscriberCategoriesPageController
	    *
	    * @param event The mouse event that triggers the method.
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

    @FXML
    void clickOnCheckout(ActionEvent event) {
    	ClientUtils.cartFlag = false;
    	new CartPageController().start(new Stage());
    }
    /**
     * The clickOnViewCart method is an event handler for the view cart button.
     * It sets the cartFlag to true and opens a new stage of the CartPageController.
     *
     * @param event The mouse event that triggers the method.
     */
    @FXML
    void clickOnViewCart(MouseEvent event) {
    	ClientUtils.cartFlag = true;
    	new CartPageController().start(new Stage());
    }
}