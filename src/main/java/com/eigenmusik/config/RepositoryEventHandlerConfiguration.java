package com.eigenmusik.config;

import com.eigenmusik.domain.events.AccountEventHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryEventHandlerConfiguration {

    @Bean
    AccountEventHandler accountEventHandler() {
        return new AccountEventHandler();
    }

}
