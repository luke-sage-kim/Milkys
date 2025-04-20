package org.milkys.domain.music.dto;

import lombok.*;
import org.milkys.common.MilkysEnum;
import org.milkys.domain.music.entity.Music;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class SelectMusicDto {
    private Long id;
    private String title;
    private String content;
    private int like;
    private String memberNickName;
    private long memberCode;
    private String musicLink;
    private MilkysEnum.MusicStatus status;

    public static SelectMusicDto frommusic(Music music) {
        return SelectMusicDto.builder()
                .id(music.getId())
                .title(music.getTitle())
                .content(music.getContent())
                .like(music.getLike())
                .status(music.getStatus())
                .musicLink(music.getMusicLink())
                .memberNickName(music.getMember().getMemberNickname())
                .memberCode(music.getMember().getMemberCode())
                .build();
    }


}
