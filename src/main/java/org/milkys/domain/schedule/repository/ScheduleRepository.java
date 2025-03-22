package org.milkys.domain.schedule.repository;

import org.milkys.domain.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule,Long> {
//    @Query("SELECT s FROM Schedule s WHERE s.scDate >= :currentDate ORDER BY s.scDate ASC")
//    Optional<Schedule> findRecentSchedule(@Param("currentDate") String currentDate);
}
