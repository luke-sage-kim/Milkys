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
public class UpdateGalleryDto {
    private long memberCode;
    private String title;
    private String content;


    public static UpdateGalleryDto fromGallery(Gallery gallery) {
        return UpdateGalleryDto.builder()
                .title(gallery.getTitle())
                .content(gallery.getContent())
                .build();
    }


}
