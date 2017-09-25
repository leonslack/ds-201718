package com.ucab.base.configurations;

import org.jsondoc.spring.boot.starter.EnableJSONDoc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.ucab.base.commons.interceptors.CORSInterceptor;
import com.ucab.base.webservices.controllers.IControllerBasePackage;

@EnableSpringDataWebSupport
@Configuration
@EnableWebMvc
@EnableJSONDoc
@ComponentScan(basePackageClasses = {IControllerBasePackage.class})
public class WebAppConfig extends WebMvcConfigurerAdapter  {
	
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CORSInterceptor());
    }
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("**/*.css", "**/*.js", "**/*.map", "*.html").addResourceLocations("classpath:META-INF/resources/").setCachePeriod(0);
	}
	
	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver createMultipartResolver() {
	    CommonsMultipartResolver resolver=new CommonsMultipartResolver();
	    resolver.setDefaultEncoding("utf-8");
	    return resolver;
	}
	
	@Bean(name="messageSource")
	public ResourceBundleMessageSource messageSource() {
	ResourceBundleMessageSource source = new ResourceBundleMessageSource();
	 source.setBasenames("messages");  // name of the resource bundle 
	 source.setUseCodeAsDefaultMessage(true);
	 return source;
	}
	
	

}
