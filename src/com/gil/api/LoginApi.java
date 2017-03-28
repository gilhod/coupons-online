 package com.gil.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.gil.beans.User;
import com.gil.enums.ErrorType;
import com.gil.exceptions.ApplicationException;
import com.gil.logic.UserLogic;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Path("/login")

public class LoginApi {
	

	private UserLogic userLogic;

	
	public LoginApi() {
		userLogic = new UserLogic();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void login(@Context HttpServletRequest request, @Context HttpServletResponse response, User loginDetails) throws ApplicationException {
		
		//if login is successful, the user ID is returned. if login failed, 0 is returned
		Long userID = userLogic.UserLogin(loginDetails);
		String userIDStr = Long.toString(userID);
		
		long userTypeCode = loginDetails.getClientCode();
		String userTypeCodeStr = Long.toString(userTypeCode);
		
		//do this if login is successful
		if(userID != 0){
			request.getSession();
			Cookie cookie1 = new Cookie("userID", userIDStr);
			response.addCookie(cookie1);
			Cookie cookie2 = new Cookie("userTypeCode", userTypeCodeStr);
			response.addCookie(cookie2);
		}
		//do this if login is failed
		else{
			throw new ApplicationException(ErrorType.INVALID_LOGIN_DETAILS, "login failed. please check your name and password");
		}

	}
	
	@DELETE
	@Path("/logout")
	public void logout(@Context HttpServletRequest request){
		request.getSession(false).invalidate();
	}

}
