package org.milkys.domain.scheduleVote.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.milkys.domain.gallery.entity.Gallery;
import org.milkys.domain.member.entity.Member;
import org.milkys.domain.scheduleVote.entity.ScheduleVote;

import javax.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WriteScvDto {
    private String scvDate;
    private String scvStart;
    private String scvEnd;

    public ScheduleVote toEntity(Member member) {
        return ScheduleVote.builder()
                .scvDate(this.scvDate)
                .scvStart(this.scvStart)
                .scvEnd(this.scvEnd)
                .member(member)
                .build();
    }
}
