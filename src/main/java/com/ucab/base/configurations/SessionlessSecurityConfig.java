package com.ucab.base.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ucab.base.security.filters.StatelessAuthenticationFilter;
import com.ucab.base.security.filters.StatelessLoginFilter;
import com.ucab.base.services.security.SecurityBasePackage;
import com.ucab.base.services.security.SecurityUserService;
import com.ucab.base.services.security.sessionless.SessionlessBasePackage;
import com.ucab.base.services.security.sessionless.TokenAuthenticationManager;

@Configuration
@EnableWebSecurity
@Order(2)
@ComponentScan(basePackageClasses = {SecurityBasePackage.class, SessionlessBasePackage.class})
public class SessionlessSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private SecurityUserService userService;

	@Autowired
	private TokenAuthenticationManager tokenManager;
	
    public SessionlessSecurityConfig() {
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
    protected void configure(HttpSecurity http) throws Exception {
    	
        http
		        .exceptionHandling().and()
		        .anonymous().and()
		        .servletApi().and()
		        .headers().cacheControl().and().and()
		        .authorizeRequests()
                
                // Allow anonymous resource requests
                .antMatchers("/*").permitAll()
                .antMatchers("/favicon.ico").permitAll()
                .antMatchers("**/**.html").permitAll()
                .antMatchers("/css/**.css").permitAll()
                .antMatchers("/js/**.js").permitAll()				
				.antMatchers("/api/user/createUser").permitAll()
				.antMatchers("/open/api/**").permitAll()

                // All other request need to be authenticated
                .anyRequest().authenticated().and()

                // custom JSON based authentication by POST of {"username":"<name>","password":"<password>"} which sets the token header upon authentication
				.addFilterBefore(new StatelessLoginFilter("/api/auth", tokenManager, userService, authenticationManager()), UsernamePasswordAuthenticationFilter.class)

				// custom Token based authentication based on the header previously given to the client
				.addFilterBefore(new StatelessAuthenticationFilter(tokenManager), UsernamePasswordAuthenticationFilter.class);
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
    public TokenAuthenticationManager tokenAuthenticationService() {
        return tokenManager;
    }

}
