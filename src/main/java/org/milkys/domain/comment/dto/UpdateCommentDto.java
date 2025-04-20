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
public class UpdateCommentDto {
    private long memberCode;
    @NotBlank(message = "내용을 입력해주십시오")
    private String content;

}
