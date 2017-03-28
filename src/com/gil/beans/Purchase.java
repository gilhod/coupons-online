package com.gil.beans;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Purchase {

	

	public Purchase(long purchaseNumber, String couponTitle) {
		super();
		this.purchaseNumber = purchaseNumber;
		this.couponTitle = couponTitle;
	}

	private long purchaseNumber;
	private long purchaseDate;
	private long customerID;
	private long couponID;
	private String couponTitle;
	private String couponType;
	private double price;
	
	public Purchase() {
	}

	public Purchase(long id, long purchaseDate, long couponID, String couponTitle, String couponType,
			double price) {
		super();
		this.purchaseNumber = id;
		this.purchaseDate = purchaseDate;
		this.couponID = couponID;
		this.couponTitle = couponTitle;
		this.couponType = couponType;
		this.price = price;
	}

	public Purchase(long id, long purchaseDate, long customerID, long couponID, String couponTitle,
			String couponType, double price) {
		super();
		this.purchaseNumber = id;
		this.purchaseDate = purchaseDate;
		this.customerID = customerID;
		this.couponID = couponID;
		this.couponTitle = couponTitle;
		this.couponType = couponType;
		this.price = price;
	}

	public Purchase(long id, long purchaseDate, long couponID, String couponType, double price) {
		super();
		this.purchaseNumber = id;
		this.purchaseDate = purchaseDate;
		this.couponID = couponID;
		this.couponType = couponType;
		this.price = price;
	}


	public long getPurchaseNumber() {
		return purchaseNumber;
	}

	public void setPurchaseNumber(long purchaseNumber) {
		this.purchaseNumber = purchaseNumber;
	}

	public long getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(long purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public long getCustomerID() {
		return customerID;
	}

	public void setCustomerID(long customerID) {
		this.customerID = customerID;
	}

	public long getCouponID() {
		return couponID;
	}

	public void setCouponID(long couponID) {
		this.couponID = couponID;
	}

	public String getCouponTitle() {
		return couponTitle;
	}

	public void setCouponTitle(String couponTitle) {
		this.couponTitle = couponTitle;
	}

	public String getCouponType() {
		return couponType;
	}

	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "Purchase [purchaseNumber=" + purchaseNumber + ", purchaseDate=" + purchaseDate + ", customerID="
				+ customerID + ", couponID=" + couponID + ", couponTitle=" + couponTitle + ", couponType=" + couponType
				+ ", price=" + price + "]";
	}

}
