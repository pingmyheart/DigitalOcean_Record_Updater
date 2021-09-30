package com.russi.do_record_updater;

import com.russi.do_record_updater.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.text.MessageFormat;
import java.util.Arrays;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
@Slf4j
public class DoRecordUpdaterApplication {

    public static void main(String[] args) {
        SpringApplication.run(DoRecordUpdaterApplication.class, args);
        Arrays.asList(args).forEach(domain -> {
            log.info(MessageFormat.format("domain to be updated -> {0}", domain));
            Constants.domains.add(domain);
        });
        Constants.checkDomainsConfiguration();
    }
}