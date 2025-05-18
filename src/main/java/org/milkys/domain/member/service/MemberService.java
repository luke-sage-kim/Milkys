package org.milkys.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.milkys.common.MilkysEnum;
import org.milkys.common.dto.ResponseDto;
import org.milkys.config.SessionUser;
import org.milkys.domain.member.dto.*;
import org.milkys.domain.member.entity.Member;
import org.milkys.domain.member.repository.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
        //여기서 데이터 검증
        String error = createUserVaildation(signUpMemberDto);
        if(StringUtils.hasText(error)) return new ResponseDto(error, HttpStatus.INTERNAL_SERVER_ERROR.value());
        Member member = signUpMemberDto.toEntity();

        Member savedMember = memberRepository.save(member);
        if(savedMember != null) {
            return new ResponseDto("회원가입이 성공했습니다.", HttpStatus.OK.value());
        } else return new ResponseDto("회원가입을 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private String createUserVaildation(SignUpMemberDto signUpMemberDto) {
        if(!StringUtils.hasText(signUpMemberDto.getMemberId())){
            return "ID가 공백입니다.";
        }
        if(!StringUtils.hasText(signUpMemberDto.getMemberPw())){
            return "비밀번호가 입력되지 않았습니다.";
        }
        Member member = signUpMemberDto.toEntity();
        Member checkMemberId = memberRepository.findByMemberId(member.getMemberId());
        if(checkMemberId !=null){
            return "아이디가 중복입니다.";
        }

        return null;
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
        Optional<Member> memberOptional = Optional.ofNullable(memberRepository.findByMemberId(id));

        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            if(member.getMemberAuth().equals(MilkysEnum.MemberRoleType.UNAPPROVAL)){
                return new ResponseDto("UNAPPROVAL", HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
            MemberDto memberDto = memberOptional.get().bringMemberInfo();
            if (memberDto.getMemberPw().equals(pw)) {
                // 패스워드가 일치하면 로그인 성공으로 처리합니다.
                SessionUser sessionUser = new SessionUser(memberDto);
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

    //전체 멤버 데이터 가져오기
    public ResponseDto selectAllMember() {
        try {
        //멤버 테이블 리스트객체로 담아옴
        List<Member> members = memberRepository.findAll();
            List<SelectMemberDto> selectMemberDtos = members.stream()
                    .map(SelectMemberDto::fromMember)  // fromMember 메서드를 사용
                    .collect(Collectors.toList());
            if (!selectMemberDtos.isEmpty()) {
                return new ResponseDto(selectMemberDtos, HttpStatus.OK.value());
            } else {
                return new ResponseDto("가져올 데이터가 없습니다.", HttpStatus.NO_CONTENT.value());
            }
        } catch (Exception e) {
            // 예외에 대한 로그 처리
            return new ResponseDto("서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

    }


    public ResponseDto updateMember(UpdateMemberDto updateMemberDto, Long memberCode) {
        Optional<Member> memberOptional = memberRepository.findById(memberCode);
        if (!memberOptional.isPresent()) {
            // 해당 회원이 존재하지 않는 경우 에러 응답을 반환합니다.
            return new ResponseDto("존재하지 않는 회원입니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        Member member = memberOptional.get();
        member.updateMemberInfo(
                updateMemberDto.getMemberPw(),
                updateMemberDto.getMemberNickname(),
                updateMemberDto.getMemberPhoneNumber(),
                updateMemberDto.getMemberBirthday()
        );
        memberRepository.save(member);

        return new ResponseDto("회원 정보가 업데이트되었습니다.", HttpStatus.OK.value());
    }

    public ResponseDto<MemberDto> deleteMember( Long memberCode) {
        Optional<Member> memberOptional = memberRepository.findById(memberCode);


        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            memberRepository.delete(member);
            return new ResponseDto("회원탈퇴 성공", HttpStatus.OK.value());
        }
        else {
            return new ResponseDto("회원탈퇴 실패", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    public ResponseDto<MemberDto> findMemberId(FindIdDto findIdDto) {
        Member member = memberRepository.findByMemberNameAndPhoneNum(findIdDto.getMemberName(), findIdDto.getMemberPhoneNumber());
        if(member.getMemberId() ==null){
            return new ResponseDto("이름 또는 전화번호를 잘못기입하셨습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return new ResponseDto(member.getMemberId(), HttpStatus.OK.value());
    }

    public ResponseDto<MemberDto> initalMemberPw(InitialPwDto initialPwDto) {
        Member member = memberRepository.findByMemberId(initialPwDto.getMemberId());
        if(member == null){
            return new ResponseDto("idError", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        if(!member.getMemberBirthday().equals(initialPwDto.getMemberBirthday())){
            return new ResponseDto("birthError", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        member.initializePassword();  // 비밀번호 초기화
        // 변경된 회원 정보 저장
        memberRepository.save(member);
        return new ResponseDto("success.", HttpStatus.OK.value());
    }

    public ResponseDto changeMememberAuth(AuthDto authDto) {
        String myAuth = authDto.getMemberAuth();
        long id = authDto.getMemberCode();
        if(!myAuth.equals("ADMIN")
        ){
            return new ResponseDto<>("관리자만 회원권한을 수정할 수 있습니다.", HttpStatus.UNAUTHORIZED);
        }
        Optional<Member> memberOptional = memberRepository.findById(id);
        if(memberOptional.isPresent()){
            Member member = memberOptional.get();
            member.changeMememberAuth(authDto.getTargetAuth());
            memberRepository.save(member);
            return new ResponseDto("회원등급이 수정되었습니다.", HttpStatus.OK.value());
        }else {
            return new ResponseDto<>("회원이 존재하지않습니다.", HttpStatus.UNAUTHORIZED);
        }
    }

//    public ResponseDto changeMememberToAdmin(Long id) {
//        Optional<Member> memberOptional = memberRepository.findById(id);
//        if(memberOptional.isPresent()){
//            Member member = memberOptional.get();
//            member.changeMememberAuth("ADMIN");
//            memberRepository.save(member);
//            return new ResponseDto("관리자로 지정되었습니다.", HttpStatus.OK.value());
//        }else {
//            return new ResponseDto<>("회원이 존재하지않습니다.", HttpStatus.UNAUTHORIZED);
//        }
//    }

    public MemberDto getMemberDto(String id) {
        Optional<Member> member = Optional.ofNullable(memberRepository.findByMemberId(id));

        if (member.isPresent()) {
            MemberDto memberDto = member.get().bringMemberInfo();
            return  memberDto;
        }else{
            return null;
        }

    }

    public ResponseDto unapprovalList() {
        try {
            //멤버 테이블 리스트객체로 담아옴
            List<Member> members = memberRepository.findByMemberAuth(MilkysEnum.MemberRoleType.UNAPPROVAL);
            List<SelectMemberDto> selectMemberDtos = members.stream()
                    .map(SelectMemberDto::fromMember)  // fromMember 메서드를 사용
                    .collect(Collectors.toList());
            if (!selectMemberDtos.isEmpty()) {
                return new ResponseDto(selectMemberDtos, HttpStatus.OK.value());
            } else {
                return new ResponseDto("가져올 데이터가 없습니다.", HttpStatus.NO_CONTENT.value());
            }
        } catch (Exception e) {
            // 예외에 대한 로그 처리
            return new ResponseDto("서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

    }

    public ResponseDto approveMember(AuthDto authDto) {
        String myAuth = authDto.getMemberAuth();
        long id = authDto.getMemberCode();
        if(!myAuth.equals("ADMIN")
        ){
            return new ResponseDto<>("관리자만 회원권한을 수정할 수 있습니다.", HttpStatus.UNAUTHORIZED);
        }
        Optional<Member> memberOptional = memberRepository.findById(id);
        if(memberOptional.isPresent()){
            Member member = memberOptional.get();
            member.changeMememberAuth("USER");
            memberRepository.save(member);
            return new ResponseDto("회원가입이 승인되었습니다.", HttpStatus.OK.value());
        }else {
            return new ResponseDto<>("회원이 존재하지않습니다.", HttpStatus.UNAUTHORIZED);
        }
    }

    public ResponseDto findDuplicateMember(String memberId) {
        boolean exists = memberRepository.existsByMemberId(memberId);
        if (exists) {
            return new ResponseDto("중복", HttpStatus.CONFLICT.value());
        } else {
            return new ResponseDto("중복없음", HttpStatus.OK.value());

        }
    }

    //approveMember
}
