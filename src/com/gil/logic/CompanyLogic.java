package com.gil.logic;

import java.util.List;

import com.gil.beans.Company;
import com.gil.dao.CompanyDao;
import com.gil.dao.CouponDao;
import com.gil.enums.ErrorType;
import com.gil.exceptions.ApplicationException;
import com.gil.utils.ValidationUtils;

public class CompanyLogic {

	// Dao objects declaration
	private CompanyDao companyDao;
	private CouponDao couponDao;

	public CompanyLogic() {
		companyDao = new CompanyDao();
		couponDao = new CouponDao();
	}

	// This method creates new company.
	public void createCompany(Company company) throws ApplicationException {

		// Company name validation: must be unique
		if (companyDao.isCompanyNameExists(company.getName())) {
			throw new ApplicationException(ErrorType.COMPANY_NAME_ALREADY_EXISTS, "Company name already exist");
		}

		// Email validation
		if (!ValidationUtils.isEmailValid(company.getEmail())) {
			throw new ApplicationException(ErrorType.INVALID_EMAIL, "Invalid email pattern");
		}

		// Password validation
		if (!ValidationUtils.isPasswordValid(company.getPassword())) {
			throw new ApplicationException(ErrorType.INVALID_PASSWORD, "Invalid password. Password must be between 6 to 12 characters in length, and contain at least 1 uppercase letter, 1 lowercase letter, and 1 digit.");
		}
		companyDao.createCompany(company);
	}

	// This method deletes a company from the records.
	// When deleting a company, all its coupons and all its purchased coupons
	// must be deleted.
	public void deleteCompany(long companyID) throws ApplicationException {
		couponDao.deletePurchasesByCompany(companyID);
		couponDao.deleteCouponsByCompany(companyID);
		companyDao.deleteCompany(companyID);
	}

	// This method updates company's email
	public void updateCompanyEmail(long companyID, String newEmail) throws ApplicationException {

		// Email validation
		if (!ValidationUtils.isEmailValid(newEmail)) {
			throw new ApplicationException(ErrorType.INVALID_EMAIL, "Invalid email pattern");
		}

		companyDao.updateCompanyEmail(companyID, newEmail);
	}

	// This method updates company's password
	public void updateCompanyPassword(long companyID, String newPassword) throws ApplicationException {

		// Password validation
		if (!ValidationUtils.isPasswordValid(newPassword)) {
			throw new ApplicationException(ErrorType.INVALID_PASSWORD, "Invalid password. Password must be between 6 to 12 characters in length, and contain at least 1 uppercase letter, 1 lowercase letter, and 1 digit.");
		}

		companyDao.updateCompanyPassword(companyID, newPassword);
	}

	// This method returns details of a specific company.
	// throws exception when company doesn't exist
	public Company getCompany(long companyID) throws ApplicationException {

		Company company = companyDao.getCompany(companyID);
		if (company == null) {
			throw new ApplicationException(ErrorType.COMPANY_DOES_NOT_EXIST, "selected company does not exist in the system");
		}
		return company;

	}

	// This method returns a list of all the registered companies
	public List<Company> getAllCompanies() throws ApplicationException {
		List<Company> allCompaniesList = companyDao.getAllCompanies();
		return allCompaniesList;
	}

}
