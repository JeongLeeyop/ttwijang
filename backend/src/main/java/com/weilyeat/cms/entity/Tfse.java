package com.weilyeat.cms.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Tfse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    private String userUid;

    private LocalDateTime tfseDate;

    private String writer;

    private String foodText;

    private String emotionText;

    private Float satietyScore;

    private boolean secretStatus;
    
    @CreationTimestamp
    private LocalDateTime createDate;

    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JoinColumn(name="tfseIdx", insertable = false, updatable = false)
    private List<TfseComment> commentList;
    
    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JoinColumn(name="tfseIdx", insertable = false, updatable = false)
    private List<TfseLike> likeList;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "userUid", insertable = false, updatable = false)
    private User user;

    @Formula("(SELECT COUNT(*) FROM tfse_comment tc WHERE tc.tfse_idx = idx)")
	private int commentCount;

    @Formula("(SELECT COUNT(*) FROM tfse_like tcl WHERE tcl.tfse_idx = idx)")
	private int likeCount;
    
}