package org.milkys.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Builder
public class UpdateMemberDto {
    /**
     * 화면에서 회원수정정보 담는 Dto
     */
    private String memberId;
    private String memberPw;
    private String memberNickname;
    private String memberBirthday;
    private String memberPhoneNumber;
}
