package com.eigenmusik.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

@Configuration
@EnableWebMvc
public class MvcConfig extends WebMvcConfigurerAdapter {
    private static final String SWAGGER_UI_RESOURCE_PATH = "classpath:/META-INF/resources/webjars/eigenmusik-swagger-ui/2.1.4/";
    private static final String JACOCO_RESOURCE_PATH = "classpath:/META-INF/resources/coverage/";
    private static final String JAVADOC_RESOURCE_PATH = "classpath:/META-INF/resources/javadoc/";


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger/**")
                .addResourceLocations(SWAGGER_UI_RESOURCE_PATH);
        registry.addResourceHandler("/coverage/**")
                .addResourceLocations(JACOCO_RESOURCE_PATH);
        registry.addResourceHandler("/javadoc/**")
                .addResourceLocations(JAVADOC_RESOURCE_PATH);
    }
}
