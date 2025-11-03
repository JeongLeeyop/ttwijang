package com.ttwijang.cms.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    private Integer orderGroupIdx;

    private String userUid;

    private String content;

    private int score;

    private int viewCount;

    private boolean deleteStatus;

    @CreationTimestamp
    private LocalDateTime createDate;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "review")
    private List<ReviewPhoto> photoes = new ArrayList<ReviewPhoto>();

    @ManyToOne
    @JoinColumn(name = "userUid", insertable = false, updatable = false)
    private User user;
}
