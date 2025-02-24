package org.milkys.domain.music.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.milkys.common.MilkysEnum;
import org.milkys.domain.member.entity.Member;
import org.milkys.domain.music.entity.Music;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WriteMusicDto {
    @NotBlank(message = "제목을 입력해주십시오")
    private String title;

    @NotBlank(message = "내용을 입력해주십시오")
    private String content;



    public Music toEntity(Member member) {
        return Music.builder()
                .title(this.title)
                .content(this.content)
                .like(0)
                .status(MilkysEnum.MusicStatus.SHARE)
                .member(member)
                .build();

    }
}
