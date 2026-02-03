package com.ttwijang.cms.entity.dto;

import lombok.Data;

public class AttachedFileDto{	
    
    @Data
	public static class Detail {
    private String uid;
    private String originalName;
    // private String fileName;
    private Long fileSize;
    // private String filePath;
    private String fileType;
    // private char useYn;
	// private LocalDateTime createDate;
    }
   
}		