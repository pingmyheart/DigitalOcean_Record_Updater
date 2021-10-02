package com.russi.do_record_updater.util;

import com.russi.do_record_updater.configuration.DOCustomPropertiesConfiguration;
import com.russi.do_record_updater.interfaces.IpInfoInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class Constants {

    static DORecordUpdaterUtils utils;
    static DOCustomPropertiesConfiguration configuration;
    public static String ipAddress;

    public Constants(@Autowired DOCustomPropertiesConfiguration customPropertiesConfiguration,
                     @Autowired DORecordUpdaterUtils updaterUtils,
                     @Autowired IpInfoInterface ipInfoInterface) throws IOException {
        configuration = customPropertiesConfiguration;
        utils = updaterUtils;
        if (!isNetAvailable()) {
            log.error("Internet connection is not established");
            utils.shutdown();
        }
        ipAddress = ipInfoInterface.getIp();
    }

    @PostConstruct
    void init() {
        log.info(MessageFormat.format("Current public IP is {0}", ipAddress));
    }

    public static List<String> domains = new ArrayList<>();

    public static void checkDomainsConfiguration() {
        if (!domains.isEmpty()) {
            if (Boolean.TRUE.equals(configuration.getProject()
                    .getUseMultiProject())) {
                domains.forEach(d -> {
                    if (!d.contains("@")) {
                        log.error("multi-project mode | domains need to have @ sign to specify project");
                        utils.shutdown();
                    }
                });
            }
        } else {
            log.error("Domain args can not be empty");
            utils.shutdown();
        }
    }

    private boolean isNetAvailable() {
        try {
            final URL url = new URL("http://www.google.com");
            final URLConnection conn = url.openConnection();
            conn.connect();
            conn.getInputStream().close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
