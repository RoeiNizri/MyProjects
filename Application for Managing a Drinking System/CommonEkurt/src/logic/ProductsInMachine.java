package logic;

import java.io.Serializable;

public class ProductsInMachine implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String productId, productName, quantity, price, quantityStatus;
	private int productLimit;

	public ProductsInMachine(String productId, String productName, String quantity, String price,
			String quantityStatus) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.quantity = quantity;
		this.price = price;
		this.quantityStatus = quantityStatus;
	}
	
	public ProductsInMachine(String string, String string2, String string3, int int1, String string4) {
		super();
		this.productId = string;
		this.productName = string2;
		this.quantity = string3;
		this.productLimit = int1;
		this.quantityStatus = string4;
	}

	public String getQuantityStatus() {
		return quantityStatus;
	}

	public void setQuantityStatus(String quantityStatus) {
		this.quantityStatus = quantityStatus;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public int getProductLimit() {
		return productLimit;
	}

	public void setProductLimit(int productLimit) {
		this.productLimit = productLimit;
	}

	@Override
	public String toString() {
		return "ProductsInMachine [productId=" + productId + ", productName=" + productName + ", quantity=" + quantity
				+ ", price=" + price + "]";
	}

}
