package com.gil.logic;

import java.util.List;

import com.gil.beans.Customer;
import com.gil.dao.CouponDao;
import com.gil.dao.CustomerDao;
import com.gil.enums.ErrorType;
import com.gil.exceptions.ApplicationException;
import com.gil.utils.ValidationUtils;

public class CustomerLogic {

	// Dao objects declaration
	private CustomerDao customerDao;
	private CouponDao couponDao;

	public CustomerLogic() {
		customerDao = new CustomerDao();
		couponDao = new CouponDao();
	}

	// This method creates new customer.
	public void createCustomer(Customer customer) throws ApplicationException {

		// Customer name validation: must be unique
		if (customerDao.isCustomerNameExists(customer.getName())) {
			throw new ApplicationException(ErrorType.CUSTOMER_NAME_ALREADY_EXISTS, "Customer name already exist");
		}

		// Password validation
		if (!ValidationUtils.isPasswordValid(customer.getPassword())) {
			throw new ApplicationException(ErrorType.INVALID_PASSWORD, "Invalid password");
		}

		customerDao.createCustomer(customer);
	}

	// This method deletes a customer from the records.
	// When deleting a customer, all its purchased coupons must be deleted.
	public void deleteCustomer(long customerID) throws ApplicationException {

		couponDao.deletePurchasesByCustomer(customerID);
		customerDao.deleteCustomer(customerID);
	}

	// This method updates customer's password
	public void updateCustomerPassword(long customerID, String newPassword) throws ApplicationException {

		// Password validation
		if (!ValidationUtils.isPasswordValid(newPassword)) {
			throw new ApplicationException(ErrorType.INVALID_PASSWORD, "Invalid Password");
		}

		customerDao.updateCustomerPassword(customerID, newPassword);
	}

	// This method returns details of a specific customer.
	// throws exception when customer doesn't exist
	public Customer getCustomer(long customerID) throws ApplicationException {

		Customer customer = customerDao.getCustomer(customerID);

		if (customer == null) {
			throw new ApplicationException(ErrorType.CUSTOMER_DOES_NOT_EXIST, "The selected customer does not exist in the system");
		}
		return customer;
	}

	// This method returns a list of all the registered customers
	public List<Customer> getAllCustomers() throws ApplicationException {
		List<Customer> allCustomersList = customerDao.getAllCustomers();
		return allCustomersList;
	}

}
