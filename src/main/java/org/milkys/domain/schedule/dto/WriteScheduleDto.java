package org.milkys.domain.schedule.dto;

import lombok.*;
import org.milkys.domain.schedule.entity.Schedule;

import javax.persistence.Column;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class WriteScheduleDto {

    private long memberCode;
    private String scDate;
    private String scStart;
    private String scEnd;
    private String scLoca;
    private String scContent;

    public Schedule toEntity() {
        return Schedule.builder()
                .scDate(scDate)
                .scStart(scStart)
                .scEnd(scEnd)
                .scLoca(scLoca)
                .scContent(scContent)
                .build();


    }
}
