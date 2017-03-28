package com.gil.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gil.beans.Customer;
import com.gil.dao.interfaces.ICustomerDao;
import com.gil.enums.ErrorType;
import com.gil.exceptions.ApplicationException;
import com.gil.utils.JdbcUtils;

//This class provides methods which perform SQL-DB actions related to customers.  
public class CustomerDao implements ICustomerDao {

	// This method creates new customer in the DB.
	// It receives a bean Customer object, and stores all it's fields in the DB.
	public void createCustomer(Customer customer) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = ConnectionPoolManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement("insert into customers (name, password) values (?, ?)");
			preparedStatement.setString(1, customer.getName());
			preparedStatement.setString(2, customer.getPassword());
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "unexpected error occured. Please try again");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}

	}

	// This method returns all the details of a specific customer.
	// It receives a customer ID, and return Customer bean object.
	// Returns null when customer doesn't exist.
	public Customer getCustomer(long customerID) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Customer customer = null;

		try {
			connection = ConnectionPoolManager.getInstance().getConnection();

			preparedStatement = connection.prepareStatement("select * from customers where ID = ?");
			preparedStatement.setLong(1, customerID);

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				customer = extractCustomerFromResltSet(resultSet);
			}

		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "unexpected error occured. Please try again");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
		return customer;

	}

	// This method returns all the details of all the customers
	// Returns a list of Customer bean objects.
	public List<Customer> getAllCustomers() throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Customer> customerList = new ArrayList<Customer>();

		try {
			connection = ConnectionPoolManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement("select id, name from customers");
			resultSet = preparedStatement.executeQuery();

			// A ResultSet object maintains a cursor pointing to its current row
			// of data.
			// Initially the cursor is positioned before the first row.
			// The next method moves the cursor to the next row, and because it
			// returns false when
			// there are no more rows in the ResultSet object, it can be used in
			// a while loop to iterate through the result set.
			while (resultSet.next()) {
				Customer customer = new Customer(resultSet.getLong("ID"), resultSet.getString("name"));
				customerList.add(customer);
			}

		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "unexpected error occured. Please try again");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}

		return customerList;

	}

	// This method deletes a customer from the DB records.
	// Deleting a customer is made in two tables:
	// 1. Deleting the customer details.
	// 2. Deleting all the coupons which have been purchased by the customer
	public void deleteCustomer(long customerID) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = ConnectionPoolManager.getInstance().getConnection();

			preparedStatement = connection.prepareStatement("delete from customers where ID = ?");
			preparedStatement.setLong(1, customerID);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "unexpected error occured. Please try again");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}

	// This method updates a customer's password in the DB.
	public void updateCustomerPassword(long customerID, String newCustomerPassword) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = ConnectionPoolManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement("update customers set password = ? where ID = ?");
			preparedStatement.setString(1, newCustomerPassword);
			preparedStatement.setLong(2, customerID);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "unexpected error occured. Please try again");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}

	}

	// This method checks if a certain name exist in customer table.
	// Returns true if exists, and false if doesn't.
	public boolean isCustomerNameExists(String customerName) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		boolean result = true;

		try {
			connection = ConnectionPoolManager.getInstance().getConnection();

			preparedStatement = connection.prepareStatement("select name from customers where binary name = ?");

			preparedStatement.setString(1, customerName);

			resultSet = preparedStatement.executeQuery();

			result = resultSet.next();

		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "unexpected error occured. Please try again");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}

		return result;

	}

	// This method matches customer name and password for login entry
	// Returns true if match, and false if doesn't.
	public long isCustomerNamePasswordMatch(String name, String password) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		long id;

		try {
			connection = ConnectionPoolManager.getInstance().getConnection();

			preparedStatement = connection.prepareStatement("select id from customers where binary name = ? and binary password = ?");

			preparedStatement.setString(1, name);
			preparedStatement.setString(2, password);

			resultSet = preparedStatement.executeQuery();

			if(resultSet.next()){
				id = resultSet.getLong("ID");
			}
			else{
				id=0;
			}

		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "unexpected error occured. Please try again");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}

		return id;

	}

	private Customer extractCustomerFromResltSet(ResultSet resultSet) throws SQLException {

		long id = resultSet.getLong("ID");
		String name = resultSet.getString("name");
		String password = resultSet.getString("password");

		Customer customer = new Customer(id, name, password);

		return customer;
	}
}
