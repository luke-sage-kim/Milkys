package org.milkys.domain.member.dto;

import lombok.*;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class AuthDto {
    private long memberCode;
    private String memberAuth;//내권한
    private String targetAuth;//바꿀
}
