package com.russi.do_record_updater.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
public class UpdateRecordRequestDTO {
    private String name;
    private String data;
}
