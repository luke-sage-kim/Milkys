package org.milkys.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.milkys.common.MilkysEnum;
import org.milkys.domain.comment.entity.Comment;
import org.milkys.domain.member.entity.Member;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WriteCommentDto {

    @NotBlank(message = "내용을 입력해주십시오")
    private String content;

    public Comment toEntity(long parentId, Member member, MilkysEnum.CommentParent commentParent) {
        return Comment.builder()
                .content(this.content)
                .parentType(commentParent)
                .parent(parentId)
                .member(member)
                .build();
    }

    //소속은 로직에서처리

}
