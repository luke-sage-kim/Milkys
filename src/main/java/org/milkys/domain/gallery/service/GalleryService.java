package org.milkys.domain.gallery.service;

import lombok.RequiredArgsConstructor;
import org.milkys.common.dto.ResponseDto;
import org.milkys.domain.board.dto.SelectBoardDto;
import org.milkys.domain.board.dto.UpdateBoardDto;
import org.milkys.domain.board.dto.WriteBoardDto;
import org.milkys.domain.board.entity.Board;
import org.milkys.domain.board.repository.BoardRepository;
import org.milkys.domain.gallery.dto.SelectGalleryDto;
import org.milkys.domain.gallery.dto.UpdateGalleryDto;
import org.milkys.domain.gallery.dto.WriteGalleryDto;
import org.milkys.domain.gallery.entity.Gallery;
import org.milkys.domain.gallery.repository.GalleryRepository;
import org.milkys.domain.member.entity.Member;
import org.milkys.domain.member.repository.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GalleryService {
    private final GalleryRepository galleryRepository;
    private final MemberRepository memberRepository;
    private final HttpSession session;

    private String createBoardVaildation(WriteGalleryDto writeGalleryDto) {
        if(!StringUtils.hasText(writeGalleryDto.getTitle())){
            return "제목이 공백입니다.";
        }
        if(!StringUtils.hasText(writeGalleryDto.getContent())){
            return "내용이 입력되지않았습니다.";
        }
        return null;
    }
    public ResponseDto galleryWrite(WriteGalleryDto writeGalleryDto, HttpSession session) {
        String error = createBoardVaildation(writeGalleryDto);

        if(StringUtils.hasText(error)) return new ResponseDto(error, HttpStatus.INTERNAL_SERVER_ERROR.value());
        String memberId = (String) session.getAttribute("memberId");
        if (memberId == null) {
            return new ResponseDto<>("로그인을 해주세요.", HttpStatus.UNAUTHORIZED);
        }
        Member member = memberRepository.findByMemberId(memberId);
        Gallery gallery = writeGalleryDto.toEntity(member);
        Gallery gallerysave = galleryRepository.save(gallery);
        if(gallerysave != null) {
            return new ResponseDto("갤러리작성을 완료하였습니다.", HttpStatus.OK.value());
        } else return new ResponseDto("갤러리작성을 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public ResponseDto<List<SelectGalleryDto>> selectGalleryList() {
        try {
                List<Gallery> galleries = galleryRepository.findAll();
                List<SelectGalleryDto> selectGalleryDtos = galleries.stream()
                        .map(SelectGalleryDto::fromGallery)  // fromMember 메서드를 사용
                        .collect(Collectors.toList());

            if (!selectGalleryDtos.isEmpty()) {
                return new ResponseDto(selectGalleryDtos, HttpStatus.OK.value());
            } else {
                return new ResponseDto("가져올 데이터가 없습니다.", HttpStatus.NO_CONTENT.value());
            }
        } catch (Exception e) {
            // 예외에 대한 로그 처리
            return new ResponseDto("서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
    @Transactional
    public void updateViewCount(Long id) {
        galleryRepository.updateViewCount(id);
    }

    public ResponseDto<List<SelectGalleryDto>> findById(Long id) {
        Optional<Gallery> optionalGallery = galleryRepository.findById(id);
        if (optionalGallery.isPresent()) {
            Gallery gallery = optionalGallery.get();
            SelectGalleryDto selectGalleryDto = SelectGalleryDto.fromGallery(gallery);
            return new ResponseDto(selectGalleryDto, HttpStatus.OK.value());
        }else{
            return new ResponseDto("서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    public Object deleteGallery(Long id) {
        Optional<Gallery> optionalGallery = galleryRepository.findById(id);
        if (optionalGallery.isPresent()) {
            Gallery gallery = optionalGallery.get();
            galleryRepository.delete(gallery);
            return new ResponseDto("갤러리 삭제 성공", HttpStatus.OK.value());
        }
        else {
            return new ResponseDto("갤러리 삭제 실패", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    public ResponseDto updateGallery(UpdateGalleryDto updateGalleryDto, Long memberCode, Long id) {
        Optional<Gallery> optionalGallery = galleryRepository.findById(id);
        if (!optionalGallery.isPresent()) {
            // 해당 회원이 존재하지 않는 경우 에러 응답을 반환합니다.
            return new ResponseDto("존재하지 않는 게시물입니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        Gallery gallery = optionalGallery.get();
        if(!gallery.getMember().getMemberCode().equals(memberCode)){
            return new ResponseDto("작성자만 수정할 수 있습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        gallery.updateGalleryInfo(updateGalleryDto.getTitle(),updateGalleryDto.getContent());
        galleryRepository.save(gallery);
        return new ResponseDto("갤러리가 업데이트되었습니다.", HttpStatus.OK.value());
    }

}
