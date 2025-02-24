//package org.milkys.common.entity;
//
//import lombok.Getter;
//
//import javax.persistence.*;
//
//@MappedSuperclass
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)  // 단일 테이블 상속 전략
//@DiscriminatorColumn(name = "parent_type", discriminatorType = DiscriminatorType.STRING)  // 부모 타입 구분 컬럼
//public abstract class ParentEntity extends BaseEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String name;  // 자식 엔티티들에서 공유할 수 있는 필드
//
//    // getters and setters
//}
