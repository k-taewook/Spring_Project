package com.mysite.resume_clinic.audit;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(value = {AuditingEntityListener.class})
@MappedSuperclass
@Getter
@Setter
public class BaseTimeEntity {
    @CreatedDate                        // 최초 생성 시점
    @Column(updatable = false)          // 수정 불가
    private LocalDateTime created;      // 등록일

    @LastModifiedDate                   // 최종 수정 시점
    private LocalDateTime updated;      // 수정일
}
