package com.ttwijang.cms.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;

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
public class SelfFeedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    private String userUid;

    private LocalDate selfFeedbackDate;

    private String question1;

    private String question2;
    
    private String question3;

    @CreationTimestamp
    private LocalDateTime createDate;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "userUid", insertable = false, updatable = false)
    private User user;
}
