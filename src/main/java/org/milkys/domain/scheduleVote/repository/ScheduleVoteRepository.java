package org.milkys.domain.scheduleVote.repository;

import org.milkys.domain.scheduleVote.entity.ScheduleVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleVoteRepository extends JpaRepository<ScheduleVote,Long> {
    @Transactional
    @Query(value = "SELECT * FROM scv_table WHERE scv_day = :scvDate", nativeQuery = true)
    List<ScheduleVote> findByVoteDate(@Param("scvDate") String scvDate);

    @Query("delete  FROM ScheduleVote st WHERE st.scvDate = :scvDate")
    void deleteByDate(String scvDate);
    @Query(value = "SELECT * FROM scv_table WHERE mem_code =:memberCode and scv_day = :scvDate", nativeQuery = true)
    Optional<ScheduleVote> findByDayAndMemberCode(@Param("memberCode") long memberCode, @Param("scvDate") String scvDate);
}