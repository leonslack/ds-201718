package com.ucab.base.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ucab.base.security.filters.SessionAuthenticationFilter;
import com.ucab.base.security.filters.SessionLoginFilter;
import com.ucab.base.services.security.SecurityBasePackage;
import com.ucab.base.services.security.SecurityUserService;
import com.ucab.base.services.security.sessionful.IAuthenticationService;
import com.ucab.base.services.security.sessionful.SessionfulBasePackage;


@Configuration
@EnableWebSecurity
@Order(2)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan(basePackageClasses = {SecurityBasePackage.class, SessionfulBasePackage.class, SessionLoginFilter.class})
public class SessionfulSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private SecurityUserService userService;

	@Autowired
	private IAuthenticationService authenticationService;
	
    public SessionfulSecurityConfig() {
        super(true);
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
     
     
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
    
    @Override
    public void configure(WebSecurity web) throws Exception {
        
    	//Ignores anonymous resource request
    	web.ignoring()
    		.antMatchers("/api/user/forgotpassword")
    		.antMatchers("/api/user/{userId}/resetpasswordrequest")
    		.antMatchers("/api/user/{userId}/resetpassword")
    		.antMatchers("/css/**.css")
    		.antMatchers("/js/**.js")
    		.antMatchers("/webjars/jsondoc-ui-webjar/js/**.js")
    		.antMatchers("/webjars/jsondoc-ui-webjar/css/**.css")
    		.antMatchers("/jsondoc")
    		.antMatchers("/jsondoc-ui.html");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	
        http
		        .exceptionHandling().and()
		        .anonymous().and()
		        .servletApi().and()
		        .headers().cacheControl().and().and()
		        .authorizeRequests()
                
                // Allow anonymous resource request
		       .antMatchers("/*").permitAll()
               .antMatchers("/favicon.ico").permitAll()
               .antMatchers("**/**.html").permitAll()
               .antMatchers("/css/**.css").permitAll()
               .antMatchers("/js/**.js").permitAll()
               .antMatchers("/open/api/**").permitAll()

               .antMatchers(HttpMethod.OPTIONS,"/**").permitAll()
               // All other request need to be authenticated
               .anyRequest().authenticated().and()
                
                //custom JSON based authentication by POST of {"username":"<name>","password":"<password>"} which sets the token header upon authentication
				.addFilterBefore(new SessionLoginFilter("/api/auth", authenticationService, authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                
				// custom Token based authentication based on the header previously given to the client
				.addFilterBefore(new SessionAuthenticationFilter(authenticationService), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    @Override
    public SecurityUserService userDetailsService() {
        return userService;
    }

    @Bean
    public IAuthenticationService authenticationService() {
        return authenticationService;
    }

}
