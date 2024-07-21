package logic;

import java.io.Serializable;

import enums.RegionEnum;


@SuppressWarnings("serial")
public class Offer implements Serializable {


	private String productID,productName,productDiscount,productPrice,IsActive;
    private RegionEnum region;



	public Offer(String productID, String productName, String productPrice, String productDiscount,String isActive) {
		super();
		this.productID = productID;
		this.productName = productName;
		this.productPrice = productPrice;
		this.productDiscount = productDiscount;
		this.IsActive = isActive;
	}
	public String getIsActive() {
		return IsActive;
	}

	public void setIsActive(String isActive) {
		this.IsActive = isActive;
	}
	public String getProductID() {
		return productID;
	}

	public void setProductID(String productID) {
		this.productID = productID;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductDiscount() {
		return productDiscount;
	}

	public void setProductDiscount(String productDiscount) {
		this.productDiscount = productDiscount;
	}

	public String getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
	}

	public RegionEnum getRegion() {
		return region;
	}
	public void setRegion(RegionEnum region) {
		this.region = region;
	}
	@Override
	public String toString() {
		return "Offer [productID=" + productID + ", productName=" + productName + ", productDiscount=" + productDiscount
				+ ", productPrice=" + productPrice + "]";
	}
	

}
