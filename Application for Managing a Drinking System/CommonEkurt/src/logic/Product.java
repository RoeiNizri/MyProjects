package logic;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import enums.CategoriesEnum;
import enums.RegionEnum;

/**
 * Represents a product with a name, price, and amount.
 */
public class Product implements Serializable{
	private String name;
	private float price;
	private String product_code;
	private int amount;
	private float finalPrice;
	private CategoriesEnum category;

	public float getFinalPrice() {
		return finalPrice;
	}
	/**
	 * Constructs a new Product object with the given name, price, and amount.
	 * 
	 * @param name   the name of the product
	 * @param price  the price of the product
	 * @param amount the amount of the product
	 */
	public Product(String name, float price, int amount, CategoriesEnum category) {
		this.name = name;
		this.price = price;
		this.amount = amount;
		this.category = category;
		finalPrice = this.price * this.amount;
		
	}
	public String getProduct_code() {
		return product_code;
	}

	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public CategoriesEnum getCategory() {
		return category;
	}

	public void setCategory(CategoriesEnum category) {
		this.category = category;
	}
	@Override
	public String toString() {
		return "Product [name=" + name + ", price=" + price + ", amount=" + amount + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		return Objects.equals(name, other.name);
	}
}
