package com.gil.api;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.gil.beans.User;
import com.gil.enums.ClientType;
import com.gil.utils.SystemLogger;


public class RequestFilter implements Filter {

	@Override
	public void doFilter( ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);
        String loginURI = "/CouponsPhase2/rest/login";
        String requestURI = request.getRequestURI();
        String requestPath = requestURI.substring("/CouponsPhase2/rest/".length());
        User userDetails = null;
        
        //always allow request for login
        if(requestURI.equals(loginURI)){      
        	chain.doFilter(request, response);
        	return;
        }
        
        //request without session - not allowed
        else if(session == null){             
        	response.setStatus(401);
        }
        
        //inspect the request for authorized role is not for login
        else{
        	userDetails = extractUserDetailsFromCookies(request);
        	ClientType allowedRole = assignAllowedRole(requestPath);
        	
        	//request not allowed since allowed role is not null and user role don't match allowed role 
        	if(allowedRole !=null && allowedRole.getClientCode() != userDetails.getClientCode()){
        		SystemLogger.logger.warn("Forbbiden access. User ID: " + userDetails.getUserId()  + "; user type code: "+ userDetails.getClientCode()+ "; requested URI: " + requestURI);
        		response.setStatus(403);
        	}
        	
        	
        	else{
        		request.setAttribute("userID", userDetails.getUserId());
        		chain.doFilter(request, response);
            	return;
        	}
        	
        	
        }
	}

	
	//This method returns the allowed client type for a certain api path 
	//null is returned if the path has no role restriction
	private ClientType assignAllowedRole(String requestPath) {
		ClientType allowedRole=null;
		if(requestPath.startsWith("company")||requestPath.startsWith("customer")){
			allowedRole = ClientType.ADMIN;
		}
		else if(requestPath.startsWith("coupon")){
			allowedRole = ClientType.COMPANY;
		}
		else if(requestPath.startsWith("purchase")){
			allowedRole = ClientType.CUSTOMER;
		}
		return allowedRole;
	}
	
	//This method extract data from the cookies.
	//it looks for two cookies: user id and user type code, and returns
	//User object with those cookie values
	private  User extractUserDetailsFromCookies(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();        	       	
		User userDetails = new User();
		if(cookies!=null){
    		for (int i=0;i<cookies.length; i++){
    			if(cookies[i].getName().equals("userID")){
    				String userIDStr = cookies[i].getValue();
    				long userID = Long.parseLong(userIDStr);
    				userDetails.setUserId(userID);
    				
    			}
    			else if(cookies[i].getName().equals("userTypeCode")){
    				String userTypeCodeStr = cookies[i].getValue();
    				int userTypeCode = Integer.parseInt(userTypeCodeStr);
    				userDetails.setClientCode(userTypeCode);
    				
    			}
    		}
		
	}
		return userDetails;
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	@Override
	public void init(FilterConfig fConfig) throws ServletException {
	}
	
	   /**
     * Default constructor. 
     */
	
    public RequestFilter() {
        
    }

	/**
	 * @see Filter#destroy()
	 */
    @Override
	public void destroy() {
	}

}
