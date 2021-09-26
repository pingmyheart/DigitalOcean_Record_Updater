package com.russi.do_record_updater;

import com.russi.do_record_updater.configuration.DOCustomPropertiesConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class Constants {

    private static DORecordUpdaterUtils utils;
    private static DOCustomPropertiesConfiguration configuration;

    private Constants(@Autowired DOCustomPropertiesConfiguration customPropertiesConfiguration,
                      @Autowired DORecordUpdaterUtils updaterUtils) {
        configuration = customPropertiesConfiguration;
        utils = updaterUtils;
    }

    public static List<String> domains = new ArrayList<>();

    public static void checkDomainsConfiguration() {
        if (Boolean.TRUE.equals(configuration.getProject()
                .getUseMultiProject())) {
            domains.forEach(d -> {
                if (!d.contains("@")) {
                    log.error("multi-project mode | domains need to have @ sign to specify project");
                    utils.shutdown();
                }
            });
        }
    }
}
