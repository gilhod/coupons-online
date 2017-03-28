package com.gil.logic;

import java.io.InputStream;
import java.util.List;

import com.gil.beans.Coupon;
import com.gil.beans.CouponThumbnail;
import com.gil.beans.Purchase;
import com.gil.dao.CouponDao;
import com.gil.enums.ErrorType;
import com.gil.exceptions.ApplicationException;
import com.gil.utils.SystemLogger;
import com.gil.utils.FilesUtil;
import com.sun.jersey.core.header.FormDataContentDisposition;

public class CouponLogic {

	// Dao objects declaration
	private CouponDao couponDao;

	public CouponLogic() {
		couponDao = new CouponDao();
	}

	// This method creates new coupon
	public long createCoupon(Coupon coupon) throws ApplicationException {

		if (couponDao.isCouponNameExists(coupon.getTitle())) {
			throw new ApplicationException(ErrorType.COUPON_NAME_ALREADY_EXISTS, "Coupon title already exist");
		}
		long couponId = couponDao.createCoupon(coupon);
		return couponId;
	}

	// This method deletes a coupon from the records
	public void deleteCoupon(long couponID, long companyID) throws ApplicationException {
		
		//check the company accessing their coupons only   
				Coupon coupon = couponDao.getCoupon(couponID);
				if (coupon == null || coupon.getCompanyID()!=companyID) {
					if(coupon.getCompanyID()!=companyID){
		        		SystemLogger.logger.warn("Forbbiden access. deleteCoupon. Company ID: " + companyID + "; request coupon ID: " + couponID);
		        		throw new ApplicationException(ErrorType.COUPON_DOES_NOT_EXIST, "Selected coupon does not exist");
					}
				}		
				
		//delete purchased coupons from purchases records
		couponDao.deletePurchasesByCoupon(couponID);
		
		//delete the coupon from the coupons database
		couponDao.deleteCoupon(couponID);
		
		//delete the image file from the images folder
		String imageUrl = coupon.getImage();
		String imageName = imageUrl.substring(imageUrl.lastIndexOf("/")+1);
		if(!imageName.equals("default.png") && imageName!=null){
			String imageLocation = "C://Coupons//Images//"+imageName;
			FilesUtil.deleteFile(imageLocation);
		}
		
		
	}

	// This following methods updates the values of some fields in a certain
	// coupon

	public void updateCouponPrice(long couponID, long companyID, double newPrice) throws ApplicationException {
		
		//check the company accessing their coupons only   
				Coupon coupon = couponDao.getCoupon(couponID);
				if (coupon == null || coupon.getCompanyID()!=companyID) {
					if(coupon.getCompanyID()!=companyID){
		        		SystemLogger.logger.warn("Forbbiden access. updateCouponPrice. Company ID: " + companyID + "; request coupon ID: " + couponID);
		        		throw new ApplicationException(ErrorType.COUPON_DOES_NOT_EXIST, "Selected coupon does not exist");
					}
				}		
		
		couponDao.updateCouponPrice(couponID, newPrice);
	}

	public void updateCouponEndDate(long couponID, long companyID, long newEndDate) throws ApplicationException {
		
		//check the company accessing their coupons only   
				Coupon coupon = couponDao.getCoupon(couponID);
				if (coupon == null || coupon.getCompanyID()!=companyID) {
					if(coupon.getCompanyID()!=companyID){
		        		SystemLogger.logger.warn("Forbbiden access. updateCouponEndDate. Company ID: " + companyID + "; request coupon ID: " + couponID);
		        		throw new ApplicationException(ErrorType.COUPON_DOES_NOT_EXIST, "Selected coupon does not exist");
					}
				}

		couponDao.updateCouponEndDate(couponID, newEndDate);
	}

	// This method returns details of a specific coupon.
	// throws exception when coupon doesn't exist
	public Coupon getCoupon(long companyID, long couponID) throws ApplicationException {
		
		Coupon coupon = couponDao.getCoupon(couponID);
		
		//check the company accessing their coupons only   
		if (coupon == null || coupon.getCompanyID()!=companyID) {
			if(coupon.getCompanyID()!=companyID){
        		SystemLogger.logger.warn("Forbbiden access. getCoupon. Company ID: " + companyID + "; request coupon ID: " + couponID);
        		throw new ApplicationException(ErrorType.COUPON_DOES_NOT_EXIST, "Selected coupon does not exist");
			}
		}
		

		return coupon;
	}

	//TODO cosnider delete this method
	public List<Coupon> getAllCoupons() throws ApplicationException {
		
		List<Coupon> couponsList = couponDao.getAllCoupons();
		
		return couponsList;
	}
	
	// The following methods returns coupons list of a certain company according
	// to different criteria

	public List<Coupon> getAllCouponsOfCompany(long companyID) throws ApplicationException {
		List<Coupon> couponsList = couponDao.getAllCouponsOfCompany(companyID);
		return couponsList;
	}

