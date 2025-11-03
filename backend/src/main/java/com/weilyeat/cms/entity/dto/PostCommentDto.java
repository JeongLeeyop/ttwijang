package com.weilyeat.cms.entity.dto;

import lombok.Data;


public class PostCommentDto{	
    
    @Data
	public static class Detail {
    private Long id;
    private String content;
    
    private String createdDate;
    private String updatedDate;
    private String useYn;
    }	

    @Data
	public static class Add{
    private String content;
    private Long postId;
    private Long accountId;
    }	

    @Data
	public static class Update{
    private Long id;
    private String content;
    }	
}