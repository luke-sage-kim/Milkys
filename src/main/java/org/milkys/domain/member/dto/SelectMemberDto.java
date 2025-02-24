package org.milkys.domain.member.dto;

import lombok.*;
import org.milkys.common.MilkysEnum;
import org.milkys.domain.member.entity.Member;

import java.util.Date;

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
    private String memberPhoneNumber;
    private MilkysEnum.MemberRoleType memberAuth;


    public static SelectMemberDto fromMember(Member member) {
        return SelectMemberDto.builder()
                .memberCode(member.getMemberCode())
                .memberId(member.getMemberId())
                .memberPw(member.getMemberPw())
                .memberName(member.getMemberName())
                .memberNickname(member.getMemberNickname())
                .memberBirthday(member.getMemberBirthday())
                .memberAuth(member.getMemberAuth())
                .memberPhoneNumber(member.getMemberPhoneNumber())
                .build();
    }



}
