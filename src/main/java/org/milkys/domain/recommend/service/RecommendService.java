package org.milkys.domain.recommend.service;

import lombok.RequiredArgsConstructor;
import org.milkys.common.MilkysEnum;
import org.milkys.common.dto.ResponseDto;
import org.milkys.domain.member.entity.Member;
import org.milkys.domain.member.repository.MemberRepository;
import org.milkys.domain.recommend.dto.SelectRecommendDto;
import org.milkys.domain.recommend.entity.Recommend;
import org.milkys.domain.recommend.repository.RecommendRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecommendService {
    private final MemberRepository memberRepository;
    private final RecommendRepository recommendRepository;

    public ResponseDto recommendCreate(long parentId, Long memberCode, MilkysEnum.CommentParent recommendParent) {
        if (memberCode == null) {
            return new ResponseDto<>("로그인을 해주세요.", HttpStatus.UNAUTHORIZED);
        }
        String convertParentType = String.valueOf(recommendParent);
        Optional<Recommend> recommendOptional = recommendRepository.findByMemberId(parentId, convertParentType,memberCode );
        if(recommendOptional.isPresent()){
            Recommend recommend =recommendOptional.get();
            Long recommendId = recommend.getId();
            deleteRecommend(recommendId);
            return new ResponseDto<>("추천을 취소하였습니다.", HttpStatus.OK.value());
        }else {
            Optional<Member> memberOptional = memberRepository.findById(memberCode);
            Member member = memberOptional.get();
            Recommend recommend = new Recommend(parentId,member,recommendParent);
            Recommend savedRecommend = recommendRepository.save(recommend);
            if(savedRecommend != null) {
                return new ResponseDto("추천 완료하였습니다.", HttpStatus.OK.value());
            } else return new ResponseDto("추천을 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

    }

    public void deleteRecommend(Long id) {
            recommendRepository.deleteById(id);
    }

    public int countRecommend(Long parentId,MilkysEnum.CommentParent recommendParent){
        String convertParentType = String.valueOf(recommendParent);
        return recommendRepository.countByParentIdAndParentType(parentId,convertParentType);
    }

    public boolean isRecommend(SelectRecommendDto selectRecommendDto){
        String convertParentType = String.valueOf(selectRecommendDto.getRecommendParent());
        Recommend recommend =recommendRepository.isRecommend(selectRecommendDto.getParentId(),convertParentType, selectRecommendDto.getMemberCode());
        if(recommend ==null){
            return false;
        }
        return true;
    }
}
