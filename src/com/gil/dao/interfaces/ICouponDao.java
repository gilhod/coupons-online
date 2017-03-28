package com.gil.dao.interfaces;

import java.util.List;

import com.gil.beans.Coupon;
import com.gil.exceptions.ApplicationException;

public interface ICouponDao {
	
	public long createCoupon(Coupon coupon) throws ApplicationException;
	public boolean isCouponNameExists(String couponName) throws ApplicationException;
	public boolean isCouponExpired(long couponID) throws ApplicationException;
	public boolean isCouponOutOfStock(long couponID) throws ApplicationException;
	public void deleteCoupon(long couponID) throws ApplicationException;
	public void deleteCouponsByCompany(long companyID) throws ApplicationException;
	public Coupon getCoupon(long couponID) throws ApplicationException;
	public List<Coupon> getAllCoupons() throws ApplicationException;
	public List<Coupon> getAllCouponsOfCompany(long companyID) throws ApplicationException;
	public List<Coupon> getAllCouponsOfCompanyFilteredByCouponType(long companyID, String couponType) throws ApplicationException;
	public List<Coupon> getAllCouponsOfCompanyFilteredByMaxPrice(long companyID, double price) throws ApplicationException;
	public List<Coupon> getAllCouponsOfCompanyFilteredByExpirationDate(long companyID, long endDate) throws ApplicationException;
	public void updateCouponPrice(long couponID, Double newPrice) throws ApplicationException;
	public void updateCouponEndDate(long couponID, long newEndDate) throws ApplicationException;
	public void deleteExpiredCoupons() throws ApplicationException;
	
		
	
	
	

}
