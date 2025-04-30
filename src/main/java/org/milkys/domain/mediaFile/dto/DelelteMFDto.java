package org.milkys.domain.mediaFile.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DelelteMFDto {

    private String domainType;
    private long parentId;
}
