package io.github.pingmyheart.digitaloceanrecordupdater.function;

import io.github.pingmyheart.digitaloceanrecordupdater.dto.response.GenericDomainResponseDTO;
import org.json.JSONObject;

import java.util.function.Function;

import static io.github.pingmyheart.digitaloceanrecordupdater.util.DOKeys.DATA;
import static io.github.pingmyheart.digitaloceanrecordupdater.util.DOKeys.ID;
import static io.github.pingmyheart.digitaloceanrecordupdater.util.DOKeys.NAME;
import static io.github.pingmyheart.digitaloceanrecordupdater.util.DOKeys.TTL;
import static io.github.pingmyheart.digitaloceanrecordupdater.util.DOKeys.TYPE;


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
