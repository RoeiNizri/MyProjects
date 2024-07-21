package logic;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import enums.OrderStatusEnum;
import enums.SupplyMethodEnum;

public class LocalOrder implements Serializable {
	private User client;
	private String machine_code;
	private String machineName;
	private Timestamp date;
	private ArrayList<Product> products;
	private OrderStatusEnum status;
	private float totalToPay = 0;
	private float firstOrderDiscount = 0;
	private int productquantity = 0;
	private List<ProductInGrid> ProductsForDisplay;
	private List<ProductInGrid> foodCategoryProducts;
	private List<ProductInGrid> snackCategoryProducts;
	private List<ProductInGrid> sweetsCategoryProducts;
	private List<ProductInGrid> drinksCategoryProducts;
	
	public LocalOrder(User client, String machineName, Timestamp date, ArrayList<Product> products,
			OrderStatusEnum status) {
		this.client = client;
		this.machineName = machineName;
		this.date = date;
		this.products = products;
		this.status = status;
		ProductsForDisplay = new ArrayList<>();
		foodCategoryProducts = new ArrayList<>();
		snackCategoryProducts = new ArrayList<>();
		sweetsCategoryProducts = new ArrayList<>();
		drinksCategoryProducts = new ArrayList<>();
	}
	
	public List<ProductInGrid> getProductsForDisplay() {
		return ProductsForDisplay;
	}
	public void setProductsForDisplay(List<ProductInGrid> productsForDisplay) {
		ProductsForDisplay = productsForDisplay;
	}
	public void setFoodCategoryProducts(List<ProductInGrid> foodCategoryProducts) {
		this.foodCategoryProducts = foodCategoryProducts;
	}
	public List<ProductInGrid> getFoodCategoryProducts() {
		return foodCategoryProducts;
	}
	public void setSnackCategoryProducts(List<ProductInGrid> snackCategoryProducts) {
		this.snackCategoryProducts = snackCategoryProducts;
	}
	public void setSweetsCategoryProducts(List<ProductInGrid> sweetsCategoryProducts) {
		this.sweetsCategoryProducts = sweetsCategoryProducts;
	}
	public void setDrinksCategoryProducts(List<ProductInGrid> drinksCategoryProducts) {
		this.drinksCategoryProducts = drinksCategoryProducts;
	}
	public List<ProductInGrid> getSnackCategoryProducts() {
		return snackCategoryProducts;
	}
	public List<ProductInGrid> getSweetsCategoryProducts() {
		return sweetsCategoryProducts;
	}
	public List<ProductInGrid> getDrinksCategoryProducts() {
		return drinksCategoryProducts;
	}

	public int getProductquantity() {
		return productquantity;
	}
	public void setProductquantity(int productquantity) {
		this.productquantity = productquantity;
	}
	public void setTotalToPay(float totalToPay) {
		this.totalToPay = totalToPay;
	}
	public float getTotalToPay() {
		return totalToPay;
	}
	public User getClient() {
		return client;
	}
	public void setClient(User client) {
		this.client = client;
	}
	public String getMachine_code() {
		return machine_code;
	}
	public void setMachine_code(String machine_code) {
		this.machine_code = machine_code;
	}
	public String getMachineName() {
		return machineName;
	}
	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}
	public Timestamp getDate() {
		return date;
	}
	public void setDate(Timestamp date) {
		this.date = date;
	}
	public ArrayList<Product> getProducts() {
		return products;
	}
	public void setProducts(ArrayList<Product> products) {
		this.products = products;
	}
	public OrderStatusEnum getStatus() {
		return status;
	}
	public void setStatus(OrderStatusEnum status) {
		this.status = status;
	}


	public float getFirstOrderDiscount() {
		return firstOrderDiscount;
	}

	public void setFirstOrderDiscount(float totalAfterAllDiscounts) {
		this.firstOrderDiscount = totalAfterAllDiscounts;
	}

	@Override
	public String toString() {
		return "LocalOrder [client=" + client + ", machineName=" + machineName + ", date=" + date + ", products="
				+ products + ", status=" + status + "]";
	}
	/**
	 * Adds a product to the shopping cart.
	 * 
	 * @param product the product to be added
	 */
	public void addProduct(Product product) {
		int flag = 0;
		for (Product index : products) {
			if (index.getName().equals(product.getName())) {
				index.setAmount(index.getAmount() + product.getAmount());
				totalToPay += (product.getPrice() * product.getAmount());
				flag = 1;
			}
		}
		if (flag == 0) {
			products.add(product);
			totalToPay += (product.getPrice() * product.getAmount());
		}
		
		productquantity += product.getAmount();
		setFirstOrderDiscount((float) (totalToPay * 0.8));
		
	}
	
	
}
