package com.ucab.base.commons.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ucab.base.commons.helpers.HttpCommunicationHandler;

public class CORSInterceptor extends HandlerInterceptorAdapter {
	
	 @Override
	 public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
	  HttpCommunicationHandler.AddCORSHeadertoResponse(request, response);
	  return true;
	 }

}
