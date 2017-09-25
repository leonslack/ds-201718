package com.ucab.base.commons.helpers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/*	
 *	This Class will contain all tasks involving the modification of a HttpRequest or HttpResponse	 
 */
public class HttpCommunicationHandler {
	
	final static Logger log = Logger.getLogger(HttpCommunicationHandler.class);
	
	private static final String CREDENTIALS_NAME = "Access-Control-Allow-Credentials";
	private static final String ORIGIN_NAME = "Access-Control-Allow-Origin";
	private static final String METHODS_NAME = "Access-Control-Allow-Methods";
	private static final String HEADERS_NAME = "Access-Control-Allow-Headers";
	private static final String HEADERS_EXPOSE = "Access-Control-Expose-Headers";
	private static final String MAX_AGE_NAME = "Access-Control-Max-Age";
	
	public static HttpServletResponse AddCORSHeadertoResponse(HttpServletRequest request ,HttpServletResponse response)
	{
		String origin = request.getHeader("Origin");
		response.setHeader(CREDENTIALS_NAME, "true");
		if(origin != null){
			response.setHeader(ORIGIN_NAME, origin);
		}
		else{
			response.setHeader(ORIGIN_NAME, "*");
		}
		response.setHeader(METHODS_NAME, "GET, OPTIONS, POST, PUT, DELETE, PATCH");
		response.setHeader(HEADERS_NAME, "Origin, X-Requested-With, Content-Type, Accept, X-Auth-Token");
		response.setHeader(HEADERS_EXPOSE, "Origin, X-Auth-Token");
		response.setHeader(MAX_AGE_NAME, "3600");
		return response;
	}
	
	public static String requestIP(HttpServletRequest request)
	{
		String ipAddress;
		
		ipAddress = request.getHeader("X-FORWARDED-FOR");  
		if (ipAddress == null) {  
		   ipAddress = request.getRemoteAddr();  
		}
		return ipAddress;
	}

}

