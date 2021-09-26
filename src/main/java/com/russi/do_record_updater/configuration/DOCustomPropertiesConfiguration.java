package com.russi.do_record_updater.configuration;

import com.russi.do_record_updater.DORecordUpdaterUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "config")
@Getter
@Setter
@Slf4j
public class DOCustomPropertiesConfiguration {

    @Autowired
    private ApplicationContext context;

    @Autowired
    DORecordUpdaterUtils utils;

    private Authentication authentication;
    private Project project;
    private String publicIpSiteResolver;
    private Schedule schedule;

    @Getter
    @Setter
    public static class Authentication {

        private String bearerToken;
    }

    @Getter
    @Setter
    public static class Project {
        private String name;
        private List<String> names;
        private Boolean useMultiProject = Boolean.FALSE;
    }

    @Getter
    @Setter
    public static class Schedule {
        private String updateCron;
    }

    @PostConstruct
    void validate() {
        authentication.setBearerToken(authentication.getBearerToken()
                .trim());
        if (authentication.getBearerToken()
                .contains("Bearer")) {
            log.error("Bearer token must not contains Bearer prefix");
            utils.shutdown();
        }
        if (authentication.getBearerToken()
                .equalsIgnoreCase("null")) {
            log.error("Invalid Bearer Token");
            utils.shutdown();
        }
        if (project.getUseMultiProject()) {
            log.info("Running in multi project mode");
            if (project.getNames().contains("null")) {
                log.error("multi-project mode | domains contains \"null\" value");
                utils.shutdown();
            }
            project.getNames().forEach(domain -> {
                if (!domain.contains(".")) {
                    log.error("multi-project mode | project is not a valid resource");
                    utils.shutdown();
                }
            });
        } else {
            log.info("Running in single project mode");
            if (!project.getName()
                    .contains(".")) {
                log.error("single-project mode | project is not a valid resource");
                utils.shutdown();
            }
        }
    }


}
