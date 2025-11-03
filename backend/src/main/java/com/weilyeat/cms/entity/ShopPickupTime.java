package com.ttwijang.cms.entity;

import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ShopPickupTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    private Integer weekDay; // 요일(0:월~6:일)

    private LocalTime pickupTime; // 픽업시간대

    @ManyToOne
    @JoinColumn(name = "shopIdx")
    private Shop shop;

    public ShopPickupTime(Integer weekDay, LocalTime pickupTime, Shop shop) {
        this.weekDay = weekDay;
        this.pickupTime = pickupTime;
        this.shop = shop;
    }
}
