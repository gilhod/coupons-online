package com.gil.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.gil.beans.ErrorBean;

@Provider
public class ExceptionsHandler extends Exception implements ExceptionMapper<Throwable> {
	private static final long serialVersionUID = 1L;
	
	@Override
    public Response toResponse(Throwable exception) 
    {
    	if (exception instanceof ApplicationException){
    		ApplicationException e = (ApplicationException) exception;
    		exception.printStackTrace();
    		int internalErrorCode = e.getErrorType().getInternalErrorCode();
    		String message = e.getMessage();
    		ErrorBean errorBean = new ErrorBean(internalErrorCode, message);
    		return Response.status(700).entity(errorBean).build();
    	}
        exception.printStackTrace();
    	return Response.status(500).entity("General failure").build();
    }
}
