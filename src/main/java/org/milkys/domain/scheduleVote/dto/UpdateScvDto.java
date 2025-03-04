package org.milkys.domain.scheduleVote.dto;

import lombok.*;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateScvDto {
    private String scvDate;
    private String scvStart;
    private String scvEnd;
}
