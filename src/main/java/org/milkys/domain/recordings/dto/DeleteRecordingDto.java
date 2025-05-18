package org.milkys.domain.recordings.dto;

import lombok.*;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class DeleteRecordingDto {
    private long recordingId;
}
