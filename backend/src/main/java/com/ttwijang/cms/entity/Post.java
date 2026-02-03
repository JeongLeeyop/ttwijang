package com.ttwijang.cms.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "post")
@Getter @Setter
public class Post implements Serializable {
	
	// 게시글 고유값
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String uid;
	
	// 게시판 고유값
	private String boardUid;
	
	// 유저 고유값
	private String userUid;
	
	// 부모글 고유값
	private String parentUid;

	// 작성자
	private String writer;

	//제목
	private String title;

	//내용
	private String content;
	
	// 게시글 비밀번호
	private String password;
	
	// depth
	private int depth;
	
	// 조회수
	private int viewCount;

	// 순서 -> 답글 처리 때문에
	private int viewOrder;

	// 공지 여부
	private boolean noticeStatus;

	private boolean deleteStatus;

	private boolean hiddenStatus;
	
	@CreationTimestamp
    private LocalDateTime createDate;

	// 게시글 카테고리 목록
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "postUid", nullable = false)
    private List<PostCategory> categoryList;
	
	// 게시글 파일 목록
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "postUid", nullable = false)
	@OrderColumn(name = "viewOrder")
    private List<PostFile> fileList;
	
	// 게시글 데이터 목록
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "postUid", nullable = false)
	@OrderColumn(name = "viewOrder")
    private List<PostData> dataList = new ArrayList<PostData>();
	
	// 답글 목록
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JoinColumn(name = "parentUid", insertable = false, updatable = false)
	private List<Post> children;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parentUid", insertable = false, updatable = false)
	private Post parent;

	@ManyToOne
	@JoinColumn(name = "boardUid", insertable = false, updatable = false)
	private Board board;

	// 댓글 목록
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JoinColumn(name = "postUid", insertable = false, updatable = false)
	private List<Comment> commentList;

	@Formula("(SELECT p.user_uid FROM post p WHERE p.uid = parent_uid)")
	private String ownerUid;

	@Formula("(SELECT COUNT(*) FROM post_like pl WHERE pl.post_uid = uid)")
	private int likeCount;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
	private List<PostTag> tags = new ArrayList<PostTag>();
	
	public void setPassword(String password) {
        if (StringUtils.hasText(password)) {
			PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			this.password = passwordEncoder.encode(password);
		}
    }
	
}
