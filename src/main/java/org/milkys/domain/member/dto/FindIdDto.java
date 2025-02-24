package org.milkys.domain.member.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class FindIdDto {

    @NotBlank
    private String memberName;
    @NotBlank
    private String memberPhoneNumber;

}
