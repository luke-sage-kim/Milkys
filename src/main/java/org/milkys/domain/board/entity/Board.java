package org.milkys.domain.board.entity;

import lombok.*;
import org.milkys.common.MilkysEnum;
import org.milkys.common.entity.BaseEntity;
import org.milkys.domain.member.entity.Member;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "board_table")
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Column(name = "b_title")
    private String title;

    @Column(name = "b_content")
    private String content;

    @Column(name = "b_view_cnt")
    private int viewCnt;

    @Column(name = "b_type")
    @Enumerated(EnumType.STRING)
    private MilkysEnum.BoardType boardType;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mem_code")
    private Member member;

//    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
//    private List<Comment> comments;  // Board와 연결된 댓글들

    @PrePersist
    public void prePersist() {
        if (this.boardType == null) {
            this.boardType = MilkysEnum.BoardType.FREE; // 기본값 설정
        }
    }

    public void updateBoardInfo(String title, String content, MilkysEnum.BoardType boardType) {
        if (title != null) {
            this.title = title;
        }
        if (content != null) {
            this.content = content;
        }
        if (boardType != null) {
            this.boardType = boardType;
        }
    }
}
