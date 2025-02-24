package org.milkys.domain.recordings.dto;

import lombok.*;
import org.milkys.domain.gallery.entity.Gallery;
import org.milkys.domain.recordings.entity.Recordings;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class SelectRecordingDto {
    private Long id;
    private String title;
    private String content;
    private String recDate;
    private String memberNickName;


    public static SelectRecordingDto fromRecordings(Recordings recordings) {
        return SelectRecordingDto.builder()
                .id(recordings.getId())
                .title(recordings.getTitle())
                .content(recordings.getContent())
                .recDate(recordings.getRecDate())
                .memberNickName(recordings.getMember().getMemberNickname())
                .build();
    }


}
