package org.milkys.domain.scheduleVote.repository;

import org.milkys.domain.scheduleVote.entity.ScheduleVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScheduleVoteRepository extends JpaRepository<ScheduleVote,Long> {
    @Query("SELECT st FROM ScheduleVote st WHERE st.scvDate = :scvDate")
    Optional<ScheduleVote> findByVoteDate(String scvDate);
    @Query("delete  FROM ScheduleVote st WHERE st.scvDate = :scvDate")
    void deleteByDate(String scvDate);
}
