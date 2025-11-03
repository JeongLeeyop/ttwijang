package com.weilyeat.cms.api.user.dto;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.weilyeat.cms.entity.User;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class UserFcmToken {
    @Id
    private String userUid;
    
    private String token;

    @ManyToOne
    @JoinColumn(name = "userUid", insertable = false, updatable = false)
    private User user;
}
