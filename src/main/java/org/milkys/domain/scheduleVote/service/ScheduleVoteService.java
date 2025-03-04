package org.milkys.domain.scheduleVote.service;

import lombok.RequiredArgsConstructor;
import org.milkys.common.dto.ResponseDto;
import org.milkys.domain.member.entity.Member;
import org.milkys.domain.member.repository.MemberRepository;
import org.milkys.domain.schedule.dto.SelectScheduleDto;
import org.milkys.domain.schedule.dto.WriteScheduleDto;
import org.milkys.domain.scheduleVote.dto.SelectScvDto;
import org.milkys.domain.scheduleVote.dto.UpdateScvDto;
import org.milkys.domain.scheduleVote.dto.WriteScvDto;
import org.milkys.domain.scheduleVote.entity.ScheduleVote;
import org.milkys.domain.scheduleVote.repository.ScheduleVoteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleVoteService {
    private final ScheduleVoteRepository scheduleVoteRepository;
    private final MemberRepository memberRepository;
    private final HttpSession session;

    public ResponseDto scvWrite(WriteScvDto writeScheduleVoteDto, HttpSession session) {

        String memberId = (String) session.getAttribute("memberId");
        if (memberId == null) {
            return new ResponseDto<>("로그인을 해주세요.", HttpStatus.UNAUTHORIZED);
        }
        Member member = memberRepository.findByMemberId(memberId);
        ScheduleVote scheduleVote = writeScheduleVoteDto.toEntity(member);
        ScheduleVote ScheduleVotesave = scheduleVoteRepository.save(scheduleVote);
        if(ScheduleVotesave != null) {
            return new ResponseDto("일정투표작성을 완료하였습니다.", HttpStatus.OK.value());
        } else return new ResponseDto("일정투표작성을 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public ResponseDto<List<SelectScvDto>> selectScheduleVoteList() {
        try {
                List<ScheduleVote> scheduleVotes = scheduleVoteRepository.findAll();
                List<SelectScvDto> selectScheduleVoteDtos = scheduleVotes.stream()
                        .map(SelectScvDto::fromScv)  // fromMember 메서드를 사용
                        .collect(Collectors.toList());
            if (!selectScheduleVoteDtos.isEmpty()) {
                return new ResponseDto(selectScheduleVoteDtos, HttpStatus.OK.value());
            } else {
                return new ResponseDto("가져올 데이터가 없습니다.", HttpStatus.NO_CONTENT.value());
            }
        } catch (Exception e) {
            // 예외에 대한 로그 처리
            return new ResponseDto("서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    public ResponseDto<List<SelectScvDto>> scvDetail(String scvDate) {
        Optional<ScheduleVote> optionalScheduleVote = scheduleVoteRepository.findByVoteDate(scvDate);
        if (optionalScheduleVote.isPresent()) {
            ScheduleVote scheduleVote = optionalScheduleVote.get();
            SelectScvDto selectScvDto = SelectScvDto.fromScv(scheduleVote);
            return new ResponseDto(selectScvDto, HttpStatus.OK.value());
        }else{
            return new ResponseDto("서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    public Object deleteScv(Long id) {
        Optional<ScheduleVote> optionalScheduleVote = scheduleVoteRepository.findById(id);
        if (optionalScheduleVote.isPresent()) {
            ScheduleVote scheduleVote = optionalScheduleVote.get();
            scheduleVoteRepository.delete(scheduleVote);
            return new ResponseDto("일정투표 삭제 성공", HttpStatus.OK.value());
        }
        else {
            return new ResponseDto("일정투표 삭제 실패", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    public ResponseDto updatescv(UpdateScvDto updateScvDto, Long memberCode, Long id) {
        Optional<ScheduleVote> optionalScheduleVote = scheduleVoteRepository.findById(id);
        if (!optionalScheduleVote.isPresent()) {
            // 해당 회원이 존재하지 않는 경우 에러 응답을 반환합니다.
            return new ResponseDto("존재하지 않는 게시물입니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        ScheduleVote scheduleVote = optionalScheduleVote.get();
        if(!scheduleVote.getMember().getMemberCode().equals(memberCode)){
            return new ResponseDto("작성자만 수정할 수 있습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        scheduleVote.updateScvInfo(updateScvDto.getScvDate(),updateScvDto.getScvStart(),updateScvDto.getScvEnd());
        scheduleVoteRepository.save(scheduleVote);
        return new ResponseDto("일정투표가 업데이트되었습니다.", HttpStatus.OK.value());
    }

    public void deleteByDay(WriteScheduleDto requestDto) {
        String scDate = requestDto.getScDate();
        scheduleVoteRepository.deleteByDate(scDate);
    }

}
