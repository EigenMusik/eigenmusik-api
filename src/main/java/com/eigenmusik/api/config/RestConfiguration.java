package com.eigenmusik.api.config;

import com.eigenmusik.api.tracks.Track;
import com.eigenmusik.api.user.User;
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

/**
 * Rest configuration.
 */
@Configuration
@Import(RepositoryRestMvcConfiguration.class)
public class RestConfiguration extends RepositoryRestMvcConfiguration {
    private static Logger logger = Logger.getLogger(RestConfiguration.class);

    @Override
    protected void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        try {
            config.setBaseUri(new URI("/"));
            config.exposeIdsFor(User.class);
            config.exposeIdsFor(Track.class);
        } catch (URISyntaxException e) {
            logger.error("Failed to set base uri.", e);
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
