package com.ttwijang.cms.api.board.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.ttwijang.cms.common.EnumField;
import com.ttwijang.cms.entity.BoardAuth;
import com.ttwijang.cms.entity.BoardRole;

import lombok.Data;

public class BoardDto {
	
	@Data
	public static class Simple {
		private String uid;
		private String name;
		private BoardSkinDto.Detail boardSkin;
		private LocalDateTime createDate;
	}
	
	@Data
	public static class Detail {
		private String uid;
		private String name;
		private String skin;
		private int fileCountLimit;
		private int fileSizeLimit;
		private boolean fileUseState;
		private int listSize;
		private BoardAuth authRead;
		private BoardAuth authWrite;
		private BoardAuth authReply;
		private BoardAuth authComment;
		private boolean privateState;
		private boolean replyState;
		private boolean commentState;
		private boolean secretState;
		private boolean noticeState;
		private LocalDateTime createDate;
		private BoardSkinDto.Detail boardSkin;
		private List<BoardFieldDto.Detail> fieldList;
		private List<BoardUseCategoryDto.Detail> categoryList;
		private List<BoardRoleDto.Detail> roleList;
	}
	
	@Data
	public static class Add {
		@NotBlank(message = "게시판 명을 입력하세요.")
		private String name;
		
		@NotBlank(message = "게시판 스킨을 입력하세요.")
		private String skin;
		
		@NotNull(message = "첨부파일 제한 개수를 입력하세요.")
		private int fileCountLimit;
		@NotNull(message = "첨부파일 제한 용량을 입력하세요.")
		private int fileSizeLimit;
		private boolean fileUseState;
		@NotNull(message = "페이지당 게시글 수를 입력하세요.")
		private int listSize;
		
		@NotBlank(message = "목록/읽기 권한을 입력하세요.")
		@EnumField(enumClass = BoardAuth.class, message = "GUEST, MEMBER, GROUP, MANAGER 중 하나로 입력하세요.")
		private String authRead;
		
		@NotBlank(message = "작성/수정 권한을 입력하세요.")
		@EnumField(enumClass = BoardAuth.class, message = "GUEST, MEMBER, GROUP, MANAGER 중 하나로 입력하세요.")
		private String authWrite;
		
		@NotBlank(message = "답글 권한을 입력하세요.")
		@EnumField(enumClass = BoardAuth.class, message = "GUEST, MEMBER, GROUP, MANAGER 중 하나로 입력하세요.")
		private String authReply;
		
		@NotBlank(message = "댓글 권한을 입력하세요.")
		@EnumField(enumClass = BoardAuth.class, message = "GUEST, MEMBER, GROUP, MANAGER 중 하나로 입력하세요.")
		private String authComment;
		
		private boolean privateState;
		private boolean replyState;
		private boolean commentState;
		private boolean secretState;
		private boolean noticeState;
		private List<BoardFieldDto.Add> fieldList = new ArrayList<BoardFieldDto.Add>();
		private List<BoardUseCategoryDto.Save> categoryList = new ArrayList<BoardUseCategoryDto.Save>();
		private List<BoardRoleDto.Save> roleList = new ArrayList<BoardRoleDto.Save>();
	}
	
	@Data
	public static class Update {
		@NotEmpty(message = "게시판 명을 입력하세요.")
		private String name;
		
		@NotEmpty(message = "게시판 스킨을 입력하세요.")
		private String skin;
		
		@NotNull(message = "첨부파일 제한 개수를 입력하세요.")
		private int fileCountLimit;
		@NotNull(message = "첨부파일 제한 용량을 입력하세요.")
		private int fileSizeLimit;
		private boolean fileUseState;
		@NotNull(message = "페이지당 게시글 수를 입력하세요.")
		private int listSize;
		
		@NotEmpty(message = "목록/읽기 권한을 입력하세요.")
		@EnumField(enumClass = BoardAuth.class, message = "GUEST, MEMBER, GROUP, MANAGER 중 하나로 입력하세요.")
		private String authRead;
		
		@NotEmpty(message = "작성/수정 권한을 입력하세요.")
		@EnumField(enumClass = BoardAuth.class, message = "GUEST, MEMBER, GROUP, MANAGER 중 하나로 입력하세요.")
		private String authWrite;
		
		@NotEmpty(message = "답글 권한을 입력하세요.")
		@EnumField(enumClass = BoardAuth.class, message = "GUEST, MEMBER, GROUP, MANAGER 중 하나로 입력하세요.")
		private String authReply;
		
		@NotEmpty(message = "댓글 권한을 입력하세요.")
		@EnumField(enumClass = BoardAuth.class, message = "GUEST, MEMBER, GROUP, MANAGER 중 하나로 입력하세요.")
		private String authComment;
		
		private boolean privateState;
		private boolean replyState;
		private boolean commentState;
		private boolean secretState;
		private boolean noticeState;
		private List<BoardFieldDto.Update> fieldList = new ArrayList<BoardFieldDto.Update>();
		private List<BoardUseCategoryDto.Save> categoryList = new ArrayList<BoardUseCategoryDto.Save>();
		private List<BoardRoleDto.Save> roleList = new ArrayList<BoardRoleDto.Save>();
	}
	
	@Data
	public static class ClientDetail {
		private String uid;
		private String name;
		private String skin;
		private int fileCountLimit;
		private int fileSizeLimit;
		private boolean fileUseState;
		private int listSize;
		private BoardAuth authRead;
		private BoardAuth authWrite;
		private BoardAuth authReply;
		private BoardAuth authComment;
		private boolean privateState;
		private boolean replyState;
		private boolean commentState;
		private boolean secretState;
		private boolean noticeState;
		private List<BoardFieldDto.Detail> fieldList;
		private List<BoardUseCategoryDto.Detail> categoryList;
		private List<String> roleList;
		private List<String> writeRoles;
		
		public void setRoleList(List<BoardRole> boardRoleList) {
			this.roleList = new ArrayList<String>();
			this.writeRoles = new ArrayList<String>();
			boardRoleList.forEach(role -> {
				if(role.getAction().equals("READ")) this.roleList.add(role.getRoleCode());
				if(role.getAction().equals("WRITE")) this.writeRoles.add(role.getRoleCode());
			});
		}
	}
}
