package com.ttwijang.cms.api.user.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ttwijang.cms.api.user.dto.UserFcmToken;
import com.ttwijang.cms.api.user.dto.UserFcmTokenDto;
import com.ttwijang.cms.api.user.exception.UserFcmNotFoundException;
import com.ttwijang.cms.api.user.exception.UserNotFoundException;
import com.ttwijang.cms.api.user.repository.UserFcmTokenRepository;
import com.ttwijang.cms.entity.User;
import com.ttwijang.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

public interface UserFcmTokenService {
    void saveUserFcmToken(UserFcmTokenDto.Save saveDto, SinghaUser authUser);
    void deleteUserFcmToken(String token, SinghaUser authUser);
}

@Service
@AllArgsConstructor
class UserFcmTokenServiceImpl implements UserFcmTokenService {
    private final UserFcmTokenRepository userFcmTokenRepository;

    @Override
    public void saveUserFcmToken(UserFcmTokenDto.Save saveDto, SinghaUser authUser) {
        // if (authUser == null || authUser.getUser() == null) throw new UserNotFoundException();
        Optional<UserFcmToken> optional = userFcmTokenRepository.findById(authUser.getUser().getUid());
        if (optional.isEmpty()) {
            UserFcmToken entity = new UserFcmToken();
            entity.setToken(saveDto.getToken());
            entity.setUserUid(authUser.getUser().getUid());
            userFcmTokenRepository.save(entity);
        } else {
            UserFcmToken entity = optional.get();
            entity.setToken(saveDto.getToken());
            // entity.setUserUid(authUser.getUser().getUid());
            userFcmTokenRepository.save(entity);
        }
    }

    @Override
    public void deleteUserFcmToken(String token, SinghaUser authUser) {
        if (authUser == null || authUser.getUser() == null) throw new UserNotFoundException();
        userFcmTokenRepository.deleteByTokenAndUserUid(token, authUser.getUser().getUid());
    }
}
