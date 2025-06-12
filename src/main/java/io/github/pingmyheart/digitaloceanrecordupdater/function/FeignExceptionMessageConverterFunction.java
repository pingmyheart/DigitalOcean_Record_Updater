package io.github.pingmyheart.digitaloceanrecordupdater.function;

import feign.FeignException;
import io.github.pingmyheart.digitaloceanrecordupdater.util.ExceptionUtils;
import lombok.SneakyThrows;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import static io.github.pingmyheart.digitaloceanrecordupdater.enumerated.DigitalOceanKeysEnum.MESSAGE;

public class FeignExceptionMessageConverterFunction implements Function<FeignException, String> {

    ExceptionUtils exceptionUtils = new ExceptionUtils();

    @SneakyThrows
    @Override
    public String apply(FeignException feignException) {
        AtomicReference<String> message = new AtomicReference<>();
        feignException.responseBody()
                .ifPresent(resp -> message.set(StandardCharsets.UTF_8
                        .decode(resp)
                        .toString()
                        .replaceAll("(?m)^\\s+$", "")
                        .trim()));
        if (Boolean.TRUE.equals(exceptionUtils.isInSocketTimeoutException(feignException)
                .get())) {
            return "Digital Ocean spent too much time to retrieve a response";
        }
        JSONObject response;
        try {
            response = new JSONObject(message.get());
        } catch (JSONException e) {
            return "Error thrown while parsing feign exception response body";
        }
        if (response.toString()
                .contains("\"message\"")) {
            return response.getString(MESSAGE.getValue());
        }
        return null;
    }
}
