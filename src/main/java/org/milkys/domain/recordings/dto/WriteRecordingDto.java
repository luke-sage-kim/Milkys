package org.milkys.domain.recordings.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.milkys.domain.gallery.entity.Gallery;
import org.milkys.domain.member.entity.Member;
import org.milkys.domain.recordings.entity.Recordings;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WriteRecordingDto {
    @NotBlank(message = "제목을 입력해주십시오")
    private String title;

    @NotBlank(message = "내용을 입력해주십시오")
    private String content;

    @NotBlank(message = "내용을 입력해주십시오")
    private String recDate;

    public Recordings toEntity(Member member) {
        return Recordings.builder()
                .title(this.title)
                .content(this.content)
                .recDate(this.recDate)
                .member(member)
                .build();

    }
}
