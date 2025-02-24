package org.milkys.domain.member.dto;

import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class LoginDto {
    private String memberId;
    private String memberPw;
}
