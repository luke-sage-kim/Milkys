package org.milkys.domain.board.dto;

import lombok.*;
import org.milkys.common.MilkysEnum;
import org.milkys.domain.board.entity.Board;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateBoardDto {
    private long memberCode;
    private String title;
    private String content;
    private MilkysEnum.BoardType boardType;

}
