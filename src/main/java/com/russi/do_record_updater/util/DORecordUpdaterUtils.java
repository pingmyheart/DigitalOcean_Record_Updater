package com.russi.do_record_updater.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.russi.do_record_updater.dto.response.GenericDomainResponseDTO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Component
@Slf4j
public class DORecordUpdaterUtils {

    ObjectMapper objectMapper;

    ApplicationContext context;

    public DORecordUpdaterUtils(@Autowired ObjectMapper objectMapper,
                                 @Autowired ApplicationContext context) {
        this.objectMapper = objectMapper;
        this.context = context;
    }

    @SneakyThrows
    public List<GenericDomainResponseDTO> parseDomainsFromResponse(String response) {
        List<GenericDomainResponseDTO> genericDomainResponseDTOList = new ArrayList<>();
        JSONObject obj = new JSONObject(response);
        IntStream.range(0, obj.getJSONArray("domain_records").length())
                .forEach(i -> {
                    genericDomainResponseDTOList.add(convertToDomain(new JSONObject(obj.getJSONArray("domain_records")
                            .get(i)
                            .toString()).toString()));
                });
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

    public void shutdown() {
        try {
            SpringApplication.exit(context, () -> 0);
        } catch (Exception e) {
            log.error("Thread Exception");
        }
    }
}
