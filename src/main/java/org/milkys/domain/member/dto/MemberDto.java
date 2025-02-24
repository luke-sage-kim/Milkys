package org.milkys.domain.member.dto;

import lombok.*;
import org.milkys.common.MilkysEnum;
import org.milkys.domain.member.entity.Member;

import java.util.Date;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDto {

    /**
     * Member객체에 들어있는 정보를 담는 Dto
     */
    private Long memberCode;
    private String memberId;
    private String memberPw;
    private String memberName;
    private String memberNickname;
    private String memberBirthday;
    private String memberPhoneNumber;
    private MilkysEnum.MemberRoleType memberAuth;

    public Member toUpdateEntity(){
        return Member.builder()
                .memberPw(memberPw)
                .memberNickname(memberNickname)
                .memberPhoneNumber(memberPhoneNumber)
                .build();
    }

    public Member toInitailizePw(){
        return Member.builder()
                .memberCode(memberCode)
                .memberPw("0618")
                .build();
    }

}
