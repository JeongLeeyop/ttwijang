package com.weilyeat.cms.entity.dto;

import lombok.Data;

@Data
public class TagDto{		

    private Long id;
    private String name;
    private String count;
    
    //FK	id	boardId	board		N		
}