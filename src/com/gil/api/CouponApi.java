package com.gil.api;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.gil.beans.Coupon;
import com.gil.beans.CouponThumbnail;
import com.gil.beans.Purchase;
import com.gil.enums.CouponType;
import com.gil.enums.ErrorType;
import com.gil.exceptions.ApplicationException;
import com.gil.logic.CouponLogic;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;


/*path is empty here because it is determined for each method separately,
*  according to user types:
* "/coupon" for companies,
* "/purchase" for customers.
* 
* When user id is needed, it is retrieved from the request "userID" attribute,
* set in the filter
*/
@Path("") 
public class CouponApi {
	
	private CouponLogic couponLogic;
	
	public CouponApi(){
		couponLogic = new CouponLogic();
	}
	
	//This function return a JSON object contains only the new generated coupon id
	@POST
	@Path("/coupon")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject createCoupon(@Context HttpServletRequest request, Coupon coupon) throws ApplicationException{
		Long companyID = (Long)request.getAttribute("userID");
		coupon.setCompanyID(companyID);
		long couponId = couponLogic.createCoupon(coupon);
		JSONObject couponIdJson = new JSONObject();
		try {
			couponIdJson.put("couponId", couponId);
		} catch (JSONException e) {
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "unexpected error occured. Please try again");
		}
		return couponIdJson;
	}
	
	@POST
	@Path("/coupon/image/{couponId}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public void updateCouponImage(
		@Context HttpServletRequest request, 
		@PathParam("couponId") long couponId,
		@FormDataParam("file") InputStream uploadedInputStream,
		@FormDataParam("file") FormDataContentDisposition fileDetail) throws ApplicationException {
		
		Long companyID = (Long)request.getAttribute("userID");
		couponLogic.updateCouponImage(couponId, companyID, uploadedInputStream, fileDetail);				
	}
	
	@PUT
	@Path("/coupon/price/{ID}")
	public void updateCouponPrice(@Context HttpServletRequest request, @PathParam("ID") long couponID, String newPriceStr) throws ApplicationException{
		Long companyID = (Long)request.getAttribute("userID");
		double newPrice = Double.parseDouble(newPriceStr);
		couponLogic.updateCouponPrice(couponID, companyID, newPrice);
	}
	
	@PUT
	@Path("/coupon/end-date/{ID}")
	public void updateCouponEndDate(@Context HttpServletRequest request, @PathParam("ID") long couponID, String newEndDateStr) throws ApplicationException{
		Long companyID = (Long)request.getAttribute("userID");
		long newEndDate = Long.parseLong(newEndDateStr);
		couponLogic.updateCouponEndDate(couponID, companyID, newEndDate);
	}
	
	@DELETE
	@Path("/coupon/{couponID}")
	public void deleteCoupon(@Context HttpServletRequest request, @PathParam("couponID") long couponID) throws ApplicationException {
		Long companyID = (Long)request.getAttribute("userID");
		couponLogic.deleteCoupon(couponID, companyID);
	}
	
	@GET
	@Path("/coupon/{couponID}")
	@Produces(MediaType.APPLICATION_JSON)
	public Coupon getCoupon(@Context HttpServletRequest request, @PathParam("couponID") long couponID) throws ApplicationException {
		Long companyID = (Long)request.getAttribute("userID");
		Coupon coupon = couponLogic.getCoupon(companyID, couponID);
		return coupon;
	}
	
	
	@GET
	@Path("/coupon/company")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Coupon> getAllCouponsOfCompany(@Context HttpServletRequest request) throws ApplicationException {
		Long companyID = (Long)request.getAttribute("userID");
		List<Coupon> coupons = couponLogic.getAllCouponsOfCompany(companyID);
		return coupons;
	}
	
	@GET
	@Path("/coupon/company/type/{type}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Coupon> getAllCouponsOfCompanyFilteredByCouponType(@Context HttpServletRequest request, @PathParam("type") String couponType) throws ApplicationException {
		Long companyID = (Long)request.getAttribute("userID");
		List<Coupon> coupons = couponLogic.getAllCouponsOfCompanyFilteredByCouponType(companyID, couponType);
		return coupons;
	}
	
	
	
	@GET
	@Path("/coupon/company/price/{maxPrice}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Coupon> getAllCouponsOfCompanyFilteredByMaxPrice(@Context HttpServletRequest request, @PathParam("maxPrice") double maxPrice) throws ApplicationException {
		Long companyID = (Long)request.getAttribute("userID");
		List<Coupon> coupons = couponLogic.getAllCouponsOfCompanyFilteredByMaxPrice(companyID, maxPrice);
		return coupons;
	}
	
	@GET
	@Path("/coupon/company/date/{expirationDate}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Coupon> getAllCouponsOfCompanyFilteredByExpirationDate(@Context HttpServletRequest request, @PathParam("expirationDate") long expirationDate) throws ApplicationException {
		Long companyID = (Long)request.getAttribute("userID");
		List<Coupon> coupons = couponLogic.getAllCouponsOfCompanyFilteredByExpirationDate(companyID, expirationDate);
		return coupons;
	}
	
	@GET
	@Path("/purchase/coupons-for-customer")
	@Produces(MediaType.APPLICATION_JSON)
	public List<CouponThumbnail> getAllCouponThumbnailsForCustomer(@Context HttpServletRequest request) throws ApplicationException {
		Long customerID = (Long)request.getAttribute("userID");
		List<CouponThumbnail> coupons = couponLogic.getAllCouponThumbnailsForCustomer(customerID);
		return coupons;
	}	
	
	@POST
	@Path("/purchase/{couponID}")
	public void purchaseCoupon(@Context HttpServletRequest request, @PathParam("couponID") long couponID) throws ApplicationException{
		Long customerID = (Long)request.getAttribute("userID");
		couponLogic.purchaseCoupon(customerID, couponID);
	}
	
	@GET
	@Path("/purchase")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Purchase> getAllPurchasedCouponsByCustomer(@Context HttpServletRequest request) throws ApplicationException {
		Long customerID = (Long)request.getAttribute("userID");
		List<Purchase> purchases = couponLogic.getAllPurchasedCouponsByCustomer(customerID);
		return purchases;
	}
	
	@GET
	@Path("/purchase/{purchaseNumber}")
	@Produces(MediaType.APPLICATION_JSON)
	public Purchase getPurchase(@PathParam("purchaseNumber") long purchaseNumber) throws ApplicationException {
		Purchase purchase = couponLogic.getPurchase(purchaseNumber);
		return purchase;
	}
	
	@GET
	@Path("/purchase/price/{maxPrice}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Purchase> getAllPurchasedCouponsByCustomerFilteredByMaxPrice(@Context HttpServletRequest request, @PathParam("maxPrice") double maxPrice) throws ApplicationException {
		Long customerID = (Long)request.getAttribute("userID");
		List<Purchase> purchases = couponLogic.getAllPurchasedCouponsByCustomerFilteredByMaxPrice(customerID, maxPrice);
		return purchases;
	}
	
	@GET
	@Path("/purchase/type/{type}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Purchase> getAllPurchasedCouponsByCustomerFilteredByCouponType(@Context HttpServletRequest request, @PathParam("type") String couponType) throws ApplicationException {
		Long customerID = (Long)request.getAttribute("userID");
		List<Purchase> purchases = couponLogic.getAllPurchasedCouponsByCustomerFilteredByCouponType(customerID, couponType);
		return purchases;
	}
	
	//This function returns a JSON array of the coupon types (to be used on select-list)
	//this is a common function for coupons and purchases, so no "/coupon" or "/purchase" path.
	@GET
	@Path("/list-coupon-types")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONArray getCouponTypes(){
		CouponType[] couponTypes = CouponType.values(); //creating array of the coupon type enums
		List<String> couponTypesNames = new ArrayList<String>();
		for (CouponType item : couponTypes){
			couponTypesNames.add(item.getName());
		}
		JSONArray couponTypesNamesJSON = new JSONArray(couponTypesNames); 
		return couponTypesNamesJSON;
	}
	
	
	

}
