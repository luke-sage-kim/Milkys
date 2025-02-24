package org.milkys.domain.comment.entity;

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
@Table(name = "comment_table")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;


    @Column(name = "com_content")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "parent_type")
    private MilkysEnum.CommentParent parentType;  // 'board' 또는 'gallery'

    @Column(name = "parent_id")
    private long parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mem_code")
    private Member member;


    public void updateComment(String content) {
        this.content = content;
    }
}
