package org.milkys.domain.scheduleVote.entity;

import lombok.*;
import org.milkys.common.entity.BaseEntity;
import org.milkys.domain.member.entity.Member;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "scv_table")
public class ScheduleVote extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scv_id")
    private Long id;

    @Column(name = "scv_day")
    private String scvDate;

    @Column(name = "scv_start")
    private String scvStart;

    @Column(name = "scv_end")
    private String scvEnd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mem_code")
    private Member member;

    public void updateScvInfo(String scvDate, String scvStart, String scvEnd) {
        if (scvDate != null) {
            this.scvDate = scvDate;
        }
        if (scvStart != null) {
            this.scvStart = scvStart;
        } if (scvEnd != null) {
            this.scvEnd = scvEnd;
        }
    }
}


