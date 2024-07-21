package client_gui;

import java.net.URL;
import java.util.ResourceBundle;
import clientUtil.ClientUtils;
import enums.CategoriesEnum;
import enums.RoleEnum;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import logic.Product;
import logic.ProductInGrid;

/**
The class ProductInGridController is a controller class for the ProductInGrid.fxml file in a JavaFX application. 
It implements the Initializable interface which is used to
 indicate that the class can be initialized by the FXML loader.
 This class sets up all the need paramaters for the productgrid

* 
* @param primaryStage the primary stage of the application
*/
public class ProductInGridController implements Initializable {

    @FXML
    private Button addToCart;

    @FXML
    private Button decreaseAmount = new Button();

    @FXML
    private Button increaseAmount = new Button();

    @FXML
    private TextField productAmount = new TextField();

    @FXML
    private ImageView productImage;

    @FXML
    private Label productName;
    
    @FXML
    private Label saleLabel = new Label();

	@FXML
    private Label productPrice;
    private int currentStock;
    private float price;
    private int desiredAmount;
    private CategoriesEnum category;
    private ProductInGrid thisProduct;
    
;

    public int getCurrentStock() {
		return currentStock;
	}

	public void setCurrentStock(int stock) {
		this.currentStock = stock;
	}

	public CategoriesEnum getCategory() {
		return category;
	}

