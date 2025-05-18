package org.milkys.domain.recordings.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.milkys.domain.gallery.entity.Gallery;
import org.milkys.domain.member.entity.Member;
import org.milkys.domain.recordings.entity.Recordings;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WriteRecordingDto {

    private long memberCode;

    private long parentId;

    private String title;

    private String content;

    @NotBlank(message = "날짜를 입력해주십시오")
    private String recDate;

    private List<MultipartFile> files;

    public Recordings toEntity(Member member) {
        return Recordings.builder()
                .title(this.title)
                .content(this.content)
                .recDate(this.recDate)
                .parentId(this.parentId)
                .member(member)
                .build();

    }
}
