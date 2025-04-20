package org.milkys.domain.comment.repository;

import org.milkys.common.MilkysEnum;
import org.milkys.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    @Modifying
    @Transactional
    @Query(value = "delete from comment_table where mem_code = :memberCode", nativeQuery = true)
    void deleteByMemberCode(Long memberCode);

    @Modifying
    @Transactional
    @Query(value = "delete from comment_table where parent_id = :parentId", nativeQuery = true)
    void deleteByParentCode(Long parentId);

    @Transactional
    @Query(value = "select * from comment_table where parent_type = :commentParent and  parent_id = :parentId", nativeQuery = true)
    List<Comment> findbyParentAndId( String commentParent,Long parentId);
}
