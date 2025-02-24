package org.milkys.domain.board.dto;

import lombok.*;
import org.milkys.common.MilkysEnum;
import org.milkys.domain.board.entity.Board;
import org.milkys.domain.member.entity.Member;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WriteBoardDto {
    @NotBlank(message = "제목을 입력해주십시오")
    private String title;

    @NotBlank(message = "내용을 입력해주십시오")
    private String content;

    private MilkysEnum.BoardType boardType;


    public Board toEntity(Member member) {
        return Board.builder()
                .title(this.title)
                .content(this.content)
                .viewCnt(0)
                .boardType(this.boardType)
                .member(member)
                .build();

    }
}
