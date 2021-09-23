package com.russi.do_record_updater.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class RetrieveDomainsResponseDTO {

    private List<GenericDomainResponseDTO> domainRecords;
}
