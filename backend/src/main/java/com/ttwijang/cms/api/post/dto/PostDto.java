package com.ttwijang.cms.api.post.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import com.ttwijang.cms.entity.Board;
import com.ttwijang.cms.entity.PostTag;

import lombok.Data;

public class PostDto {

	@Data
	public static class Detail {
		private String uid;
		private String boardUid;
		private String parentUid;
		private String title;
		private String content;
		private String writer;
		private int viewCount;
		private boolean hiddenStatus;
		private LocalDateTime createDate;
		private List<PostCategoryDto.Detail> categoryList;
		private List<PostFileDto.Detail> fileList;
		private List<PostDataDto.Detail> dataList;
		private List<PostTagDto.list> tags = new ArrayList<PostTagDto.list>();
	}

	@Data
	public static class Add {
		private String boardUid;
		private String teamUid;
		private String parentUid;
		@NotBlank(message = "제목을 입력하세요.")
		private String title;
		private String content;
		private String writer;
		private String password;
		private boolean noticeStatus;
		private boolean hiddenStatus;
		private List<PostCategoryDto.Save> categoryList = new ArrayList<PostCategoryDto.Save>();
		private List<PostFileDto.Save> fileList = new ArrayList<PostFileDto.Save>();
		private List<PostDataDto.Save> dataList = new ArrayList<PostDataDto.Save>();

		private List<PostTagDto.save> tags = new ArrayList<PostTagDto.save>();
	}

	@Data
	public static class Update {
		@NotBlank(message = "제목을 입력하세요.")
		private String title;
		private String content;
		private String writer;
		private String password;
		private boolean noticeStatus;
		private boolean hiddenStatus;
		private List<PostCategoryDto.Save> categoryList = new ArrayList<PostCategoryDto.Save>();
		private List<PostFileDto.Save> fileList = new ArrayList<PostFileDto.Save>();
		private List<PostDataDto.Save> dataList = new ArrayList<PostDataDto.Save>();

		private List<PostTagDto.save> tags = new ArrayList<PostTagDto.save>();
	}
	
	@Data
	public static class ClientList {
		private String uid;
		private String boardUid;
		private String title;
		private String content;
		private String writer;
		private int viewCount;
		private boolean hiddenStatus;
		private int likeCount;
		private boolean likeStatus;
		private List<String> categoryList = new ArrayList<String>();
		private List<String> fileList = new ArrayList<String>();
		private boolean replyStatus;
		private List<String> tags = new ArrayList<String>();
		private String createDate;
		private boolean noticeStatus;
		private int commentCount;
		private String userUid;
		private boolean hasAuthority;
	}
	
	@Data
	public static class ClientDetail {
		private String uid;
		private String title;
		private String content;
		private String writer;
		private int depth;
		private int viewCount;
		private boolean hiddenStatus;
		private boolean deleteStatus;
		private int likeCount;
		private boolean likeStatus;
		private List<PostCategoryDto.Detail> categoryList;
		private List<PostFileDto.ClientDetail> fileList;
		private List<PostDataDto.Detail> dataList;
		private LocalDateTime createDate;
		private boolean hasAuthority;
		private List<PostTagDto.list> tags = new ArrayList<PostTagDto.list>();
		private List<PostDto.ClientDetail> children;
	}
}
