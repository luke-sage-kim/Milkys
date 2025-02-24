package org.milkys.domain.recordings.service;

import lombok.RequiredArgsConstructor;
import org.milkys.common.dto.ResponseDto;
import org.milkys.domain.recordings.dto.SelectRecordingDto;
import org.milkys.domain.recordings.dto.UpdateRecordingDto;
import org.milkys.domain.recordings.dto.WriteRecordingDto;
import org.milkys.domain.recordings.entity.Recordings;
import org.milkys.domain.recordings.repository.RecordingsRepository;
import org.milkys.domain.member.entity.Member;
import org.milkys.domain.member.repository.MemberRepository;
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
public class RecordingService {
    private final RecordingsRepository recordingsRepository;
    private final MemberRepository memberRepository;
    private final HttpSession session;

    private String createBoardVaildation(WriteRecordingDto writeRecordingDto) {
        if(!StringUtils.hasText(writeRecordingDto.getTitle())){
            return "제목이 공백입니다.";
        }
        if(!StringUtils.hasText(writeRecordingDto.getContent())){
            return "내용이 입력되지않았습니다.";
        }
        return null;
    }
    public ResponseDto recordingWrite(WriteRecordingDto writeRecordingDto, HttpSession session) {
        String error = createBoardVaildation(writeRecordingDto);

        if(StringUtils.hasText(error)) return new ResponseDto(error, HttpStatus.INTERNAL_SERVER_ERROR.value());
        String memberId = (String) session.getAttribute("memberId");
        if (memberId == null) {
            return new ResponseDto<>("로그인을 해주세요.", HttpStatus.UNAUTHORIZED);
        }
        Member member = memberRepository.findByMemberId(memberId);
        Recordings recordings = writeRecordingDto.toEntity(member);
        Recordings Recordingsave = recordingsRepository.save(recordings);
        if(Recordingsave != null) {
            return new ResponseDto("음원기록작성을 완료하였습니다.", HttpStatus.OK.value());
        } else return new ResponseDto("음원기록작성을 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public ResponseDto<List<SelectRecordingDto>> selectRecordingList() {
        try {
                List<Recordings> recordings = recordingsRepository.findAll();
                List<SelectRecordingDto> selectRecordingDtos = recordings.stream()
                        .map(SelectRecordingDto::fromRecordings)  // fromMember 메서드를 사용
                        .collect(Collectors.toList());

            if (!selectRecordingDtos.isEmpty()) {
                return new ResponseDto(selectRecordingDtos, HttpStatus.OK.value());
            } else {
                return new ResponseDto("가져올 데이터가 없습니다.", HttpStatus.NO_CONTENT.value());
            }
        } catch (Exception e) {
            // 예외에 대한 로그 처리
            return new ResponseDto("서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    public ResponseDto<List<SelectRecordingDto>> findById(Long id) {
        Optional<Recordings> optionalRecording = recordingsRepository.findById(id);
        if (optionalRecording.isPresent()) {
            Recordings recordings = optionalRecording.get();
            SelectRecordingDto selectRecordingDto = SelectRecordingDto.fromRecordings(recordings);
            return new ResponseDto(selectRecordingDto, HttpStatus.OK.value());
        }else{
            return new ResponseDto("서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    public Object deleteRecording(Long id) {
        Optional<Recordings> optionalRecording = recordingsRepository.findById(id);
        if (optionalRecording.isPresent()) {
            Recordings recordings = optionalRecording.get();
            recordingsRepository.delete(recordings);
            return new ResponseDto("음원기록 삭제 성공", HttpStatus.OK.value());
        }
        else {
            return new ResponseDto("음원기록 삭제 실패", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    public ResponseDto updateRecording(UpdateRecordingDto updateRecordingDto, Long memberCode, Long id) {
        Optional<Recordings> optionalRecording = recordingsRepository.findById(id);
        if (!optionalRecording.isPresent()) {
            // 해당 회원이 존재하지 않는 경우 에러 응답을 반환합니다.
            return new ResponseDto("존재하지 않는 게시물입니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        Recordings recordings = optionalRecording.get();
        if(!recordings.getMember().getMemberCode().equals(memberCode)){
            return new ResponseDto("작성자만 수정할 수 있습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        recordings.updateRecordingInfo(updateRecordingDto.getTitle(),updateRecordingDto.getContent(),updateRecordingDto.getRecDate());
        recordingsRepository.save(recordings);
        return new ResponseDto("음원기록가 업데이트되었습니다.", HttpStatus.OK.value());
    }

}
