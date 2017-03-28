package com.gil.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//This class provides tool for input validations
public class ValidationUtils {
	


	//Password validation method. 
	//Password must be between 6 to 12 characters in length, and contain at 
	//least 1 uppercase letter, 1 lowercase letter, and 1 digit.
	public static boolean isPasswordValid(String password) {

		String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{6,12}$";
		Pattern checkRegex = Pattern.compile(regex);	
		Matcher regexMatcher = checkRegex.matcher(password);
		boolean result = regexMatcher.find();
		return result;
	}

	// Email validation method. 
	public static boolean isEmailValid(String email) {
		
		//source for legal email regex pattern:
		//www.regular-expressions.info/email.html
		String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
		Pattern checkRegex = Pattern.compile(regex);	
		Matcher regexMatcher = checkRegex.matcher(email);
		boolean result = regexMatcher.find();
		return result;
		
	}

}
