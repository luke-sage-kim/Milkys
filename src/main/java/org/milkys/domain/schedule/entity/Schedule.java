package org.milkys.domain.schedule.entity;

import lombok.*;
import org.milkys.common.entity.BaseEntity;
import org.milkys.domain.member.entity.Member;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "sc_table")
public class Schedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sc_id")
    private Long id;

    @Column(name = "sc_day")
    private String scDate;

    @Column(name = "sc_start")
    private String scStart;//합주시작시간

    @Column(name = "sc_end")
    private String scEnd;//합주종료시간

    @Column(name = "sc_loca")
    private String scLoca;
    //나중에 로케이션엔티티만들어서 받자

    @Column(name = "sc_content")
    private String scContent;
    //내용

    public void updateScheduleInfo(String scStart,String scEnd, String scLoca, String scContent) {
        if (scStart != null) {
            this.scStart = scStart;
        }
        if (scEnd != null) {
            this.scEnd = scEnd;
        }
        if (scLoca != null) {
            this.scLoca = scLoca;
        } if (scContent != null) {
            this.scContent = scContent;
        }
    }
}


