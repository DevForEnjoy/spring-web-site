package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Value("${upload.path}")
    private String uploadPath;


    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/").setViewName("home");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/main").setViewName("main");
        registry.addViewController("/main/add").setViewName("main-add");
        registry.addViewController("/main/addAD").setViewName("home-addAD");
        registry.addViewController("/main/registrationAdmin").setViewName("registrationAdmin");


    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images").addResourceLocations("classpath:static/img/**");
        registry.addResourceHandler("/css").addResourceLocations("classpath:/static/css/**");
        registry.addResourceHandler("/files/**").addResourceLocations( "file://" + uploadPath + "/" );
    }
}