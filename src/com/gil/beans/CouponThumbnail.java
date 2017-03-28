package com.gil.beans;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CouponThumbnail {
	
	private long id;
	private String title;
	private long startDate;
	private long endDate;
	private double price;
	private String image;
	private String message;
	private String companyName;
	
	
	
	public CouponThumbnail() {
		super();
	}



	public CouponThumbnail(long id, String title, long startDate, long endDate, double price, String image,
			String message, String companyName) {
		super();
		this.id = id;
		this.title = title;
		this.startDate = startDate;
		this.endDate = endDate;
		this.price = price;
		this.image = image;
		this.message = message;
		this.companyName = companyName;
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



	public String getMessage() {
		return message;
	}



	public void setMessage(String message) {
		this.message = message;
	}



	public String getCompanyName() {
		return companyName;
	}



	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	
	
	
}
