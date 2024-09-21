package org.milkys.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.milkys.common.dto.ResponseDto;
import org.milkys.config.SessionUser;
import org.milkys.domain.member.dto.LoginDto;
import org.milkys.domain.member.dto.SignUpMemberDto;
import org.milkys.domain.member.entity.Member;
import org.milkys.domain.member.repository.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final HttpSession session;

    /**
     * controller에서 회원
     * @param signUpMemberDto
     * @param
     * @return
     */
    public ResponseDto memberInfoSave(SignUpMemberDto signUpMemberDto) {
        Member member = signUpMemberDto.toEntity();
        Member savedMember = memberRepository.save(member);
        if(savedMember != null) {
            return new ResponseDto("회원가입이 성공했습니다.", HttpStatus.OK.value());
        } else return new ResponseDto("회원가입을 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    //회원가입 시 유효성 검사
    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();

        for(FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }
        return  validatorResult;
    }

    /**
     * 로그인기능 아이디랑 비밀번호를 받아서 디비에서 조회후 일치하는 아이디가있으면 비밀번호조회후
     * 비밀번호마저일치하면 멤버정보를 세션에 전달
     * @param id
     * @param pw
     * @return
     */
    public ResponseDto login(String id, String pw) {
        Optional<Member> member = Optional.ofNullable(memberRepository.findByMemberId(id));

        if (member.isPresent()) {
            LoginDto loginDto = member.get().bringMemberInfo();
            if (loginDto.getMemberPw().equals(pw)) {
                // 패스워드가 일치하면 로그인 성공으로 처리합니다.
                SessionUser sessionUser = new SessionUser(loginDto);
                session.setAttribute("loggedInUser", sessionUser);

                return new ResponseDto("로그인에 성공하였습니다.", HttpStatus.OK.value());
            } else {
                // 패스워드가 일치하지 않으면 로그인 실패로 처리합니다.
                return new ResponseDto("비밀번호가 일치하지 않습니다", HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
        }
        else {

            return new ResponseDto("일치하는 회원이 없습니다" , HttpStatus.INTERNAL_SERVER_ERROR.value());

        }
    }
}
