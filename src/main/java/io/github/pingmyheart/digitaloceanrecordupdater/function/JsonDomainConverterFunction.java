package io.github.pingmyheart.digitaloceanrecordupdater.function;

import io.github.pingmyheart.digitaloceanrecordupdater.dto.response.GenericDomainResponseDTO;
import org.json.JSONObject;

import java.util.function.Function;

import static io.github.pingmyheart.digitaloceanrecordupdater.enumerated.DigitalOceanKeysEnum.DATA;
import static io.github.pingmyheart.digitaloceanrecordupdater.enumerated.DigitalOceanKeysEnum.ID;
import static io.github.pingmyheart.digitaloceanrecordupdater.enumerated.DigitalOceanKeysEnum.NAME;
import static io.github.pingmyheart.digitaloceanrecordupdater.enumerated.DigitalOceanKeysEnum.TTL;
import static io.github.pingmyheart.digitaloceanrecordupdater.enumerated.DigitalOceanKeysEnum.TYPE;


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
