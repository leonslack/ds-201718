package com.ucab.base.security.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.ucab.base.commons.helpers.HttpCommunicationHandler;
import com.ucab.base.services.security.sessionless.TokenAuthenticationManager;

public class StatelessAuthenticationFilter extends GenericFilterBean {

    private final TokenAuthenticationManager authenticationService;

    public StatelessAuthenticationFilter(TokenAuthenticationManager authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        Authentication authentication = authenticationService.getAuthentication(httpRequest);
        if(!authentication.isAuthenticated())
        {
        	response = HttpCommunicationHandler.AddCORSHeadertoResponse((HttpServletRequest)request, (HttpServletResponse)response);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
