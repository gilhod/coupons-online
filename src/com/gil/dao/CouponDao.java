package com.gil.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gil.beans.Coupon;
import com.gil.beans.CouponThumbnail;
import com.gil.beans.Purchase;
import com.gil.dao.interfaces.ICouponDao;
import com.gil.enums.ErrorType;
import com.gil.exceptions.ApplicationException;
import com.gil.utils.JdbcUtils;
import com.mysql.jdbc.Statement;

//This class provides methods which perform SQL-DB actions related to coupons.  
public class CouponDao implements ICouponDao {

	// This method creates new coupon in the DB.
	// It receives a bean Coupon object, and stores all it's fields in the DB.
	// It returns the generated coupon id.
	public long createCoupon(Coupon coupon) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		long couponId=0;
		try {
			connection = ConnectionPoolManager.getInstance().getConnection();

			preparedStatement = connection.prepareStatement(
					"insert into coupons (title, start_date, end_date, amount, type, message, price, image, company_id) values (?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

			preparedStatement.setString(1, coupon.getTitle());
			preparedStatement.setLong(2, coupon.getStartDate());
			preparedStatement.setLong(3, coupon.getEndDate());
			preparedStatement.setInt(4, coupon.getAmount());
			preparedStatement.setString(5, coupon.getCouponType());
			preparedStatement.setString(6, coupon.getMessage());
			preparedStatement.setDouble(7, coupon.getPrice());
			preparedStatement.setString(8, coupon.getImage());
			preparedStatement.setLong(9, coupon.getCompanyID());

			preparedStatement.executeUpdate();
			
			resultSet = preparedStatement.getGeneratedKeys();
			if (resultSet.next()) {
				couponId = resultSet.getLong(1);
			}
			

		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "unexpected error occured. Please try again");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
		return couponId;

	}

	// This method checks if a certain name exist in coupons table.
	// Returns true if exists, and false if doesn't.
	public boolean isCouponNameExists(String couponName) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		boolean result = true;

