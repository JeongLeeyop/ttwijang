package com.weilyeat.cms.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Entity
@Data
public class TfseComment {
    
    // 고유값
    @Id
    @GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String uid;

    // Tfse 일지 고유값
    private Long tfseIdx;

    // 유저 고유값
    private String userUid;
    
    // 부모 댓글 고유값
    private String parentUid;

    // 작성자
    private String writer;

    // 내용
    private String contents;

    // depth
    private int depth;

    // 순서
    private int viewOrder;

    // 패스워드
    private String password;

    private boolean hide;

    // 작성일
    @CreationTimestamp
    private LocalDateTime createDate;

    // 대댓글
    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JoinColumn(name = "parentUid", insertable = false, updatable = false)
    @OrderBy("createDate")
    private List<TfseComment> children;

    @ManyToOne
    @JoinColumn(name ="tfseIdx", insertable = false, updatable = false)
    private Tfse tfse;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "userUid", insertable = false, updatable = false)
    private User user;
}
