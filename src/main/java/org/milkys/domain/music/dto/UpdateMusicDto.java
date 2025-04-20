package org.milkys.domain.music.dto;

import lombok.*;
import org.milkys.domain.gallery.entity.Gallery;
import org.milkys.domain.music.entity.Music;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateMusicDto {
    private Long memberCode;
    private String title;
    private String content;
    private String musicLink;



}
