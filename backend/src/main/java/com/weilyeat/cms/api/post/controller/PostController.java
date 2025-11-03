package com.weilyeat.cms.api.post.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.weilyeat.cms.api.attached_file.dto.AttachedFileDto;
import com.weilyeat.cms.api.attached_file.dto.mapper.AttachedFileMapper;
import com.weilyeat.cms.api.attached_file.service.AttachedFileService;
import com.weilyeat.cms.api.post.dto.PostDto;
import com.weilyeat.cms.api.post.dto.mapper.PostMapper;
import com.weilyeat.cms.api.post.dto.search.PostSearch;
import com.weilyeat.cms.api.post.service.PostService;
import com.weilyeat.cms.entity.Post;
import org.springframework.data.domain.Sort;

import lombok.AllArgsConstructor;

@RestController
// @PreAuthorize("hasRole('ROLE_ADMIN')")
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_LECTURE_ADMIN', 'ROLE_RESERVATION_ADMIN', 'ROLE_BOARD')")
@RequestMapping("/api/post")
@AllArgsConstructor
public class PostController {
	
	private final PostService postService;
	
	private final AttachedFileService attachedFileService;
	
	@GetMapping
	public ResponseEntity<Page<PostDto.Detail>> list(
		@PageableDefault(size=10, page=0)
		@SortDefault.SortDefaults({
			@SortDefault(sort = "viewOrder", direction = Sort.Direction.DESC),
			@SortDefault(sort = "createDate", direction = Sort.Direction.ASC)}) Pageable pageable,
			PostSearch postSearch) {
		return ResponseEntity.ok(postService.list(postSearch, pageable));
	}
	
	@GetMapping("{uid}")
	public ResponseEntity<PostDto.Detail> get(@PathVariable("uid") Post post) {
		return ResponseEntity.ok(PostMapper.INSTANCE.entityToDetailDto(post));
	}

	@GetMapping("{uid}/view")
	public ResponseEntity<PostDto.Detail> view(@PathVariable("uid") Post post) {
		return ResponseEntity.ok(postService.view(post));
	}
	
	@PostMapping
	public ResponseEntity<PostDto.Detail> add(
			@AuthenticationPrincipal UserDetails userDetail,
			@Valid @RequestBody PostDto.Add addDto) {
		return ResponseEntity.ok(postService.add(addDto, userDetail));
	}
	
	@PreAuthorize("permitAll()")
	@PostMapping("/upload")
	public ResponseEntity<AttachedFileDto.detail> fileUpload(MultipartFile file) {
		return ResponseEntity.ok(AttachedFileMapper.INSTANCE.entityToDetailDto(attachedFileService.save(file, "post")));
	}
	
	@PutMapping("{uid}")
	public ResponseEntity<PostDto.Detail> update(
			@PathVariable("uid") Post post,
			@Valid @RequestBody PostDto.Update updateDto) {
		return ResponseEntity.ok(postService.update(post, updateDto));
	}
	
	@DeleteMapping("{uid}")
	public ResponseEntity delete(@PathVariable("uid") Post post) {
		postService.delete(post);
		return ResponseEntity.ok().build();
	}
	
}
