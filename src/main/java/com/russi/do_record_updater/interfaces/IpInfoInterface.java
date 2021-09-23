package com.russi.do_record_updater.interfaces;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "IpInfo-Rest-Interface",
        url = "ipinfo.io")
public interface IpInfoInterface {

    @GetMapping("/ip")
    String getIp();
}
