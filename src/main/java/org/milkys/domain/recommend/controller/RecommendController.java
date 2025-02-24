package org.milkys.domain.recommend.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.milkys.common.dto.ResponseDto;
import org.milkys.config.SessionUser;
import org.milkys.domain.comment.dto.UpdateCommentDto;
import org.milkys.domain.comment.service.CommentService;
import org.milkys.domain.recommend.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/recommend")
public class RecommendController {
    @Autowired
    RecommendService recommendService;
    @Autowired
    private final HttpSession session;


    @ApiOperation(
            value = "추천 삭제"
            , notes = "화면에서 입력받은 글아이디로 삭제")
    @DeleteMapping(value = "/v1/{id}")
    public ResponseDto deleteComment(@PathVariable Long id){
        recommendService.deleteRecommend(id);
        return new ResponseDto (" 추천취소");
    }


}
