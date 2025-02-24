package org.milkys.domain.recommend.repository;

import org.milkys.common.MilkysEnum;
import org.milkys.domain.recommend.entity.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecommendRepository extends JpaRepository<Recommend,Long> {
    @Query(value = "SELECT COUNT(r) FROM recommend_table r WHERE r.parent_id = :parentId AND r.parent_type = :parentType", nativeQuery = true)
    int countByParentIdAndParentType( Long parentId,  @Param("parentType")String parentType);
    @Query(value = "SELECT * FROM recommend_table r WHERE r.parent_id = :parentId AND r.parent_type = :parentType AND r.mem_code = :memberCode", nativeQuery = true)
    Optional<Recommend> findByMemberId(Long parentId,  @Param("parentType") String recommendParent,Long memberCode);

}
