package org.milkys.domain.board.dto;

import lombok.*;
import org.milkys.common.MilkysEnum;
import org.milkys.domain.board.entity.Board;
import org.milkys.domain.member.dto.SelectMemberDto;
import org.milkys.domain.member.entity.Member;

import javax.persistence.*;
import java.util.Optional;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class SelectBoardDto {
    private Long id;
    private String title;
    private String content;
    private int viewCnt;
    private MilkysEnum.BoardType boardType;
    private String memberNickName;
    private long memberCode;


    public static SelectBoardDto fromBoard(Board board) {
        return SelectBoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .viewCnt(board.getViewCnt())
                .boardType(board.getBoardType())
                .memberNickName(board.getMember().getMemberNickname())
                .memberCode(board.getMember().getMemberCode())
                .build();
    }


}
