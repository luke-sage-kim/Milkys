package org.milkys.domain.music.entity;

import lombok.*;
import org.milkys.common.MilkysEnum;
import org.milkys.common.entity.BaseEntity;
import org.milkys.domain.member.entity.Member;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "music_table")
public class Music extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mu_id")
    private Long id;

    @Column(name = "mu_title")
    private String title;

    @Column(name = "mu_content")
    private String content;

    @Column(name = "mu_link")
    private String musicLink;

    //좋아요 테이블 만들어서 누를때 좋아요 갯수 합을 반영해도될듯
    @Column(name = "mu_like")
    private int like;

    @Column(name = "mu_status")
    private MilkysEnum.MusicStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mem_code")
    private Member member;

    public void updateMusicInfo(String title, String content,String musicLink) {
        if (title != null) {
            this.title = title;
        }
        if (content != null) {
            this.content = content;
        }

        if (musicLink != null) {
            this.musicLink = musicLink;
        }
    }

    public void promoteStatus() {
        this.status = MilkysEnum.MusicStatus.SETLIST;
    }

    public void demoteStatus() {
        this.status = MilkysEnum.MusicStatus.SHARE;
    }
}
