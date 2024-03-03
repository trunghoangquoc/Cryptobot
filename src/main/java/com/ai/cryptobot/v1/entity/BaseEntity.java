package com.ai.cryptobot.v1.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;


@Getter
@Setter
@EnableAutoConfiguration
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
public abstract class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID", length = 100)
    private String id;

    @Column(name = "CREATED_BY", updatable = false)
    @CreatedBy
    private String createdBy;

    @Column(name = "CREATED_AT", updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "LAST_MODIFIED_AT")
    @LastModifiedDate
    private LocalDateTime lastModifiedAt;

    @Column(name = "LAST_MODIFIED_BY")
    @LastModifiedBy
    private String lastModifiedBy;

    @Column(name = "IS_DELETED")
    private boolean deleted = false;

}
