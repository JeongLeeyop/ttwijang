package com.weilyeat.cms.api.post.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
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

import com.weilyeat.cms.api.mission_user_inquiry.new_alarm.service.NewAlarmService;
import com.weilyeat.cms.api.post.dto.PostDto;
import com.weilyeat.cms.api.post.dto.search.PostSearch;
import com.weilyeat.cms.api.post.service.ClientPostService;
import com.weilyeat.cms.entity.Post;
import com.weilyeat.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/client/post")
@AllArgsConstructor
public class ClientPostController {
	
	private final ClientPostService clientPostService;
		
	@GetMapping
	public ResponseEntity<Page<PostDto.ClientList>> list(
			@AuthenticationPrincipal SinghaUser authUser,
			PostSearch search,
			@PageableDefault(
					size=10,
					page=0,
					direction = Direction.DESC,
					sort = {"createDate"}) Pageable pageable) {
		return ResponseEntity.ok(clientPostService.list(search, pageable, authUser));
	}
	
	@GetMapping("{uid}")
	public ResponseEntity<PostDto.ClientDetail> get(
			@AuthenticationPrincipal SinghaUser authUser,
			@PathVariable("uid") String uid) {
		return ResponseEntity.ok(clientPostService.detail(uid, authUser));
	}
	
	@GetMapping("{uid}/view")
	public ResponseEntity<PostDto.ClientDetail> addViewCount(
			@AuthenticationPrincipal SinghaUser authUser,
			@PathVariable("uid") String uid) {
		return ResponseEntity.ok(clientPostService.addViewCount(uid, authUser));
	}
	
	@PostMapping
	public ResponseEntity add(
			@AuthenticationPrincipal SinghaUser authUser,
			@Valid @RequestBody PostDto.Add postAddDto) {
		return ResponseEntity.ok(clientPostService.add(postAddDto, authUser));
	}
	
	@PutMapping("{uid}")
	public ResponseEntity update(
			@AuthenticationPrincipal SinghaUser authUser,
			@PathVariable("uid") Post post,
			@Valid @RequestBody PostDto.Update postUpdateDto) {
		return ResponseEntity.ok(clientPostService.update(post, postUpdateDto, authUser));
	}
	
	@DeleteMapping("{uid}")
	public ResponseEntity delete(
			@AuthenticationPrincipal SinghaUser authUser,
			@PathVariable("uid") String uid) {
		clientPostService.delete(uid, authUser);
		return ResponseEntity.ok().build();
	}
	
}
