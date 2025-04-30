package org.milkys.domain.mediaFile.repository;

import org.milkys.domain.mediaFile.entity.MediaFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MediaFileRepository extends JpaRepository<MediaFile, Long> {
    @Query("SELECT m FROM MediaFile m WHERE m.domainType = :domainType AND m.parentId = :parentId")
    List<MediaFile> findByDomainTypeAndParentId(@Param("domainType") String domainType, @Param("parentId") Long parentId);

}
