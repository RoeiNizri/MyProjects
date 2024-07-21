package logic;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import enums.OrderStatusEnum;
import enums.RegionEnum;
/**
 * The DeliveryOrder class is a Java class that represents a delivery order. It
 * contains various fields such as order ID, client ID, order date, supply date,
 * price, and order status. It also contains fields for the region, street name,
 * city, and house number where the order is to be delivered. Additionally, it
 * contains lists of products for display, and products separated by category
 * (food, snacks, sweets, and drinks). The class also has methods for getting
 * and setting the values of these fields, as well as a constructor that
 * initializes the fields.
 * 
 * @author Ilanit & Noy
 *
 */
public class DeliveryOrder implements Serializable {
	private String OrderId;
    private String ClientId; //need to be user
    private User client;
	private Timestamp OrderDate;
    private Timestamp SuppDate;
	private ArrayList<Product> products;
    private float Price;
    private OrderStatusEnum status;
    private RegionEnum region;
    private String streetName;
    private String city;
    private int houseNum;
	private List<ProductInGrid> ProductsForDisplay;
	private List<ProductInGrid> foodCategoryProducts;
	private List<ProductInGrid> snackCategoryProducts;
	private List<ProductInGrid> sweetsCategoryProducts;
	private List<ProductInGrid> drinksCategoryProducts;
	private float totalToPay = 0;
	private float firstOrderDiscount = 0;
	private Timestamp estimatedDelivery;
	private int productquantity = 0;


	public DeliveryOrder(String orderId2, String clientId2, Timestamp orderDate2, Timestamp supplyDate, float price2, OrderStatusEnum suppMethod) {
		this.OrderId=orderId2;
		this.ClientId=clientId2;
		this.OrderDate=orderDate2;
		this.SuppDate=supplyDate;
		this.Price=price2;
		this.status = suppMethod;
	}
	public DeliveryOrder(User client, Timestamp OrderDate, ArrayList<Product> products, OrderStatusEnum status) {
		this.client = client;
		this.OrderDate = OrderDate;
		this.products = products;
		this.status = status;
		ProductsForDisplay = new ArrayList<>();
		foodCategoryProducts = new ArrayList<>();
		snackCategoryProducts = new ArrayList<>();
		sweetsCategoryProducts = new ArrayList<>();
		drinksCategoryProducts = new ArrayList<>();
	}
	
    public User getClient() {
		return client;
	}

	public void setClient(User client) {
		this.client = client;
	}
	public String getOrderId() {
		return OrderId;
	}
	public void setOrderId(String orderId) {
		OrderId = orderId;
	}
	public String getClientId() {
		return ClientId;
	}
	public void setClientId(String clientId) {
		ClientId = clientId;
	}
	public Timestamp getOrderDate() {
		return OrderDate;
	}
	public void setOrderDate(Timestamp orderDate) {
		OrderDate = orderDate;
	}
	public Timestamp getSuppDate() {
		return SuppDate;
	}
	public void setSuppDate(Timestamp suppDate) {
		SuppDate = suppDate;
	}
	public float getPrice() {
		return Price;
	}
	public void setPrice(float price) {
		Price = price;
	}
	public OrderStatusEnum getStatus() {
		return status;
	}
	public void setStatus(OrderStatusEnum status) {
		this.status = status;
	}
	public RegionEnum getRegion() {
		return region;
	}
	public void setRegion(RegionEnum region) {
		this.region = region;
	}
    public String getStreetName() {
		return streetName;
	}
	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public int getHouseNum() {
		return houseNum;
	}
	public void setHouseNum(int houseNum) {
		this.houseNum = houseNum;
	}
	public ArrayList<Product> getProducts() {
		return products;
	}
	public void setProducts(ArrayList<Product> products) {
		this.products = products;
	}
	public float getTotalToPay() {
		return totalToPay;
	}
	public void setTotalToPay(float totalToPay) {
		this.totalToPay = totalToPay;
	}
	/**
	 * Gets the list of products for display.
	 * 
	 * @return the list of products for display
	 */
	public List<ProductInGrid> getProductsForDisplay() {
		return ProductsForDisplay;
	}
	/**
	 * Sets the list of products for display.
	 * 
	 * @param productsForDisplay the list of products for display
	 */
	public void setProductsForDisplay(List<ProductInGrid> productsForDisplay) {
		ProductsForDisplay = productsForDisplay;
	}
	/**
	 * Gets the list of food category products.
	 * 
	 * @return the list of food category products
	 */
	public List<ProductInGrid> getFoodCategoryProducts() {
		return foodCategoryProducts;
	}
	/**
	 * Sets the list of food category products.
	 * 
	 * @param foodCategoryProducts the list of food category products
	 */
	public void setFoodCategoryProducts(List<ProductInGrid> foodCategoryProducts) {
		this.foodCategoryProducts = foodCategoryProducts;
	}
	/**
	 * Gets the list of snack category products.
	 * 
	 * @return the list of snack category products
	 */
	public List<ProductInGrid> getSnackCategoryProducts() {
		return snackCategoryProducts;
	}
	/**
	 * Sets the list of snack category products.
	 * 
	 * @param snackCategoryProducts the list of snack category products
	 */
	public void setSnackCategoryProducts(List<ProductInGrid> snackCategoryProducts) {
		this.snackCategoryProducts = snackCategoryProducts;
	}
	/**
	 * Gets the list of sweets category products.
	 * 
	 * @return the list of sweets category products
	 */
	public List<ProductInGrid> getSweetsCategoryProducts() {
		return sweetsCategoryProducts;
	}
	/**
	 * Sets the list of sweets category products.
	 * 
	 * @param sweetsCategoryProducts the list of sweets category products
	 */
	public void setSweetsCategoryProducts(List<ProductInGrid> sweetsCategoryProducts) {
		this.sweetsCategoryProducts = sweetsCategoryProducts;
	}
	/**
	 * Gets the list of drinks category products.
	 * 
	 * @return the list of drinks category products
	 */
	public List<ProductInGrid> getDrinksCategoryProducts() {
		return drinksCategoryProducts;
	}
	/**
	 * Sets the list of drinks category products.
	 * 
	 * @param drinksCategoryProducts the list of drinks category products
	 */
	public void setDrinksCategoryProducts(List<ProductInGrid> drinksCategoryProducts) {
		this.drinksCategoryProducts = drinksCategoryProducts;
	}
	/**
	 * Gets the estimated delivery time.
	 * 
	 * @return the estimated delivery time
	 */
	public Timestamp getEstimatedDelivery() {
		return estimatedDelivery;
	}
	/**
	 * Sets the estimated delivery time.
	 * 
	 * @param estimatedDelivery the estimated delivery time
	 */
	public void setEstimatedDelivery(Timestamp estimatedDelivery) {
		this.estimatedDelivery = estimatedDelivery;
	}
	public float getFirstOrderDiscount() {
		return firstOrderDiscount;
	}

	public void setFirstOrderDiscount(float totalAfterAllDiscounts) {
		this.firstOrderDiscount = totalAfterAllDiscounts;
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
				totalToPay  += (product.getPrice() * product.getAmount());
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
