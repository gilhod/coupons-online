package com.gil.dao.interfaces;

import java.util.List;

import com.gil.beans.Company;
import com.gil.exceptions.ApplicationException;

public interface ICompanyDao {
	
	public void createCompany(Company company) throws ApplicationException;
	public Company getCompany(long companyID) throws ApplicationException;
	public List<Company> getAllCompanies() throws ApplicationException;
	public void deleteCompany(long companyID) throws ApplicationException;
	public void updateCompanyPassword(long companyID, String newCompanyPassword) throws ApplicationException;
	public void updateCompanyEmail(long companyID, String newCompanyEmail) throws ApplicationException;
	public boolean isCompanyNameExists(String companyName) throws ApplicationException;
	public long isCompanyNamePasswordMatch(String name, String password) throws ApplicationException;
	
	
	
	
	

}
