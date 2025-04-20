package org.milkys.domain.scheduleVote.dto;

import lombok.*;
import org.milkys.domain.gallery.entity.Gallery;
import org.milkys.domain.scheduleVote.entity.ScheduleVote;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class SelectScvDto {
    private Long id;
    private String scvDate;
    private String scvStart;
    private String scvEnd;
    private String memberNickName;
    private long memberCode;


    public static SelectScvDto fromScv(ScheduleVote scheduleVote) {
        return SelectScvDto.builder()
                .id(scheduleVote.getId())
                .scvDate(scheduleVote.getScvDate())
                .scvStart(scheduleVote.getScvStart())
                .scvEnd(scheduleVote.getScvEnd())
                .memberNickName(scheduleVote.getMember().getMemberNickname())
                .memberCode(scheduleVote.getMember().getMemberCode())
                .build();
    }


}
