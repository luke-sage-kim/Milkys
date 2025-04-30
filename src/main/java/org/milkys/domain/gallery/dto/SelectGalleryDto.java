package org.milkys.domain.gallery.dto;

import lombok.*;
import org.milkys.common.MilkysEnum;
import org.milkys.domain.board.entity.Board;
import org.milkys.domain.gallery.entity.Gallery;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class SelectGalleryDto {

    private Long id;
    private Long memberCode;
    private String title;
    private String content;
    private int viewCnt;
    private String memberNickName;


    public static SelectGalleryDto fromGallery(Gallery gallery) {
        return SelectGalleryDto.builder()
                .id(gallery.getId())
                .title(gallery.getTitle())
                .content(gallery.getContent())
                .viewCnt(gallery.getViewCnt())
                .memberNickName(gallery.getMember().getMemberNickname())
                .memberCode(gallery.getMember().getMemberCode())
                .build();
    }


}
