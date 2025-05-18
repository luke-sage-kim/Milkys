package org.milkys.domain.board.repository;

import org.milkys.common.MilkysEnum;
import org.milkys.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board,Long> {
    @Modifying
    @Query(value = "update Board b set b.viewCnt= b.viewCnt+1 where b.id=:id")
    void updateViewCount(Long id);

    List<Board> findTop3ByBoardTypeOrderByCreatedTimeDesc(MilkysEnum.BoardType boardType);

}
