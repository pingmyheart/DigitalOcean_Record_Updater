package com.russi.do_record_updater.function;

import com.russi.do_record_updater.dto.response.GenericDomainResponseDTO;
import org.json.JSONObject;

import java.util.function.Function;

import static com.russi.do_record_updater.util.DOKeys.*;


public class JsonDomainConverterFunction implements Function<String, GenericDomainResponseDTO> {

    @Override
    public GenericDomainResponseDTO apply(String s) {
        JSONObject obj = new JSONObject(s);
        return GenericDomainResponseDTO.builder()
                .id(obj.getLong(ID.getValue()))
                .data(obj.getString(DATA.getValue()))
                .name(obj.getString(NAME.getValue()))
                .ttl(obj.getInt(TTL.getValue()))
                .type(obj.getString(TYPE.getValue()))
                .build();
    }
}
