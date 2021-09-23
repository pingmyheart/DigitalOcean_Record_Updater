package com.russi.do_record_updater.component;

import com.russi.do_record_updater.DORecordUpdaterUtils;
import com.russi.do_record_updater.dto.request.UpdateRecordRequestDTO;
import com.russi.do_record_updater.dto.response.GenericDomainResponseDTO;
import com.russi.do_record_updater.dto.response.RetrieveDomainsResponseDTO;
import com.russi.do_record_updater.interfaces.DORestInterface;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class DORecordUpdaterImpl implements DORecordUpdater {

    @Autowired
    DORestInterface doRestInterface;

    @Autowired
    DORecordUpdaterUtils doRecordUpdaterUtils;

    @Value("${config.authentication.bearer-token}")
    String bearerToken;

    @Value("${config.project.name}")
    String baseDomain;

    @Override
    public RetrieveDomainsResponseDTO getAllDomains() {
        String response;
        List<GenericDomainResponseDTO> genericDomainResponseDTOList = new ArrayList<>();
        int index = 1;
        do {
            response = doRestInterface.getPagedDomains(MessageFormat.format("Bearer {0}", bearerToken),
                    baseDomain,
                    index,
                    20);
            genericDomainResponseDTOList.addAll(doRecordUpdaterUtils.retrieveDomainsFromResponse(response));
            index++;
        } while (Boolean.TRUE.equals(hasNext(response)));

        return RetrieveDomainsResponseDTO.builder()
                .domainRecords(genericDomainResponseDTOList)
                .build();
    }

    @SneakyThrows
    @Override
    public Boolean hasNext(String response) {
        return new JSONObject(response).getJSONObject("links").toString().contains("next");
    }

    @Override
    public Boolean updateRecord(String recordId, UpdateRecordRequestDTO updateRecordRequestDTO) {
        try {
            doRestInterface.updateRecord(MessageFormat.format("Bearer {0}", bearerToken),
                    baseDomain,
                    recordId,
                    updateRecordRequestDTO);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
