package org.milkys.domain.mediaFile.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MediaFileRequestDto {
    private String domainType;  // 'GALLERY' or 'RECORDING'
    private Long parentId;      // 글 ID (갤러리 ID 또는 레코딩 ID)
}
