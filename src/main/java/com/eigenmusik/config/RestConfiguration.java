package com.eigenmusik.config;

import com.eigenmusik.domain.Account;
import com.eigenmusik.domain.AccountValidator;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
@Import(RepositoryRestMvcConfiguration.class)
public class RestConfiguration extends RepositoryRestMvcConfiguration {
    private static Logger logger = Logger.getLogger(RestConfiguration.class);

    @Override
    protected void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        try {
            config.setBaseUri(new URI("/rest"));
            config.exposeIdsFor(Account.class);
        } catch (URISyntaxException e) {
            logger.error("failed to set base uri", e);
        }
    }

    @Override
    @Bean
    public HateoasPageableHandlerMethodArgumentResolver pageableResolver() {
        HateoasPageableHandlerMethodArgumentResolver resolver = super.pageableResolver();
        resolver.setOneIndexedParameters(true);
        return resolver;
    }

    @Override
    protected void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener validatingListener) {
    }
}
