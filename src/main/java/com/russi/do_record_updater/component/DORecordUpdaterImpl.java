package com.russi.do_record_updater.component;

import com.russi.do_record_updater.dto.request.UpdateRecordRequestDTO;
import com.russi.do_record_updater.dto.response.GenericDomainResponseDTO;
import com.russi.do_record_updater.dto.response.RetrieveDomainsResponseDTO;
import com.russi.do_record_updater.function.FeignExceptionMessageConverterFunction;
import com.russi.do_record_updater.interfaces.DORestInterface;
import com.russi.do_record_updater.util.DOJsonUtils;
import com.russi.do_record_updater.util.DORecordUpdaterUtils;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${config.authentication.bearer-token}")
    String bearerToken;

    public DORecordUpdaterImpl(@Autowired DORestInterface doRestInterface,
                               @Autowired DORecordUpdaterUtils doRecordUpdaterUtils,
                               @Autowired DOJsonUtils jsonUtils) {
        this.doRestInterface = doRestInterface;
        this.doRecordUpdaterUtils = doRecordUpdaterUtils;
        this.jsonUtils = jsonUtils;
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
