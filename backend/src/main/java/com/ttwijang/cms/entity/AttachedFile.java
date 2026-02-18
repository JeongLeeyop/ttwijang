package com.ttwijang.cms.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@Setter
@Getter
@ToString
public class AttachedFile{

    @Id
    private String uid;
	
	@Column
    // @Comment("첨부파일명")
    private String originalName;
	@Column
    // @Comment("실제파일명")
    private String fileName;
	@Column
    // @Comment("파일크기")
    private long fileSize;
	@Column
    // @Comment("파일타입")
    private String fileType;
	// 사용 여부
	@Column
	@ColumnDefault("'Y'")
	private char useYn;
	
	@JsonIgnore
    private String filePath;

	// 생성일
	@CreationTimestamp
	@Column(name = "create_date")
	private LocalDateTime createDate;
    
	public AttachedFile(String fileUid, MultipartFile file, String path) {
		this.uid = fileUid;
		this.originalName = file.getOriginalFilename();
		this.fileName = fileUid + "_" + file.getOriginalFilename();
		this.fileSize = file.getSize();
		this.fileType = file.getContentType();
		this.useYn = 'Y';
		this.filePath = path;
		this.createDate = LocalDateTime.now();
	}
}