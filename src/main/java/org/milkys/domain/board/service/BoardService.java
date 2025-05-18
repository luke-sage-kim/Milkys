package org.milkys.domain.board.service;

import lombok.RequiredArgsConstructor;
import org.milkys.common.MilkysEnum;
import org.milkys.common.dto.ResponseDto;
import org.milkys.domain.board.dto.SelectBoardDto;
import org.milkys.domain.board.dto.UpdateBoardDto;
import org.milkys.domain.board.dto.WriteBoardDto;
import org.milkys.domain.board.entity.Board;
import org.milkys.domain.board.repository.BoardRepository;
import org.milkys.domain.comment.dto.WriteCommentDto;
import org.milkys.domain.comment.entity.Comment;
import org.milkys.domain.member.dto.SelectMemberDto;
import org.milkys.domain.member.dto.SignUpMemberDto;
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
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final HttpSession session;

    private String createBoardVaildation(WriteBoardDto writeBoardDto) {
        if(!StringUtils.hasText(writeBoardDto.getTitle())){
            return "제목이 공백입니다.";
        }
        if(!StringUtils.hasText(writeBoardDto.getContent())){
            return "내용이 입력되지않았습니다.";
        }
        return null;
    }
    public ResponseDto boardWrite(WriteBoardDto writeBoardDto,  Long memberCode) {

        if (memberCode == null) {
            return new ResponseDto<>("로그인을 해주세요.", HttpStatus.UNAUTHORIZED);
        }
        Optional<Member> memberOptional = memberRepository.findById(memberCode);
        if (memberOptional.isPresent()){
            Member member = memberOptional.get();
            Board board = writeBoardDto.toEntity(member);
            Board savedBoard = boardRepository.save(board);
            if(savedBoard != null) {
                return new ResponseDto("글작성을 완료하였습니다.", HttpStatus.OK.value());
            } else return new ResponseDto("글작성을 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }else {
            return new ResponseDto("회원이없습니다", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

    }

    public ResponseDto<List<SelectBoardDto>> selectBoardList() {
        try {
                List<Board> boards = boardRepository.findAll();
                List<SelectBoardDto> selectBoardDtos = boards.stream()
                        .map(SelectBoardDto::fromBoard)  // fromMember 메서드를 사용
                        .collect(Collectors.toList());

            if (!selectBoardDtos.isEmpty()) {
                return new ResponseDto(selectBoardDtos, HttpStatus.OK.value());
            } else {
                return new ResponseDto("가져올 데이터가 없습니다.", HttpStatus.NO_CONTENT.value());
            }
        } catch (Exception e) {
            // 예외에 대한 로그 처리
            return new ResponseDto("서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
    @Transactional
    public void updateViewCount(Long id) {
        boardRepository.updateViewCount(id);
    }

    public ResponseDto<List<SelectBoardDto>> findById(Long id) {
        Optional<Board> optionalBoard = boardRepository.findById(id);
        if (optionalBoard.isPresent()) {
            Board board = optionalBoard.get();
            SelectBoardDto selectBoardDto = SelectBoardDto.fromBoard(board);
            return new ResponseDto(selectBoardDto, HttpStatus.OK.value());
        }else{
            return new ResponseDto("서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    public Object deleteBoard(Long id) {
        Optional<Board> optionalBoard = boardRepository.findById(id);
        if (optionalBoard.isPresent()) {
            Board board = optionalBoard.get();
            boardRepository.delete(board);
            return new ResponseDto("게시글 삭제 성공", HttpStatus.OK.value());
        }
        else {
            return new ResponseDto("게시글 삭제 실패", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    public ResponseDto updateBoard(UpdateBoardDto updateBoardDto, Long memberCode, Long id) {
        Optional<Board> optionalBoard = boardRepository.findById(id);
        if (!optionalBoard.isPresent()) {
            // 해당 회원이 존재하지 않는 경우 에러 응답을 반환합니다.
            return new ResponseDto("존재하지 않는 게시물입니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        Board board = optionalBoard.get();
        if(!board.getMember().getMemberCode().equals(memberCode)){
            return new ResponseDto("작성자만 수정할 수 있습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        board.updateBoardInfo(updateBoardDto.getTitle(),updateBoardDto.getContent(),updateBoardDto.getBoardType());
        boardRepository.save(board);
        return new ResponseDto("게시글이 업데이트되었습니다.", HttpStatus.OK.value());
    }
    public ResponseDto<List<SelectBoardDto>> selectNoticeList() {
        try {
            List<Board> notices = boardRepository.findTop3ByBoardTypeOrderByCreatedTimeDesc(MilkysEnum.BoardType.NOTICE);
            List<SelectBoardDto> selectBoardDtos = notices.stream()
                    .map(SelectBoardDto::fromBoard)  // fromMember 메서드를 사용
                    .collect(Collectors.toList());

            if (!selectBoardDtos.isEmpty()) {
                return new ResponseDto(selectBoardDtos, HttpStatus.OK.value());
            } else {
                return new ResponseDto("가져올 데이터가 없습니다.", HttpStatus.NO_CONTENT.value());
            }
        } catch (Exception e) {
            // 예외에 대한 로그 처리
            return new ResponseDto("서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}
