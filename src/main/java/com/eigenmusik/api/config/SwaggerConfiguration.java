package com.eigenmusik.api.config;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
public class SwaggerConfiguration {
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
