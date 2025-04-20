package org.milkys.domain.scheduleVote.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class DeleteScvDto {
    private String scvDate;
    private long memberCode;
}
