package org.milkys.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.milkys.common.MilkysEnum;
import org.milkys.common.dto.ResponseDto;
import org.milkys.domain.comment.dto.SelectCommentDto;
import org.milkys.domain.comment.dto.UpdateCommentDto;
import org.milkys.domain.comment.dto.WriteCommentDto;
import org.milkys.domain.comment.entity.Comment;
import org.milkys.domain.comment.repository.CommentRepository;
import org.milkys.domain.member.entity.Member;
import org.milkys.domain.member.repository.MemberRepository;
import org.milkys.domain.music.dto.SelectMusicDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    public ResponseDto commentWrite(WriteCommentDto writeCommentDto, long parentId, Long memberCode, String commentParent) {
        if (memberCode == null) {
            return new ResponseDto<>("로그인을 해주세요.", HttpStatus.UNAUTHORIZED);
        }
        Optional<Member> memberOptional = memberRepository.findById(memberCode);
        Member member = memberOptional.get();
        MilkysEnum.CommentParent convertCommentParent = MilkysEnum.CommentParent.valueOf(commentParent);
        Comment comment =writeCommentDto.toEntity(parentId,member,convertCommentParent);
        Comment savedComment = commentRepository.save(comment);
        if(savedComment != null) {
            return new ResponseDto("댓글작성을 완료하였습니다.", HttpStatus.OK.value());
        } else return new ResponseDto("댓글작성을 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public void deleteComment(Long id,String col_type) {
        if(col_type.equals("member")){
        commentRepository.deleteByMemberCode(id);
        } else if (col_type.equals("parent")) {
        commentRepository.deleteByParentCode(id);

        }else{
            commentRepository.deleteById(id);
        }
    }

    public ResponseDto updateComment(Long id,Long memberCode, UpdateCommentDto updateCommentDto) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        if (!optionalComment.isPresent()) {
            // 해당 회원이 존재하지 않는 경우 에러 응답을 반환합니다.
            return new ResponseDto("존재하지 않는 댓글입니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        Comment comment = optionalComment.get();
        if(!comment.getMember().getMemberCode().equals(memberCode)){
            return new ResponseDto("작성자만 수정할 수 있습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        comment.updateComment(updateCommentDto.getContent());
        commentRepository.save(comment);
        return new ResponseDto("댓글이 업데이트되었습니다.", HttpStatus.OK.value());
    }
    public ResponseDto readComment(Long parent_id, String commentParent) {
        try {
            List<Comment> comments = commentRepository.findbyParentAndId(commentParent,parent_id);
            List<SelectCommentDto> selectCommentDtos = comments.stream()
                    .map(SelectCommentDto::fromComment)  // fromMember 메서드를 사용
                    .collect(Collectors.toList());

            if (!selectCommentDtos.isEmpty()) {
                return new ResponseDto(selectCommentDtos, HttpStatus.OK.value());
            } else {
                return new ResponseDto("가져올 데이터가 없습니다.", HttpStatus.NO_CONTENT.value());
            }
        } catch (Exception e) {
            // 예외에 대한 로그 처리
            return new ResponseDto("서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }


}
