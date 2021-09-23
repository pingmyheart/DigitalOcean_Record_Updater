package com.russi.do_record_updater;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.russi.do_record_updater.dto.response.GenericDomainResponseDTO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class DORecordUpdaterUtils {

    @Autowired
    ObjectMapper objectMapper;

    @SneakyThrows
    public List<GenericDomainResponseDTO> retrieveDomainsFromResponse(String response) {
        List<GenericDomainResponseDTO> genericDomainResponseDTOList = new ArrayList<>();
        JSONObject obj = new JSONObject(response);
        for (int i = 0; i < obj.getJSONArray("domain_records").length(); i++) {
            genericDomainResponseDTOList.add(convertToDomain(new JSONObject(obj.getJSONArray("domain_records")
                    .get(i)
                    .toString()).toString()));
        }
        return genericDomainResponseDTOList;
    }

    @SneakyThrows
    private GenericDomainResponseDTO convertToDomain(String domain) {

        JSONObject obj = new JSONObject(domain);
        return GenericDomainResponseDTO.builder()
                .id(obj.getLong("id"))
                .data(obj.getString("data"))
                .name(obj.getString("name"))
                .ttl(obj.getInt("ttl"))
                .type(obj.getString("type"))
                .build();
    }
}
