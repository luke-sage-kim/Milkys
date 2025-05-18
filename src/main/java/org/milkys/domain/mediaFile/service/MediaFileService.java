package org.milkys.domain.mediaFile.service;

import lombok.RequiredArgsConstructor;
import org.milkys.common.dto.ResponseDto;
import org.milkys.domain.mediaFile.dto.MediaFileRequestDto;
import org.milkys.domain.mediaFile.dto.UploadMediaFileDto;
import org.milkys.domain.mediaFile.entity.MediaFile;
import org.milkys.domain.mediaFile.repository.MediaFileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MediaFileService {

    private final MediaFileRepository mediaFileRepository;

    private final String uploadDir = "C:/milkysDatabase"; // 파일 저장 위치

    public ResponseDto uploadMediaFiles(UploadMediaFileDto uploadDto) {
        List<MediaFile> mediaFiles = new ArrayList<>();

        try {
            List<MultipartFile> files = uploadDto.getFiles();
            String domainType = uploadDto.getDomainType();
            Long parentId = uploadDto.getParentId();

            if (files != null) {
                for (MultipartFile file : files) {
                    if (file.isEmpty()) continue;

                    // 디렉토리 생성
                    File dir = new File(uploadDir);
                    if (!dir.exists()) dir.mkdirs();

                    // 파일명 생성
                    String originalFilename = file.getOriginalFilename();
                    String savedFileName = UUID.randomUUID() + "_" + originalFilename;
                    String fullPath = uploadDir + "/" + savedFileName;

                    // 파일 저장
                    file.transferTo(new File(fullPath));

                    // 파일 타입 감지
                    String fileType = detectFileType(file);

                    // 엔티티 생성
                    MediaFile mediaFile = MediaFile.builder()
                            .originalFileName(originalFilename)
                            .storedFilePath(fullPath)
                            .fileType(fileType)
                            .fileSize(file.getSize())
                            .domainType(domainType)
                            .parentId(parentId)
                            .build();

                    mediaFiles.add(mediaFile);
                }
                mediaFileRepository.saveAll(mediaFiles);
            }
            return new ResponseDto<>("파일 업로드를 완료하였습니다.", HttpStatus.OK.value());

        } catch (IOException e) {
            return new ResponseDto<>("파일 업로드 중 오류 발생: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    private String detectFileType(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType != null) {
            if (contentType.startsWith("image")) return "IMAGE";
            if (contentType.startsWith("video")) return "VIDEO";
            if (contentType.startsWith("audio")) return "AUDIO";
        }
        return "ETC";
    }

    public List<MediaFile> getMediaFilesByDomainAndParentId(MediaFileRequestDto mediaFileRequestDto) {
        String domainType = mediaFileRequestDto.getDomainType();
        long parentId = mediaFileRequestDto.getParentId();
        // domainType과 parentId에 맞는 미디어 파일들 조회
        return mediaFileRepository.findByDomainTypeAndParentId(domainType, parentId);
    }

    /**
     * 도메인타입과  부모아이디를 받아서 그거에 해당하는거 다삭제
     * @param id
     * @return
     */
    public void deleteMediaFile(String domainType,long id ) {
        List<MediaFile> mediaFiles = mediaFileRepository.findByDomainTypeAndParentId(domainType, id);
        if(!mediaFiles.isEmpty()){
            for (MediaFile mediaFile : mediaFiles) {
                String fullPath = mediaFile.getStoredFilePath(); // 예: "C:/milkysDatabase/audio/abc123.mp3"
                File file = new File(fullPath);

                // 파일 존재 여부 확인 후 삭제
                if (file.exists()) {
                    boolean deleted = file.delete();
                    if (!deleted) {
                        System.err.println("파일 삭제 실패: " + fullPath);
                    }
                } else {
                    System.err.println("파일이 존재하지 않음: " + fullPath);
                }
            }
            mediaFileRepository.deleteAll(mediaFiles);
        }else{

        }
    }

    public Object deleteOneMediaFile(Long id) {
        Optional<MediaFile> optionalMediaFile = mediaFileRepository.findById(id);
        if (optionalMediaFile.isPresent()) {
            MediaFile mediaFile = optionalMediaFile.get();
            mediaFileRepository.delete(mediaFile);
            return new ResponseDto("삭제 성공", HttpStatus.OK.value());
        }
        else {
            return new ResponseDto("삭제 실패", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}