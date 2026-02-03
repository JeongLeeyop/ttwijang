package com.ttwijang.cms.api.post.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ttwijang.cms.api.board.exception.BoardNotFoundException;
import com.ttwijang.cms.api.board.repository.BoardRepository;
import com.ttwijang.cms.api.post.dto.PostDataDto;
import com.ttwijang.cms.api.post.dto.PostDto;
import com.ttwijang.cms.api.post.dto.mapper.PostMapper;
import com.ttwijang.cms.api.post.dto.search.PostSearch;
import com.ttwijang.cms.api.post.exception.PostNotFoundException;
import com.ttwijang.cms.api.post.exception.PostNotValidException;
import com.ttwijang.cms.api.post.repository.PostRepository;
import com.ttwijang.cms.api.user.dto.UserFcmToken;
import com.ttwijang.cms.api.user.exception.UserNotFoundException;
import com.ttwijang.cms.api.user.repository.UserFcmTokenRepository;
import com.ttwijang.cms.api.user.repository.UserRepository;
import com.ttwijang.cms.entity.Board;
import com.ttwijang.cms.entity.BoardField;
import com.ttwijang.cms.entity.Post;
import com.ttwijang.cms.entity.User;

import lombok.AllArgsConstructor;

public interface PostService {
	Page<PostDto.Detail> list(PostSearch postSearch, Pageable pageable);

	PostDto.Detail view(Post post);

	PostDto.Detail add(PostDto.Add addDto, UserDetails userDetail);

	PostDto.Detail update(Post post, PostDto.Update updateDto);

	void delete(Post post);
}

@Service
@AllArgsConstructor
class PostServiceImpl implements PostService {
	private final PostRepository postRepository;
	private final BoardRepository boardRepository;
	private final UserRepository userRepository;
    private final UserFcmTokenRepository userFcmTokenRepository;

	@Override
	public Page<PostDto.Detail> list(PostSearch postSearch, Pageable pageable) {
		return postRepository.findAll(postSearch.search(), pageable).map(data -> PostMapper.INSTANCE.entityToDetailDto(data));
	}

	@Transactional
	@Override
	public PostDto.Detail view(Post post) {
		post.setViewCount(post.getViewCount() + 1);
		return PostMapper.INSTANCE.entityToDetailDto(post);
	}

	@Transactional
	@Override
	public PostDto.Detail add(PostDto.Add addDto, UserDetails userDetail) {
		if (userDetail == null || userDetail.getUsername() == null) throw new UserNotFoundException("잘못된 접근입니다.");
		User user = userRepository.findByUserId(userDetail.getUsername()).orElseThrow(() -> new UserNotFoundException("잘못된 접근입니다."));
		Post post = PostMapper.INSTANCE.addDtoToEntity(addDto);
		Board board = boardRepository.findById(post.getBoardUid()).orElseThrow(() -> new BoardNotFoundException());
		validate(board.getFieldList(), addDto.getDataList());

		int  viewOrder = 0;
		Optional<Post> optional = postRepository.findTopByOrderByViewOrderDesc();
		if (optional.isPresent()) viewOrder = optional.get().getViewOrder() + 1;
		post.setUserUid(user.getUid());
		post.setWriter(user.getActualName());
		post.setViewOrder(viewOrder);
		post.setDepth(1);

		if (StringUtils.hasText(addDto.getParentUid())) {
			postRepository.findById(addDto.getParentUid()).ifPresent(parent -> {
				post.setViewOrder(parent.getViewOrder());
				post.setDepth(parent.getDepth() + 1);
			});
		} else {
			post.setParentUid(null);
		}

		post.getTags().forEach(t -> t.setPost(post));
		
		PostDto.Detail result = PostMapper.INSTANCE.entityToDetailDto(postRepository.save(post));
		
		Optional<Post> parentPost = postRepository.findById(addDto.getParentUid());
		return result;
	}

	@Transactional
	@Override
	public PostDto.Detail update(Post post, PostDto.Update updateDto) {
		post.getTags().forEach(t -> t.setPost(null));
		
		PostMapper.INSTANCE.updateDtoToEntity(updateDto, post);
		Board board = boardRepository.findById(post.getBoardUid()).orElseThrow();
		validate(board.getFieldList(), updateDto.getDataList());

		post.getTags().forEach(t -> t.setPost(post));

		return PostMapper.INSTANCE.entityToDetailDto(postRepository.save(post));
	}

	@Transactional
	@Override
	public void delete(Post post) {
		// postRepository.delete(post);
		post.setDeleteStatus(true);
		postRepository.save(post);
	}

	public void validate(List<BoardField> boardFieldList, List<PostDataDto.Save> postDataList) {
		int index = 0;
		for (BoardField boardField : boardFieldList) {
			PostDataDto.Save postData = postDataList.get(index);
			if (boardField.isRequiredState() && !StringUtils.hasText(postData.getInputValue()))
				throw new PostNotValidException(boardField.getFieldName() + "은(는) 필수 입력 항목입니다.");
			if (boardField.getFieldType().getTypeCode().equals("FILE") || boardField.getFieldType().getTypeCode().equals("PHOTO")) {
				if (boardField.getInputLimit() < postData.getInputValue().split(",").length) {
					throw new PostNotValidException(boardField.getFieldName() + "은(는) 최대 " + boardField.getInputLimit() + "개 까지 첨부할 수 있습니다.");
				}
			} else {
				if (boardField.getInputLimit() < postData.getInputValue().length()) {
					throw new PostNotValidException(boardField.getFieldName() + "은(는) 최대 " + boardField.getInputLimit() + "자 입니다.");
				}
			}
			index++;
		}
	}
}
