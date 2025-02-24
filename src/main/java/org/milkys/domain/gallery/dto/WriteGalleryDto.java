package org.milkys.domain.gallery.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.milkys.common.MilkysEnum;
import org.milkys.domain.board.entity.Board;
import org.milkys.domain.gallery.entity.Gallery;
import org.milkys.domain.member.entity.Member;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WriteGalleryDto {
    @NotBlank(message = "제목을 입력해주십시오")
    private String title;

    @NotBlank(message = "내용을 입력해주십시오")
    private String content;



    public Gallery toEntity(Member member) {
        return Gallery.builder()
                .title(this.title)
                .content(this.content)
                .viewCnt(0)
                .member(member)
                .build();

    }
}
