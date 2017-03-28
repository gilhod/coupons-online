package com.gil.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum CouponType {
	
	RESTAURANTS("Restaurants"),
	ELECTRICTY("Electricity"),
	FOOD("Food"),
	HEALTH("Health"),
	SPORTS("Sports"),
	CAMPING("Camping"),
	TRAVELLING("Travelling");
	
	private static final Map<String,CouponType> couponTypeLookup = new HashMap<String,CouponType>();
	
	static {
		for(CouponType couponType : EnumSet.allOf(CouponType.class))
			couponTypeLookup.put(couponType.getName(), couponType);
	}
	
	private String name;
		
	CouponType(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}	
	
	public static CouponType getCouponType(String couponTypeName){
		return couponTypeLookup.get(couponTypeName);
	}
}
