package com.ttwijang.cms.api.product.repository.query;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ttwijang.cms.api.product.dto.ShopAdmProductOrderDto;
import com.ttwijang.cms.api.product.repository.search.AdmProductOrderSearch;

@Component
public class ShopAdmProductOrderQuery {
    
    @PersistenceContext
    private EntityManager entityManager;

    /* public List<ShopAdmProductOrderDto.list> list(Integer shopIdx, Pageable pageable) {
        List<ShopAdmProductOrderDto.list> datas = new ArrayList<ShopAdmProductOrderDto.list>();

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT po.idx, \n");
        sql.append("    p.name, \n");
        sql.append("    po.week_num, \n");
        sql.append("    po.day_num, \n");
        sql.append("    po.amount, \n");
        sql.append("    po.product_num, \n");
        sql.append("    po.settle_status, \n");
        sql.append("    date_format(pog.create_date, '%Y-%m-%d') as order_date, \n");
        sql.append("    date_format( \n");
        sql.append("        date_add( \n");
        sql.append("            ADDDATE( \n");
        sql.append("                pog.create_date, \n");
        sql.append("                -WEEKDAY(pog.create_date) + (po.week_num - 1) \n");
        sql.append("            ), \n");
        sql.append("            INTERVAL po.week_num week \n");
        sql.append("        ) \n");
        sql.append("    , '%Y-%m-%d') as pickup_date, \n");
        sql.append("    po.settle_apply_date, \n");
        sql.append("    s.name as shop_name \n");
        sql.append("FROM product_order po \n");
        sql.append("LEFT JOIN product_order_group pog ON pog.idx = po.group_id \n");
        sql.append("LEFT JOIN product p ON p.idx = po.product_id \n");
        sql.append("LEFT JOIN shop s ON pog.shop_id = s.idx \n");
        sql.append("WHERE pog.shop_id = :shopIdx AND po.pickup_status = 1 AND po.settle_status IS NULL \n");
        sql.append("ORDER BY po.settle_status, pickup_date desc \n");
        sql.append("LIMIT :startLimit, :endLimit");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("shopIdx", shopIdx);
        query.setParameter("startLimit", pageable.getPageNumber() * pageable.getPageSize());
        query.setParameter("endLimit", pageable.getPageSize());

        List<Object[]> resultList = query.getResultList();
        for (Object[] row : resultList) {
            int index = 0;
            ShopAdmProductOrderDto.list data = new ShopAdmProductOrderDto.list();
            
            data.setIdx(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setProductName(row[index].toString());
            if (row[++index] != null) data.setWeekNum(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setDayNum(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setAmount(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setProductNum(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setSettleStatus(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setOrderDate(row[index].toString());
            if (row[++index] != null) data.setPickupDate(row[index].toString());
            if (row[++index] != null) data.setSettleDate(row[index].toString());
            if (row[++index] != null) data.setShopName(row[index].toString());

            datas.add(data);
        }
        return datas;
    } */

    public int getCount(Integer shopIdx) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT \n");
        sql.append("    COUNT(po.idx) \n");
        sql.append("FROM product_order po \n");
        sql.append("LEFT JOIN product_order_group pog ON pog.idx = po.group_id \n");
        sql.append("LEFT JOIN product p ON p.idx = po.product_id \n");
        sql.append("WHERE pog.shop_id = :shopIdx AND po.pickup_status = 1");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("shopIdx", shopIdx);

        return ((BigInteger) query.getSingleResult()).intValue();
    }

    public ShopAdmProductOrderDto.totalStatistics getTotalStatistics(AdmProductOrderSearch search) {
        ShopAdmProductOrderDto.totalStatistics data = new ShopAdmProductOrderDto.totalStatistics();
        Integer shopId = search.getShopId();

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT \n");
        sql.append("    SUM(po.product_num), \n");
        sql.append("    SUM(po.amount), \n");
        sql.append("    COUNT(DISTINCT(pog.shop_id)) \n");
        sql.append("FROM product_order po \n");
        sql.append("LEFT JOIN product_order_group pog ON pog.idx = po.group_id \n");
        sql.append("LEFT JOIN user u ON pog.user_uid = u.uid \n");
        sql.append("LEFT JOIN shop s ON s.idx = pog.shop_id \n");
        sql.append("WHERE pog.payment_status = 1 AND po.order_status > -1 AND shop_id = :shopId \n");

        if (StringUtils.hasText(search.getSearchType()) && StringUtils.hasText(search.getSearchValue())) {
            if (search.getSearchType().equals("productName")) sql.append("    AND po.product_name LIKE CONCAT('%', :searchValue, '%') \n");
            if (search.getSearchType().equals("actualName")) sql.append("    AND u.actual_name LIKE CONCAT('%', :searchValue, '%') \n");
            if (search.getSearchType().equals("shopName")) sql.append("    AND s.name LIKE CONCAT('%', :searchValue, '%') \n");
        }

        if (StringUtils.hasText(search.getStartDate()) && StringUtils.hasText(search.getEndDate())) {
            sql.append(" AND po.order_date BETWEEN :startDate AND :endDate \n");
        }

        Query query = entityManager.createNativeQuery(sql.toString());
        if (StringUtils.hasText(search.getSearchType()) && StringUtils.hasText(search.getSearchValue())) query.setParameter("searchValue", search.getSearchValue());
        if (StringUtils.hasText(search.getStartDate()) && StringUtils.hasText(search.getEndDate())) {
            query.setParameter("startDate", search.getStartDate());
            query.setParameter("endDate", search.getEndDate());
        }
        query.setParameter("shopId", shopId);

        List<Object[]> resultList = query.getResultList();
        for (Object[] row : resultList) {
            int index = 0;
            if (row[index] != null) data.setTotalProductNum(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setTotalAmount(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setTotalShop(Integer.parseInt(row[index].toString()));
        }
        return data;
    }

}
