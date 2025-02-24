package org.milkys.domain.music.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.milkys.common.MilkysEnum;
import org.milkys.common.dto.ResponseDto;
import org.milkys.config.SessionUser;
import org.milkys.domain.comment.dto.WriteCommentDto;
import org.milkys.domain.comment.service.CommentService;
import org.milkys.domain.music.dto.SelectMusicDto;
import org.milkys.domain.music.dto.UpdateMusicDto;
import org.milkys.domain.music.dto.WriteMusicDto;
import org.milkys.domain.music.service.MusicService;
import org.milkys.domain.recommend.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/music")
public class MusicController {
    @Autowired
    private final MusicService musicService;
    @Autowired
    private final CommentService commentService;

    @Autowired
    private final RecommendService recommendService;
    private final HttpSession session;

    /**
     * 추후 사진 첨부 개발필요
     * @param session
     * @return
     */
    @ApiOperation(
            value = "음악 작성"
            , notes = "화면에서 입력받은 글정보 작성")
    @PostMapping(value = "/v1")
    public ResponseDto musicWrite(@Valid @RequestBody WriteMusicDto writeMusicDto, HttpSession session) {

        return musicService.musicWrite(writeMusicDto,session);

    }

    @ApiOperation(
            value = "음악 전체조회"
            , notes = "음악테이블에있는 전체데이터조회")
    @GetMapping(value = "/v1")
    public ResponseDto<List<SelectMusicDto>> musicList() {

        return musicService.selectMusicList();

    }
    @ApiOperation(
            value = "음악 단일조회"
            , notes = "음악테이블에있는 전체데이터조회 조회시 조회수 증가")
    @GetMapping(value = "/v1/{id}")
    public ResponseDto<List<SelectMusicDto>> musicDetail(@PathVariable Long id) {
        int likeCount = recommendService.countRecommend(id,MilkysEnum.CommentParent.MUSIC);
        return musicService.findById(id);

    }
    @ApiOperation(
            value = "게시글 수정하기"
            , notes = "로그인된 아이디와 글작성자 아이디 비교 후 수정할 정보를 수정")
    @PutMapping(value = "/v1/{id}")
    public ResponseDto  updatemusic(@RequestBody UpdateMusicDto updateMusicDto, @PathVariable Long id ) {

        SessionUser loggedInUser = (SessionUser) session.getAttribute("loggedInUser");
        Long memberCode = loggedInUser.getMemberCode();

        return new ResponseDto(musicService.updateMusic(updateMusicDto,memberCode,id));
    }
    @ApiOperation(
            value = "음악 삭제"
            , notes = "화면에서 입력받은 음악아이디로 삭제")
    @DeleteMapping(value = "/v1/{id}")
    public ResponseDto deleteMusic(@PathVariable Long id){
        commentService.deleteComment(id,"parent");
        return new ResponseDto (musicService.deleteMusic(id));
    }

    @ApiOperation(
            value = "음악댓글 작성"
            , notes = "화면에서 입력받은 글정보 작성")
    @PostMapping(value = "/v1/{id}/comment")
    public ResponseDto musicCommentWrite(@Valid @RequestBody WriteCommentDto writeCommentDto,@PathVariable Long id, HttpSession session) {
        SessionUser loggedInUser = (SessionUser) session.getAttribute("loggedInUser");
        Long memberCode = loggedInUser.getMemberCode();

        return commentService.commentWrite(writeCommentDto,id,memberCode, MilkysEnum.CommentParent.MUSIC);

    }

    @ApiOperation(
            value = "음악 추천/추천취소"
            , notes = "추천테이블에 관련글에 추천기록이없으면 추천 추천이있으면 추천취소됨" +
            "추천누르면 음악게시글이 1증가 취소하면 1감소")
    @PostMapping(value = "/v1/{id}/recommend")
    public ResponseDto musicRecommend(@PathVariable Long id, HttpSession session) {
        SessionUser loggedInUser = (SessionUser) session.getAttribute("loggedInUser");
        Long memberCode = loggedInUser.getMemberCode();
        ResponseDto option = recommendService.recommendCreate(id,memberCode,MilkysEnum.CommentParent.MUSIC);
       return musicService.updateMusicRecommend(option,id);


    }


}
