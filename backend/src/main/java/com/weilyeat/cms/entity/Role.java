package com.ttwijang.cms.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.Setter;

// 권한 
@Entity
@Getter
@Setter
public class Role implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    // 권한 코드
    @Id
    private String roleCode;
    
    private String siteUid;

    // 권한 이름
    private String roleName;

    private String description;

    private boolean joinAccessState;

    // 생성일
    @CreationTimestamp
    @Column(name = "create_date")
    private LocalDateTime createDate;

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "roleCode", insertable = false, updatable = false)
    private List<UserRole> userRoles;

}