	public void setCategory(CategoriesEnum category) {
		this.category = category;
	}
	/**
	The addProductToCart(ActionEvent event) method is responsible for adding the product to the cart, it gets the name, price, and desired amount from the label and text field respectively.
	 It then creates a new instance of the Product class with
	  these details and adds it to the cart..
	 This class sets up all the need paramaters for the productgrid

	* 
	* @param event the MouseEvent that triggers the handle
	*/
	@FXML
    void addProductToCart(ActionEvent event) {
		if(ClientUtils.localOrderInProcess != null) {
			ClientUtils.localOrderInProcess.addProduct(new Product(productName.getText(),
					price, desiredAmount,thisProduct.getCategory()));
			currentStock -= desiredAmount;
			int index=0;
			switch (thisProduct.getCategory()) {
			case FOOD: {
				index = ClientUtils.localOrderInProcess.getFoodCategoryProducts().indexOf(thisProduct);
				ClientUtils.localOrderInProcess.getFoodCategoryProducts().get(index).setStock(currentStock);
				break;
			}
			case DRINKS: {
				index = ClientUtils.localOrderInProcess.getDrinksCategoryProducts().indexOf(thisProduct);
				ClientUtils.localOrderInProcess.getDrinksCategoryProducts().get(index).setStock(currentStock);
				break;
			}
			case SNACKS: {
				index = ClientUtils.localOrderInProcess.getSnackCategoryProducts().indexOf(thisProduct);
				ClientUtils.localOrderInProcess.getSnackCategoryProducts().get(index).setStock(currentStock);
				break;
			}
			case SWEETS: {
				index = ClientUtils.localOrderInProcess.getSweetsCategoryProducts().indexOf(thisProduct);
				ClientUtils.localOrderInProcess.getSweetsCategoryProducts().get(index).setStock(currentStock);
				break;
			}
			default:
				break;
			}
			productAmount.setText("0");
			
			if(productAmount.getText().equals("0"))
				decreaseAmount.setDisable(true);
		}else if(ClientUtils.pickupOrderInProcess != null) {
			ClientUtils.pickupOrderInProcess.addProduct(new Product(productName.getText(),
					price, desiredAmount, thisProduct.getCategory()));
			currentStock -= desiredAmount;
			int index=0;
			switch (thisProduct.getCategory()) {
			case FOOD: {
				index = ClientUtils.pickupOrderInProcess.getFoodCategoryProducts().indexOf(thisProduct);
				ClientUtils.pickupOrderInProcess.getFoodCategoryProducts().get(index).setStock(currentStock);
				break;
			}
			case DRINKS: {
				index = ClientUtils.pickupOrderInProcess.getDrinksCategoryProducts().indexOf(thisProduct);
				ClientUtils.pickupOrderInProcess.getDrinksCategoryProducts().get(index).setStock(currentStock);
				break;
			}
			case SNACKS: {
				index = ClientUtils.pickupOrderInProcess.getSnackCategoryProducts().indexOf(thisProduct);
				ClientUtils.pickupOrderInProcess.getSnackCategoryProducts().get(index).setStock(currentStock);
				break;
			}
			case SWEETS: {
				index = ClientUtils.pickupOrderInProcess.getSweetsCategoryProducts().indexOf(thisProduct);
				ClientUtils.pickupOrderInProcess.getSweetsCategoryProducts().get(index).setStock(currentStock);
				break;
			}
			default:
				break;
			}
			productAmount.setText("0");
			if(productAmount.getText().equals("0"))
				decreaseAmount.setDisable(true);
		}else {
			ClientUtils.deliveryOrderInProcess.addProduct(new Product(productName.getText(),
					price, desiredAmount, thisProduct.getCategory()));
			currentStock -= desiredAmount;
			int index=0;
			switch (thisProduct.getCategory()) {
			case FOOD: {
				index = ClientUtils.deliveryOrderInProcess.getFoodCategoryProducts().indexOf(thisProduct);
				ClientUtils.deliveryOrderInProcess.getFoodCategoryProducts().get(index).setStock(currentStock);
				break;
			}
			case DRINKS: {
				index = ClientUtils.deliveryOrderInProcess.getDrinksCategoryProducts().indexOf(thisProduct);
				ClientUtils.deliveryOrderInProcess.getDrinksCategoryProducts().get(index).setStock(currentStock);
				break;
			}
			case SNACKS: {
				index = ClientUtils.deliveryOrderInProcess.getSnackCategoryProducts().indexOf(thisProduct);
				ClientUtils.deliveryOrderInProcess.getSnackCategoryProducts().get(index).setStock(currentStock);
				break;
			}
			case SWEETS: {
				index = ClientUtils.deliveryOrderInProcess.getSweetsCategoryProducts().indexOf(thisProduct);
				ClientUtils.deliveryOrderInProcess.getSweetsCategoryProducts().get(index).setStock(currentStock);
				break;
			}
			default:
				break;
			}
			productAmount.setText("0");
			if(productAmount.getText().equals("0"))
				decreaseAmount.setDisable(true);
		}
    }
	/**
	The decreaseAmountByOne(ActionEvent event) method decreases the desired amount of product by 1.
	 It also checks if the desired amount is 0 and disables the decrease button if it is.

	* 
	* @param event the MouseEvent that triggers the handle
	*/
    @FXML
    void decreaseAmountByOne(ActionEvent event) {
    	desiredAmount = Integer.parseInt(productAmount.getText())- 1;
    	if(desiredAmount == 0) {
    		decreaseAmount.setDisable(true);
    		productAmount.setText(String.valueOf(desiredAmount));
    	}else {
    		if(Integer.parseInt(productAmount.getText()) == currentStock) {
    			increaseAmount.setDisable(false);
    		}
    		productAmount.setText(String.valueOf(desiredAmount));
    	}
		if(productAmount.getText().equals("0")) {
			decreaseAmount.setDisable(true);
		}
    }
    /**
   	The increaseAmountByOne(ActionEvent event) method increases the desired amount of product by 1.
   	 It also checks if the desired amount is more than the current stock and disables the increase button if it is.

   	* 
   	* @param event the MouseEvent that triggers the handle
   	*/
    @FXML
    void increaseAmountByOne(ActionEvent event) {
    	desiredAmount = Integer.parseInt(productAmount.getText())+1;
    	if(desiredAmount > currentStock) {
    		increaseAmount.setDisable(true);
    	}else {
    		productAmount.setText(String.valueOf(desiredAmount));
    		decreaseAmount.setDisable(false);
    		if(desiredAmount == currentStock)
        		increaseAmount.setDisable(true);
    	}
		if(productAmount.getText().equals("0")) {
			decreaseAmount.setDisable(true);
		}
    	//check 
    }
    /**
  	The setData(ProductInGrid product) method is used to set the data for the product in the grid.
  	 It takes in a ProductInGrid object as a parameter and sets the product's image, name, 
  	and price to the corresponding elements on the screen.
  	 It also sets the current stock and the desired amount to 0.

  It sets the image of the product by creating a new Image
   object from the image file path that is passed in the ProductInGrid object and sets it to the productImage ImageView.

  It sets the product name to the productName label 
  and sets the product amount to 0 in the productAmount TextField.

  It sets the price variable to the price of the 
  product passed in the ProductInGrid object and sets the productPrice label to the price of the product.

  It sets the currentStock variable to the stock of the product passed in the ProductInGrid object and disables the decrease button.

  It checks if the user is a subscriber and if 
  the product is on sale, it sets the saleLabel to visible and sets the text to the offer name.

  It also sets the original price to
   strikethrough and sets the graphic of the product price label to the original price.

  It then sets the price of the product 
  after discount to the productPrice label if the product is on sale.

  	* 
  	* @param Product product
  	*/
	public void setData(ProductInGrid product) {
		thisProduct = product;
		Image image = new Image(product.getImage());
		System.out.println(product.getImage().toString());
		//Image image = new Image(getClass().getResourceAsStream(product.getImage()));
		productImage.setImage(image);
		productName.setText(product.getPro_name());
		productAmount.setText("0");
		price = product.getPrice();
		productPrice.setText(String.valueOf(product.getPrice()) + " ¤");
		decreaseAmount.setDisable(true);
		setCurrentStock(product.getStock());
		if (ClientUtils.currUser.getRole().equals(RoleEnum.SUBSCRIBER)) {
			if (product.isIs_in_sale()) {		
				saleLabel.setVisible(true);
				saleLabel.setText(product.getOfferName());
				Text originalPriceText = new Text(String.valueOf(product.getPrice()) + " ¤");
				originalPriceText.setStrikethrough(true);
				originalPriceText.setFont(new Font("Calibri", 17));
				originalPriceText.setFill(Color.RED);
				originalPriceText.setStyle("-fx-font-weight: bold;");
				productPrice.setGraphic(originalPriceText);
				price = product.getPrice_after_discount();
				productPrice.setText(String.valueOf(product.getPrice_after_discount()) + " ¤");

			} else {
				saleLabel.setVisible(false);
			}
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	saleLabel.setVisible(false);
	}

}
