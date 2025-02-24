package org.milkys.domain.member.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class InitialPwDto {
    @NotBlank
    private String memberId;
    @NotBlank
    private String memberBirthday;
}
