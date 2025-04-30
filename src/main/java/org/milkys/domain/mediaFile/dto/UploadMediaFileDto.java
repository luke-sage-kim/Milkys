package org.milkys.domain.mediaFile.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class

UploadMediaFileDto {
    private List<MultipartFile> files;  // 업로드할 파일들
    private String domainType;          // 연결할 도메인 (Gallery, Recording)
    private Long parentId;               // 연결할 글 ID
}