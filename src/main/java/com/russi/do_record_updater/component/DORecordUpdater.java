package com.russi.do_record_updater.component;

import com.russi.do_record_updater.dto.request.UpdateRecordRequestDTO;
import com.russi.do_record_updater.dto.response.RetrieveDomainsResponseDTO;

public interface DORecordUpdater {

    RetrieveDomainsResponseDTO getAllDomains(String base);

    Boolean hasNext(String response);

    Boolean updateRecord(String recordId, UpdateRecordRequestDTO updateRecordRequestDTO, String base);
}
