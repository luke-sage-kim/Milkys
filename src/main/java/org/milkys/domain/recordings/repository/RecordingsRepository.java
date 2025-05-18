package org.milkys.domain.recordings.repository;

import org.milkys.domain.recordings.entity.Recordings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecordingsRepository extends JpaRepository<Recordings,Long> {

    @Query("SELECT r FROM Recordings r WHERE r.parentId = :parentId")
    List<Recordings> findByParentId(@Param("parentId") Long parentId);

}
