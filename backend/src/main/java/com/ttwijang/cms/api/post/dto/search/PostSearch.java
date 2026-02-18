package com.ttwijang.cms.api.post.dto.search;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.ttwijang.cms.entity.QPost;
import com.ttwijang.cms.entity.QPostData;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PostSearch {
	private String boardUid;
	private String teamUid;
	private String searchType;
	private String searchValue;
	private List<String> categoryList = new ArrayList<String>();
	
	private boolean secretBoard;
	private String userUid;

	private boolean deleteStatus;
	private boolean adminStatus;

	private String sort;

	private String postUid;
	
	public Predicate search() {
		
		QPost post = QPost.post;
		QPostData postData = QPostData.postData;
		
		BooleanBuilder builder = new BooleanBuilder();
		
		// builder.and(post.parentUid.eq("").or(post.parentUid.isNull()));
		if(StringUtils.hasText(boardUid)) builder.and(post.boardUid.eq(boardUid));

		builder.and(post.deleteStatus.eq(deleteStatus));
		
		if (secretBoard) {
			if (StringUtils.hasText(userUid)) {
				Predicate myPost = post.userUid.eq(userUid);
				Predicate myPost2 = post.ownerUid.eq(userUid);
				builder.andAnyOf(myPost, myPost2);
			} else {
				builder.and(post.userUid.eq("anonymous-user"));
			}
		}
		
		if(!StringUtils.hasText(searchType)) {
			if(StringUtils.hasText(searchValue)) builder.and(post.dataList.any().inputValue.contains(searchValue));
		} else {
			if(StringUtils.hasText(searchValue)) {
				switch (searchType) {
				case "writer":
					builder.and(post.writer.contains(searchValue));
					break;
				case "title":
					builder.and(post.title.contains(searchValue));
					break;
				case "content":
					builder.and(post.content.contains(searchValue));
					break;
				case "titleOrContent":
					builder.and(post.title.contains(searchValue).or(post.content.contains(searchValue)));
					break;
				default:
					BooleanExpression subquery = JPAExpressions.selectOne()
		            .from(postData)
		            .where(postData.post.eq(post)
		                    .and(postData.inputValue.contains(searchValue)
                    		.and(postData.field.uid.eq(searchType))))
		            .exists();
					builder.and(subquery);
					break;
				}
			}
		}
		
		if(categoryList != null && categoryList.size() > 0) {
			int allIndex = categoryList.indexOf("all");
			if(allIndex < 0) builder.and(post.categoryList.any().categoryUid.in(categoryList));
		}
		
		return builder;
	}

	public Predicate clientSearch() {
		QPost post = QPost.post;
		QPostData postData = QPostData.postData;
		BooleanBuilder builder = new BooleanBuilder();
		
		if(StringUtils.hasText(boardUid)) builder.and(post.boardUid.eq(boardUid));
		if(StringUtils.hasText(teamUid)) builder.and(post.teamUid.eq(teamUid));
		builder.and(post.deleteStatus.eq(false));
		
		if (secretBoard) {
			if (StringUtils.hasText(userUid)) {
				Predicate myPost = post.userUid.eq(userUid);
				Predicate myPost2 = post.ownerUid.eq(userUid);
				builder.andAnyOf(myPost, myPost2);
			} else {
				builder.and(post.userUid.eq("anonymous-user"));
			}
		}
		
		if(!StringUtils.hasText(searchType)) {
			if(StringUtils.hasText(searchValue)) builder.and(post.dataList.any().inputValue.contains(searchValue));
		} else {
			if(StringUtils.hasText(searchValue)) {
				switch (searchType) {
				case "writer":
					builder.and(post.writer.contains(searchValue));
					break;
				case "title":
					builder.and(post.title.contains(searchValue));
					break;
				case "content":
					builder.and(post.content.contains(searchValue));
					break;
				case "titleOrContent":
					builder.and(post.title.contains(searchValue).or(post.content.contains(searchValue)));
					break;
				default:
					BooleanExpression subquery = JPAExpressions.selectOne()
		            .from(postData)
		            .where(postData.post.eq(post)
		                    .and(postData.inputValue.contains(searchValue)
                    		.and(postData.field.uid.eq(searchType))))
		            .exists();
					builder.and(subquery);
					break;
				}
			}
		}
		
		if(categoryList != null && categoryList.size() > 0) {
			int allIndex = categoryList.indexOf("all");
			if(allIndex < 0) builder.and(post.categoryList.any().categoryUid.in(categoryList));
		}

		if (!adminStatus) {
			if (StringUtils.hasText(userUid)) builder.and((post.hiddenStatus.eq(true).and(post.userUid.eq(userUid)).or(post.hiddenStatus.eq(false))));
			builder.and((post.hiddenStatus.eq(true).and(post.userUid.isNull())).or(post.hiddenStatus.eq(false)));
		}

		builder.and(post.parentUid.isNull());

		if (StringUtils.hasText(postUid)) {
			builder.and(post.uid.eq(postUid));
		}
				
		return builder;
	}
}
