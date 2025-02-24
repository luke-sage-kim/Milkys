package org.milkys.domain.comment.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.milkys.common.dto.ResponseDto;
import org.milkys.config.SessionUser;
import org.milkys.domain.comment.dto.UpdateCommentDto;
import org.milkys.domain.comment.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

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
            value = "댓글 삭제"
            , notes = "화면에서 입력받은 글아이디로 삭제")
    @DeleteMapping(value = "/v1/{id}")
    public ResponseDto deleteComment(@PathVariable Long id){
        commentService.deleteComment(id,"comment");
        return new ResponseDto ("삭제완료");
    }
    @ApiOperation(
            value = "댓글 수정"
            , notes = "화면에서 입력받은 글아이디로 삭제")
    @PutMapping(value = "/v1/{id}")
    public ResponseDto updateComment(@PathVariable Long id, @RequestBody UpdateCommentDto updateCommentDto){
;       SessionUser loggedInUser = (SessionUser) session.getAttribute("loggedInUser");
        Long memberCode = loggedInUser.getMemberCode();
        return new ResponseDto(commentService.updateComment(id,memberCode,updateCommentDto));
    }


}
