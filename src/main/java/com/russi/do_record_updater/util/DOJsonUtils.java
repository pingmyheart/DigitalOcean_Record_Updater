package com.russi.do_record_updater.util;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

/**
 * Component class for Json Utility Functions
 *
 * @author Antonio Russiù
 */
@Slf4j
@Component
public class DOJsonUtils {

    /**
     * Verify if a domain response from DigitalOcean contains next link to get other paged responses
     *
     * @param response string that contains generic paged response from digitalocean record api
     * @return {@code true} if response has next link or else {@code false}
     * @since 1.3.8
     * @deprecated
     */
    @SneakyThrows
    @Deprecated(since = "1.3.8")
    public Boolean hasNext(String response) {
        return new JSONObject(response).getJSONObject("links")
                .toString()
                .contains("\"next\"");
    }

    /**
     * Verify if a domain response from DigitalOcean contains next link to get other paged responses
     *
     * @param response string that contains generic paged response from digitalocean record api
     * @return {@code true} if response has next link or else {@code false}
     */
    public Boolean containsNext(String response) {
        if (new JSONObject(response).getJSONObject("links")
                .has("pages")) {
            if (new JSONObject(response).getJSONObject("links")
                    .getJSONObject("pages")
                    .has("next")) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }
}
