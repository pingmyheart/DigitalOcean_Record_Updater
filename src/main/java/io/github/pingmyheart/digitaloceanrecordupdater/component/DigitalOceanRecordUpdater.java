package io.github.pingmyheart.digitaloceanrecordupdater.component;

import io.github.pingmyheart.digitaloceanrecordupdater.dto.request.UpdateRecordRequestDTO;
import io.github.pingmyheart.digitaloceanrecordupdater.dto.response.RetrieveDomainsResponseDTO;

public interface DigitalOceanRecordUpdater {

    RetrieveDomainsResponseDTO getAllDomains(String base);

    Boolean updateRecord(String recordId, UpdateRecordRequestDTO updateRecordRequestDTO, String base);
}
