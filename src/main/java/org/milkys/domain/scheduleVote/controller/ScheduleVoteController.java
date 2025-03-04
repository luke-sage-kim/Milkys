package org.milkys.domain.scheduleVote.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.milkys.common.dto.ResponseDto;
import org.milkys.config.SessionUser;
import org.milkys.domain.scheduleVote.dto.SelectScvDto;
import org.milkys.domain.scheduleVote.dto.UpdateScvDto;
import org.milkys.domain.scheduleVote.dto.WriteScvDto;
import org.milkys.domain.scheduleVote.service.ScheduleVoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/scv")
public class ScheduleVoteController {
    @Autowired
    private final ScheduleVoteService scheduleVoteService;
    private final HttpSession session;

    /**
     * 추후 사진 첨부 개발필요
     * @param requestDto
     * @param session
     * @return
     */
    @ApiOperation(
            value = "일정투표 작성"
            , notes = "화면에서 입력받은 글정보 작성")
    @PostMapping(value = "/v1/write")
    public ResponseDto scvWrite(@Valid @RequestBody WriteScvDto requestDto, HttpSession session) {

        return scheduleVoteService.scvWrite(requestDto,session);

    }

    @ApiOperation(
            value = "일정투표 전체조회"
            , notes = "일정투표테이블에있는 전체데이터조회")
    @GetMapping(value = "/v1")
    public ResponseDto<List<SelectScvDto>> scvList() {

        return scheduleVoteService.selectScheduleVoteList();

    }
    @ApiOperation(
            value = "일정투표 단일조회"
            , notes = "일정투표테이블에있는 해당날짜에 해당되는 투표가져오기")
    @GetMapping(value = "/v1/{id}")
    public ResponseDto<List<SelectScvDto>> scvDetail(@PathVariable String scvDate) {
        return scheduleVoteService.scvDetail(scvDate);

    }
    @ApiOperation(
            value = "게시글 수정하기"
            , notes = "로그인된 아이디와 글작성자 아이디 비교 후 수정할 정보를 수정")
    @PutMapping(value = "/v1/{id}")
    public ResponseDto  updatescv(@RequestBody UpdateScvDto updatescvDto, @PathVariable Long id ) {

        SessionUser loggedInUser = (SessionUser) session.getAttribute("loggedInUser");
        Long memberCode = loggedInUser.getMemberCode();

        return new ResponseDto(scheduleVoteService.updatescv(updatescvDto,memberCode,id));
    }
    @ApiOperation(
            value = "일정투표 삭제"
            , notes = "화면에서 입력받은 일정투표아이디로 삭제")
    @DeleteMapping(value = "/v1/{id}")
    public ResponseDto deleteScv(@PathVariable Long id){
        return new ResponseDto (scheduleVoteService.deleteScv(id));
    }



}
