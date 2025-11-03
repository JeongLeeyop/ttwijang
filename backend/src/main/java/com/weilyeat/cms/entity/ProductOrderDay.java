package com.weilyeat.cms.entity;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ProductOrderDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;
    private Integer groupId;
    private Integer weekNum; 
    private LocalDate orderDate;
    @OneToMany(mappedBy = "day")
    private List<ProductOrder> products;
}
