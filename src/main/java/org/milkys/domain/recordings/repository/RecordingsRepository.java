package org.milkys.domain.recordings.repository;

import org.milkys.domain.recordings.entity.Recordings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordingsRepository extends JpaRepository<Recordings,Long> {
}
