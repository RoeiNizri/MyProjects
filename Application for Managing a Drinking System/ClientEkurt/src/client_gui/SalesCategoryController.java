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

*This class is the controller class for SaleCategory.fxml.
*It contains the logic and methods that handles the events and actions of the Sale Category page
*It also loads the products of this category and display them in a grid format
*It also handles the back button, checkout button and view cart button
*/
public class SalesCategoryController implements Initializable {

	@FXML
	private ImageView backBtn;

	@FXML
	private Button checkoutBtn;

	@FXML
	private GridPane gridPane;

	@FXML
	private Label numOfProductsInCartLabel;

	@FXML
	private ImageView viewCartBtn;

	public void start(Stage primaryStage) {
		generalMethods.displayScreen(primaryStage, getClass(), "/client_fxml/SaleCategory.fxml", "Sale");
	}
	/**

	*This method is used to display the Sale category screen for the user.
	*It takes in a primaryStage object of type Stage, which is used to display the Sale category screen.
	*The method calls the generalMethods.displayScreen() method and passes it the required parameters
	*such as the class object, the fxml file path and the title of the screen.
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

	*The clickOnCheckout() method is an event handler method that is triggered when the user clicks the "Checkout" button.
	*When this method is called, it sets the cartFlag variable in the ClientUtils class to false, and
	*creates an instance of the CartPageController class and calls the start() method on it.
	*This causes the cart page to be displayed and the checkout process to begin.
	*/
	@FXML
	void clickOnCheckout(ActionEvent event) {
		ClientUtils.cartFlag = false;
		new CartPageController().start(new Stage());
	}
	/**
	* This method is used to handle the event of clicking on the view cart button.
	* It sets the flag {@link ClientUtils#cartFlag} to true, and opens the CartPageController.
	*
	* @param event MouseEvent representing the button click event.
	*/
	@FXML
	void clickOnViewCart(MouseEvent event) {
		ClientUtils.cartFlag = true;
		new CartPageController().start(new Stage());
	}
	/**

	*Initializes and populates a grid pane with products from an order.
	*The products are loaded from an FXML file called "ProductInGrid.fxml"
	*and displayed in a 4-column layout, with a margin of 8 pixels.
	*The products added to the grid pane are determined by their stock and sale status.
	*The order used can be one of three possibilities: localOrderInProcess, pickupOrderInProcess, deliveryOrderInProcess
	*The added elements are also added to a list called "ClientUtils.categoryProducts"
	*@param arg0 a URL object used for loading the FXML file
	*@param arg1 a ResourceBundle object used for getting resources for the application
	*@throws java.io.IOException if the FXML file for the product grid layout
	*/
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		int columns = 0;
		int row = 1;

		try {
			if (ClientUtils.localOrderInProcess != null) {
				for (int i = 0; i < ClientUtils.localOrderInProcess.getProductsForDisplay().size(); i++) {
					VBox box;
					if (ClientUtils.localOrderInProcess.getProductsForDisplay().get(i).getStock() > 0
							&& ClientUtils.localOrderInProcess.getProductsForDisplay().get(i).isIs_in_sale()) {
						FXMLLoader fxmlLoader = new FXMLLoader();
						fxmlLoader.setLocation(getClass().getResource("/client_fxml/ProductInGrid.fxml"));
						box = fxmlLoader.load();
						ProductInGridController productInGridController = fxmlLoader.getController();
						productInGridController
								.setData(ClientUtils.localOrderInProcess.getProductsForDisplay().get(i));

						if (columns == 4) {
							columns = 0;
							++row;
						}
						gridPane.add(box, columns++, row);
						gridPane.setMargin(box, new Insets(8));
						ClientUtils.categoryProducts.add(box);
					}
				}
			}else if (ClientUtils.pickupOrderInProcess != null) {
				for (int i = 0; i < ClientUtils.pickupOrderInProcess.getProductsForDisplay().size(); i++) {
					VBox box;
					if (ClientUtils.pickupOrderInProcess.getProductsForDisplay().get(i).getStock() > 0
							&& ClientUtils.pickupOrderInProcess.getProductsForDisplay().get(i).isIs_in_sale()) {
						FXMLLoader fxmlLoader = new FXMLLoader();
						fxmlLoader.setLocation(getClass().getResource("/client_fxml/ProductInGrid.fxml"));
						box = fxmlLoader.load();
						ProductInGridController productInGridController = fxmlLoader.getController();
						productInGridController
								.setData(ClientUtils.pickupOrderInProcess.getProductsForDisplay().get(i));

						if (columns == 4) {
							columns = 0;
							++row;
						}
						gridPane.add(box, columns++, row);
						gridPane.setMargin(box, new Insets(8));
						ClientUtils.categoryProducts.add(box);
					}
				}
			} else {
				for (int i = 0; i < ClientUtils.deliveryOrderInProcess.getProductsForDisplay().size(); i++) {
					VBox box;
					if (ClientUtils.deliveryOrderInProcess.getProductsForDisplay().get(i).getStock() > 0
							&& ClientUtils.deliveryOrderInProcess.getProductsForDisplay().get(i).isIs_in_sale()) {
						FXMLLoader fxmlLoader = new FXMLLoader();
						fxmlLoader.setLocation(getClass().getResource("/client_fxml/ProductInGrid.fxml"));
						box = fxmlLoader.load();
						ProductInGridController productInGridController = fxmlLoader.getController();
						productInGridController
								.setData(ClientUtils.deliveryOrderInProcess.getProductsForDisplay().get(i));

						if (columns == 4) {
							columns = 0;
							++row;
						}
						gridPane.add(box, columns++, row);
						gridPane.setMargin(box, new Insets(8));
						ClientUtils.categoryProducts.add(box);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
