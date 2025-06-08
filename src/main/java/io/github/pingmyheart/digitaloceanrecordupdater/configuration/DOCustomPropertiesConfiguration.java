package io.github.pingmyheart.digitaloceanrecordupdater.configuration;

import io.github.pingmyheart.digitaloceanrecordupdater.util.CronUtils;
import io.github.pingmyheart.digitaloceanrecordupdater.util.DORecordUpdaterUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.support.CronExpression;

import javax.annotation.PostConstruct;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Configuration
@ConfigurationProperties(prefix = "config")
@Getter
@Setter
@Slf4j
public class DOCustomPropertiesConfiguration {

    ApplicationContext context;
    DORecordUpdaterUtils utils;
    CronUtils cronUtils;

    private Authentication authentication;
    private Project project;
    private String publicIpSiteResolver;
    private Schedule schedule;

    public DOCustomPropertiesConfiguration(@Autowired ApplicationContext context,
                                           @Autowired DORecordUpdaterUtils utils,
                                           @Autowired CronUtils cronUtils) {
        this.context = context;
        this.utils = utils;
        this.cronUtils = cronUtils;
    }

    @PostConstruct
    void validate() {
        printProjectMode();
        baseProjectConfigurationChecker();
        if (project.getUseMultiProject()) {
            multiProjectChecker();
        } else {
            singleProjectChecker();
        }
    }

    private void baseProjectConfigurationChecker() {
        authentication.setBearerToken(authentication.getBearerToken()
                .trim());
        if (authentication.getBearerToken()
                .toLowerCase()
                .contains("bearer")) {
            log.error("Bearer token must not contains \"Bearer\" prefix");
            utils.shutdown();
        }
        if (authentication.getBearerToken()
                .equalsIgnoreCase("null")) {
            log.error("Invalid Bearer Token");
            utils.shutdown();
        }
        cronExpressionChecker();
    }

    private void multiProjectChecker() {
        if (project.getNames().contains("null")) {
            log.error("Multi-Project Mode | Domains contains \"null\" value");
            utils.shutdown();
        }
        project.getNames().forEach(domain -> {
            if (!domain.contains(".")) {
                log.error("Multi-Project Mode | Project is not a valid resource");
                utils.shutdown();
            }
        });
    }

    private void singleProjectChecker() {
        if (!project.getName()
                .contains(".")) {
            log.error("Single-Project Mode | Project is not a valid resource");
            utils.shutdown();
        }
    }

    private void cronExpressionChecker() {
        Optional.ofNullable(schedule.getUpdateCron())
                .ifPresentOrElse(cron -> {
                            if (Boolean.FALSE.equals(CronExpression.isValidExpression(cron))) {
                                log.error("cron expression is not valid");
                                utils.shutdown();
                            }
                        },
                        () -> {
                            log.error("cron expression can not be empty");
                            utils.shutdown();
                        });
        Optional.ofNullable(schedule.getIpAddressUpdate())
                .ifPresentOrElse(cron -> {
                            if (Boolean.FALSE.equals(CronExpression.isValidExpression(cron))) {
                                log.error("cron expression is not valid");
                                utils.shutdown();
                            }
                        },
                        () -> {
                            log.error("cron expression can not be empty");
                            utils.shutdown();
                        });
        if (Boolean.FALSE.equals(utils.getShut())) {
            log.info(MessageFormat.format("Application started with \"{0}\" cron domain update expression", cronUtils.describeCron(schedule.getUpdateCron())));
            log.info(MessageFormat.format("Application started with \"{0}\" cron ip address update expression", cronUtils.describeCron(schedule.getIpAddressUpdate())));
        }
    }

    private void printProjectMode() {
        if (project.getUseMultiProject()) {
            log.info("Running in Multi-Project Mode");
        } else {
            log.info("Running in Single-Project Mode");
        }
    }

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
        private String ipAddressUpdate;
    }

}
