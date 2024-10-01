package org.milkys.config;

import lombok.*;
import org.milkys.common.MilkysEnum;
import org.milkys.domain.member.dto.MemberDto;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SessionUser implements Serializable {
    private Long memberCode;
    private String memberId;
    private String memberPw;
    private String memberName;
    private String memberNickname;
    private String memberBirthday;
    private MilkysEnum.MemberRoleType memberAuth;

    public SessionUser(MemberDto memberInfo){
        this.memberCode = memberInfo.getMemberCode();
        this.memberId = memberInfo.getMemberId();
        this.memberPw = memberInfo.getMemberPw();
        this.memberName = memberInfo.getMemberName();
        this.memberNickname = memberInfo.getMemberNickname();
        this.memberBirthday = memberInfo.getMemberBirthday();
        this.memberAuth = memberInfo.getMemberAuth();

    }

}
