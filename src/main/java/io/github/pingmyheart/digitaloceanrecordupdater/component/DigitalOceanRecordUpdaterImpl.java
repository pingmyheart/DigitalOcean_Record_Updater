package io.github.pingmyheart.digitaloceanrecordupdater.component;

import feign.FeignException;
import io.github.pingmyheart.digitaloceanrecordupdater.dto.request.UpdateRecordRequestDTO;
import io.github.pingmyheart.digitaloceanrecordupdater.dto.response.GenericDomainResponseDTO;
import io.github.pingmyheart.digitaloceanrecordupdater.dto.response.RetrieveDomainsResponseDTO;
import io.github.pingmyheart.digitaloceanrecordupdater.function.FeignExceptionMessageConverterFunction;
import io.github.pingmyheart.digitaloceanrecordupdater.interfaces.DigitalOceanRestInterface;
import io.github.pingmyheart.digitaloceanrecordupdater.properties.DigitalOceanRecordUpdaterProperties;
import io.github.pingmyheart.digitaloceanrecordupdater.util.DigitalOceanJsonUtils;
import io.github.pingmyheart.digitaloceanrecordupdater.util.DigitalOceanRecordUpdaterUtils;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DigitalOceanRecordUpdaterImpl implements DigitalOceanRecordUpdater {

    private final DigitalOceanRestInterface digitalOceanRestInterface;
    private final DigitalOceanRecordUpdaterProperties properties;
    private final DigitalOceanRecordUpdaterUtils digitalOceanRecordUpdaterUtils;
    private final DigitalOceanJsonUtils jsonUtils;
    private String bearerToken;

    @PostConstruct
    void init() {
        this.bearerToken = properties.getAuthentication().getDigitalOceanToken();
    }

    @Override
    public RetrieveDomainsResponseDTO getAllDomains(String base) {
        String response;
        List<GenericDomainResponseDTO> genericDomainResponseDTOList = new ArrayList<>();
        int index = 1;
        do {
            try {
                response = digitalOceanRestInterface.getPagedDomains(MessageFormat.format("Bearer {0}", bearerToken.replaceAll("[Bb]earer", "")),
                        base,
                        index,
                        20);
            } catch (FeignException feignException) {
                log.error(new FeignExceptionMessageConverterFunction()
                        .apply(feignException));
                return null;
            } catch (Exception e) {
                return null;
            }
            genericDomainResponseDTOList.addAll(digitalOceanRecordUpdaterUtils.parseDomainsFromResponse(response));
            index++;
        } while (Boolean.TRUE.equals(jsonUtils.containsNext(response)));

        return RetrieveDomainsResponseDTO.builder()
                .domainRecords(genericDomainResponseDTOList)
                .build();
    }

    @Override
    public Boolean updateRecord(String recordId,
                                UpdateRecordRequestDTO updateRecordRequestDTO,
                                String base) {
        try {
            digitalOceanRestInterface.updateRecord(MessageFormat.format("Bearer {0}", bearerToken.replaceAll("[Bb]earer", "")),
                    base,
                    recordId,
                    updateRecordRequestDTO);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
