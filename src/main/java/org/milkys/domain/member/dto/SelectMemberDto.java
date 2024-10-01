package org.milkys.domain.member.dto;

import lombok.*;
import org.milkys.common.MilkysEnum;
import org.milkys.domain.member.entity.Member;

@Setter
@Getter
@ToString(exclude = "passwd")
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class SelectMemberDto {

    private Long memberCode;
    private String memberId;
    private String memberPw;
    private String memberName;
    private String memberNickname;
    private String memberBirthday;
    private MilkysEnum.MemberRoleType memberAuth;


    public SelectMemberDto (Member member) {
        this.memberCode = member.getMemberCode();
        this.memberId = member.getMemberId();
        this.memberPw = member.getMemberPw();
        this.memberName = member.getMemberName();
        this.memberNickname = member.getMemberBirthday();
        this.memberAuth = member.getMemberAuth();

    }



}
