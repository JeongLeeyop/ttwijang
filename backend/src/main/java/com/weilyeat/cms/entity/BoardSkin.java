package com.weilyeat.cms.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class BoardSkin {
    @Id
    private String code;

    private String name;
}
