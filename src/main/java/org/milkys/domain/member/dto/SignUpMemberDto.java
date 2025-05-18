package org.milkys.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.milkys.common.MilkysEnum;
import org.milkys.domain.member.entity.Member;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpMemberDto {
    /**
     * 화면에서 회원가입정보들 받는 DTO
     */


    @NotBlank(message = "아이디를 입력해주십시오")
    private  String memberId;
    @NotBlank(message = "비밀번호를 입력해주십시오")
    private  String memberPw;
    @NotBlank
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "특수문자를 제외하고 2자리 이상, 10자리 이하로 입력해주십시오.")
    private  String memberName;
    @NotBlank
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "특수문자를 제외하고 2자리 이상, 10자리 이하로 입력해주십시오.")
    private  String memberNickname;
    @NotBlank(message = "생일을 입력해주십시오")
    @JsonFormat(pattern = "yyyyMMdd")
    private String memberBirthday;
    @NotBlank(message = "전화번호를 입력해주십시오")
    private  String memberPhoneNumber;

    /**
     * 권한 기본값으로 회원
     */
    private  String memberAuth;


    public Member toEntity( ) {
        return Member.builder()
                .memberId(this.memberId)
                .memberPw(this.memberPw)
                .memberName(this.memberName)
                .memberNickname(this.memberNickname)
                .memberBirthday(this.memberBirthday)
                .memberPhoneNumber(this.memberPhoneNumber)
                .memberAuth(MilkysEnum.MemberRoleType.UNAPPROVAL)
                .build();
    }

}
