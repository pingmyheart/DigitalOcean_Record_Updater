package com.russi.do_record_updater.function;

import com.russi.do_record_updater.dto.response.GenericDomainResponseDTO;
import org.json.JSONObject;

import java.util.function.Function;

public class JsonDomainConverterFunction implements Function<String, GenericDomainResponseDTO> {

    @Override
    public GenericDomainResponseDTO apply(String s) {
        JSONObject obj = new JSONObject(s);
        return GenericDomainResponseDTO.builder()
                .id(obj.getLong("id"))
                .data(obj.getString("data"))
                .name(obj.getString("name"))
                .ttl(obj.getInt("ttl"))
                .type(obj.getString("type"))
                .build();
    }
}
