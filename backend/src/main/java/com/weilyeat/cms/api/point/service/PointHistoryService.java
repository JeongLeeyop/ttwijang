package com.weilyeat.cms.api.point.service;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.weilyeat.cms.api.point.dto.PointHistoryDto;
import com.weilyeat.cms.api.point.dto.mapper.PointHistoryMapper;
import com.weilyeat.cms.api.point.dto.search.PointHistorySearch;
import com.weilyeat.cms.api.point.repository.PointHistoryRepository;
import com.weilyeat.cms.api.user.repository.UserRepository;
import com.weilyeat.cms.common.exception.NotFoundException;
import com.weilyeat.cms.common.exception.code.NotFound;
import com.weilyeat.cms.entity.PointHistory;
import com.weilyeat.cms.entity.User;
import com.weilyeat.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

public interface PointHistoryService {
    Page<PointHistoryDto.list> list(SinghaUser authUser, Pageable pageable);
    void addPoint(int point, String reason, String userUid);
}

@Service
@AllArgsConstructor
class PointHistoryServiceImpl implements PointHistoryService {
    private final PointHistoryRepository pointHistoryRepository;
    private final UserRepository userRepository;

    @Override
    public Page<PointHistoryDto.list> list(SinghaUser authUser, Pageable pageable) {
        PointHistorySearch search = new PointHistorySearch();
        search.setUserUid(authUser.getUser().getUid());
        return pointHistoryRepository.findAll(search.search(), pageable)
            .map(e -> PointHistoryMapper.INSTANCE.entityToListDto(e));
    }

    @Override
    public void addPoint(int point, String reason, String userUid) {
        User user = userRepository.findById(userUid).orElseThrow(() -> new NotFoundException(NotFound.USER));
        
        PointHistory history = new PointHistory();
        history.setUserUid(user.getUid());
        history.setPoint(point);
        history.setReason(reason);
        if (user.getPoint() == null) user.setPoint(0);
        history.setRemainPoint(user.getPoint() + point);
        pointHistoryRepository.save(history);

        user.setPoint(user.getPoint() + point);
        userRepository.save(user);
    }
}
