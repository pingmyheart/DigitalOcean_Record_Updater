package io.github.pingmyheart.digitaloceanrecordupdater.dto.response;

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
