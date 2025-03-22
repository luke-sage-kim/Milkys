package org.milkys.domain.music.service;

import lombok.RequiredArgsConstructor;
import org.milkys.common.dto.ResponseDto;
import org.milkys.domain.member.entity.Member;
import org.milkys.domain.member.repository.MemberRepository;
import org.milkys.domain.music.dto.SelectMusicDto;
import org.milkys.domain.music.dto.UpdateMusicDto;
import org.milkys.domain.music.dto.WriteMusicDto;
import org.milkys.domain.music.entity.Music;
import org.milkys.domain.music.repository.MusicRepository;
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
public class MusicService {
    private final MusicRepository musicRepository;
    private final MemberRepository memberRepository;
    private final HttpSession session;

    private String createBoardVaildation(WriteMusicDto writeMusicDto) {
        if(!StringUtils.hasText(writeMusicDto.getTitle())){
            return "제목이 공백입니다.";
        }
        if(!StringUtils.hasText(writeMusicDto.getContent())){
            return "내용이 입력되지않았습니다.";
        }
        return null;
    }
    public ResponseDto musicWrite(WriteMusicDto writeMusicDto, HttpSession session) {
        String error = createBoardVaildation(writeMusicDto);

        if(StringUtils.hasText(error)) return new ResponseDto(error, HttpStatus.INTERNAL_SERVER_ERROR.value());
        String memberId = (String) session.getAttribute("memberId");
        if (memberId == null) {
            return new ResponseDto<>("로그인을 해주세요.", HttpStatus.UNAUTHORIZED);
        }
        Member member = memberRepository.findByMemberId(memberId);
        Music music = writeMusicDto.toEntity(member);
        Music musicSave = musicRepository.save(music);
        if(musicSave != null) {
            return new ResponseDto("음악작성을 완료하였습니다.", HttpStatus.OK.value());
        } else return new ResponseDto("음악작성을 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public ResponseDto<List<SelectMusicDto>> selectMusicList() {
        try {
                List<Music> musics = musicRepository.findAll();
                List<SelectMusicDto> selectmusicDtos = musics.stream()
                        .map(SelectMusicDto::frommusic)  // fromMember 메서드를 사용
                        .collect(Collectors.toList());

            if (!selectmusicDtos.isEmpty()) {
                return new ResponseDto(selectmusicDtos, HttpStatus.OK.value());
            } else {
                return new ResponseDto("가져올 데이터가 없습니다.", HttpStatus.NO_CONTENT.value());
            }
        } catch (Exception e) {
            // 예외에 대한 로그 처리
            return new ResponseDto("서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    public ResponseDto<List<SelectMusicDto>> findById(Long id) {
        Optional<Music> optionalmusic = musicRepository.findById(id);
        if (optionalmusic.isPresent()) {
            Music music = optionalmusic.get();
            SelectMusicDto selectMusicDto = SelectMusicDto.frommusic(music);
            return new ResponseDto(selectMusicDto, HttpStatus.OK.value());
        }else{
            return new ResponseDto("서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    public Object deleteMusic(Long id) {
        Optional<Music> optionalmusic = musicRepository.findById(id);
        if (optionalmusic.isPresent()) {
            Music music = optionalmusic.get();
            musicRepository.delete(music);
            return new ResponseDto("음악 삭제 성공", HttpStatus.OK.value());
        }
        else {
            return new ResponseDto("음악 삭제 실패", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    public ResponseDto updateMusic(UpdateMusicDto updateMusicDto, Long memberCode, Long id) {
        Optional<Music> optionalMusic = musicRepository.findById(id);
        if (!optionalMusic.isPresent()) {
            // 해당 회원이 존재하지 않는 경우 에러 응답을 반환합니다.
            return new ResponseDto("존재하지 않는 게시물입니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        Music music = optionalMusic.get();
        if(!music.getMember().getMemberCode().equals(memberCode)){
            return new ResponseDto("작성자만 수정할 수 있습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        music.updateMusicInfo(updateMusicDto.getTitle(),updateMusicDto.getContent());
        musicRepository.save(music);
        return new ResponseDto("음악가 업데이트되었습니다.", HttpStatus.OK.value());
    }
    public ResponseDto likeMusic(UpdateMusicDto updateMusicDto, Long memberCode, Long id) {
        Optional<Music> optionalMusic = musicRepository.findById(id);
        if (!optionalMusic.isPresent()) {
            // 해당 회원이 존재하지 않는 경우 에러 응답을 반환합니다.
            return new ResponseDto("존재하지 않는 게시물입니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        Music music = optionalMusic.get();
        if(!music.getMember().getMemberCode().equals(memberCode)){
            return new ResponseDto("작성자만 수정할 수 있습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        music.updateMusicInfo(updateMusicDto.getTitle(),updateMusicDto.getContent());
        musicRepository.save(music);
        return new ResponseDto("음악가 업데이트되었습니다.", HttpStatus.OK.value());
    }

    public ResponseDto updateMusicRecommend(ResponseDto option,Long id) {
        if(option.getResultData().equals("추천 완료하였습니다.")){
            musicRepository.plusLikeCnt(id);
            Optional<Music> musicOptional =musicRepository.findById(id);
            if(musicOptional.isPresent()){
                Music music = musicOptional.get();
                if(music.getLike()>4){
                    music.promoteStatus();
                    musicRepository.save(music);
                }
            }
            return new ResponseDto("추천 완료하였습니다.", HttpStatus.OK.value());
        }else{
            musicRepository.minusLikeCnt(id);
            Optional<Music> musicOptional =musicRepository.findById(id);
            if(musicOptional.isPresent()){
                Music music = musicOptional.get();
                if(music.getLike()<5){
                    music.demoteStatus();
                    musicRepository.save(music);
                }
            }
            return new ResponseDto("추천 취소하였습니다.", HttpStatus.OK.value());
        }
    }


    public ResponseDto<List<SelectMusicDto>> getSetList() {
        try {
            List<Music> musics = musicRepository.getSetList();
            List<SelectMusicDto> selectmusicDtos = musics.stream()
                    .map(SelectMusicDto::frommusic)  // fromMember 메서드를 사용
                    .collect(Collectors.toList());

            if (!selectmusicDtos.isEmpty()) {
                return new ResponseDto(selectmusicDtos, HttpStatus.OK.value());
            } else {
                return new ResponseDto("가져올 데이터가 없습니다.", HttpStatus.NO_CONTENT.value());
            }
        } catch (Exception e) {
            // 예외에 대한 로그 처리
            return new ResponseDto("서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}
