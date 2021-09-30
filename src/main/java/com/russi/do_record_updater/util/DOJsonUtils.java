package com.russi.do_record_updater.util;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DOJsonUtils {

    @SneakyThrows
    public Boolean hasNext(String response) {
        return new JSONObject(response).getJSONObject("links").toString().contains("\"next\"");
    }
}
