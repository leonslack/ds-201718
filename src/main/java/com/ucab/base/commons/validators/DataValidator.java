package com.ucab.base.commons.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.ucab.base.commons.exceptions.CustomBaseException;
import com.ucab.base.commons.exceptions.CustomInvalidAttributeException;
import com.ucab.base.commons.exceptions.CustomMissingAttributeException;

public class DataValidator {
	
	final static Logger log = Logger.getLogger(DataValidator.class);

	public static boolean IsValidEmail(String emailAddress) throws CustomBaseException {
		log.debug("Validating EmailAdress");		
		if (emailAddress == null || emailAddress.isEmpty() )
		{
			log.info("The given email is null or empty");
			throw new CustomMissingAttributeException("Null or Empty Attribute emailAdress");
		}	
		else if( !emailAddress.contains("@")){
			log.info("The given email address does not cointains @");
			throw new CustomInvalidAttributeException("Invalid Client Email Address:"+emailAddress);
		}		
		log.debug("The email adress "+emailAddress+" Is valid");
		return true;
		
	}

	public static boolean IsEmailFormat(String emailAddress)throws CustomBaseException{
		boolean response = false;
		if (emailAddress == null || emailAddress.isEmpty() )
		{
			log.info("The given email is null or empty");
			throw new CustomMissingAttributeException("Null or Empty Attribute email");
		}
		String EMAIL_PATTERN = 
				"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";	
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		response = pattern.matcher(emailAddress).find();
		return response;
	}
	
	public static boolean IsValidPhoneNumber(String phoneNumber) throws CustomBaseException {
		log.debug("Validating PhoneNumber");		
		if (phoneNumber == null || phoneNumber.isEmpty() )
		{
			log.info("The given id PhoneNumber is null or Empty");
			throw new CustomMissingAttributeException("Null or empty Attribute PhoneNumber");
		}		
		log.debug("The PhoneNumber "+phoneNumber+" Is valid");
		return true;
		
	}

	public static void IsValidPasswordFormat(String password) throws CustomBaseException{
		if(password==null)
		{
		 log.info("The given password is empty.");
		 throw new CustomMissingAttributeException("Null Attribute Password");
		}
		Pattern patternSC = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
		Matcher matcherSC = patternSC.matcher(password);
		Pattern patternA = Pattern.compile("[a-z]+", Pattern.CASE_INSENSITIVE);
		Matcher matcherA = patternA.matcher(password); 
		log.debug("Validating Password");
		
		if(!matcherSC.find()){
			throw new CustomInvalidAttributeException("Password must cointain special characters");
		}
		else if(password.length()<8){
			throw new CustomInvalidAttributeException("Password must contain at least 8 characters");
		}
		else if(!matcherA.find()){
			throw new CustomInvalidAttributeException("password must have at least one letter");	
		}
	}
	
	public static void IsStringNull(String string) throws CustomBaseException{
		if((string==null)||(string.isEmpty()))
		{
		 log.info("The given name is empty.");
		 throw new CustomMissingAttributeException("Null Attribute");
		}
	}
}
