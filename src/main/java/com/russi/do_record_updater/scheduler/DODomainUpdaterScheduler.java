package com.russi.do_record_updater.scheduler;

import com.russi.do_record_updater.Constants;
import com.russi.do_record_updater.component.DORecordUpdaterImpl;
import com.russi.do_record_updater.dto.request.UpdateRecordRequestDTO;
import com.russi.do_record_updater.interfaces.IpInfoInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.MessageFormat;
import java.util.stream.Collectors;

@Component
@Slf4j
public class DODomainUpdaterScheduler {

    @Autowired
    IpInfoInterface ipInfoInterface;

    @Autowired
    DORecordUpdaterImpl doRecordUpdater;

    @Value("${config.authentication.bearer-token}")
    String bearerToken;

    @Value("${config.project.name}")
    String baseDomain;

    String ipAddress;

    @PostConstruct
    void init() {
        ipAddress = ipInfoInterface.getIp();
    }

    @Scheduled(cron = "${config.schedule.update-cron}")
    void updateRecord() {
        log.info(MessageFormat.format("Current ip is {0}", ipAddress));
        doRecordUpdater.getAllDomains()
                .getDomainRecords()
                .stream()
                .filter(o -> o.getType().equals("A"))
                .collect(Collectors.toList())
                .forEach(e -> {
                    if (Constants.domains.contains(e.getName())) {
                        if (Boolean.TRUE.equals(doRecordUpdater.updateRecord(String.valueOf(e.getId()), UpdateRecordRequestDTO.builder()
                                .name(e.getName())
                                .data(ipAddress)
                                .build()))) {
                            log.info(MessageFormat.format("Domain {0}.{1} successfully updated", e.getName(), baseDomain));
                        } else {
                            log.error(MessageFormat.format("Error occurred while updating domain {0}.{1} ", e.getName(), baseDomain));
                        }
                    }
                });
    }
}
