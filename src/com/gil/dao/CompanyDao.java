package com.gil.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gil.utils.JdbcUtils;
import com.gil.beans.Company;
import com.gil.dao.interfaces.ICompanyDao;
import com.gil.enums.ErrorType;
import com.gil.exceptions.ApplicationException;



//This class provides methods which perform SQL-DB actions related to companies.  
public class CompanyDao implements ICompanyDao {
	
	//This method creates new company in the DB.
	//It receives a bean Company object, and stores all it's fields in the DB.
	public void createCompany(Company company) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try 
		{
			connection = ConnectionPoolManager.getInstance().getConnection();

			preparedStatement = connection.prepareStatement("insert into companies (name, password,email) values (?, ?, ?)");

			preparedStatement.setString(1, company.getName());
			preparedStatement.setString(2, company.getPassword());
			preparedStatement.setString(3, company.getEmail());

			preparedStatement.executeUpdate();

		} 
		catch (SQLException e) 
		{
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "unexpected error occured. Please try again");
		} 
		finally 
		{
			JdbcUtils.closeResources(connection, preparedStatement);
		}

	}
	
	//This method returns all the details of a specific company.
	//It receives a company ID, and return Company bean object.
	//Returns null when company doesn't exist.
	
	public Company getCompany(long companyID) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Company company = null;
		try 
		{
			connection = ConnectionPoolManager.getInstance().getConnection();

			preparedStatement = connection.prepareStatement("select * from companies where ID = ?");
			preparedStatement.setLong(1, companyID);		
			resultSet = preparedStatement.executeQuery();
			
			
			//A ResultSet object maintains a cursor pointing to its current row of data. 
			//Initially the cursor is positioned before the first row. 
			//The next method moves the cursor to the next row.
			if(resultSet.next()){
				company = extractCompanyFromResltSet(resultSet);
			}

		} 
		catch (SQLException e) 
		{
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "unexpected error occured. Please try again");
		} 
		finally 
		{
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
		
		return company;
		

	}
	
	//This method returns all the details of all the companies.
	//Returns a list of Company bean objects.
	public List<Company> getAllCompanies() throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Company> companyList = new ArrayList<Company>();
		
		try{
			connection = ConnectionPoolManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement("select id,name from companies");
			resultSet = preparedStatement.executeQuery();
			
			
			//A ResultSet object maintains a cursor pointing to its current row of data. 
			//Initially the cursor is positioned before the first row. 
			//The next method moves the cursor to the next row, and because it returns false when 
			//there are no more rows in the ResultSet object, it can be used in 
			//a while loop to iterate through the result set. 
			while (resultSet.next()){
				Company company = new Company(resultSet.getLong("ID"), resultSet.getString("name"));
				companyList.add(company);
			}	

		}
		catch (SQLException e) 
		{
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "unexpected error occured. Please try again");
		} 
		finally 
		{
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
		
		return companyList;
		
		
	} 
	
	
	//This method deletes a company 
	public void deleteCompany(long companyID) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try 
		{
			connection = ConnectionPoolManager.getInstance().getConnection();

			preparedStatement = connection.prepareStatement("delete from companies where ID = ?");
			preparedStatement.setLong(1, companyID);
			preparedStatement.executeUpdate();
		} 
		catch (SQLException e) 
		{
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "unexpected error occured. Please try again");
		} 
		finally 
		{
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}

	
	
	//This method updates a company's password in the DB.
	public void updateCompanyPassword(long companyID, String newCompanyPassword) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try 
		{
			connection = ConnectionPoolManager.getInstance().getConnection();
			
			preparedStatement = connection.prepareStatement("update companies set password = ? where ID = ?");
			preparedStatement.setString(1, newCompanyPassword);
			preparedStatement.setLong(2, companyID);
			
			preparedStatement.executeUpdate();

		} 
		catch (SQLException e) 
		{
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "unexpected error occured. Please try again");
		} 
		finally 
		{
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	
	}
	
	//This method updates a company's email in the DB.
	public void updateCompanyEmail(long companyID, String newCompanyEmail) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try 
		{
			connection = ConnectionPoolManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement("update companies set email = ? where ID = ?");
			preparedStatement.setString(1, newCompanyEmail);
			preparedStatement.setLong(2, companyID);
			preparedStatement.executeUpdate();

		} 
		catch (SQLException e) 
		{
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "unexpected error occured. Please try again");
		} 
		finally 
		{
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}

	
	//This method checks if a certain name exist in company table.
	//Returns true if exists, and false if doesn't.
	public boolean isCompanyNameExists(String companyName) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		boolean result = true;
		
		try{
			connection = ConnectionPoolManager.getInstance().getConnection();

			preparedStatement = connection.prepareStatement("select name from companies where binary name = ?");
			
			preparedStatement.setString(1, companyName);
			
			resultSet = preparedStatement.executeQuery();
			
			result = resultSet.next();
			
		}
		catch (SQLException e) 
		{
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "unexpected error occured. Please try again");
		} 
		finally 
		{
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
		
		return result;
	
	}
	
	//This method matches company name and password for login entry
	//Returns true if match, and false if doesn't.
	public long isCompanyNamePasswordMatch(String name, String password) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		long id;
		
		try{
			connection = ConnectionPoolManager.getInstance().getConnection();

			preparedStatement = connection.prepareStatement("select id from companies where binary name = ? and binary password = ?");
			
			preparedStatement.setString(1, name);
			preparedStatement.setString(2, password);
			
			resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()){
				id = resultSet.getLong("ID");
			}
			else{
				id=0;
			}
			
			
			
			
		}
		catch (SQLException e) 
		{
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "unexpected error occured. Please try again");
		} 
		finally 
		{
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
		
		return id;
	
	}
	
	
	//This method is used as a tool to extract company details of a resultSet
	//Receives a resultSet and return a Company object.
	private Company extractCompanyFromResltSet(ResultSet resultSet) throws SQLException{
		
		long id = resultSet.getLong("ID");
		String name = resultSet.getString("name");
		String password = resultSet.getString("password");
		String email = resultSet.getString("email");
		
		Company company = new Company(id, name, password, email);
		
		return company;
	
	}
	
	
}

			

