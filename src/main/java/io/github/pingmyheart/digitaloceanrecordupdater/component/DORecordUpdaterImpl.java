package io.github.pingmyheart.digitaloceanrecordupdater.component;

import feign.FeignException;
import io.github.pingmyheart.digitaloceanrecordupdater.configuration.DOCustomPropertiesConfiguration;
import io.github.pingmyheart.digitaloceanrecordupdater.dto.request.UpdateRecordRequestDTO;
import io.github.pingmyheart.digitaloceanrecordupdater.dto.response.GenericDomainResponseDTO;
import io.github.pingmyheart.digitaloceanrecordupdater.dto.response.RetrieveDomainsResponseDTO;
import io.github.pingmyheart.digitaloceanrecordupdater.function.FeignExceptionMessageConverterFunction;
import io.github.pingmyheart.digitaloceanrecordupdater.interfaces.DORestInterface;
import io.github.pingmyheart.digitaloceanrecordupdater.util.DOJsonUtils;
import io.github.pingmyheart.digitaloceanrecordupdater.util.DORecordUpdaterUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class DORecordUpdaterImpl implements DORecordUpdater {

    DORestInterface doRestInterface;

    DORecordUpdaterUtils doRecordUpdaterUtils;

    DOJsonUtils jsonUtils;

    String bearerToken;

    public DORecordUpdaterImpl(@Autowired DORestInterface doRestInterface,
                               @Autowired DORecordUpdaterUtils doRecordUpdaterUtils,
                               @Autowired DOJsonUtils jsonUtils,
                               @Autowired DOCustomPropertiesConfiguration config) {
        this.doRestInterface = doRestInterface;
        this.doRecordUpdaterUtils = doRecordUpdaterUtils;
        this.jsonUtils = jsonUtils;
        this.bearerToken = config.getAuthentication()
                .getBearerToken();
    }

    @Override
    public RetrieveDomainsResponseDTO getAllDomains(String base) {
        String response;
        List<GenericDomainResponseDTO> genericDomainResponseDTOList = new ArrayList<>();
        int index = 1;
        do {
            try {
                response = doRestInterface.getPagedDomains(MessageFormat.format("Bearer {0}", bearerToken),
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
            genericDomainResponseDTOList.addAll(doRecordUpdaterUtils.parseDomainsFromResponse(response));
            index++;
        } while (Boolean.TRUE.equals(jsonUtils.containsNext(response)));

        return RetrieveDomainsResponseDTO.builder()
                .domainRecords(genericDomainResponseDTOList)
                .build();
    }

    @Override
    public Boolean updateRecord(String recordId, UpdateRecordRequestDTO updateRecordRequestDTO, String base) {
        try {
            doRestInterface.updateRecord(MessageFormat.format("Bearer {0}", bearerToken),
                    base,
                    recordId,
                    updateRecordRequestDTO);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
