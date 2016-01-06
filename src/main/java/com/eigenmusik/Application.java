package com.eigenmusik;

import com.google.common.base.Predicate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

@EnableAutoConfiguration
@Configuration
@EnableSwagger2
@ComponentScan
public class Application extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .paths(apiPaths())
                .build();
    }

    private Predicate<String> apiPaths() {
        return or(
                regex("/user.*"),
                regex("/tracks.*"),
                regex("/source.*")
        );

    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("EigenMusik API")
                .description("This is the documentation for the EigenMusik API.")
                .contact("tim@eigenmusik.com")
                .license("Apache License Version 2.0")
                .licenseUrl("https://github.com/eigenmusik")
                .version("pre-alpha")
                .build();
    }
}
