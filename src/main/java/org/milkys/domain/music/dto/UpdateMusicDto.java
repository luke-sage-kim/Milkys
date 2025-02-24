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
    private String title;
    private String content;


    public static UpdateMusicDto fromGallery(Music music) {
        return UpdateMusicDto.builder()
                .title(music.getTitle())
                .content(music.getContent())
                .build();
    }


}
