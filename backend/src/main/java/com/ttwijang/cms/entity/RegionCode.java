package com.ttwijang.cms.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 지역 코드 엔티티
 * 시/도, 시/군/구 코드 관리
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "region_code")
public class RegionCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String uid;

    // 지역 코드
    @Column(nullable = false, unique = true, length = 10)
    private String code;

    // 지역명
    @Column(nullable = false, length = 50)
    private String name;

    // 부모 코드 (시/도의 경우 null)
    private String parentCode;

    // 레벨 (1: 시/도, 2: 시/군/구)
    private Integer level;

    // 정렬 순서
    private Integer sortOrder;

    // 사용 여부
    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean enabled;

    @CreationTimestamp
    private LocalDateTime createdDate;
}
