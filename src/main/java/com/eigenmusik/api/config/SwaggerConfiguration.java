package com.eigenmusik.api.config;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.security.Principal;
import java.util.List;

import static com.google.common.base.Predicates.or;
import static com.google.common.collect.Lists.newArrayList;
import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
public class SwaggerConfiguration {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .paths(apiPaths())
                .build()
                .securitySchemes(newArrayList(bearerToken()))
                .securityContexts(newArrayList(securityContext()))
                .ignoredParameterTypes(Principal.class, Pageable.class);
    }

    private ApiKey bearerToken() {
        return new ApiKey(bearerSchema, "bearer", "header");
    }

    public static final String bearerSchema = "bearer";

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("/.*"))
                .build();
    }

    public AuthorizationScope global()
    {
        return new AuthorizationScope("global", "accessEverything");
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = global();
        return newArrayList(
                new SecurityReference(bearerSchema, authorizationScopes));
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
