package com.ucab.base.configurations;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.ucab.base.services.IBaseServicePackage;


@Configuration
@ComponentScan(basePackageClasses={IBaseServicePackage.class})
public class ServiceConfig {

}
