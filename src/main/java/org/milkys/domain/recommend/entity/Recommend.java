package org.milkys.domain.recommend.entity;

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
@Table(name = "recommend_table")
public class Recommend extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recommend_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "parent_type")
    private MilkysEnum.CommentParent parentType;  // 'board' 또는 'gallery'

    @Column(name = "parent_id")
    private long parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mem_code")
    private Member member;

    // Recommend 클래스의 생성자 변경
    public Recommend(long parentId, Member member, MilkysEnum.CommentParent recommendParent) {
        this.parent = parentId;
        this.member = member;
        this.parentType = recommendParent;
    }

}
