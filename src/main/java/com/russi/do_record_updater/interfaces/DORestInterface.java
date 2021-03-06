package com.russi.do_record_updater.interfaces;

import com.russi.do_record_updater.dto.request.UpdateRecordRequestDTO;
import org.apache.http.HttpHeaders;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

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
