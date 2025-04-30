package org.milkys.domain.mediaFile.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.milkys.common.dto.ResponseDto;
import org.milkys.domain.mediaFile.dto.MediaFileRequestDto;
import org.milkys.domain.mediaFile.dto.UploadMediaFileDto;
import org.milkys.domain.mediaFile.entity.MediaFile;
import org.milkys.domain.mediaFile.service.MediaFileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/media")
@RequiredArgsConstructor
public class MediaFileController {

    private final MediaFileService mediaFileService;

    @ApiOperation(value = "미디어 파일 업로드", notes = "도메인에 파일을 업로드합니다 (이미지, 비디오, 오디오)")
    @PostMapping(value = "/v1/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseDto uploadMediaFiles(@ModelAttribute UploadMediaFileDto uploadMediaFileDto) {
        return mediaFileService.uploadMediaFiles(uploadMediaFileDto);
    }

    @ApiOperation(value = "미디어 파일 조회", notes = "domainType과 parentId에 해당하는 미디어 파일들을 조회합니다.")
    @PostMapping("/v1/list")
    public ResponseDto<List<MediaFile>> getMediaFiles(@Valid @RequestBody MediaFileRequestDto mediaFileRequestDto) {
        List<MediaFile> mediaFiles = mediaFileService.getMediaFilesByDomainAndParentId(mediaFileRequestDto );

        if (mediaFiles.isEmpty()) {
            return new ResponseDto("미디어 파일이 없습니다.", HttpStatus.NO_CONTENT.value());
        }

        return new ResponseDto<>(mediaFiles, HttpStatus.OK.value());
    }

    @ApiOperation(
            value = "사진 삭제"
            , notes = "화면에서 입력받은 미디어아이디를 삭제")
    @DeleteMapping(value = "/v1/{id}")
    public ResponseDto deleteMediaFile(@PathVariable Long id){
        return new ResponseDto (mediaFileService.deleteOneMediaFile(id));
    }

}