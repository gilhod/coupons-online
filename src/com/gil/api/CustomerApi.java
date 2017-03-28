package com.gil.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.gil.beans.Customer;
import com.gil.exceptions.ApplicationException;
import com.gil.logic.CustomerLogic;

@Path("/customer")

public class CustomerApi {
	
	private CustomerLogic customerLogic;
	
	public CustomerApi(){
		customerLogic = new CustomerLogic();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void createCustomer(Customer customer) throws ApplicationException{
		customerLogic.createCustomer(customer);
	}
	
	@DELETE
	@Path("/{id}")
	public void deleteCustomer(@PathParam("id") long customerID) throws ApplicationException {
		customerLogic.deleteCustomer(customerID);
	}
	
	@PUT
	@Path("/updatePassword/{ID}")
	public void updateCompanyPassword(@PathParam("ID") long customerId, String newPassword) throws ApplicationException{
		customerLogic.updateCustomerPassword(customerId, newPassword);
	}
	
	@GET
	@Path("/{ID}")
	@Produces(MediaType.APPLICATION_JSON)
	public Customer getCustomer(@PathParam("ID") long customerID) throws ApplicationException {
		Customer customer = customerLogic.getCustomer(customerID);
		return customer;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Customer> getAllCustomers(@PathParam("ID") long customerID) throws ApplicationException {
		List<Customer> allCustomersList = customerLogic.getAllCustomers();
		return allCustomersList;
	}

}
