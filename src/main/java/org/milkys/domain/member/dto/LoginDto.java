package org.milkys.domain.member.dto;

import lombok.*;
import org.milkys.common.MilkysEnum;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class LoginDto {
    private Long memberCode;
    private String memberId;
    private String memberPw;
    private String memberName;
    private String memberNickname;
    private String memberBirthday;
    private MilkysEnum.MemberRoleType memberAuth;
}
