package org.milkys.domain.gallery.entity;

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
@Table(name = "gallery_table")
public class Gallery extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gal_id")
    private Long id;

    @Column(name = "gal_title")
    private String title;

    @Column(name = "gal_content")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mem_code")
    private Member member;

}