	public List<Coupon> getAllCouponsOfCompanyFilteredByCouponType(long companyID, String couponType)
			throws ApplicationException {
		List<Coupon> couponsList = couponDao.getAllCouponsOfCompanyFilteredByCouponType(companyID, couponType);
		return couponsList;
	}

	public List<Coupon> getAllCouponsOfCompanyFilteredByMaxPrice(long companyID, double price)
			throws ApplicationException {
		List<Coupon> couponsList = couponDao.getAllCouponsOfCompanyFilteredByMaxPrice(companyID, price);
		return couponsList;
	}

	public List<Coupon> getAllCouponsOfCompanyFilteredByExpirationDate(long companyID, long endDate)
			throws ApplicationException {
		List<Coupon> couponsList = couponDao.getAllCouponsOfCompanyFilteredByExpirationDate(companyID, endDate);
		return couponsList;
	}
	
	public List<CouponThumbnail> getAllCouponThumbnailsForCustomer(long customerID) throws ApplicationException {
		List<CouponThumbnail> couponsList = couponDao.getAllCouponThumbnailsForCustomer(customerID);
		return couponsList;
	}

	// This method execute the purchasing of a single coupon by a customer.
	// It performs three validations before executing the purchase: first
	// purchase, out-of-stock, and expiration date.
	public void purchaseCoupon(long customerID, long couponID) throws ApplicationException {

		if (couponDao.isCouponExpired(couponID)) {
			throw new ApplicationException(ErrorType.COUPON_EXPIRED, "The coupon you wish to buy is expired. Please try our other coupons");
		}

		if (couponDao.isCouponOutOfStock(couponID)) {
			throw new ApplicationException(ErrorType.COUPON_OUT_OF_STOCK, "The coupon you wish to buy is currently out of stock. Please our other coupons");
		}

		if (couponDao.isCouponPurchased(customerID, couponID)) {
			throw new ApplicationException(ErrorType.COUPON_HAVE_BEEN_PURCHASED_BEFORE, "You have the purchase limit for this coupon. Please our other coupons");
		}

		couponDao.createPurchaseAndReduceCouponAmountByOne(customerID, couponID);
	}
	
	
	
	
	public Purchase getPurchase(long purchaseNumber) throws ApplicationException {
		Purchase purchase = couponDao.getPurchase(purchaseNumber);
		return purchase;
	}

	// The following methods returns coupons list purchased by a certain
	// customer according to different criteria

	public List<Purchase> getAllPurchasedCouponsByCustomer(long customerID) throws ApplicationException {
		List<Purchase> purchaseList = couponDao.getAllPurchasesByCustomer(customerID);
		return purchaseList;
	}

	public List<Purchase> getAllPurchasedCouponsByCustomerFilteredByMaxPrice(long customerID, double maxPrice)
			throws ApplicationException {
		List<Purchase> purchaseList = couponDao.getAllPurchasedCouponsByCustomerFilteredByMaxPrice(customerID,
				maxPrice);
		return purchaseList;
	}

	public List<Purchase> getAllPurchasedCouponsByCustomerFilteredByCouponType(long customerID, String couponType)
			throws ApplicationException {
		List<Purchase> purchaseList = couponDao.getAllPurchasedCouponsByCustomerFilteredByCouponType(customerID,
				couponType);
		return purchaseList;
	}

	public void updateCouponImage(long couponId, long companyId, InputStream uploadedInputStream, FormDataContentDisposition fileDetail) throws ApplicationException {
		
		
		//check the company accessing their coupons only   
		Coupon coupon = couponDao.getCoupon(couponId);
		if (coupon == null || coupon.getCompanyID()!=companyId) {
			if(coupon.getCompanyID()!=companyId){
        		SystemLogger.logger.warn("Forbbiden access. updateCouponImage. Company ID: " + companyId + "; request coupon ID: " + couponId);
        		throw new ApplicationException(ErrorType.COUPON_DOES_NOT_EXIST, "Selected coupon does not exist");
			}
		}
		
		String incomingFileName = fileDetail.getFileName();
		String fileFormat = incomingFileName.substring(incomingFileName.lastIndexOf(".")+1);
		
		//make sure the file is in the correct image format
		if(!fileFormat.equals("png") && !fileFormat.equals("jpg")){
			throw new ApplicationException(ErrorType.UNSUPPORTED_FILE_FORMAT, "unsupoorted file format. please choose JPG or PNG formats only");
		}
		
		//generate unique file name for each coupon image by adding its coupon and company ids as a prefix
		String imageNameToSave = couponId + "." + fileFormat;
		
		//saving the file in the images folder
		String imageLocation = 	
				"C://Coupons//Images//"+imageNameToSave;
		FilesUtil.uploadFile(uploadedInputStream, imageLocation);
		
		//saving the file access URL
		String imageUrl = "http://localhost:8080/coupons/images/"+imageNameToSave;
		
		
		//update the coupon image location in the database
		couponDao.updateCouponImage(couponId, imageUrl);
		
	}

	
	

}
