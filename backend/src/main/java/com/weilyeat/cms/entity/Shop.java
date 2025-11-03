package com.weilyeat.cms.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
							
						
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Shop implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    private String name;
    
    private String tel;

    private String postCode;

    private String address;

    private String addressDetail;

    private String lat;

    private String lon;

    private LocalTime openTime;

    private LocalTime closeTime;

    private LocalDate holidayStartDate;
    
    private LocalDate holidayEndDate;

    @CreationTimestamp
    private LocalDateTime createDate;

    private boolean withdrawStatus;

    private LocalDateTime withdrawDate;

    private LocalDate openingDate; // 개업일

    private String bankCode; // 은행사 코드

    private String holderName; // 예금주

    private String accountNumber; // 계좌번호
    
    private int maxHoldCnt; // 주문 가능 수량
    
    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    @OrderBy("weekDay asc, pickupTime asc")
    private List<ShopPickupTime> pickupTimes;
    
    @OneToOne(mappedBy = "shop", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("weekDay asc")
    private ShopHoliday holidays;

    public void setHolidays(ShopHoliday holidays) {
        this.holidays = holidays;
        if (holidays != null) {
            holidays.setShop(this);
        }
    }
}	
	