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


    private String scDate;
    private String scLoca;
    private String scContent;

    public Schedule toEntity() {
        return Schedule.builder()
                .scDate(scDate)
                .scLoca(scLoca)
                .scContent(scContent)
                .build();


    }
}
