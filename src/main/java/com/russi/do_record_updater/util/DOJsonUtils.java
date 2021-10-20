package com.russi.do_record_updater.util;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import static com.russi.do_record_updater.util.DOKeys.*;

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
        return new JSONObject(response).getJSONObject(LINKS.getValue())
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
        JSONObject jsonObject = new JSONObject(response).getJSONObject(LINKS.getValue());
        return jsonObject.has(PAGES.getValue()) &&
                jsonObject.getJSONObject(PAGES.getValue()).has(NEXT.getValue()) ?
                Boolean.TRUE : Boolean.FALSE;
    }
}
