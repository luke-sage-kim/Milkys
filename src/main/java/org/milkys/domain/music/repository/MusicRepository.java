package org.milkys.domain.music.repository;

import org.milkys.domain.music.entity.Music;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MusicRepository extends JpaRepository<Music,Long> {
    @Transactional
    @Modifying
    @Query(value = "update Music m set m.like= m.like+1 where m.id=:id")
    void plusLikeCnt(Long id);

    @Transactional
    @Modifying
    @Query(value = "update Music m set m.like= m.like-1 where m.id=:id")
    void minusLikeCnt(Long id);

    @Query(value = "SELECT * FROM music_table WHERE mu_status = 0", nativeQuery = true)
    List<Music> getSetList();

}
