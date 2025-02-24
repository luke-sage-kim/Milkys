package org.milkys.domain.gallery.repository;

import org.milkys.domain.gallery.entity.Gallery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GalleryRepository extends JpaRepository<Gallery,Long> {

    @Modifying
    @Query(value = "update Gallery g set g.viewCnt= g.viewCnt+1 where g.id=:id")
    void updateViewCount(Long id);
}
