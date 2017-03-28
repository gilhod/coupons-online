package com.gil.enums;

public enum ErrorType {
	
	GENERAL_ERROR(1),
	COMPANY_NAME_ALREADY_EXISTS(2),
	CUSTOMER_NAME_ALREADY_EXISTS(3),
	COUPON_NAME_ALREADY_EXISTS(4),
	COMPANY_DOES_NOT_EXIST(5),
	CUSTOMER_DOES_NOT_EXIST(6),
	COUPON_DOES_NOT_EXIST(7),
	INVALID_EMAIL(8),
	INVALID_PASSWORD(9),
	COUPON_OUT_OF_STOCK(10),
	COUPON_EXPIRED(11),
	COUPON_HAVE_BEEN_PURCHASED_BEFORE(12),
	INVALID_LOGIN_DETAILS(13),
	UNSUPPORTED_FILE_FORMAT(14);
	
	private int internalErrorCode;
	ErrorType(int internalErrorCode){
		this.internalErrorCode = internalErrorCode;
	}
	
	public int getInternalErrorCode() {
		return internalErrorCode;
	}
	

}
