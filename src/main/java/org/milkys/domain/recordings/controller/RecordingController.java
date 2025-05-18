package org.milkys.domain.recordings.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.milkys.common.MilkysEnum;
import org.milkys.common.dto.ResponseDto;
import org.milkys.config.SessionUser;
import org.milkys.domain.comment.dto.WriteCommentDto;
import org.milkys.domain.comment.service.CommentService;
import org.milkys.domain.music.service.MusicService;
import org.milkys.domain.recordings.dto.DeleteRecordingDto;
import org.milkys.domain.recordings.dto.SelectRecordingDto;
import org.milkys.domain.recordings.dto.UpdateRecordingDto;
import org.milkys.domain.recordings.dto.WriteRecordingDto;
import org.milkys.domain.recordings.service.RecordingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/recording")
public class RecordingController {
    @Autowired
    private final RecordingService recordingService;

    @Autowired
    private  final MusicService musicService;
    @Autowired
    private final CommentService commentService;
    private final HttpSession session;

    /**
     * 추후 사진 첨부 개발필요
     * @param requestDto
     * @return
     */
    @ApiOperation(
            value = "기록 작성"
            , notes = "화면에서 입력받은 글정보 작성")
    @PostMapping(value = "/v1/write", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseDto recordingWrite(@ModelAttribute WriteRecordingDto requestDto){
        String title = musicService.getMusicTitleById(requestDto.getParentId());
        requestDto.setTitle(title);
        return recordingService.recordingWrite(requestDto);
    }

    @ApiOperation(
            value = "기록 전체조회"
            , notes = "기록테이블에있는 전체데이터조회")
    @GetMapping(value = "/v1")
    public ResponseDto<List<SelectRecordingDto>> recordingList() {

        return recordingService.selectRecordingList();

    }
    @ApiOperation(
            value = "기록 단일조회"
            , notes = "해당 음악아이디에 해당하는 음원들 가져옴")
    @GetMapping(value = "/v1/{id}")
    public ResponseDto<List<SelectRecordingDto>> recordingDetail(@PathVariable Long id) {
        return recordingService.findByParentId(id);

    }
    @ApiOperation(
            value = "게시글 수정하기"
            , notes = "로그인된 아이디와 글작성자 아이디 비교 후 수정할 정보를 수정")
    @PutMapping(value = "/v1/{id}")
    public ResponseDto  updateRecording(@RequestBody UpdateRecordingDto updateRecordingDto, @PathVariable Long id ) {

        SessionUser loggedInUser = (SessionUser) session.getAttribute("loggedInUser");
        Long memberCode = loggedInUser.getMemberCode();

        return new ResponseDto(recordingService.updateRecording(updateRecordingDto,memberCode,id));
    }
    @ApiOperation(
            value = "기록 삭제"
            , notes = "화면에서 입력받은 기록아이디로 삭제")
    @DeleteMapping(value = "/v1")
    public ResponseDto deleteRecording(@RequestBody DeleteRecordingDto deleteRecordingDto){
        long id = deleteRecordingDto.getRecordingId();
        commentService.deleteComment(id,"parent");
        return new ResponseDto (recordingService.deleteRecording(id));
    }



}
