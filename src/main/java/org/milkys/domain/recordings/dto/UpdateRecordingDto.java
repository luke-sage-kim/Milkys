package org.milkys.domain.recordings.dto;

import lombok.*;
import org.milkys.domain.gallery.entity.Gallery;
import org.milkys.domain.recordings.entity.Recordings;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateRecordingDto {
    private String title;
    private String content;
    private String recDate;


    public static UpdateRecordingDto fromRecordingDto(Recordings recordings) {
        return UpdateRecordingDto.builder()
                .title(recordings.getTitle())
                .content(recordings.getContent())
                .recDate(recordings.getRecDate())
                .build();
    }


}
