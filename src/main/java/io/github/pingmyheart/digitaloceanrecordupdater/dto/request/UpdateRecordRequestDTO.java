package io.github.pingmyheart.digitaloceanrecordupdater.dto.request;

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
public class UpdateRecordRequestDTO {
    private String name;
    private String data;
}
