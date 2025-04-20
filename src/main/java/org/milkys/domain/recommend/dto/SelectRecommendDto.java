package org.milkys.domain.recommend.dto;

import lombok.*;
import org.milkys.common.MilkysEnum;
import org.milkys.domain.music.entity.Music;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class SelectRecommendDto {
    private Long parentId;
    private MilkysEnum.CommentParent recommendParent;
    private Long memberCode;





}
