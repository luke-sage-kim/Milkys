package org.milkys.domain.member.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.milkys.common.dto.ResponseDto;
import org.milkys.config.SessionUser;
import org.milkys.domain.comment.service.CommentService;
import org.milkys.domain.member.dto.*;
import org.milkys.domain.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final CommentService commentService;
    private final HttpSession session; // HttpSession 객체를 주입받음

    @PostMapping("/test")
    public ResponseDto test(){
        return new ResponseDto("테스트",200);
    }

    @ApiOperation(
            value = "회원 가입하기"
            , notes = "화면에서 입력받은 회원정보 멤버테이블에 삽입")
    @PostMapping(value = "/v1/sign")
    public ResponseDto signUp(@Valid @RequestBody SignUpMemberDto requestDto) {
        return memberService.memberInfoSave(requestDto);
    }

    @ApiOperation(
            value = "로그인"
            , notes = "화면에서 입력받은 id와 pw를 멤버테이블에서 조회후 일치시 세션으로 전송 ")
    @PostMapping("/v1/login")
    public ResponseEntity login(@RequestBody LoginDto loginDto) {
        ResponseDto response;

        // 회원 로그인 서비스 호출
        try {
            response = memberService.login(loginDto.getMemberId(), loginDto.getMemberPw());
            // 로그인 성공 시 세션에 사용자 ID 저장
            if (response.getStatus() == HttpStatus.OK.value()) {
                session.setAttribute("memberId", loginDto.getMemberId());  // 세션에 회원 아이디 저장
            }
        }catch(Exception e ){
            log.info(e.getMessage());
            response = new ResponseDto("로그인에 실패하였습니다. 다시 시도해주세요.", HttpStatus.NOT_FOUND.value());
        }
        return new ResponseEntity<ResponseDto<?>>(response,HttpStatus.OK);
    }
    @ApiOperation(
            value = "로그아웃"
            , notes = "세션제거 ")
    @PostMapping("/v1/logout")
    public ResponseDto logoutMember(HttpServletRequest request) {

        request.getSession().invalidate();

        ResponseDto response = new ResponseDto("로그아웃 되었습니다.", HttpStatus.OK.value());
        return response;
    }

    @ApiOperation(
            value = "회원 목록가져오기"
            , notes = "멤버테이블에서 모든회원정보 가져오기")
    @GetMapping(value = "/v1/memberList")
    public ResponseDto<List<SelectMemberDto>> selectAllMember( ) {
        return memberService.selectAllMember();
    }

    @ApiOperation(
            value = "회원정보 수정하기"
            , notes = "멤버테이블에서 모든회원정보 가져오기")
    @PutMapping(value = "/v1/memberUpdate")
    public ResponseDto  updateMember(@RequestBody UpdateMemberDto request
    ) {

        SessionUser loggedInUser = (SessionUser) session.getAttribute("loggedInUser");
        Long memberCode = loggedInUser.getMemberCode();

        return new ResponseDto(memberService.updateMember(request,memberCode));
    }

    //회원탈퇴
    @DeleteMapping(value = "/v1/memberDelete")
    public ResponseDto deleteMember(){
        SessionUser loggedInUser = (SessionUser) session.getAttribute("loggedInUser");
        Long memberCode = loggedInUser.getMemberCode();
        commentService.deleteComment(memberCode,"member");
        return new ResponseDto (memberService.deleteMember(memberCode));
    }

    @ApiOperation(
            value = "세션 조회"
            , notes = "현재 로그인된 사용자의 세션 정보를 조회합니다.")
    @GetMapping("/v1/session")
    public ResponseEntity getSessionInfo(HttpSession session) {
        String memberId = (String) session.getAttribute("memberId");

        if (memberId == null) {
            return new ResponseEntity<>("로그인된 사용자가 없습니다.", HttpStatus.UNAUTHORIZED);
        }

        // 세션에서 다른 정보들도 조회 가능 (예: 권한 정보)
        return new ResponseEntity<>("로그인된 사용자 ID: " + memberId, HttpStatus.OK);
    }

    @ApiOperation(
            value = "아이디 찾기"
            , notes = "입력받은 이름과 생년월일로 아이디를조회합니다")
    @GetMapping("/v1/findId")
    public ResponseDto findMemberId(@RequestBody FindIdDto findIdDto ) {

        return new ResponseDto (memberService.findMemberId(findIdDto));
    }

    @ApiOperation(
            value = "비밀번호 초기화하기"
            , notes = "입력받은 아이디 생년월일로 비밀번호를 초기화합니다")
    @PutMapping("/v1/initialPw")
    public ResponseDto initailizeMemberPw(@RequestBody InitialPwDto initialPwDto ) {

        return new ResponseDto (memberService.initalMemberPw(initialPwDto));
    }

}
