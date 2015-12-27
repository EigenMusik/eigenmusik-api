package com.eigenmusik.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by timcoulson on 27/12/2015.
 */
@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
public class DatabaseConfiguration {

    private String url;

    public void setUrl(String url) {
        this.url = url;
    }

    @Bean
    @Primary
    public BasicDataSource dataSource() throws URISyntaxException {
        URI dbUri = new URI(url);
        BasicDataSource basicDataSource = new BasicDataSource();

        String[] userInfo = dbUri.getUserInfo().split(":");
        if (userInfo.length >= 1) {
            basicDataSource.setUsername(userInfo[0]);
        }

        if (userInfo.length == 2) {
            basicDataSource.setPassword(userInfo[1]);
        }

        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

        basicDataSource.setUrl(dbUrl);
        return basicDataSource;
    }
}
