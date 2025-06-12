package io.github.pingmyheart.digitaloceanrecordupdater.util;

import io.github.pingmyheart.digitaloceanrecordupdater.dto.response.GenericDomainResponseDTO;
import io.github.pingmyheart.digitaloceanrecordupdater.function.JsonDomainConverterFunction;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

import static io.github.pingmyheart.digitaloceanrecordupdater.enumerated.DigitalOceanKeysEnum.DOMAIN_RECORDS;

@Component
@RequiredArgsConstructor
@Slf4j
public class DigitalOceanRecordUpdaterUtils {

    private static final AtomicReference<Boolean> shut = new AtomicReference<>(Boolean.FALSE);
    private final ApplicationContext context;

    private static void updateShut() {
        shut.set(Boolean.TRUE);
    }

    @SneakyThrows
    public List<GenericDomainResponseDTO> parseDomainsFromResponse(String response) {
        List<GenericDomainResponseDTO> genericDomainResponseDTOList = new ArrayList<>();
        JSONObject obj = new JSONObject(response);
        IntStream.range(0, obj.getJSONArray(DOMAIN_RECORDS.getValue()).length())
                .forEach(i -> genericDomainResponseDTOList.add(convertToDomain(new JSONObject(obj.getJSONArray(DOMAIN_RECORDS.getValue())
                        .get(i)
                        .toString()).toString())));
        return genericDomainResponseDTOList;
    }

    private GenericDomainResponseDTO convertToDomain(String domain) {
        return new JsonDomainConverterFunction().apply(domain);
    }

    public void shutdown() {
        if (Boolean.FALSE.equals(shut.get())) {
            updateShut();
            try {
                SpringApplication.exit(context, () -> 0);
            } catch (Exception e) {
                log.error("Thread Exception");
            }
        }
    }

    public Boolean getShut() {
        return shut.get();
    }
}
