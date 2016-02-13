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
import springfox.documentation.swagger.web.SecurityConfiguration;

import java.security.Principal;
import java.util.List;

import static com.google.common.base.Predicates.or;
import static com.google.common.collect.Lists.newArrayList;
import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
public class SwaggerConfiguration {

    public static final String securitySchemaOAuth2 = "oauth2schema";
    public static final String authorizationScopeGlobal = "global";
    public static final String authorizationScopeGlobalDesc ="accessEverything";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .paths(apiPaths())
                .build()
                .securitySchemes(newArrayList(securitySchema()))
                .securityContexts(newArrayList(securityContext()))
                .ignoredParameterTypes(Principal.class, Pageable.class);
    }

    public AuthorizationScope global() {
        return new AuthorizationScope("global", "accessEverything");
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope(authorizationScopeGlobal, authorizationScopeGlobalDesc);
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return newArrayList(
                new SecurityReference(securitySchemaOAuth2, authorizationScopes));
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(apiPaths())
                .build();
    }

    private Predicate<String> apiPaths() {
        return or(
                regex("/sources.*"),
                regex("/tracks.*"),
                regex("/user.*")
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

    private OAuth securitySchema() {
        TokenRequestEndpoint tre = new TokenRequestEndpoint("/oauth/token", "web", "secret");
        TokenEndpoint te = new TokenEndpoint("/oauth/token", "access_token");
        GrantType grantType = new AuthorizationCodeGrant(tre, te);
        return new OAuth(securitySchemaOAuth2, newArrayList(global()), newArrayList(grantType));
    }
}
