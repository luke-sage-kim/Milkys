package org.milkys.domain.comment.dto;

import lombok.*;
import org.milkys.domain.comment.entity.Comment;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SelectCommentDto {
    private Long id;
    private Long parent_id;
    private String content;
    private String memberNickName;
    private long memberCode;

    public static SelectCommentDto fromComment(Comment comment) {
        return SelectCommentDto.builder()
                .id(comment.getId())
                .parent_id(comment.getParent())
                .content(comment.getContent())
                .memberNickName(comment.getMember().getMemberNickname())
                .memberCode(comment.getMember().getMemberCode())
                .build();
    }

}
