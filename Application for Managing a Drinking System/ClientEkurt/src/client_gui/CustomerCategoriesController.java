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
 * The CustomerCategoriesController class handles the customer's Categories to choose from.
 * including displaying the different Categories we have.
 */
public class CustomerCategoriesController implements Initializable {
    @FXML
    private ImageView backBtn;
    @FXML
    private ImageView cartBtn;
    @FXML
    private ImageView drinksCategory;
    @FXML
    private ImageView foodCategory;
    @FXML
    private ImageView snackCategory;
    @FXML
    private ImageView sweetsCategory;
    @FXML
    private Label machineName = new Label();
    /**
	 * This method is used to start the Customer Categories Page process by
	 * displaying the CustomerCategoriesPage.fxml file on the primary stage. 
	 * @param primaryStage The stage on which the CustomerApprovingPage.fxml file will be displayed.
	 */
	public void start (Stage primaryStage) {
		generalMethods.displayScreen(primaryStage, getClass(), "/client_fxml/CustomerCategoriesPage.fxml", "Categories");
	}
	/**
	 * This method is called when the back button is clicked. It hides the current
	 * window and opens the chosen page.
	 * 
	 * @param event The mouse event that triggers this method.
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
		 *  this method opens the 
		 * @param event The mouse event that triggers this method.
		 */
    @FXML
    void clickOnViewCart(MouseEvent event) {
    	ClientUtils.cartFlag = true;
    	new CartPageController().start(new Stage());
    }
    /**
 	 *  this method opens the DrinksCategoryController page.
 	 * @param event The mouse event that triggers this method.
 	 */
    @FXML
    void clickOnDrinksCategory(MouseEvent event) {
    	((Node) event.getSource()).getScene().getWindow().hide();
    	new DrinksCategoryController().start(new Stage());
    }
    /**
  	 *  this method opens the FoodCategoryController page.
  	 * @param event The mouse event that triggers this method.
  	 */
    @FXML
    void clickOnFoodCategory(MouseEvent event) {
    	((Node) event.getSource()).getScene().getWindow().hide();
    	new FoodCategoryController().start(new Stage()); 
    }
    /**
  	 *  this method opens the SnackCategoryController page.
  	 * @param event The mouse event that triggers this method.
  	 */
    @FXML
    void clickOnSnackCategory(MouseEvent event) {
    	((Node) event.getSource()).getScene().getWindow().hide();
    	new SnackCategoryController().start(new Stage()); 
    }
    /**
  	 *  this method opens the SweetsCategoryController page.
  	 * @param event The mouse event that triggers this method.
  	 */
    @FXML
    void clickOnSweetsCategory(MouseEvent event) {
    	((Node) event.getSource()).getScene().getWindow().hide();
    	new SweetsCategoryController().start(new Stage());
    }
    /**
  	 *  thid method sorts between the different categories and puts it on screen.
  	 * @param event The mouse event that triggers this method.
  	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// add if to pickUp order only
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
