package io.github.pingmyheart.digitaloceanrecordupdater.scheduler;

import io.github.pingmyheart.digitaloceanrecordupdater.component.DigitalOceanRecordUpdaterImpl;
import io.github.pingmyheart.digitaloceanrecordupdater.dto.request.UpdateRecordRequestDTO;
import io.github.pingmyheart.digitaloceanrecordupdater.dto.response.GenericDomainResponseDTO;
import io.github.pingmyheart.digitaloceanrecordupdater.interfaces.IpInfoInterface;
import io.github.pingmyheart.digitaloceanrecordupdater.properties.DigitalOceanRecordUpdaterProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static io.github.pingmyheart.digitaloceanrecordupdater.enumerated.DigitalOceanKeysEnum.RecordType;

@Component
@RequiredArgsConstructor
@Slf4j
public class DigitalOceanDomainUpdaterScheduler {

    private final DigitalOceanRecordUpdaterImpl digitalOceanRecordUpdater;
    private final DigitalOceanRecordUpdaterProperties properties;
    private final IpInfoInterface ipInfoInterface;
    private String ipAddress;

    @PostConstruct
    void postConstruct() {
        scheduleIpAddressUpdate();
    }

    @Scheduled(cron = "${io.github.pingmyheart.digitaloceanrecordupdater.scheduling.digital-ocean-sync}")
    void scheduledUpdateRecord() {
        log.info("Starting scheduled update of DigitalOcean records");
        properties.getProjects()
                .forEach(project -> digitalOceanRecordUpdater.getAllDomains(project.getDomain())
                        .getDomainRecords()
                        .stream()
                        .filter(e -> e.getType().equals(RecordType.A.getValue()))
                        .filter(e -> project.getHostnames().contains(e.getName()))
                        .forEach(e -> {
                            log.info("Candidate record: {}", e);
                            update(GenericDomainResponseDTO.builder()
                                            .id(e.getId())
                                            .type(e.getType())
                                            .name(e.getName())
                                            .data(ipAddress)
                                            .ttl(e.getTtl())
                                            .build(),
                                    project.getDomain());
                        }));
    }

    @Scheduled(cron = "${io.github.pingmyheart.digitaloceanrecordupdater.scheduling.public-ip-sync}")
    void scheduleIpAddressUpdate() {
        ipAddress = ipInfoInterface.getIp();
        log.info("Current public IP address: {}", ipAddress);
    }

    private void update(GenericDomainResponseDTO responseDTO, String domain) {
        if (Boolean.TRUE.equals(digitalOceanRecordUpdater.updateRecord(String.valueOf(responseDTO.getId()),
                UpdateRecordRequestDTO.builder()
                        .name(responseDTO.getName())
                        .data(ipAddress)
                        .build(),
                domain))) {
            log.info("Domain {}.{} successfully updated", responseDTO.getName(), domain);
        } else {
            log.error("Error occurred while updating domain {}.{} ", responseDTO.getName(), domain);
        }
    }
}
