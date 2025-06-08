package io.github.pingmyheart.digitaloceanrecordupdater.interfaces;

import io.github.pingmyheart.digitaloceanrecordupdater.dto.request.UpdateRecordRequestDTO;
import org.apache.http.HttpHeaders;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "DO-Rest-Interface",
        url = "https://api.digitalocean.com")
public interface DORestInterface {

    @GetMapping("/v2/domains/{baseDomain}/records")
    String getDomains(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearer,
                      @PathVariable String baseDomain);

    @GetMapping("/v2/domains/{baseDomain}/records")
    String getPagedDomains(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearer,
                           @PathVariable String baseDomain,
                           @RequestParam Integer page,
                           @RequestParam(name = "per_page") Integer perPage);

    @PutMapping("/v2/domains/{baseDomain}/records/{recordId}")
    String updateRecord(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearer,
                        @PathVariable String baseDomain,
                        @PathVariable String recordId,
                        @RequestBody UpdateRecordRequestDTO updateRecordRequestDTO);
}
