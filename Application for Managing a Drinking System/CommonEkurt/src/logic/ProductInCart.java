package logic;

public class ProductInCart {
	
	private String productName;
	private float productPrice;
	private int productsQuantity;
	private float finalPrice;
	
	public ProductInCart(String productName, float productPrice, int productsQuantity, float finalPrice) {
		super();
		this.productName = productName;
		this.productPrice = productPrice;
		this.productsQuantity = productsQuantity;
		this.finalPrice = finalPrice;
	}
	
	@Override
	public String toString() {
		return "ProductInCart [productName=" + productName + ", productPrice=" + productPrice + ", productsQuantity="
				+ productsQuantity + ", finalPrice=" + finalPrice + "]";
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public float getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(float productPrice) {
		this.productPrice = productPrice;
	}
	public int getProductsQuantity() {
		return productsQuantity;
	}
	public void setProductsQuantity(int productsQuantity) {
		this.productsQuantity = productsQuantity;
	}
	public float getFinalPrice() {
		return finalPrice;
	}
	public void setFinalPrice(float finalPrice) {
		this.finalPrice = finalPrice;
	}

}
