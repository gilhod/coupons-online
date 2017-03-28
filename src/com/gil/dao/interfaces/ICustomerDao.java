package com.gil.dao.interfaces;

import java.util.List;

import com.gil.beans.Customer;
import com.gil.exceptions.ApplicationException;

public interface ICustomerDao {

	public void createCustomer(Customer customer) throws ApplicationException;
	public Customer getCustomer(long customerID) throws ApplicationException;
	public List<Customer> getAllCustomers() throws ApplicationException;
	public void deleteCustomer(long customerID) throws ApplicationException;
	public void updateCustomerPassword(long customerID, String newCustomerPassword) throws ApplicationException;
	public boolean isCustomerNameExists(String customerName) throws ApplicationException;
	public long isCustomerNamePasswordMatch(String name, String password) throws ApplicationException;

	
}
