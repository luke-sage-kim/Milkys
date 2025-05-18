package org.milkys.domain.member.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.milkys.common.MilkysEnum;
import org.milkys.common.dto.ResponseDto;
import org.milkys.config.SessionUser;
import org.milkys.domain.comment.service.CommentService;
import org.milkys.domain.member.dto.*;
import org.milkys.domain.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Map<String, String> sessionData = new HashMap<>();
        // 회원 로그인 서비스 호출
        try {
            response = memberService.login(loginDto.getMemberId(), loginDto.getMemberPw());
            // 로그인 성공 시 세션에 사용자 ID 저장
            if (response.getStatus() == HttpStatus.OK.value()) {
                session.setAttribute("memberId", loginDto.getMemberId());  // 세션에 회원 아이디 저장
                //원초적방법쓰자 memberDTO다 세팅
                MemberDto memberDto = memberService.getMemberDto(loginDto.getMemberId());
                response.setResultData(memberDto);
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
    public ResponseDto updateMember(@RequestBody UpdateMemberDto request) {

        // 세션에서 memberId를 가져오기
        String memberId = request.getMemberId();

        // memberId가 null인 경우 처리
        if (memberId == null) {
            return new ResponseDto("로그인 정보가 없습니다.", HttpStatus.FORBIDDEN.value());
        }

        // memberId로 회원 정보 조회
        MemberDto memberDto = memberService.getMemberDto(memberId);

        // 회원 정보가 존재하지 않는 경우 처리
        if (memberDto == null) {
            return new ResponseDto("회원 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND.value());
        }

        // 회원 정보 수정
        Long memberCode = memberDto.getMemberCode();
        return new ResponseDto(memberService.updateMember(request, memberCode));
    }


    //회원탈퇴
    @DeleteMapping("/v1/memberDelete")
    public ResponseDto deleteMember(@RequestParam long memberCode) {
        commentService.deleteComment(memberCode, "member");
        return new ResponseDto(memberService.deleteMember(memberCode));
    }

    @GetMapping("/v1/session")
    public ResponseEntity<String> getSessionInfo(HttpSession session, HttpServletResponse response) {
        // 세션에서 사용자 정보 조회
        SessionUser sessionUser = (SessionUser) session.getAttribute("loggedInUser");

        if (sessionUser == null) {
            return new ResponseEntity<>("로그인된 사용자가 없습니다.", HttpStatus.UNAUTHORIZED);
        }

        // 세션에서 사용자 정보 출력
        StringBuilder userInfo = new StringBuilder("로그인된 사용자 정보: ");
        userInfo.append("memberCode=").append(sessionUser.getMemberCode()).append(", ");
        userInfo.append("memberId=").append(sessionUser.getMemberId()).append(", ");
        userInfo.append("memberPw=").append(sessionUser.getMemberPw()).append(", ");
        userInfo.append("memberName=").append(sessionUser.getMemberName()).append(", ");
        userInfo.append("memberNickname=").append(sessionUser.getMemberNickname()).append(", ");
        userInfo.append("memberBirthday=").append(sessionUser.getMemberBirthday()).append(", ");
        userInfo.append("memberPhoneNumber=").append(sessionUser.getMemberPhoneNumber()).append(", ");
        userInfo.append("memberAuth=").append(sessionUser.getMemberAuth());

        // 세션 쿠키 설정
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");

        return new ResponseEntity<>(userInfo.toString(), HttpStatus.OK);
    }

    @ApiOperation(
            value = "아이디 찾기"
            , notes = "입력받은 이름과 생년월일로 아이디를조회합니다")
    @PostMapping("/v1/findId")
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
    @ApiOperation(
            value = "회원 권한 수정"
            , notes = "관리자가 회원 권한 수정")
    @PutMapping("/v1/auth")
    public ResponseDto changeMememberAuth(@RequestBody AuthDto authDto ) {

        return new ResponseDto (memberService.changeMememberAuth(authDto));
    }

    @ApiOperation(
            value = "승인전 회원조회"
            , notes = "승인전 회원조회")
    @GetMapping(value = "/v1/unapprovalList")
    public ResponseDto<List<SelectMemberDto>> unapprovalList(
    ) {
        return memberService.unapprovalList();
    }

    @ApiOperation(
            value = "회원가입요청 승인"
            , notes = "선택한 회원 가입 승인")
    @PutMapping("/v1/approve")
    public ResponseDto approveMember(@RequestBody AuthDto authDto ) {

        return new ResponseDto (memberService.approveMember(authDto));
    }

    @ApiOperation(
            value = "중복아이디 판별"
            , notes = "아이디를 입력받아 중복인지 아닌지 검사")
    @PostMapping("/v1/duplicate")
    public ResponseDto findDuplicateMember(@RequestBody LoginDto loginDto ) {
        return new ResponseDto (memberService.findDuplicateMember(loginDto.getMemberId()));
    }




}
