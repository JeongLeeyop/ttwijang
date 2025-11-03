package com.ttwijang.cms.api.attached_file.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ttwijang.cms.api.attached_file.exception.AttachedFileNotFoundException;
import com.ttwijang.cms.api.attached_file.service.AttachedFileService;
import com.ttwijang.cms.entity.AttachedFile;
import com.ttwijang.cms.util.FileNameUtil;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/attached-file")
@AllArgsConstructor
public class AttachedFileController {
	
	private final AttachedFileService attachedFileService;
	
	@GetMapping("{fileUid}/info")
	public ResponseEntity<AttachedFile> get(@PathVariable("fileUid") AttachedFile attachedFile) {
		if(attachedFile == null) throw new AttachedFileNotFoundException("파일을 찾을 수 없습니다.");
		return ResponseEntity.ok(attachedFile);
	}
	
	@GetMapping("{fileUid}")
	public ResponseEntity<Resource> fileDownload(@PathVariable("fileUid") AttachedFile attachedFile, HttpServletRequest request) {
		if(attachedFile == null) throw new AttachedFileNotFoundException("파일을 찾을 수 없습니다.");
		Resource fileResource = attachedFileService.getResource(attachedFile);
		String filename = FileNameUtil.resolveFilename(request.getHeader("User-Agent"), attachedFile.getOriginalName());
		
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
		headers.add(HttpHeaders.CONTENT_TYPE, attachedFile.getFileType());
		return ResponseEntity.ok().headers(headers).body(fileResource);
	}
}
