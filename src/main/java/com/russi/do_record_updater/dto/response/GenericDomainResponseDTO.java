package com.russi.do_record_updater.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
public class GenericDomainResponseDTO {
    private Long id;
    private String type;
    private String name;
    private String data;
    private Integer ttl;
}
