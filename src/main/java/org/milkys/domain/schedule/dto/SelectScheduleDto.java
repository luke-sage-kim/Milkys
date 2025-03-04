package org.milkys.domain.schedule.dto;

import lombok.*;
import org.milkys.domain.schedule.entity.Schedule;
import org.milkys.domain.scheduleVote.entity.ScheduleVote;

import javax.persistence.Column;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class SelectScheduleDto {
    private Long id;
    private String scDate;
    private String scLoca;
    private String scContent;


    public static SelectScheduleDto fromSchedule(Schedule schedule) {
        return SelectScheduleDto.builder()
                .id(schedule.getId())
                .scDate(schedule.getScDate())
                .scLoca(schedule.getScLoca())
                .scContent(schedule.getScContent())
                .build();
    }


}
