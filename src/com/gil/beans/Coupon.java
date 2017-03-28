package com.gil.beans;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Coupon {

	

	public Coupon(long id, String title) {
		super();
		this.id = id;
		this.title = title;
	}

	private long id;
	private String title;
	private long startDate;
	private long endDate;
	private int amount;
	private String couponType;
	private String message;
	private double price;
	private String image;
	private long companyID;
	
	public Coupon() {
	}

	public Coupon(String title, long startDate, long endDate, int amount, String couponType, String message,
			double price, String image, long companyID) {
		this.title = title;
		this.startDate = startDate;
		this.endDate = endDate;
		this.amount = amount;
		this.couponType = couponType;
		this.message = message;
		this.price = price;
		this.image = image;
		this.companyID = companyID;
	}

	public Coupon(long id, String title, long startDate, long endDate, int amount, String couponType,
			String message, double price, String image, long companyID) {
		this.id = id;
		this.title = title;
		this.startDate = startDate;
		this.endDate = endDate;
		this.amount = amount;
		this.couponType = couponType;
		this.message = message;
		this.price = price;
		this.image = image;
		this.companyID = companyID;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getStartDate() {
		return startDate;
	}

	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}

	public long getEndDate() {
		return endDate;
	}

	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getCouponType() {
		return couponType;
	}

	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public long getCompanyID() {
		return companyID;
	}

	public void setCompanyID(long companyID) {
		this.companyID = companyID;
	}

	@Override
	public String toString() {
		return "\nID: " + this.id + "\nTitle: " + this.title + "\nStart Date: " + this.startDate + "\nEnd Date: "
				+ this.endDate + "\nAmount: " + this.amount + "\nType: " + this.couponType + "\nMessage: "
				+ this.message + "\nPrice: " + this.price + "\nImage: " + this.image + "\nCompany ID: "
				+ this.companyID;
	}

}
