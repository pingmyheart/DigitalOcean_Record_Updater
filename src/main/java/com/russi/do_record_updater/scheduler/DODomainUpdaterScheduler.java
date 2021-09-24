package com.russi.do_record_updater.scheduler;

import com.russi.do_record_updater.Constants;
import com.russi.do_record_updater.component.DORecordUpdaterImpl;
import com.russi.do_record_updater.configuration.DOCustomPropertiesConfiguration;
import com.russi.do_record_updater.dto.request.UpdateRecordRequestDTO;
import com.russi.do_record_updater.dto.response.GenericDomainResponseDTO;
import com.russi.do_record_updater.interfaces.IpInfoInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.MessageFormat;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class DODomainUpdaterScheduler {

    @Autowired
    IpInfoInterface ipInfoInterface;

    @Autowired
    DORecordUpdaterImpl doRecordUpdater;

    @Autowired
    DOCustomPropertiesConfiguration properties;

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
    void scheduledUpdateRecord() {
        log.info(MessageFormat.format("Current ip is {0}", ipAddress));
        if (Boolean.FALSE.equals(properties.getProject().getUseMultiProject())) {
            Optional.ofNullable(doRecordUpdater.getAllDomains(baseDomain))
                    .ifPresentOrElse(obj -> obj.getDomainRecords()
                                    .stream()
                                    .filter(o -> o.getType().equals("A"))
                                    .collect(Collectors.toList())
                                    .forEach(e -> {
                                        checkAndUpdate(e, baseDomain, e.getName());
                                    }),
                            () -> log.error("Error while retrieving records"));
        } else {
            properties.getProject()
                    .getNames()
                    .forEach(domain -> {
                        Optional.ofNullable(doRecordUpdater.getAllDomains(domain))
                                .ifPresentOrElse(obj -> {
                                            obj.getDomainRecords()
                                                    .stream()
                                                    .filter(o -> o.getType().equals("A"))
                                                    .collect(Collectors.toList())
                                                    .forEach(e -> {
                                                        checkAndUpdate(e, domain, e.getName(), domain);
                                                    });
                                        },
                                        () -> {
                                            log.error(MessageFormat.format("Error while retrieving domain records for {0}", domain));
                                        });
                    });
        }
    }

    private void checkAndUpdate(GenericDomainResponseDTO responseDTO, String domain, String... args) {
        if (Boolean.TRUE.equals(properties.getProject().getUseMultiProject())) {
            if (Constants.domains.contains(MessageFormat.format("{0}@{1}", args))) {
                update(responseDTO, domain);
            }
        } else {
            if (Constants.domains.contains(MessageFormat.format("{0}", args))) {
                update(responseDTO, domain);
            }
        }

    }

    private void update(GenericDomainResponseDTO responseDTO, String domain) {
        if (Boolean.TRUE.equals(doRecordUpdater.updateRecord(String.valueOf(responseDTO.getId()),
                UpdateRecordRequestDTO.builder()
                        .name(responseDTO.getName())
                        .data(ipAddress)
                        .build(),
                domain))) {
            log.info(MessageFormat.format("Domain {0}.{1} successfully updated", responseDTO.getName(), domain));
        } else {
            log.error(MessageFormat.format("Error occurred while updating domain {0}.{1} ", responseDTO.getName(), domain));
        }
    }
}
