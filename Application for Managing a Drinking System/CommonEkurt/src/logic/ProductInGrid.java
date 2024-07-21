package logic;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import enums.CategoriesEnum;
import enums.RegionEnum;

public class ProductInGrid implements Serializable {
	
	private String pro_code;
	private String pro_name;
	private float price;
	private CategoriesEnum category;
	private String image;
	private int stock;
	private float price_after_discount;
	private boolean is_in_sale;
	private String offerName;
	
	
	public ProductInGrid(String pro_code, String pro_name, float price, CategoriesEnum category, String image) {
		this.pro_code = pro_code;
		this.pro_name = pro_name;
		this.price = price;
		this.category = category;
		this.image = image;
		price_after_discount = this.price;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}

	public String getPro_code() {
		return pro_code;
	}
	public void setPro_code(String pro_code) {
		this.pro_code = pro_code;
	}
	public String getPro_name() {
		return pro_name;
	}
	public void setPro_name(String pro_name) {
		this.pro_name = pro_name;
	}
	/**
	 * Sets the price after discount for a product.
	 * 
	 * @param name the name of the offer
	 */
	public void setPrice_after_discount(String name) {
		if (isIs_in_sale()) {
			float num = 0;
			float present = 0;
			if (name.contains("%")) {
				num = Float.valueOf(offerName.replace("%", ""));
				present = (1 - (num / 100));
				price_after_discount = price * present;
			}
		}
	}
	public float getPrice_after_discount() {
        return price_after_discount;
	}

	public float getPrice() {
		return price;
	}
	public String getOfferName() {
		return offerName;
	}

	public void setOfferName(String offerName) {
		this.offerName = offerName;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public CategoriesEnum getCategory() {
		return category;
	}
	public void setCategory(CategoriesEnum category) {
		this.category = category;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	
    public boolean isIs_in_sale() {
		return is_in_sale;
	}
	public void setIs_in_sale(boolean is_in_sale) {
		this.is_in_sale = is_in_sale;
	}
	@Override
	public String toString() {
		return "ProductInGrid [pro_code=" + pro_code + ", pro_name=" + pro_name + ", price=" + price + ", category="
				+ category + ", image=" + image + "]";
	}


	/**
	 * Creates a ProductInGrid object from a ResultSet of query results.
	 * 
	 * @param rs         the ResultSet to create the product from
	 * @param stock      the stock of the product
	 * @param is_in_sale whether the product is in sale or not
	 * @param offerName  the name of the offer for the product
	 * @return a ProductInGrid object
	 */
	public static ProductInGrid getProductFromResultSet(ResultSet rs, int stock, boolean is_in_sale, String offerName){
        //ProductInGrid> products = new ArrayList<>();
	
        try{
            while(rs.next()){
            	ProductInGrid product = new ProductInGrid(rs.getString("pro_code"),
                						rs.getString("pro_name"),
                						rs.getFloat("price"),
                						CategoriesEnum.valueOf(rs.getString("category")),
                						rs.getString("image"));
            	product.setStock(stock);
            	product.setIs_in_sale(is_in_sale);
            	if(offerName != null) {
                	product.setOfferName(offerName);
                	product.setPrice_after_discount(offerName);
            	}
            	return product;

            	}
            } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