		try {
			connection = ConnectionPoolManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement("select id from coupons where binary title = ?");
			preparedStatement.setString(1, couponName);
			resultSet = preparedStatement.executeQuery();
			result = resultSet.next();

		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "unexpected error occured. Please try again");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}

		return result;

	}

	// This method checks if a certain coupon is expired.
	// Returns true if expired, and false if doesn't.
	public boolean isCouponExpired(long couponID) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		long currentTime = System.currentTimeMillis();
		boolean result = true;

		try {
			connection = ConnectionPoolManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement("select id from coupons where id = ? and end_date < ?");
			preparedStatement.setLong(1, couponID);
			preparedStatement.setLong(2, currentTime);
			resultSet = preparedStatement.executeQuery();
			result = resultSet.next();

		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "unexpected error occured. Please try again");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}

		return result;

	}

	// This method checks if a certain coupon is out of stock.
	// Returns true if out of stock, and false if doesn't.
	public boolean isCouponOutOfStock(long couponID) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		boolean result = true;

		try {
			connection = ConnectionPoolManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement("select id from coupons where id = ? and amount <= 0");
			preparedStatement.setLong(1, couponID);
			resultSet = preparedStatement.executeQuery();
			result = resultSet.next();

		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "unexpected error occured. Please try again");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}

		return result;

	}

	// This method deletes a coupon
	public void deleteCoupon(long couponID) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = ConnectionPoolManager.getInstance().getConnection();

			preparedStatement = connection.prepareStatement("delete from coupons where ID = ?");
			preparedStatement.setLong(1, couponID);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "unexpected error occured. Please try again");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}

	}

	// This method deletes coupons of a certain company
	public void deleteCouponsByCompany(long companyID) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = ConnectionPoolManager.getInstance().getConnection();

			preparedStatement = connection.prepareStatement("delete from coupons where Company_ID = ?");
			preparedStatement.setLong(1, companyID);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "unexpected error occured. Please try again");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}

	// This method returns all the details of a specific coupon.
	// It receives a coupon ID, and return Coupon bean object.
	// Returns null when coupon doesn't exist.
	public Coupon getCoupon(long couponID) throws ApplicationException {
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		ResultSet resultSet = null;
		Coupon coupon = null;

		try {
			connection = ConnectionPoolManager.getInstance().getConnection();

			preparedStatement = connection.prepareStatement("select * from coupons where ID = ?");

			preparedStatement.setLong(1, couponID);

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				coupon = extractCouponFromResltSet(resultSet);
			}

			resultSet.next();

		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "unexpected error occured. Please try again");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
		return coupon;

	}

	// This method returns all the details of all the coupons of a certain
	// company
	// Returns a list of Coupon bean objects.

	public List<Coupon> getAllCouponsOfCompany(long companyID) throws ApplicationException {
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		ResultSet resultSet = null;
		List<Coupon> couponList = new ArrayList<Coupon>();

		try {
			connection = ConnectionPoolManager.getInstance().getConnection();

			preparedStatement = connection.prepareStatement("select id,title from coupons where company_ID = ?");

			preparedStatement.setLong(1, companyID);

			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				Coupon coupon = new Coupon(resultSet.getLong("ID"), resultSet.getString("title"));
				couponList.add(coupon);
			}

		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "unexpected error occured. Please try again");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}

		return couponList;
	}
	
	// This method returns all the details of all the coupons of a certain
		// company
		// Returns a list of Coupon bean objects.

		public List<Coupon> getAllCoupons() throws ApplicationException {
			PreparedStatement preparedStatement = null;
			Connection connection = null;
			ResultSet resultSet = null;
			List<Coupon> couponList = new ArrayList<Coupon>();

			try {
				connection = ConnectionPoolManager.getInstance().getConnection();

				preparedStatement = connection.prepareStatement("select id, title from coupons");

				resultSet = preparedStatement.executeQuery();

				while (resultSet.next()) {
					Coupon coupon = new Coupon(resultSet.getLong("ID"), resultSet.getString("title"));
					couponList.add(coupon);
				}

			} catch (SQLException e) {
				throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "unexpected error occured. Please try again");
			} finally {
				JdbcUtils.closeResources(connection, preparedStatement, resultSet);
			}

			return couponList;
		}

	// This method returns the details of the coupons of a certain company
	// of a certain type
	// Returns a list of Coupon bean objects.
	public List<Coupon> getAllCouponsOfCompanyFilteredByCouponType(long companyID, String couponType)
			throws ApplicationException {

		PreparedStatement preparedStatement = null;
		Connection connection = null;
		ResultSet resultSet = null;
		List<Coupon> couponList = new ArrayList<Coupon>();

		try {
			connection = ConnectionPoolManager.getInstance().getConnection();

			preparedStatement = connection.prepareStatement("select id, title from coupons where company_id = ? and type = ?");
			preparedStatement.setLong(1, companyID);
			preparedStatement.setString(2, couponType);

			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				Coupon coupon = new Coupon(resultSet.getLong("ID"), resultSet.getString("title"));
				couponList.add(coupon);
			}

		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "unexpected error occured. Please try again");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}

		return couponList;

	}

	// This method returns the details of the coupons of a certain company
	// up to certain price
	// Returns a list of Coupon bean objects.

	public List<Coupon> getAllCouponsOfCompanyFilteredByMaxPrice(long companyID, double price)
			throws ApplicationException {
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		ResultSet resultSet = null;
		List<Coupon> couponList = new ArrayList<Coupon>();

		try {
			connection = ConnectionPoolManager.getInstance().getConnection();

			preparedStatement = connection
					.prepareStatement("select id, title from coupons where company_ID = ? and price <= ?");

			preparedStatement.setLong(1, companyID);
			preparedStatement.setDouble(2, price);

			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				Coupon coupon = new Coupon(resultSet.getLong("ID"), resultSet.getString("title"));
				couponList.add(coupon);
			}

		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "unexpected error occured. Please try again");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}

		return couponList;
	}

	// This method returns the details of the coupons of a certain company
	// up to certain expiration date
	// Returns a list of Coupon bean objects.

	public List<Coupon> getAllCouponsOfCompanyFilteredByExpirationDate(long companyID, long endDate)
			throws ApplicationException {
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		ResultSet resultSet = null;
		List<Coupon> couponList = new ArrayList<Coupon>();

		try {
			connection = ConnectionPoolManager.getInstance().getConnection();

			preparedStatement = connection
					.prepareStatement("select id, title from coupons where company_ID = ? and end_date <= ?");

			preparedStatement.setLong(1, companyID);
			preparedStatement.setLong(2, endDate);

			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				Coupon coupon = new Coupon(resultSet.getLong("ID"), resultSet.getString("title"));
				couponList.add(coupon);
			}

		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "unexpected error occured. Please try again");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}

		return couponList;
	}

	// This method updates the price of a certain coupon
	public void updateCouponPrice(long couponID, Double newPrice) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = ConnectionPoolManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement("update coupons set price = ? where ID = ?");
			preparedStatement.setDouble(1, newPrice);
			preparedStatement.setLong(2, couponID);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "unexpected error occured. Please try again");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}

	}

	// This method updates the expiration date of a certain coupon
	public void updateCouponEndDate(long couponID, long newEndDate) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = ConnectionPoolManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement("update coupons set end_date = ? where ID = ?");
			preparedStatement.setLong(1, newEndDate);
			preparedStatement.setLong(2, couponID);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "unexpected error occured. Please try again");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}

	}

	// This method deletes expired coupons from the DB.
	public void deleteExpiredCoupons() throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		long currentDate = System.currentTimeMillis();

		try {
			connection = ConnectionPoolManager.getInstance().getConnection();

			preparedStatement = connection.prepareStatement("delete from coupons where end_date < ?");
			preparedStatement.setLong(1, currentDate);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "unexpected error occured. Please try again");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}

	}

	// This method creates new purchase
	public void createPurchaseAndReduceCouponAmountByOne(long customerID, long couponID) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement1 = null;
		PreparedStatement preparedStatement2 = null;
		long currentTime = System.currentTimeMillis();

		try {
			connection = ConnectionPoolManager.getInstance().getConnection();

			preparedStatement1 = connection
					.prepareStatement("insert into purchases (purchase_date, customer_id, coupon_id) values (?, ?,?)");
			preparedStatement1.setLong(1, currentTime);
			preparedStatement1.setLong(2, customerID);
			preparedStatement1.setLong(3, couponID);
			preparedStatement1.executeUpdate();

			preparedStatement2 = connection.prepareStatement("update coupons set amount = amount -1 where ID = ?");
			preparedStatement2.setLong(1, couponID);
			preparedStatement2.executeUpdate();

		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "unexpected error occured. Please try again");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement1, preparedStatement2);
		}
	}

	// This method deletes purchases of coupons of a certain company
	public void deletePurchasesByCompany(long companyID) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = ConnectionPoolManager.getInstance().getConnection();

			preparedStatement = connection.prepareStatement(
					"delete from purchases where COUPON_ID IN (select id from coupons where company_id = ?)");
			preparedStatement.setLong(1, companyID);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "unexpected error occured. Please try again");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}

	// This method deletes purchases of a certain coupon
	public void deletePurchasesByCoupon(long couponID) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = ConnectionPoolManager.getInstance().getConnection();

			preparedStatement = connection.prepareStatement("delete from purchases where coupon_ID = ?");
			preparedStatement.setLong(1, couponID);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "unexpected error occured. Please try again");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}

	}

	// This method deletes purchases of a certain customer
	public void deletePurchasesByCustomer(long customerID) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = ConnectionPoolManager.getInstance().getConnection();

			preparedStatement = connection.prepareStatement("delete from purchases where customer_ID = ?");
			preparedStatement.setLong(1, customerID);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "unexpected error occured. Please try again");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}

	}

	// This method deletes purchases of expired coupons
	public void deletePurchasesOfExpiredCoupons() throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		long currentDate = System.currentTimeMillis();

		try {
			connection = ConnectionPoolManager.getInstance().getConnection();

			preparedStatement = connection.prepareStatement(
					"delete from purchases where COUPON_ID IN (select id from coupons where end_date < ?)");
			preparedStatement.setLong(1, currentDate);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "unexpected error occured. Please try again");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}

	}

	// This method checks if a certain coupon has already been purchased by a
	// certain customer.
	// Returns true if purchased, and false if doesn't.
	public boolean isCouponPurchased(long customerID, long couponID) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		boolean result = true;

		try {
			connection = ConnectionPoolManager.getInstance().getConnection();

			preparedStatement = connection
					.prepareStatement("select purchase_number from purchases where customer_id = ? and coupon_id = ?");

			preparedStatement.setLong(1, customerID);
			preparedStatement.setLong(2, couponID);

			resultSet = preparedStatement.executeQuery();

			result = resultSet.next();

		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "unexpected error occured. Please try again");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}

		return result;
	}
	
	//This method returns all the details of a specific purchase.
	// It receives a purchase ID, and return Purchase bean object.
	// Returns null when purchase doesn't exist.
	public Purchase getPurchase(long purchaseNumber) throws ApplicationException {
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		ResultSet resultSet = null;
		Purchase purchase = null;

		try {
			connection = ConnectionPoolManager.getInstance().getConnection();

			preparedStatement = connection.prepareStatement(
					"select p.purchase_number, p.purchase_date, p.coupon_id, c.title, c.type, c.price  "
							+ "from purchases p inner join coupons c " + "on p.coupon_id = c.id "
							+ "where p.purchase_number = ?;");


			preparedStatement.setLong(1, purchaseNumber);

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				purchase = extractPurchaseFromResltSet(resultSet);
			}

			resultSet.next();

		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "unexpected error occured. Please try again");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
		return purchase;

	}


	// This method returns all the coupons which have been purchased by a
	// certain customer
	public List<Purchase> getAllPurchasesByCustomer(long customerID) throws ApplicationException {
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		ResultSet resultSet = null;
		List<Purchase> purchaseList = new ArrayList<Purchase>();

		try {
			connection = ConnectionPoolManager.getInstance().getConnection();

			preparedStatement = connection.prepareStatement(
					"select p.purchase_number, c.title  "
							+ "from purchases p inner join coupons c " + "on p.coupon_id = c.id "
							+ "where p.customer_id = ?;");

			preparedStatement.setLong(1, customerID);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				Purchase purchase = new Purchase(resultSet.getLong("purchase_number"), resultSet.getString("title"));
				purchaseList.add(purchase);
			}

		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "unexpected error occured. Please try again");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}

		return purchaseList;

	}

	// This method returns all the coupons which have been purchased by a
	// certain customer up to a certain price
	public List<Purchase> getAllPurchasedCouponsByCustomerFilteredByMaxPrice(long customerID, double maxPrice)
			throws ApplicationException {
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		ResultSet resultSet = null;
		List<Purchase> purchaseList = new ArrayList<Purchase>();

		try {
			connection = ConnectionPoolManager.getInstance().getConnection();

			preparedStatement = connection.prepareStatement(
					"select p.purchase_number, c.title   "
							+ "from purchases p inner join coupons c " + "on p.coupon_id = c.id "
							+ "where p.customer_id = ? and c.price <= ?;");

			preparedStatement.setLong(1, customerID);
			preparedStatement.setDouble(2, maxPrice);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				Purchase purchase = new Purchase(resultSet.getLong("purchase_number"), resultSet.getString("title"));
				purchaseList.add(purchase);
			}

		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "unexpected error occured. Please try again");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}

		return purchaseList;

	}

	// This method returns all the coupons which have been purchased by a
	// certain customer, of a specific type
	public List<Purchase> getAllPurchasedCouponsByCustomerFilteredByCouponType(long customerID, String couponType)
			throws ApplicationException {
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		ResultSet resultSet = null;
		List<Purchase> purchaseList = new ArrayList<Purchase>();

		try {
			connection = ConnectionPoolManager.getInstance().getConnection();

			preparedStatement = connection.prepareStatement(
					"select p.purchase_number, c.title  "
							+ "from purchases p inner join coupons c " + "on p.coupon_id = c.id "
							+ "where p.customer_id = ? and c.type = ?;");

			preparedStatement.setLong(1, customerID);
			preparedStatement.setString(2, couponType);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				Purchase purchase = new Purchase(resultSet.getLong("purchase_number"), resultSet.getString("title"));
				purchaseList.add(purchase);
			}

		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "unexpected error occured. Please try again");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}

		return purchaseList;

	}

	private Purchase extractPurchaseFromResltSet(ResultSet resultSet) throws SQLException {

		long purchaseID = resultSet.getLong("purchase_number");
		long purchaseDate = resultSet.getLong("purchase_date");
		long couponID = resultSet.getLong("coupon_id");
		String couponTitle = resultSet.getString("title");
		String couponType = resultSet.getString("type");
		double couponPrice = resultSet.getDouble("price");

		Purchase purchase = new Purchase(purchaseID, purchaseDate, couponID, couponTitle, couponType, couponPrice);

		return purchase;
	}

	// This method is a tool to extract the coupon detail off a reusltSet
	private Coupon extractCouponFromResltSet(ResultSet resultSet) throws SQLException {

		long id = resultSet.getLong("ID");
		String title = resultSet.getString("title");
		long startDate = resultSet.getLong("start_date");
		long endDate = resultSet.getLong("end_date");
		int amount = resultSet.getInt("amount");
		String couponType = resultSet.getString("type");
		String message = resultSet.getString("message");
		double price = resultSet.getDouble("price");
		String image = resultSet.getString("image");
		long companyID = resultSet.getLong("company_ID");

		Coupon coupon = new Coupon(id, title, startDate, endDate, amount, couponType, message, price, image, companyID);

		return coupon;
	}

	
	//this method return valid coupons for purchase by a customer. it returns only coupons which haven't been
	//purchased by the customer already, are in stock, and not expired
	public List<CouponThumbnail> getAllCouponThumbnailsForCustomer(long customerID) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		CouponThumbnail couponDetails = null;
		List<CouponThumbnail> couponList = new ArrayList<CouponThumbnail>();
		long currentTime = System.currentTimeMillis();
	
		try {
			connection = ConnectionPoolManager.getInstance().getConnection();

			preparedStatement = connection.prepareStatement("select cp.id, cp.title, cp.start_date, cp.end_date, cp.price, cp.image, cp.message, cm.name from coupons cp inner join companies cm on cp.COMPANY_ID = cm.ID where cp.id not in (select coupon_id from purchases where customer_id=?) and amount>0 and end_date>?");
			preparedStatement.setLong(1, customerID);
			preparedStatement.setLong(2, currentTime);
			
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				couponDetails = extractCouponDetailsFromResltSet(resultSet);
				couponList.add(couponDetails);
			}

		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "unexpected error occured. Please try again");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
		return couponList;
	}
	
	private CouponThumbnail extractCouponDetailsFromResltSet(ResultSet resultSet) throws SQLException {
		
		long id = resultSet.getLong("ID");
		String title = resultSet.getString("title");
		long startDate = resultSet.getLong("start_date");
		long endDate = resultSet.getLong("end_date");
		double price = resultSet.getDouble("price");
		String image = resultSet.getString("image");
		String message = resultSet.getString("message");
		String companyName = resultSet.getString("name");
		
		CouponThumbnail couponDetails = new CouponThumbnail(id, title, startDate, endDate, price, image, message, companyName);
		
		return couponDetails;
	}

	public void updateCouponImage(long couponId, String imageUrl) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = ConnectionPoolManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement("update coupons set image = ? where ID = ?");
			preparedStatement.setString(1, imageUrl);
			preparedStatement.setLong(2, couponId);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "unexpected error occured. Please try again");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
		
	}

	
	

}
