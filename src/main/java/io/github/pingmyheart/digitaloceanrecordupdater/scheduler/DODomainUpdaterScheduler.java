package io.github.pingmyheart.digitaloceanrecordupdater.scheduler;

import io.github.pingmyheart.digitaloceanrecordupdater.component.DORecordUpdaterImpl;
import io.github.pingmyheart.digitaloceanrecordupdater.configuration.DOCustomPropertiesConfiguration;
import io.github.pingmyheart.digitaloceanrecordupdater.dto.request.UpdateRecordRequestDTO;
import io.github.pingmyheart.digitaloceanrecordupdater.dto.response.GenericDomainResponseDTO;
import io.github.pingmyheart.digitaloceanrecordupdater.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.github.pingmyheart.digitaloceanrecordupdater.util.Constants.ipAddress;
import static io.github.pingmyheart.digitaloceanrecordupdater.util.DOKeys.RecordType;

@Component
@Slf4j
public class DODomainUpdaterScheduler {

    DORecordUpdaterImpl doRecordUpdater;

    DOCustomPropertiesConfiguration properties;

    String bearerToken;

    public DODomainUpdaterScheduler(@Autowired DORecordUpdaterImpl doRecordUpdater,
                                    @Autowired DOCustomPropertiesConfiguration properties) {
        this.doRecordUpdater = doRecordUpdater;
        this.properties = properties;
        this.bearerToken = properties.getAuthentication()
                .getBearerToken();
    }

    @Scheduled(cron = "${config.schedule.update-cron}")
    void scheduledUpdateRecord() {
        if (Boolean.FALSE.equals(properties.getProject().getUseMultiProject())) {
            Optional.ofNullable(doRecordUpdater.getAllDomains(properties.getProject().getName()))
                    .ifPresentOrElse(obj -> obj.getDomainRecords()
                                    .stream()
                                    .filter(o -> o.getType().equals(RecordType.A.getValue()))
                                    .collect(Collectors.toList())
                                    .forEach(e -> checkAndUpdate(e, properties.getProject().getName(), e.getName())),
                            () -> log.error("Error while retrieving records"));
        } else {
            properties.getProject()
                    .getNames()
                    .forEach(domain -> Optional.ofNullable(doRecordUpdater.getAllDomains(domain))
                            .ifPresentOrElse(obj -> obj.getDomainRecords()
                                            .stream()
                                            .filter(o -> o.getType().equals(RecordType.A.getValue()))
                                            .collect(Collectors.toList())
                                            .forEach(e -> checkAndUpdate(e, domain, e.getName(), domain)),
                                    () -> log.error(MessageFormat.format("Error while retrieving domain records for {0}", domain))));
        }
    }

    private void checkAndUpdate(GenericDomainResponseDTO responseDTO, String domain, String... args) {
        if (Boolean.TRUE.equals(properties.getProject().getUseMultiProject())) {
            if (Constants.domains.contains(MessageFormat.format("{0}@{1}", args[0], args[1]))) {
                update(responseDTO, domain);
            }
        } else {
            if (Constants.domains.contains(MessageFormat.format("{0}", args[0]))) {
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
