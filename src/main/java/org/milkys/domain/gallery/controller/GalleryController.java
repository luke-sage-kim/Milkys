package org.milkys.domain.gallery.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.milkys.common.MilkysEnum;
import org.milkys.common.dto.ResponseDto;
import org.milkys.config.SessionUser;
import org.milkys.domain.board.dto.SelectBoardDto;
import org.milkys.domain.board.dto.UpdateBoardDto;
import org.milkys.domain.board.dto.WriteBoardDto;
import org.milkys.domain.board.service.BoardService;
import org.milkys.domain.comment.dto.WriteCommentDto;
import org.milkys.domain.comment.service.CommentService;
import org.milkys.domain.gallery.dto.SelectGalleryDto;
import org.milkys.domain.gallery.dto.UpdateGalleryDto;
import org.milkys.domain.gallery.dto.WriteGalleryDto;
import org.milkys.domain.gallery.service.GalleryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/gallery")
public class GalleryController {
    @Autowired
    private final GalleryService galleryService;
    @Autowired
    private final CommentService commentService;
    private final HttpSession session;

    /**
     * 추후 사진 첨부 개발필요
     * @param requestDto
     * @param session
     * @return
     */
    @ApiOperation(
            value = "갤러리 작성"
            , notes = "화면에서 입력받은 글정보 작성")
    @PostMapping(value = "/v1/write")
    public ResponseDto galleryWrite(@Valid @RequestBody WriteGalleryDto requestDto, HttpSession session) {

        return galleryService.galleryWrite(requestDto,session);

    }

    @ApiOperation(
            value = "갤러리 전체조회"
            , notes = "갤러리테이블에있는 전체데이터조회")
    @GetMapping(value = "/v1")
    public ResponseDto<List<SelectGalleryDto>> galleryList() {

        return galleryService.selectGalleryList();

    }
    @ApiOperation(
            value = "갤러리 단일조회"
            , notes = "갤러리테이블에있는 전체데이터조회 조회시 조회수 증가")
    @GetMapping(value = "/v1/{id}")
    public ResponseDto<List<SelectGalleryDto>> galleryDetail(@PathVariable Long id) {
        galleryService.updateViewCount(id);
        return galleryService.findById(id);

    }
    @ApiOperation(
            value = "게시글 수정하기"
            , notes = "로그인된 아이디와 글작성자 아이디 비교 후 수정할 정보를 수정")
    @PutMapping(value = "/v1/{id}")
    public ResponseDto  updateGallery(@RequestBody UpdateGalleryDto updateGalleryDto, @PathVariable Long id ) {

        SessionUser loggedInUser = (SessionUser) session.getAttribute("loggedInUser");
        Long memberCode = loggedInUser.getMemberCode();

        return new ResponseDto(galleryService.updateGallery(updateGalleryDto,memberCode,id));
    }
    @ApiOperation(
            value = "갤러리 삭제"
            , notes = "화면에서 입력받은 갤러리아이디로 삭제")
    @DeleteMapping(value = "/v1/{id}")
    public ResponseDto deleteGallery(@PathVariable Long id){
        commentService.deleteComment(id,"parent");
        return new ResponseDto (galleryService.deleteGallery(id));
    }

    @ApiOperation(
            value = "갤러리댓글 작성"
            , notes = "화면에서 입력받은 글정보 작성")
    @PostMapping(value = "/v1/{id}/comment")
    public ResponseDto galleryCommentWrite(@Valid @RequestBody WriteCommentDto writeCommentDto,@PathVariable Long id, HttpSession session) {
        SessionUser loggedInUser = (SessionUser) session.getAttribute("loggedInUser");
        Long memberCode = loggedInUser.getMemberCode();

        return commentService.commentWrite(writeCommentDto,id,memberCode, MilkysEnum.CommentParent.GALLERY);

    }


}
