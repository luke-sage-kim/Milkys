package org.milkys.domain.recordings.entity;

import lombok.*;
import org.milkys.common.entity.BaseEntity;
import org.milkys.domain.member.entity.Member;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "recordings_table")
public class Recordings extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rec_id")
    private Long id;

    @Column(name = "rec_title")
    private String title; //음원 작성시 음악 title이 자동을 들어가게  라벨용이지뭐

    @Column(name = "parent_id")
    private long parentId;

    @Column(name = "rec_content")
    private String content;//부가설명

    @Column(name = "rec_date")
    private String recDate;//녹음날짜  거의 이게 메인 가로로 책처럼 펼치는거임

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mem_code")
    private Member member;



    public void updateRecordingInfo(String title, String content,String recDate) {
        if (title != null) {
            this.title = title;
        }
        if (content != null) {
            this.content = content;
        }
        if (recDate != null) {
            this.recDate = recDate;
        }
    }
}
