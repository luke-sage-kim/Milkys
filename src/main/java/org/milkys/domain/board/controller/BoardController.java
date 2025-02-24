package org.milkys.domain.board.controller;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/board")
public class BoardController {
    @Autowired
    private final BoardService boardService;
    @Autowired
    private final CommentService commentService;
    private final HttpSession session;

    @ApiOperation(
            value = "게시글 작성"
            , notes = "화면에서 입력받은 글정보 작성")
    @PostMapping(value = "/v1/write")
    public ResponseDto boardWrite(@Valid @RequestBody WriteBoardDto requestDto, HttpSession session) {

        return boardService.boardWrite(requestDto,session);

    }

    @ApiOperation(
            value = "게시글 전체조회"
            , notes = "게시판테이블에있는 전체데이터조회")
    @GetMapping(value = "/v1")
    public ResponseDto<List<SelectBoardDto>> boardList() {

        return boardService.selectBoardList();

    }
    @ApiOperation(
            value = "게시글 전체조회"
            , notes = "게시판테이블에있는 전체데이터조회 조회시 조회수 증가")
    @GetMapping(value = "/v1/{id}")
    public ResponseDto<List<SelectBoardDto>> boardDetail(@PathVariable Long id) {
        boardService.updateViewCount(id);
        return boardService.findById(id);

    }
    @ApiOperation(
            value = "게시글 수정하기"
            , notes = "로그인된 아이디와 글작성자 아이디 비교 후 수정할 정보를 수정")
    @PutMapping(value = "/v1/{id}")
    public ResponseDto  updateBoard(@RequestBody UpdateBoardDto updateBoardDto, @PathVariable Long id ) {

        SessionUser loggedInUser = (SessionUser) session.getAttribute("loggedInUser");
        Long memberCode = loggedInUser.getMemberCode();

        return new ResponseDto(boardService.updateBoard(updateBoardDto,memberCode,id));
    }
    @ApiOperation(
            value = "게시글 삭제"
            , notes = "화면에서 입력받은 글아이디로 삭제")
    @DeleteMapping(value = "/v1/{id}")
    public ResponseDto deleteBoard(@PathVariable Long id){
        commentService.deleteComment(id,"parent");
        return new ResponseDto (boardService.deleteBoard(id));
    }

    @ApiOperation(
            value = "게시글 작성"
            , notes = "화면에서 입력받은 글정보 작성")
    @PostMapping(value = "/v1/{id}/comment")
    public ResponseDto boardCommentWrite(@Valid @RequestBody WriteCommentDto writeCommentDto,@PathVariable Long id, HttpSession session) {
        SessionUser loggedInUser = (SessionUser) session.getAttribute("loggedInUser");
        Long memberCode = loggedInUser.getMemberCode();

        return commentService.commentWrite(writeCommentDto,id,memberCode, MilkysEnum.CommentParent.BOARD);

    }


}
