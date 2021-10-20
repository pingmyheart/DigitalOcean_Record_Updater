package com.russi.do_record_updater.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum DOKeys {

    DOMAIN_RECORDS(1, "domain_records"),
    LINKS(2, "links"),
    PAGES(3, "pages"),
    NEXT(4, "next"),
    ID(4, "id"),
    DATA(5, "data"),
    NAME(6, "name"),
    TTL(7, "ttl"),
    TYPE(8, "type"),
    MESSAGE(9, "message");

    private final Integer id;
    private final String value;

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public enum RecordType {
        A(1, "A"),
        AAAA(2, "AAAA"),
        CNAME(3, "CNAME"),
        MX(4, "MX"),
        TXT(5, "TXT"),
        NS(6, "NS"),
        SRV(7, "SRV"),
        CAA(8, "CAA");

        private final Integer id;
        private final String value;
    }
}
