package org.milkys.domain.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.milkys.common.dto.ResponseDto;
import org.milkys.domain.member.dto.SignUpMemberDto;
import org.milkys.domain.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;

    @Autowired
    private HttpSession session; // HttpSession 객체를 주입받음

    @PostMapping(value = "/v1/sign")
    public ResponseDto signUp(@Valid @ModelAttribute("member") SignUpMemberDto requestDto, BindingResult errors, Model model) {
        /**
         * 웹에서 받아온 데이터에 에러존재시처리로직
         */
        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                log.info("Object name: {}", error.getObjectName());
                log.info("Error message: {}", error.getDefaultMessage());
                log.info("Error codes: {}", Arrays.toString(error.getCodes()));
            }

            model.addAttribute("member", requestDto);
            Map<String, String> validatorResult = memberService.validateHandling(errors);

            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }
        }

        return memberService.memberInfoSave(requestDto);

    }
/**
 * @Valid @ModelAttribute("member") SignUpMemberDto requestDto: 클라이언트가 전송한 데이터를 SignUpMemberDto 객체로 바인딩하고 유효성 검사를 수행합니다.
 * BindingResult errors: 유효성 검사 결과를 담고 있는 객체입니다. 검증 오류가 발생하면 이 객체를 통해 오류 정보를 확인할 수 있습니다.
 * Model model: 뷰에 전달할 데이터를 담는 객체입니다.
 * 유효성 검사 및 에러 처리:
 *
 * if (errors.hasErrors()): BindingResult 객체의 hasErrors() 메소드를 사용하여 검증 오류가 있는지 확인합니다.
 * 에러가 있을 경우, errors.getAllErrors()를 통해 모든 오류를 조회하고, 각 오류의 정보를 로깅합니다.
 * model.addAttribute("member", requestDto): 오류가 발생했을 때 사용자가 입력한 값을 다시 폼에 보여주기 위해 model에 requestDto를 추가합니다.
 * memberService.validateHandling(errors): 사용자 정의 검증 로직을 사용하여 추가적인 에러 메시지를 처리합니다.
 * for (String key : validatorResult.keySet()): validatorResult에 담긴 에러 메시지를 model에 추가합니다. 이는 뷰에서 에러 메시지를 표시하는 데 사용됩니다.
 */

@PostMapping("/v1/login")
public ResponseEntity login(@RequestParam String email, @RequestParam String passwd) {
    ResponseDto response;

    // 회원 로그인 서비스 호출
    try {
        response = memberService.login(email, passwd);
    }catch(Exception e ){
        log.info(e.getMessage());
        response = new ResponseDto("로그인에 실패하였습니다. 다시 시도해주세요.", HttpStatus.NOT_FOUND.value());
    }
    return new ResponseEntity<ResponseDto<?>>(response,HttpStatus.OK);
}

}
