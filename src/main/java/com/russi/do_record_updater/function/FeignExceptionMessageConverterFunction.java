package com.russi.do_record_updater.function;

import feign.FeignException;
import lombok.SneakyThrows;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public class FeignExceptionMessageConverterFunction implements Function<FeignException, String> {

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
        JSONObject response = new JSONObject(message.get());
        if (response.toString()
                .contains("message")) {
            return response.getString("message");
        }
        return null;
    }
}
