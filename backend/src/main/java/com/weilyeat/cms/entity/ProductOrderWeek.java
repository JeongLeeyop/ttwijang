package com.ttwijang.cms.entity;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
@Entity
public class ProductOrderWeek {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;
    private Integer groupId;
    private Integer weekNum; 
    private LocalDate startDate;
    private LocalDate endDate;
    @OneToMany(mappedBy = "week")
    private List<ProductOrder> products;

    public ProductOrderWeek(Integer weekNum, LocalDate startDate, LocalDate endDate) {
        this.weekNum = weekNum;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
