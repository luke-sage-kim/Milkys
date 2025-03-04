package org.milkys.domain.schedule.service;

import lombok.RequiredArgsConstructor;
import org.milkys.common.MilkysEnum;
import org.milkys.common.dto.ResponseDto;
import org.milkys.domain.member.entity.Member;
import org.milkys.domain.member.repository.MemberRepository;
import org.milkys.domain.schedule.dto.SelectScheduleDto;
import org.milkys.domain.schedule.dto.UpdateScheduleDto;
import org.milkys.domain.schedule.dto.WriteScheduleDto;
import org.milkys.domain.schedule.entity.Schedule;
import org.milkys.domain.schedule.repository.ScheduleRepository;
import org.milkys.domain.scheduleVote.dto.SelectScheduleDto;
import org.milkys.domain.scheduleVote.dto.UpdateScheduleDto;
import org.milkys.domain.scheduleVote.dto.WriteScheduleDto;
import org.milkys.domain.scheduleVote.entity.ScheduleVote;
import org.milkys.domain.scheduleVote.repository.ScheduleVoteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final MemberRepository memberRepository;
    private final HttpSession session;

    public ResponseDto scWrite(WriteScheduleDto writeScheduleDto, HttpSession session) {

        String memberId = (String) session.getAttribute("memberId");
        if (memberId == null) {
            return new ResponseDto<>("로그인을 해주세요.", HttpStatus.UNAUTHORIZED);
        }
        String memberAuth = (String) session.getAttribute("memberAuth");
        if(!memberAuth.equals(MilkysEnum.MemberRoleType.LEADER)
        ){
            return new ResponseDto<>("리더만 스케줄을 등록할 수 있습니다.", HttpStatus.UNAUTHORIZED);

        }
        Schedule schedule =  writeScheduleDto.toEntity();
        Schedule scheduleSave = scheduleRepository.save(schedule);
        if(scheduleSave != null) {
            return new ResponseDto("일정작성을 완료하였습니다.", HttpStatus.OK.value());
        } else return new ResponseDto("일정작성을 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public ResponseDto<List<SelectScheduleDto>> selectScheduleList() {
        try {
                List<Schedule> schedules = scheduleRepository.findAll();
                List<SelectScheduleDto> selectScheduleDtos = schedules.stream()
                        .map(SelectScheduleDto::fromSchedule)  // fromMember 메서드를 사용
                        .collect(Collectors.toList());
            if (!selectScheduleDtos.isEmpty()) {
                return new ResponseDto(selectScheduleDtos, HttpStatus.OK.value());
            } else {
                return new ResponseDto("가져올 데이터가 없습니다.", HttpStatus.NO_CONTENT.value());
            }
        } catch (Exception e) {
            // 예외에 대한 로그 처리
            return new ResponseDto("서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    public ResponseDto<List<SelectScheduleDto>> scheduleDetail(long id) {
        Optional<Schedule> optionalSchedule = scheduleRepository.findById(id);
        if (optionalSchedule.isPresent()) {
            Schedule schedule = optionalSchedule.get();
            SelectScheduleDto selectScheduleDto = SelectScheduleDto.fromSchedule(schedule);
            return new ResponseDto(selectScheduleDto, HttpStatus.OK.value());
        }else{
            return new ResponseDto("서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    public Object deleteschedule(Long id) {
        Optional<Schedule> optionalSchedule = scheduleRepository.findById(id);
        if (optionalSchedule.isPresent()) {
            Schedule schedule = optionalSchedule.get();
            scheduleRepository.delete(schedule);
            return new ResponseDto("일정 삭제 성공", HttpStatus.OK.value());
        }
        else {
            return new ResponseDto("일정 삭제 실패", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    public ResponseDto updateSchedule(UpdateScheduleDto updateScheduleDto, Long memberCode, Long id) {
        Optional<Schedule> optionalSchedule = scheduleRepository.findById(id);
        if (!optionalSchedule.isPresent()) {
            // 해당 회원이 존재하지 않는 경우 에러 응답을 반환합니다.
            return new ResponseDto("존재하지 않는 게시물입니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        String memberAuth = (String) session.getAttribute("memberAuth");
        if(!memberAuth.equals(MilkysEnum.MemberRoleType.LEADER)
        ){
            return new ResponseDto<>("리더만 스케줄을 수정할 수 있습니다.", HttpStatus.UNAUTHORIZED);

        }
        Schedule schedule = optionalSchedule.get();
        schedule.updateScheduleInfo(updateScheduleDto.getScDate(),updateScheduleDto.getScLoca(),updateScheduleDto.getScContent());
        scheduleRepository.save(schedule);
        return new ResponseDto("일정가 업데이트되었습니다.", HttpStatus.OK.value());
    }

}
