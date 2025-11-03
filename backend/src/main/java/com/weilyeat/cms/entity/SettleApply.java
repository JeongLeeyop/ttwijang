package com.ttwijang.cms.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class SettleApply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    private String userUid;

    private Integer shopIdx;

    private String shopName;

    private boolean approvalStatus;

    private boolean cancelStatus;

    private int totalSale;

    private int totalSettle;

    private int totalProductNum;

    @CreationTimestamp
    private LocalDateTime applyDate;

    private LocalDateTime approvalDate;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "apply")
    private List<SettleApplyItem> items;

    @ManyToOne
    @JoinColumn(name = "shopIdx", insertable = false, updatable = false)
    private Shop shop;

    public SettleApply(Shop shop, int totalSale, int totalSettle, int totalProductNum, User user) {
        this.shopIdx = shop.getIdx();
        this.shopName = shop.getName();
        this.approvalStatus = false;
        this.cancelStatus = false;
        this.totalSale = totalSale;
        this.totalSettle = totalSettle;
        this.totalProductNum = totalProductNum;
        this.userUid =  user.getUid();
    }
}
