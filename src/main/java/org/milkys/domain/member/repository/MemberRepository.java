package org.milkys.domain.member.repository;

import org.milkys.common.MilkysEnum;
import org.milkys.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("SELECT m FROM Member m WHERE m.memberId = :memberId")
    Member findByMemberId(@Param("memberId") String memberId);

    @Query(value = "select * from member_table where mem_name = :memberName and mem_phone = :memberPhoneNumber", nativeQuery = true)
    Member findByMemberNameAndPhoneNum(String memberName, String memberPhoneNumber);

    List<Member> findByMemberAuth(MilkysEnum.MemberRoleType memberAuth);

    boolean existsByMemberId(String memberId);
}

/**
 * Member 엔티티에 대한 CRUD 작업과 더불어 Spring Data JPA에서 제공하는 기본적인 데이터베이스 작업을
 * 자동으로 처리해주는 리포지토리 인터페이스를 정의합니다.
 * Spring Data JPA는 이 인터페이스를 구현하여,
 * 개발자가 직접 구현하지 않고도 데이터베이스 작업을 손쉽게 할 수 있도록 도와줍니다.
 *
 *
 * Member: 이 제네릭 파라미터는 엔티티 클래스의 타입을 지정합니다.
 * 즉, 이 리포지토리가 Member 엔티티와 관련된 작업을 수행할 것임을 의미합니다.
 * Long: 이 제네릭 파라미터는 엔티티의 식별자(ID) 타입을 지정합니다.
 * Member 엔티티에서 ID로 표시한게 memberCode니까
 * memberCode의 자료타입을 표시함
 * 즉, Member 엔티티의 ID가 Long 타입임을 의미합니다.
 *
 *
 */