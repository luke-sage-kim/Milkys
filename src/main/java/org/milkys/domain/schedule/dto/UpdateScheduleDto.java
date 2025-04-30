package org.milkys.domain.schedule.dto;

import lombok.*;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateScheduleDto {
    private String scDate;
    private String scStart;
    private String scEnd;
    private String scLoca;
    private String scContent;
}
