package client_gui;

import java.net.URL;
import java.util.ResourceBundle;
import clientUtil.ClientUtils;
import enums.CategoriesEnum;
import enums.StatusEnum;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import logic.Product;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
/**
The class ProductInCartController is a controller class for the handling the products in the cart

* 
* @param primaryStage the primary stage of the application
*/
public class ProductInCartController implements Initializable {

	@FXML
	private Label finalPrice;

	@FXML
	private Label productName;

	@FXML
	private Label productPrice;

	@FXML
	private Label productsQuantity;

	@FXML
	private ImageView removeProductBtn;

	public static GridPane cartGridPane;
	private String name;

	private CategoriesEnum category;
	  /**
		 The class clickOnTrashbin is a controller class for the handling the products in the cart and removing the product from the cart
		 
		 * 
		 * @param event the MouseEvent that triggers the handle
		 */
	@FXML
	void clickOnTrashbin(MouseEvent event) {
		int i = 0;
		if (ClientUtils.localOrderInProcess != null) {
			for (i = 0; i < ClientUtils.localOrderInProcess.getProducts().size(); i++) {
				if (ClientUtils.localOrderInProcess.getProducts().get(i).getName().equals(productName.getText())) {
					break;
				}
			}
			ClientUtils.localOrderInProcess.setTotalToPay(ClientUtils.localOrderInProcess.getTotalToPay()
					- ClientUtils.localOrderInProcess.getProducts().get(i).getFinalPrice());
			if (ClientUtils.currUser.getStatus().equals(StatusEnum.FIRST_ORDER)) {
				ClientUtils.localOrderInProcess
						.setFirstOrderDiscount((float) (ClientUtils.localOrderInProcess.getTotalToPay() * 0.8));
			}
			switch (this.getCategory()) {
			case FOOD: {
				for (int j = 0; j < ClientUtils.localOrderInProcess.getFoodCategoryProducts().size(); j++) {
					if (ClientUtils.localOrderInProcess.getFoodCategoryProducts().get(j).getPro_name().equals(name)) {
						ClientUtils.localOrderInProcess.getFoodCategoryProducts().get(j)
								.setStock(ClientUtils.localOrderInProcess.getFoodCategoryProducts().get(j).getStock() +
										ClientUtils.localOrderInProcess.getProducts().get(i).getAmount());		
					}
				}
				break;
			}
			case DRINKS: {
				for (int j = 0; j < ClientUtils.localOrderInProcess.getDrinksCategoryProducts().size(); j++) {
					if (ClientUtils.localOrderInProcess.getDrinksCategoryProducts().get(j).getPro_name().equals(name)) {
						ClientUtils.localOrderInProcess.getDrinksCategoryProducts().get(j)
								.setStock(ClientUtils.localOrderInProcess.getDrinksCategoryProducts().get(j).getStock() +
										ClientUtils.localOrderInProcess.getProducts().get(i).getAmount());		
					}
				}
				break;
			}
			case SNACKS: {
				for (int j = 0; j < ClientUtils.localOrderInProcess.getSnackCategoryProducts().size(); j++) {
					if (ClientUtils.localOrderInProcess.getSnackCategoryProducts().get(j).getPro_name().equals(name)) {
						ClientUtils.localOrderInProcess.getSnackCategoryProducts().get(j)
								.setStock(ClientUtils.localOrderInProcess.getSnackCategoryProducts().get(j).getStock() +
										ClientUtils.localOrderInProcess.getProducts().get(i).getAmount());		
					}
				}
				break;
			}
			case SWEETS: {
				for (int j = 0; j < ClientUtils.localOrderInProcess.getSweetsCategoryProducts().size(); j++) {
					if (ClientUtils.localOrderInProcess.getSweetsCategoryProducts().get(j).getPro_name().equals(name)) {
						ClientUtils.localOrderInProcess.getSweetsCategoryProducts().get(j)
								.setStock(ClientUtils.localOrderInProcess.getSweetsCategoryProducts().get(j).getStock() +
										ClientUtils.localOrderInProcess.getProducts().get(i).getAmount());		
					}
				}
				break;
			}
			default:
				break;
			}
			ClientUtils.localOrderInProcess.getProducts().remove(i);
			((Node) event.getSource()).getScene().getWindow().hide();
			new CartPageController().start(new Stage());

		} else if (ClientUtils.pickupOrderInProcess != null) {
			for (i = 0; i < ClientUtils.pickupOrderInProcess.getProducts().size(); i++) {
				if (ClientUtils.pickupOrderInProcess.getProducts().get(i).getName().equals(productName.getText())) {
					break;
				}
			}
			ClientUtils.pickupOrderInProcess.setTotalToPay(ClientUtils.pickupOrderInProcess.getTotalToPay()
					- ClientUtils.pickupOrderInProcess.getProducts().get(i).getFinalPrice());
			if (ClientUtils.currUser.getStatus().equals(StatusEnum.FIRST_ORDER)) {
				ClientUtils.pickupOrderInProcess
						.setFirstOrderDiscount((float) (ClientUtils.pickupOrderInProcess.getTotalToPay() * 0.8));
			}
			switch (this.getCategory()) {
			case FOOD: {
				for (int j = 0; j < ClientUtils.pickupOrderInProcess.getFoodCategoryProducts().size(); j++) {
					if (ClientUtils.pickupOrderInProcess.getFoodCategoryProducts().get(j).getPro_name().equals(name)) {
						ClientUtils.pickupOrderInProcess.getFoodCategoryProducts().get(j)
								.setStock(ClientUtils.pickupOrderInProcess.getFoodCategoryProducts().get(j).getStock() +
										ClientUtils.pickupOrderInProcess.getProducts().get(i).getAmount());		
					}
				}
				break;
			}
			case DRINKS: {
				for (int j = 0; j < ClientUtils.pickupOrderInProcess.getDrinksCategoryProducts().size(); j++) {
					if (ClientUtils.pickupOrderInProcess.getDrinksCategoryProducts().get(j).getPro_name().equals(name)) {
						ClientUtils.pickupOrderInProcess.getDrinksCategoryProducts().get(j)
								.setStock(ClientUtils.pickupOrderInProcess.getDrinksCategoryProducts().get(j).getStock() +
										ClientUtils.pickupOrderInProcess.getProducts().get(i).getAmount());		
					}
				}
				break;
			}
			case SNACKS: {
				for (int j = 0; j < ClientUtils.pickupOrderInProcess.getSnackCategoryProducts().size(); j++) {
					if (ClientUtils.pickupOrderInProcess.getSnackCategoryProducts().get(j).getPro_name().equals(name)) {
						ClientUtils.pickupOrderInProcess.getSnackCategoryProducts().get(j)
								.setStock(ClientUtils.pickupOrderInProcess.getSnackCategoryProducts().get(j).getStock() +
										ClientUtils.pickupOrderInProcess.getProducts().get(i).getAmount());		
					}
				}
				break;
			}
			case SWEETS: {
				for (int j = 0; j < ClientUtils.pickupOrderInProcess.getSweetsCategoryProducts().size(); j++) {
					if (ClientUtils.pickupOrderInProcess.getSweetsCategoryProducts().get(j).getPro_name().equals(name)) {
						ClientUtils.pickupOrderInProcess.getSweetsCategoryProducts().get(j)
								.setStock(ClientUtils.pickupOrderInProcess.getSweetsCategoryProducts().get(j).getStock() +
										ClientUtils.pickupOrderInProcess.getProducts().get(i).getAmount());		
					}
				}
				break;
			}
			default:
				break;
			}
			ClientUtils.pickupOrderInProcess.getProducts().remove(i);
			((Node) event.getSource()).getScene().getWindow().hide();
			new CartPageController().start(new Stage());
		} else {
			for (i = 0; i < ClientUtils.deliveryOrderInProcess.getProducts().size(); i++) {
				if (ClientUtils.deliveryOrderInProcess.getProducts().get(i).getName().equals(productName.getText())) {
					break;
				}
			}
			ClientUtils.deliveryOrderInProcess.setTotalToPay(ClientUtils.deliveryOrderInProcess.getTotalToPay()
					- ClientUtils.deliveryOrderInProcess.getProducts().get(i).getFinalPrice());
			if (ClientUtils.currUser.getStatus().equals(StatusEnum.FIRST_ORDER)) {
				ClientUtils.deliveryOrderInProcess
						.setFirstOrderDiscount((float) (ClientUtils.deliveryOrderInProcess.getTotalToPay() * 0.8));
			}
			switch (this.getCategory()) {
			case FOOD: {
				for (int j = 0; j < ClientUtils.deliveryOrderInProcess.getFoodCategoryProducts().size(); j++) {
					if (ClientUtils.deliveryOrderInProcess.getFoodCategoryProducts().get(j).getPro_name().equals(name)) {
						ClientUtils.deliveryOrderInProcess.getFoodCategoryProducts().get(j)
								.setStock(ClientUtils.deliveryOrderInProcess.getFoodCategoryProducts().get(j).getStock() +
										ClientUtils.deliveryOrderInProcess.getProducts().get(i).getAmount());		
					}
				}
				break;
			}
			case DRINKS: {
				for (int j = 0; j < ClientUtils.deliveryOrderInProcess.getDrinksCategoryProducts().size(); j++) {
					if (ClientUtils.deliveryOrderInProcess.getDrinksCategoryProducts().get(j).getPro_name().equals(name)) {
						ClientUtils.deliveryOrderInProcess.getDrinksCategoryProducts().get(j)
								.setStock(ClientUtils.deliveryOrderInProcess.getDrinksCategoryProducts().get(j).getStock() +
										ClientUtils.deliveryOrderInProcess.getProducts().get(i).getAmount());		
					}
				}
				break;
			}
			case SNACKS: {
				for (int j = 0; j < ClientUtils.deliveryOrderInProcess.getSnackCategoryProducts().size(); j++) {
					if (ClientUtils.deliveryOrderInProcess.getSnackCategoryProducts().get(j).getPro_name().equals(name)) {
						ClientUtils.deliveryOrderInProcess.getSnackCategoryProducts().get(j)
								.setStock(ClientUtils.deliveryOrderInProcess.getSnackCategoryProducts().get(j).getStock() +
										ClientUtils.deliveryOrderInProcess.getProducts().get(i).getAmount());		
					}
				}
				break;
			}
			case SWEETS: {
				for (int j = 0; j < ClientUtils.deliveryOrderInProcess.getSweetsCategoryProducts().size(); j++) {
					if (ClientUtils.deliveryOrderInProcess.getSweetsCategoryProducts().get(j).getPro_name().equals(name)) {
						ClientUtils.deliveryOrderInProcess.getSweetsCategoryProducts().get(j)
								.setStock(ClientUtils.deliveryOrderInProcess.getSweetsCategoryProducts().get(j).getStock() +
										ClientUtils.deliveryOrderInProcess.getProducts().get(i).getAmount());		
					}
				}
				break;
			}
			default:
				break;
			}
			ClientUtils.deliveryOrderInProcess.getProducts().remove(i);
			((Node) event.getSource()).getScene().getWindow().hide();
			new CartPageController().start(new Stage());
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// cartGridPane = new CartPageController().getCartGridPane();
	}
	 /**
	  * sets the products parameters in the label	 
	 * 
	 * @param Product product
	 */
	public void setData(Product product) {
		name = product.getName();
		category = product.getCategory();
		productName.setText(product.getName());
		productsQuantity.setText("x" + String.valueOf(product.getAmount()));
		productPrice.setText(String.valueOf(product.getPrice()) + " ¤");
		finalPrice.setText(String.format("%.2f", product.getPrice() * product.getAmount()) + " ¤");

	}

	public CategoriesEnum getCategory() {
		return category;
	}

	public void setCategory(CategoriesEnum category) {
		this.category = category;
	}

}
