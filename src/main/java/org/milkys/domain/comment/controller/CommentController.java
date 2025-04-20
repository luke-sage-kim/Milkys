package org.milkys.domain.comment.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.milkys.common.MilkysEnum;
import org.milkys.common.dto.ResponseDto;
import org.milkys.config.SessionUser;
import org.milkys.domain.comment.dto.CommentRequestDto;
import org.milkys.domain.comment.dto.SelectCommentDto;
import org.milkys.domain.comment.dto.UpdateCommentDto;
import org.milkys.domain.comment.dto.WriteCommentDto;
import org.milkys.domain.comment.service.CommentService;
import org.milkys.domain.music.dto.SelectMusicDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    CommentService commentService;
    @Autowired
    private final HttpSession session;


    @ApiOperation(
            value = "댓글 작성"
            , notes = "화면에서 입력받은 글정보 작성")
    @PostMapping(value = "/v1/{id}/comment")
    public ResponseDto musicCommentWrite(@Valid @RequestBody WriteCommentDto writeCommentDto, @PathVariable Long id) {
        Long memberCode =writeCommentDto.getMemberCode();
        return commentService.commentWrite(writeCommentDto,id,memberCode, writeCommentDto.getParentType());

    }

    @ApiOperation(
            value = "댓글 삭제"
            , notes = "화면에서 입력받은 글아이디로 삭제")
    @DeleteMapping(value = "/v1/{id}")
    public ResponseDto deleteComment(@PathVariable Long id){
        commentService.deleteComment(id,"comment");
        return new ResponseDto ("삭제완료");
    }
    @ApiOperation(
            value = "댓글 수정"
            , notes = "화면에서 입력받은 글아이디로 수정")
    @PutMapping(value = "/v1/{id}")
    public ResponseDto updateComment(@PathVariable Long id, @RequestBody UpdateCommentDto updateCommentDto){
        Long memberCode = updateCommentDto.getMemberCode(
        );
        return new ResponseDto(commentService.updateComment(id,memberCode,updateCommentDto));
    }
    @ApiOperation(
            value = "댓글 조회"
            , notes = "게시글에있는 댓글데이터조회")
    @PostMapping(value = "/v1")
    public ResponseDto<List<SelectCommentDto>> commentList( @RequestBody CommentRequestDto dto)  {

        return commentService.readComment(dto.getParent_id(), dto.getParentType());

    }

}
