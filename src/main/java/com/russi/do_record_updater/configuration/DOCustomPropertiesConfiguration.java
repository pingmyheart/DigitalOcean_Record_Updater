package com.russi.do_record_updater.configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@ConfigurationProperties(prefix = "config")
@Getter
@Setter
@Slf4j
public class DOCustomPropertiesConfiguration {

    @Autowired
    private ApplicationContext context;

    private Authentication authentication;
    private Project project;
    private String publicIpSiteResolver;
    private Schedule schedule;

    @Getter
    @Setter
    private static class Authentication {

        private String bearerToken;
    }

    @Getter
    @Setter
    private static class Project {
        private String name;
    }

    @Getter
    @Setter
    private static class Schedule {
        private String updateCron;
    }

    @PostConstruct
    void validate() {
        authentication.setBearerToken(authentication.getBearerToken()
                .trim());
        if (authentication.getBearerToken()
                .contains("Bearer")) {
            log.error("Bearer token must not contains Bearer prefix");
            SpringApplication.exit(context, () -> 0);
        }
        if (authentication.getBearerToken()
                .equalsIgnoreCase("null")) {
            log.error("Invalid Bearer Token");
            SpringApplication.exit(context, () -> 0);
        }
        if (!project.getName()
                .contains(".")) {
            log.error("Invalid domain name");
            SpringApplication.exit(context, () -> 0);
        }
    }
}
