package com.russi.do_record_updater.util;

import com.russi.do_record_updater.configuration.DOCustomPropertiesConfiguration;
import com.russi.do_record_updater.interfaces.IpInfoInterface;
import com.russi.do_record_updater.util.DORecordUpdaterUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class Constants {

    private static DORecordUpdaterUtils utils;
    private static DOCustomPropertiesConfiguration configuration;
    public static String ipAddress;

    public Constants(@Autowired DOCustomPropertiesConfiguration customPropertiesConfiguration,
                      @Autowired DORecordUpdaterUtils updaterUtils,
                      @Autowired IpInfoInterface ipInfoInterface) {
        configuration = customPropertiesConfiguration;
        utils = updaterUtils;
        ipAddress = ipInfoInterface.getIp();
    }

    @PostConstruct
    void init(){
        log.info(MessageFormat.format("Current public IP is {0}", ipAddress));
    }

    public static List<String> domains = new ArrayList<>();

    public static void checkDomainsConfiguration() {
        if(!domains.isEmpty()){
            if (Boolean.TRUE.equals(configuration.getProject()
                    .getUseMultiProject())) {
                domains.forEach(d -> {
                    if (!d.contains("@")) {
                        log.error("multi-project mode | domains need to have @ sign to specify project");
                        utils.shutdown();
                    }
                });
            }
        }else{
            log.error("Domain args can not be empty");
            utils.shutdown();
        }
    }
}
