package client_gui;

import java.net.URL;
import java.util.ResourceBundle;
import Utils.generalMethods;
import clientUtil.ClientUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
/**
* The SubscriberCategoriesPageController class is a controller class for the subscriber categories page.
* It provides event handlers for the back button, view cart button, drinks category, food category, sales category, snack category, and sweets category.
* It also displays the machine name on the screen if the order is for a local machine.
*
*/
public class SubscriberCategoriesPageController implements Initializable {

    @FXML
    private ImageView backBtn;
    @FXML
    private ImageView cartBtn;
    @FXML
    private ImageView drinksCategory;
    @FXML
    private ImageView foodCategory;

    @FXML
    private ImageView salesCategory;

    @FXML
    private ImageView snackCategory;

    @FXML
    private ImageView sweetsCategory;
    @FXML
    private Label machineName= new Label();
    /**
     * The start method is used to display the subscriber categories page on the primary stage.
     * It uses the generalMethods class to display the FXML file associated with the subscriber categories page.
     *
     * @param primaryStage The primary stage on which the subscriber categories page will be displayed.
     */

	public void start (Stage primaryStage) {
		generalMethods.displayScreen(primaryStage, getClass(), "/client_fxml/SubscriberCategoriesPage.fxml", "Categories");
	}
	/**
	* This method handles the event when the user clicks on the back button.
	* It hides the current window and navigates to the previous page depending on the current configuration and order process.
	*
	* @param event the event that triggers the method when the user clicks on the back button.
	*/
	@FXML
	void clickOnBackButton(MouseEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide();
		if (ClientUtils.configuration.equals("ek")) {
			new EkWelcomePageController().start(new Stage());
		} else {
			if (ClientUtils.localOrderInProcess != null) {
				LoginController.logout(ClientUtils.currUser.getUsername());
				new LoginController().start(new Stage());
			}
			if (ClientUtils.pickupOrderInProcess != null) {
				new ChooseMachinePickupController().start(new Stage());
			} else {
				new EnterDeliAddController().start(new Stage());
			}
		}

	}
	  /**
     * Event handler for the "View Cart" button. 
     * Sets the cartFlag to true and opens the CartPage.
     * 
     * @param event the mouse event that triggered the handler
     */
    @FXML
    void clickOnViewCart(MouseEvent event) {
    	ClientUtils.cartFlag = true;
    	new CartPageController().start(new Stage());
    }
    /**
     * Event handler for the "Drinks" category button.
     * Hides the current window and opens the DrinksCategory page.
     * 
     * @param event the mouse event that triggered the handler
     */
    @FXML
    void clickOnDrinksCategory(MouseEvent event) {
    	((Node) event.getSource()).getScene().getWindow().hide();
    	new DrinksCategoryController().start(new Stage()); 
    }
    /**
     * Event handler for the "Food" category button.
     * Hides the current window and opens the FoodCategory page.
     * 
     * @param event the mouse event that triggered the handler
     */
    @FXML
    void clickOnFoodCategory(MouseEvent event) {
    	((Node) event.getSource()).getScene().getWindow().hide();
    	new FoodCategoryController().start(new Stage()); 
    }
    /**
     * Event handler for the "Sales" category button.
     * Hides the current window and opens the SalesCategory page.
     * 
     * @param event the mouse event that triggered the handler
     */
    @FXML
    void clickOnSalesCategory(MouseEvent event) {
    	((Node) event.getSource()).getScene().getWindow().hide();
    	new SalesCategoryController().start(new Stage()); 
    }
    /**
     * Event handler for the "Snacks" category button.
     * Hides the current window and opens the SnackCategory page.
     * 
     * @param event the mouse event that triggered the handler
     */
    @FXML
    void clickOnSnackCategory(MouseEvent event) {
    	((Node) event.getSource()).getScene().getWindow().hide();
    	new SnackCategoryController().start(new Stage()); 
    }
    /**
     * Event handler for the "Sweets" category button.
     * Hides the current window and opens the SweetsCategory page.
     * 
     * @param event the mouse event that triggered the handler
     */
    @FXML
    void clickOnSweetsCategory(MouseEvent event) {
    	((Node) event.getSource()).getScene().getWindow().hide();
    	new SweetsCategoryController().start(new Stage());
    }
    /**
     * Initializes the MainPageController by setting the machine name and displaying the cart if necessary.
     * 
     * @param arg0 the URL location of the FXML file
     * @param arg1 the resource bundle associated with the FXML file
     */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		if (ClientUtils.localOrderInProcess != null) {
			machineName.setText(ClientUtils.localOrderInProcess.getMachineName());
			if (ClientUtils.cartDisplayFlag) {
				for (int i = 0; i < ClientUtils.localOrderInProcess.getProductsForDisplay().size(); i++) {
					switch (ClientUtils.localOrderInProcess.getProductsForDisplay().get(i).getCategory()) {
					case FOOD: {
						ClientUtils.localOrderInProcess.getFoodCategoryProducts()
								.add(ClientUtils.localOrderInProcess.getProductsForDisplay().get(i));
						break;
					}
					case SNACKS: {
						ClientUtils.localOrderInProcess.getSnackCategoryProducts()
								.add(ClientUtils.localOrderInProcess.getProductsForDisplay().get(i));
						break;
					}
					case SWEETS: {
						ClientUtils.localOrderInProcess.getSweetsCategoryProducts()
								.add(ClientUtils.localOrderInProcess.getProductsForDisplay().get(i));
						break;
					}
					case DRINKS: {
						ClientUtils.localOrderInProcess.getDrinksCategoryProducts()
								.add(ClientUtils.localOrderInProcess.getProductsForDisplay().get(i));
						break;
					}
					}
				}
				ClientUtils.cartDisplayFlag = false;
			}
		}else if (ClientUtils.pickupOrderInProcess != null) {
			machineName.setText(ClientUtils.pickupOrderInProcess.getMachineName());
			if (ClientUtils.cartDisplayFlag) {
				for (int i = 0; i < ClientUtils.pickupOrderInProcess.getProductsForDisplay().size(); i++) {
					switch (ClientUtils.pickupOrderInProcess.getProductsForDisplay().get(i).getCategory()) {
					case FOOD: {
						ClientUtils.pickupOrderInProcess.getFoodCategoryProducts()
								.add(ClientUtils.pickupOrderInProcess.getProductsForDisplay().get(i));
						break;
					}
					case SNACKS: {
						ClientUtils.pickupOrderInProcess.getSnackCategoryProducts()
								.add(ClientUtils.pickupOrderInProcess.getProductsForDisplay().get(i));
						break;
					}
					case SWEETS: {
						ClientUtils.pickupOrderInProcess.getSweetsCategoryProducts()
								.add(ClientUtils.pickupOrderInProcess.getProductsForDisplay().get(i));
						break;
					}
					case DRINKS: {
						ClientUtils.pickupOrderInProcess.getDrinksCategoryProducts()
								.add(ClientUtils.pickupOrderInProcess.getProductsForDisplay().get(i));
						break;
					}
					}
				}
				ClientUtils.cartDisplayFlag = false;
			}
		} else {
			machineName.setText(String.valueOf(ClientUtils.deliveryOrderInProcess.getClient().getRegion()));
			if (ClientUtils.cartDisplayFlag) {
				for (int i = 0; i < ClientUtils.deliveryOrderInProcess.getProductsForDisplay().size(); i++) {
					switch (ClientUtils.deliveryOrderInProcess.getProductsForDisplay().get(i).getCategory()) {
					case FOOD: {
						ClientUtils.deliveryOrderInProcess.getFoodCategoryProducts()
								.add(ClientUtils.deliveryOrderInProcess.getProductsForDisplay().get(i));
						break;
					}
					case SNACKS: {
						ClientUtils.deliveryOrderInProcess.getSnackCategoryProducts()
								.add(ClientUtils.deliveryOrderInProcess.getProductsForDisplay().get(i));
						break;
					}
					case SWEETS: {
						ClientUtils.deliveryOrderInProcess.getSweetsCategoryProducts()
								.add(ClientUtils.deliveryOrderInProcess.getProductsForDisplay().get(i));
						break;
					}
					case DRINKS: {
						ClientUtils.deliveryOrderInProcess.getDrinksCategoryProducts()
								.add(ClientUtils.deliveryOrderInProcess.getProductsForDisplay().get(i));
						break;
					}
					}
				}
			}
			ClientUtils.cartDisplayFlag = false;
		}

	}
}
