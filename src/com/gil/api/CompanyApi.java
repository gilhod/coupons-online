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

import com.gil.beans.Company;
import com.gil.exceptions.ApplicationException;
import com.gil.logic.CompanyLogic;


@Path("/company")

public class CompanyApi {
	
	private CompanyLogic companyLogic;
	
	public CompanyApi(){
		companyLogic = new CompanyLogic();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void createCompany(Company company) throws ApplicationException{
		companyLogic.createCompany(company);
	}
	
	
	@DELETE
	@Path("/{id}")
	public void deleteCompany(@PathParam("id") long companyID) throws ApplicationException {
		companyLogic.deleteCompany(companyID);
	}
	
	@PUT
	@Path("/email/{ID}")
	public void updateCompanyEmail(@PathParam("ID") long companyID, String newEmail) throws ApplicationException{
		companyLogic.updateCompanyEmail(companyID, newEmail);
		
	}
	
	@PUT
	@Path("/password/{ID}")
	public void updateCompanyPassword(@PathParam("ID") long companyID, String newPassword) throws ApplicationException{
		companyLogic.updateCompanyPassword(companyID, newPassword);
	}
	
	@GET
	@Path("/{ID}")
	@Produces(MediaType.APPLICATION_JSON)
	public Company getCompany(@PathParam("ID") long companyID) throws ApplicationException {
		Company company = companyLogic.getCompany(companyID);
		return company;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Company> getAllCompanies() throws ApplicationException {		
		List<Company> allCompaniesList = companyLogic.getAllCompanies();
		return allCompaniesList;
	}

	
	
		
	
	
	
}
