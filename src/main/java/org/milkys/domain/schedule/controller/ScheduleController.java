package org.milkys.domain.schedule.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.milkys.common.dto.ResponseDto;
import org.milkys.config.SessionUser;
import org.milkys.domain.schedule.dto.SelectScheduleDto;
import org.milkys.domain.schedule.dto.UpdateScheduleDto;
import org.milkys.domain.schedule.dto.WriteScheduleDto;
import org.milkys.domain.schedule.service.ScheduleService;
import org.milkys.domain.scheduleVote.service.ScheduleVoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/sc")
public class ScheduleController {
    @Autowired
    private final ScheduleService scheduleService;
    private final ScheduleVoteService scheduleVoteService;
    private final HttpSession session;

    /**
     * 추후 사진 첨부 개발필요
     * @param requestDto
     * @param session
     * @return
     */
    @ApiOperation(
            value = "일정등록"
            , notes = "화면에서 입력받은 일정 작성" +
            "리더만작성가능")
    @PostMapping(value = "/v1/write")
    public ResponseDto scWrite(@Valid @RequestBody WriteScheduleDto requestDto, HttpSession session) {
        scheduleVoteService.deleteByDay(requestDto);
        return scheduleService.scWrite(requestDto,session);

    }

    @ApiOperation(
            value = "일정 전체조회"
            , notes = "일정테이블에있는 전체데이터조회")
    @GetMapping(value = "/v1")
    public ResponseDto<List<SelectScheduleDto>> scheduleList() {

        return scheduleService.selectScheduleList();

    }
    @ApiOperation(
            value = "일정 단일조회"
            , notes = "일정테이블에있는 해당날짜에 해당되는 투표가져오기")
    @GetMapping(value = "/v1/{id}")
    public ResponseDto<List<SelectScheduleDto>> scheduleDetail(@PathVariable long id) {
        return scheduleService.scheduleDetail(id);

    }
    @ApiOperation(
            value = "게시글 수정하기"
            , notes = "로그인된 아이디와 글작성자 아이디 비교 후 수정할 정보를 수정")
    @PutMapping(value = "/v1/{id}")
    public ResponseDto  updateschedule(@RequestBody UpdateScheduleDto updatescheduleDto, @PathVariable Long id ) {

        SessionUser loggedInUser = (SessionUser) session.getAttribute("loggedInUser");
        Long memberCode = loggedInUser.getMemberCode();

        return new ResponseDto(scheduleService.updateSchedule(updatescheduleDto,memberCode,id));
    }
    @ApiOperation(
            value = "일정 삭제"
            , notes = "화면에서 입력받은 일정아이디로 삭제")
    @DeleteMapping(value = "/v1/{id}")
    public ResponseDto deleteschedule(@PathVariable Long id){
        return new ResponseDto (scheduleService.deleteschedule(id));
    }
//    @ApiOperation(
//            value = "가장 최신 스케줄 가져오기"
//            , notes = "일정에있는 날짜중 가장 최신에 해당하는 글 정보가져오기")
//    @GetMapping(value = "/v1/recent")
//    public ResponseDto <SelectScheduleDto> getRecent() {
//
//        return scheduleService.getRecent();
//
//    }


}
