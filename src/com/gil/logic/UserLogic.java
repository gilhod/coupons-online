package com.gil.logic;

import com.gil.beans.User;
import com.gil.dao.CompanyDao;
import com.gil.dao.CustomerDao;
import com.gil.enums.ClientType;
import com.gil.exceptions.ApplicationException;

public class UserLogic {
	
	private static final  String ADMIN_NAME = "admin";
	private static final  String ADMIN_PASSWORD = "1234";

	public long UserLogin(User userLogin) throws ApplicationException {
		
		long id=0;
		ClientType clientType = ClientType.getClientType(userLogin.getClientCode());
		
		switch (clientType){
		case ADMIN:
			id = adminLogin(userLogin.getUsername(), userLogin.getPassword());
			break;
		case COMPANY:
			id = companyLogin(userLogin.getUsername(), userLogin.getPassword());
			break;
		case CUSTOMER:
			id = customerLogin(userLogin.getUsername(), userLogin.getPassword());
			break;
		}
		return id;
	}

	private long adminLogin(String name, String password) throws ApplicationException {
		if (name.equals(ADMIN_NAME) && password.equals(ADMIN_PASSWORD)){
			return 1;
		}
		return 0;
	}
	
	private long companyLogin(String name, String password) throws ApplicationException {
		CompanyDao companyDao = new CompanyDao();
		long id = companyDao.isCompanyNamePasswordMatch(name, password);
		return id;
	}

	private long customerLogin(String name, String password) throws ApplicationException {
		CustomerDao customerDao = new CustomerDao();
		long id = customerDao.isCustomerNamePasswordMatch(name, password);
		return id;
	}

	
}
