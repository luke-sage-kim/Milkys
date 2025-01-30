package org.milkys.common.entity;

import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreationTimestamp // 생성되었을때 시간을 만들어주는거
    @Column(updatable = false)//업데이트 안되게 해주는거
    private LocalDateTime createdTime;

    @UpdateTimestamp  // 업데이트되었을때 시간정보주는거
    @Column(insertable = false)//인서트할때는 관여를 안하는거
    private LocalDateTime updatedTime;

    private LocalDateTime deletedAt = null;
    public void delete(){ this.deletedAt = LocalDateTime.now();}
    public void restore(){ this.deletedAt = null;}

}