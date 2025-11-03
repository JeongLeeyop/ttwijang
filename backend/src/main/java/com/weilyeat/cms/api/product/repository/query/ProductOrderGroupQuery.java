package com.ttwijang.cms.api.product.repository.query;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.ttwijang.cms.api.product.dto.ProductOrderGroupDto;

import org.springframework.stereotype.Component;

@Component
public class ProductOrderGroupQuery {
    
    @PersistenceContext
    private EntityManager entityManager;

    public ProductOrderGroupDto.detail detail() {
        StringBuffer sql = new StringBuffer();
        return null;
    }
}
