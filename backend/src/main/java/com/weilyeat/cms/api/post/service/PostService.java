package com.weilyeat.cms.api.post.service;

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

import com.weilyeat.cms.api.board.exception.BoardNotFoundException;
import com.weilyeat.cms.api.board.repository.BoardRepository;
import com.weilyeat.cms.api.mission_user_inquiry.new_alarm.dto.NewAlarmDto;
import com.weilyeat.cms.api.mission_user_inquiry.new_alarm.repository.NewAlarmRepository;
import com.weilyeat.cms.api.mission_user_inquiry.new_alarm.service.NewAlarmService;
import com.weilyeat.cms.api.post.dto.PostDataDto;
import com.weilyeat.cms.api.post.dto.PostDto;
import com.weilyeat.cms.api.post.dto.mapper.PostMapper;
import com.weilyeat.cms.api.post.dto.search.PostSearch;
import com.weilyeat.cms.api.post.exception.PostNotFoundException;
import com.weilyeat.cms.api.post.exception.PostNotValidException;
import com.weilyeat.cms.api.post.repository.PostRepository;
import com.weilyeat.cms.api.push_alarm.dto.PushAlarmDto;
import com.weilyeat.cms.api.push_alarm.service.PushAlarmService;
import com.weilyeat.cms.api.user.dto.UserFcmToken;
import com.weilyeat.cms.api.user.exception.UserNotFoundException;
import com.weilyeat.cms.api.user.repository.UserFcmTokenRepository;
import com.weilyeat.cms.api.user.repository.UserRepository;
import com.weilyeat.cms.entity.Board;
import com.weilyeat.cms.entity.BoardField;
import com.weilyeat.cms.entity.Post;
import com.weilyeat.cms.entity.User;
import com.weilyeat.cms.fcm.model.PushNotificationRequest;
import com.weilyeat.cms.fcm.service.PushNotificationService;

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
    private final NewAlarmService newAlarmService;

	@Autowired
    PushNotificationService pushNotificationService;

	@Autowired
    PushAlarmService pushAlarmService;

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
		
		// 공지사항이면 FCM전송 후 기록
		if(post.getBoardUid().equals("8ed8c768-93e0-4502-a906-9c62bd44d26d")){

			List<UserFcmToken> fcmTokenList = userFcmTokenRepository.findAll();
			List<String> fcmTokenStrList = new ArrayList<String>();
			for(UserFcmToken item : fcmTokenList){
				fcmTokenStrList.add(item.getToken());
			}

			String title = "새로운 공지사항이 등록되었습니다";
            String content = post.getTitle();
            String link = "/board/post/8ed8c768-93e0-4502-a906-9c62bd44d26d/"+result.getUid();

			PushNotificationRequest pushRequest = new PushNotificationRequest(title,content,link,null,fcmTokenStrList);
			pushNotificationService.sendPushNotificationToToken(pushRequest);

			 // 푸쉬알람 저장
			 PushAlarmDto.Add pushAlarmDto = new PushAlarmDto.Add();
			 pushAlarmDto.setUserUid(null);
			 pushAlarmDto.setTitle(title);
			 pushAlarmDto.setContent(content);
			 pushAlarmDto.setLink(link);
			 pushAlarmDto.setUserUidList(userRepository.findAll());
			 pushAlarmService.add(pushAlarmDto);

			// 공지사항이면 new alarm 표시
			NewAlarmDto.Add newAlarmDto = new NewAlarmDto.Add();
			newAlarmDto.setPostUid(result.getUid());
			newAlarmDto.setType(1);
			newAlarmService.add(newAlarmDto);
		} 

		Optional<Post> parentPost = postRepository.findById(addDto.getParentUid());
		
		if (parentPost.isPresent()) {
			// 답변글 FCM전송 후 기록	
			UserFcmToken fcmToken = userFcmTokenRepository.findById(parentPost.get().getUserUid()).orElseThrow(() -> new UserNotFoundException("잘못된 접근입니다."));
				String title = "답글이 등록되었습니다";
				String content = post.getTitle();
				String link = "/board/post/d485f6c8-ea3b-439e-9308-80a5eaf3ffe0/"+parentPost.get().getUid();
	
				PushNotificationRequest pushRequest = new PushNotificationRequest(title,content,link,fcmToken.getToken(),null);
				pushNotificationService.sendPushNotificationToToken(pushRequest);
	
				 // 푸쉬알람 저장
				 PushAlarmDto.Add pushAlarmDto = new PushAlarmDto.Add();
				 pushAlarmDto.setUserUid(parentPost.get().getUserUid());
				 pushAlarmDto.setTitle(title);
				 pushAlarmDto.setContent(content);
				 pushAlarmDto.setLink(link);
				 pushAlarmDto.setUserUidList(null);
				 pushAlarmService.add(pushAlarmDto);

		}
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
