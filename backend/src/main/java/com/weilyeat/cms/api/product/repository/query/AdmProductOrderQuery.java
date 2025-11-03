package com.weilyeat.cms.api.product.repository.query;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.weilyeat.cms.api.product.dto.AdmProductOrderDto;
import com.weilyeat.cms.api.product.dto.AdmProductOrderDto.detailByDay;
import com.weilyeat.cms.api.product.dto.AdmProductOrderDto.detailByProduct;
import com.weilyeat.cms.api.product.dto.AdmProductOrderDto.detailByShop;
import com.weilyeat.cms.api.product.dto.AdmProductOrderDto.listByDay;
import com.weilyeat.cms.api.product.dto.AdmProductOrderDto.listByProduct;
import com.weilyeat.cms.api.product.dto.AdmProductOrderDto.listByShop;
import com.weilyeat.cms.api.product.dto.AdmProductOrderDto.updateReceiveStatus;
import com.weilyeat.cms.api.product.repository.search.AdmProductOrderSearch;
import com.weilyeat.cms.entity.ProductOrderType;

@Component
public class AdmProductOrderQuery {
    @PersistenceContext
    private EntityManager entityManager;

    public AdmProductOrderDto.totalStatistics getTotalStatistics(AdmProductOrderSearch search) {
        AdmProductOrderDto.totalStatistics data = new AdmProductOrderDto.totalStatistics();

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT \n");
        sql.append("    SUM(po.product_num), \n");
        sql.append("    SUM(po.amount), \n");
        sql.append("    COUNT(DISTINCT(pog.shop_id)) \n");
        sql.append("FROM product_order po \n");
        sql.append("LEFT JOIN product_order_group pog ON pog.idx = po.group_id \n");
        sql.append("LEFT JOIN user u ON pog.user_uid = u.uid \n");
        sql.append("LEFT JOIN shop s ON s.idx = pog.shop_id \n");
        sql.append("WHERE pog.payment_status = 1 AND po.order_status > -1 \n");
        sql.append("    AND pog.user_uid IS NOT NULL\n");

        if (StringUtils.hasText(search.getSearchType()) && StringUtils.hasText(search.getSearchValue())) {
            if (search.getSearchType().equals("productName")) sql.append("    AND po.product_name LIKE CONCAT('%', :searchValue, '%') \n");
            if (search.getSearchType().equals("actualName")) sql.append("    AND u.actual_name LIKE CONCAT('%', :searchValue, '%') \n");
            if (search.getSearchType().equals("shopName")) sql.append("    AND s.name LIKE CONCAT('%', :searchValue, '%') \n");
        }

        if (search.getShopId()!=null) {
            sql.append("    AND pog.shop_id = :shopId \n");
        }
        
        if (StringUtils.hasText(search.getStartDate()) && StringUtils.hasText(search.getEndDate())) {
            sql.append("    AND po.order_date BETWEEN :startDate AND :endDate \n");
            // sql.append("    AND pod.order_date BETWEEN :startDate AND :endDate \n");
        }
        if (StringUtils.hasText(search.getOrderType())) {
            sql.append("    AND pog.order_type = :orderType \n");
        }

        Query query = entityManager.createNativeQuery(sql.toString());
        if (search.getShopId()!=null) {
            query.setParameter("shopId", search.getShopId());
        }
        
        if (StringUtils.hasText(search.getSearchType()) && StringUtils.hasText(search.getSearchValue())) query.setParameter("searchValue", search.getSearchValue());
        if (StringUtils.hasText(search.getStartDate()) && StringUtils.hasText(search.getEndDate())) {
            query.setParameter("startDate", search.getStartDate());
            query.setParameter("endDate", search.getEndDate());
        }
        if (search.getOrderType()!=null) {
            query.setParameter("orderType", search.getOrderType());   
        }

        List<Object[]> resultList = query.getResultList();
        for (Object[] row : resultList) {
            int index = 0;
            if (row[index] != null) data.setTotalProductNum(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setTotalAmount(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setTotalShop(Integer.parseInt(row[index].toString()));
        }
        return data;
    }

    public List<listByDay> listByDay(AdmProductOrderSearch search, Pageable pageable) {
        List<AdmProductOrderDto.listByDay> datas = new ArrayList<AdmProductOrderDto.listByDay>();

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT pog.idx as groupIdx, \n");
        sql.append("   po.idx as idx, \n");
        sql.append("   s.name as shopName, \n");
        sql.append("   st.name as stationName, \n");
        sql.append("   st.address, \n");
        sql.append("   pog.address_detail, \n");
        sql.append("   u.actual_name, \n");
        sql.append("   u.concat_number, \n");
        sql.append("   po.pickup_status, \n");
        sql.append("   po.pickup_time, \n");
        sql.append("    pog.create_date, \n");
        sql.append("    pod.order_date, \n");
        sql.append("    po.day_id, \n");
        sql.append("    po.week_num, \n");
        // sql.append("    po.day_num, \n");
        sql.append("    po.start_date, \n");
        sql.append("    po.end_date, \n");
        if(search.getExcelType() != null && search.getOrderType().equals(ProductOrderType.PICKUP.toString())) {
            sql.append("    (select SUM(product_num) from product_order po2 where po2.day_id = po.day_id) as product_num, \n");
            sql.append("    (select SUM(amount) from product_order po2 where po2.day_id = po.day_id) as amount \n");
        } else {
            sql.append("    SUM(po.product_num) as product_num, \n");
            sql.append("    SUM(po.amount) as amount \n");
        }
        if(search.getExcelType() != null) {
            sql.append("    ,pog.memo \n");
            sql.append("    ,po.product_name \n");
            sql.append("    ,po.product_num as product_num2\n");
            sql.append("    ,po.amount as product_price\n");
        }
        sql.append("FROM product_order po \n");
        sql.append("LEFT JOIN product_order_group pog ON pog.idx = po.group_id \n");
        sql.append("LEFT JOIN product_order_day pod ON po.day_id = pod.idx \n");
        sql.append("LEFT JOIN product p ON p.idx = po.product_id \n");
        sql.append("LEFT JOIN user u ON pog.user_uid = u.uid \n");
        sql.append("LEFT JOIN shop s ON s.idx = pog.shop_id \n");
        sql.append("LEFT JOIN station st ON st.idx = pog.station_id \n");

        sql.append("WHERE pog.payment_status = 1 \n");
        sql.append("    AND pog.user_uid IS NOT NULL\n");
        if (StringUtils.hasText(search.getSearchType()) && StringUtils.hasText(search.getSearchValue())) {
            if (search.getSearchType().equals("productName")) sql.append("    AND po.product_name LIKE CONCAT('%', :searchValue, '%') \n");
            if (search.getSearchType().equals("actualName")) sql.append("    AND u.actual_name LIKE CONCAT('%', :searchValue, '%') \n");
            if (search.getSearchType().equals("shopName")) sql.append("    AND s.name LIKE CONCAT('%', :searchValue, '%') \n");
        }

        if (search.getShopId()!=null) {
            sql.append("    AND pog.shop_id = :shopId \n");
        }
        if (search.getStationId()!=null) {
            // sql.append("    AND pog.station_id = :stationId \n");
        }

        if (search.getOrderStatus()!=null) {
            sql.append("    AND po.order_status = :orderStatus \n");
        } else {
            sql.append("    AND po.order_status > -1 \n");
        }
        if (search.getGroupOrderStatus()!=null) {
            sql.append("    AND pog.order_status = :groupOrderStatus \n");
        } else {
            sql.append("    AND pog.order_status > -1 \n");
        }
        if (search.getReceiveStatus()!=null) {
            sql.append("    AND po.receive_status = :receiveStatus \n");
        } else {
            // sql.append("    AND po.receive_status != 2 \n");
        }
        
        if (search.getSettleStatus()!=null) {
            sql.append("    AND po.settle_status = :settleStatus \n");
        }

        if (StringUtils.hasText(search.getStartDate()) && StringUtils.hasText(search.getEndDate())) {
            sql.append("    AND pod.order_date BETWEEN :startDate AND :endDate \n");
            // sql.append("    AND po.start_date BETWEEN :startDate AND :endDate \n");
            // sql.append("    AND po.end_date BETWEEN :startDate AND :endDate \n");
        } 
        
        if (StringUtils.hasText(search.getOrderType())) {
            sql.append("    AND pog.order_type = :orderType \n");
        }
        
        sql.append("GROUP BY po.group_id \n");
        sql.append(",pog.shop_id \n");
        sql.append(",pog.station_id \n");
        sql.append(",po.idx \n");
        sql.append(",po.start_date \n");
        sql.append(",po.end_date \n");
        sql.append(",po.week_num \n");
        sql.append(",po.day_id \n");
        sql.append(",po.pickup_time \n");
        sql.append(",po.pickup_status \n");
        sql.append(",pod.order_date \n");
        if(search.getExcelType() != null && search.getOrderType().equals(ProductOrderType.PICKUP.toString())){
            sql.append(",po.product_id \n");
        }

        sql.append("order by pod.order_date, po.day_id \n");
        // sql.append(",po.day_num \n");
        if(search.getExcelType() == null) sql.append("LIMIT :startLimit, :endLimit");

        Query query = entityManager.createNativeQuery(sql.toString());

        if(search.getExcelType() == null) {
            query.setParameter("startLimit", pageable.getPageNumber() * pageable.getPageSize());
            query.setParameter("endLimit", pageable.getPageSize());
        }

        if (StringUtils.hasText(search.getSearchType()) && StringUtils.hasText(search.getSearchValue())) query.setParameter("searchValue", search.getSearchValue());
        if (StringUtils.hasText(search.getStartDate()) && StringUtils.hasText(search.getEndDate())) {
            query.setParameter("startDate", search.getStartDate());
            query.setParameter("endDate", search.getEndDate());
        }
        
        if (search.getShopId()!=null) {
            query.setParameter("shopId", search.getShopId());
        }
        if (search.getStationId()!=null) {
            // query.setParameter("stationId", search.getStationId());
        }

        if (search.getOrderStatus()!=null) {
            query.setParameter("orderStatus", search.getOrderStatus());
        }
        if (search.getGroupOrderStatus()!=null) {
            query.setParameter("groupOrderStatus", search.getGroupOrderStatus());
        }
        if (search.getReceiveStatus()!=null) {
            query.setParameter("receiveStatus", search.getReceiveStatus());
        }
        if (search.getSettleStatus()!=null) {
            query.setParameter("settleStatus", search.getSettleStatus());
        }
        if (search.getOrderType()!=null) {
            query.setParameter("orderType", search.getOrderType());
        }

        List<Object[]> resultList = query.getResultList();
        for (Object[] row : resultList) {
            int index = 0;
            AdmProductOrderDto.listByDay data = new AdmProductOrderDto.listByDay();
            data.setGroupIdx(Integer.parseInt(row[index].toString()));
            
            if (row[++index] != null) data.setIdx(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setShopName(row[index].toString());
            if (row[++index] != null) data.setStationName(row[index].toString());
            if (row[++index] != null) data.setAddress(row[index].toString());
            if (row[++index] != null) data.setAddressDetail(row[index].toString());
            if (row[++index] != null) data.setName(row[index].toString());
            if (row[++index] != null) data.setNumber(row[index].toString());
            if (row[++index] != null) data.setPickupStatus(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setPickupTime(row[index].toString());
            if (row[++index] != null) data.setCreateDate(row[index].toString());
            if (row[++index] != null) data.setPickupDate(row[index].toString());
            if (row[++index] != null) data.setDayId(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setWeekNum(Integer.parseInt(row[index].toString()));
            // if (row[++index] != null) data.setDayNum(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setStartDate(row[index].toString());
            if (row[++index] != null) data.setEndDate(row[index].toString());
            if (row[++index] != null) data.setProductNum(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setAmount(Integer.parseInt(row[index].toString()));

            if(search.getExcelType() != null) {
                if (row[++index] != null) data.setMemo(row[index].toString());
                if (row[++index] != null) data.setProductName(row[index].toString());
                if (row[++index] != null) data.setProductNum2(Integer.parseInt(row[index].toString()));
                if (row[++index] != null) data.setProductPrice(Integer.parseInt(row[index].toString()));
            }
            datas.add(data);
        }
        return datas;
    }


    public int countByDay(AdmProductOrderSearch search) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT count(c.count) from (\n");
        // sql.append("SELECT count(c.row) from (\n");
        sql.append("SELECT COUNT(DISTINCT(pog.idx)) as count\n");
        // sql.append("SELECT COUNT(DISTINCT(pog.idx)) as row \n");
        sql.append("FROM product_order po \n");
        sql.append("LEFT JOIN product_order_group pog ON pog.idx = po.group_id \n");
        sql.append("LEFT JOIN product_order_day pod ON po.day_id = pod.idx \n");
        sql.append("LEFT JOIN product p ON p.idx = po.product_id \n");
        sql.append("LEFT JOIN user u ON pog.user_uid = u.uid \n");
        sql.append("LEFT JOIN shop s ON s.idx = pog.shop_id \n");
        sql.append("WHERE pog.payment_status = 1 \n");
        sql.append("    AND pog.user_uid IS NOT NULL\n");
        if (StringUtils.hasText(search.getSearchType()) && StringUtils.hasText(search.getSearchValue())) {
            if (search.getSearchType().equals("productName")) sql.append("    AND po.product_name LIKE CONCAT('%', :searchValue, '%') \n");
            if (search.getSearchType().equals("actualName")) sql.append("    AND u.actual_name LIKE CONCAT('%', :searchValue, '%') \n");
            if (search.getSearchType().equals("shopName")) sql.append("    AND s.name LIKE CONCAT('%', :searchValue, '%') \n");
        }

        if (search.getShopId()!=null) {
            sql.append("    AND pog.shop_id = :shopId \n");
        }
        if (search.getStationId()!=null) {
            sql.append("    AND pog.station_id = :stationId \n");
        }

        if (search.getOrderStatus()!=null) {
            sql.append("    AND po.order_status = :orderStatus \n");
        } else {
            sql.append("    AND po.order_status > -1 \n");
        }

        if (search.getGroupOrderStatus()!=null) {
            sql.append("    AND pog.order_status = :groupOrderStatus \n");
        } else {
            sql.append("    AND pog.order_status > -1 \n");
        }
          if (search.getReceiveStatus()!=null) {
            sql.append("    AND po.receive_status = :receiveStatus \n");
        } else {
            // sql.append("    AND po.receive_status != 2 \n");
        }

        if (search.getSettleStatus()!=null) {
            sql.append("    AND po.settle_status = :settleStatus \n");
        }

        if (StringUtils.hasText(search.getStartDate()) && StringUtils.hasText(search.getEndDate())) {
            // sql.append("    AND po.start_date BETWEEN :startDate AND :endDate \n");
            // sql.append("    AND po.end_date BETWEEN :startDate AND :endDate \n");
            sql.append("    AND pod.order_date BETWEEN :startDate AND :endDate \n");
        }

        if (StringUtils.hasText(search.getOrderType())) {
            sql.append("    AND pog.order_type = :orderType \n");
        }
        
        sql.append("GROUP BY po.group_id \n");
        sql.append(",pog.shop_id \n");
        sql.append(",pog.station_id \n");
        sql.append(",po.idx \n");
        sql.append(",po.start_date \n");
        sql.append(",po.end_date \n");
        sql.append(",po.week_num \n");
        sql.append(",po.day_id \n");
        sql.append(",po.pickup_time \n");
        sql.append(",po.pickup_status \n");
        sql.append(",pod.order_date \n");
        /*  sql.append(",pog.shop_id \n");
        sql.append(",po.start_date \n");
        sql.append(",po.end_date \n");
        sql.append(",po.week_num \n");
        sql.append(",po.day_num \n"); */
        
        sql.append(") as c\n");

        Query query = entityManager.createNativeQuery(sql.toString());

        if (StringUtils.hasText(search.getSearchType()) && StringUtils.hasText(search.getSearchValue())) query.setParameter("searchValue", search.getSearchValue());
        if (StringUtils.hasText(search.getStartDate()) && StringUtils.hasText(search.getEndDate())) {
            query.setParameter("startDate", search.getStartDate());
            query.setParameter("endDate", search.getEndDate());
        }
        
        if (search.getShopId()!=null) {
            query.setParameter("shopId", search.getShopId());
        }
        if (search.getOrderStatus()!=null) {
            query.setParameter("orderStatus", search.getOrderStatus());
        }
        if (search.getGroupOrderStatus()!=null) {
            query.setParameter("groupOrderStatus", search.getGroupOrderStatus());
        }
         if (search.getReceiveStatus()!=null) {
            query.setParameter("receiveStatus", search.getReceiveStatus());
        }
        if (search.getSettleStatus()!=null) {
            query.setParameter("settleStatus", search.getSettleStatus());
        }
        if (search.getOrderType()!=null) {
            query.setParameter("orderType", search.getOrderType());
            
        }
        try {
            BigInteger result = (BigInteger) query.getSingleResult();
            return result.intValue();
        } catch (NoResultException e) {
            return  0; // 기본값 설정
        }
    }

    public List<listByProduct> listByProduct(AdmProductOrderSearch search, Pageable pageable) {
        List<AdmProductOrderDto.listByProduct> datas = new ArrayList<AdmProductOrderDto.listByProduct>();

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT p.idx, \n");
        sql.append("    p.name, \n");
        sql.append("    SUM(po.product_num), \n");
        sql.append("    SUM(po.amount) \n");
        sql.append("FROM product_order po \n");
        sql.append("LEFT JOIN product_order_group pog ON pog.idx = po.group_id \n");
        sql.append("LEFT JOIN product p ON p.idx = po.product_id \n");
        sql.append("LEFT JOIN user u ON pog.user_uid = u.uid \n");
        sql.append("LEFT JOIN shop s ON s.idx = pog.shop_id \n");
        sql.append("WHERE pog.payment_status = 1 AND po.order_status > -1 \n");
        sql.append("    AND pog.user_uid IS NOT NULL\n");
        if (StringUtils.hasText(search.getSearchType()) && StringUtils.hasText(search.getSearchValue())) {
            if (search.getSearchType().equals("productName")) sql.append("    AND po.product_name LIKE CONCAT('%', :searchValue, '%') \n");
            if (search.getSearchType().equals("actualName")) sql.append("    AND u.actual_name LIKE CONCAT('%', :searchValue, '%') \n");
            if (search.getSearchType().equals("shopName")) sql.append("    AND s.name LIKE CONCAT('%', :searchValue, '%') \n");
        }

        if (StringUtils.hasText(search.getStartDate()) && StringUtils.hasText(search.getEndDate())) {
            sql.append("    AND po.order_date BETWEEN :startDate AND :endDate \n");
        }
        if (StringUtils.hasText(search.getOrderType())) {
            sql.append("    AND pog.order_type = :orderType \n");
        }
        sql.append("GROUP BY p.idx \n");

        if(search.getExcelType() == null) sql.append("LIMIT :startLimit, :endLimit");

        Query query = entityManager.createNativeQuery(sql.toString());

        if(search.getExcelType() == null) {
            query.setParameter("startLimit", pageable.getPageNumber() * pageable.getPageSize());
            query.setParameter("endLimit", pageable.getPageSize());
        }

        if (StringUtils.hasText(search.getSearchType()) && StringUtils.hasText(search.getSearchValue())) query.setParameter("searchValue", search.getSearchValue());
        if (StringUtils.hasText(search.getStartDate()) && StringUtils.hasText(search.getEndDate())) {
            query.setParameter("startDate", search.getStartDate());
            query.setParameter("endDate", search.getEndDate());
        }
        if (search.getOrderType()!=null) {
            query.setParameter("orderType", search.getOrderType());   
        }

        List<Object[]> resultList = query.getResultList();
        for (Object[] row : resultList) {
            int index = 0;
            AdmProductOrderDto.listByProduct data = new AdmProductOrderDto.listByProduct();
            data.setProductId(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setProductName(row[index].toString());
            if (row[++index] != null) data.setTotalProductNum(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setTotalAmount(Integer.parseInt(row[index].toString()));
            datas.add(data);
        }
        return datas;
    }

    public int countByProduct(AdmProductOrderSearch search) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(DISTINCT(p.idx)) \n");
        sql.append("FROM product_order po \n");
        sql.append("LEFT JOIN product_order_group pog ON pog.idx = po.group_id \n");
        sql.append("LEFT JOIN product p ON p.idx = po.product_id \n");
        sql.append("LEFT JOIN user u ON pog.user_uid = u.uid \n");
        sql.append("LEFT JOIN shop s ON s.idx = pog.shop_id \n");
        sql.append("WHERE pog.payment_status = 1 AND po.order_status > -1 \n");
        sql.append("    AND pog.user_uid IS NOT NULL\n");
        if (StringUtils.hasText(search.getSearchType()) && StringUtils.hasText(search.getSearchValue())) {
            if (search.getSearchType().equals("productName")) sql.append("    AND po.product_name LIKE CONCAT('%', :searchValue, '%') \n");
            if (search.getSearchType().equals("actualName")) sql.append("    AND u.actual_name LIKE CONCAT('%', :searchValue, '%') \n");
            if (search.getSearchType().equals("shopName")) sql.append("    AND s.name LIKE CONCAT('%', :searchValue, '%') \n");
        }

        if (StringUtils.hasText(search.getStartDate()) && StringUtils.hasText(search.getEndDate())) {
            sql.append("    AND po.order_date BETWEEN :startDate AND :endDate \n");
        }
        if (StringUtils.hasText(search.getOrderType())) {
            sql.append("    AND pog.order_type = :orderType \n");
        }

        Query query = entityManager.createNativeQuery(sql.toString());
        if (StringUtils.hasText(search.getSearchType()) && StringUtils.hasText(search.getSearchValue())) query.setParameter("searchValue", search.getSearchValue());
        if (StringUtils.hasText(search.getStartDate()) && StringUtils.hasText(search.getEndDate())) {
            query.setParameter("startDate", search.getStartDate());
            query.setParameter("endDate", search.getEndDate());
        }
        if (search.getOrderType()!=null) {
            query.setParameter("orderType", search.getOrderType());   
        }
        return ((BigInteger) query.getSingleResult()).intValue();
    }

    public List<detailByProduct> detailByProduct(Integer idx, AdmProductOrderSearch search) {
        List<AdmProductOrderDto.detailByProduct> datas = new ArrayList<AdmProductOrderDto.detailByProduct>();
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT s.idx, \n");
        sql.append("    s.name, \n");
        sql.append("    SUM(po.product_num), \n");
        sql.append("    SUM(po.amount) \n");
        sql.append("FROM product_order po \n");
        sql.append("LEFT JOIN product_order_group pog ON pog.idx = po.group_id \n");
        sql.append("LEFT JOIN product p ON p.idx = po.product_id \n");
        if(search.getOrderType().equals(ProductOrderType.PICKUP.toString())) {
            sql.append("LEFT JOIN shop s ON s.idx = pog.shop_id \n");
        } else if(search.getOrderType().equals(ProductOrderType.STATION.toString())) {
            sql.append("LEFT JOIN station s ON s.idx = pog.station_id \n");
        }
        sql.append("LEFT JOIN user u ON pog.user_uid = u.uid \n");
        sql.append("WHERE pog.payment_status = 1 AND po.order_status > -1 AND po.product_id = :productId \n");
        if (StringUtils.hasText(search.getSearchType()) && StringUtils.hasText(search.getSearchValue())) {
            if (search.getSearchType().equals("productName")) sql.append("    AND po.product_name LIKE CONCAT('%', :searchValue, '%') \n");
            if (search.getSearchType().equals("actualName")) sql.append("    AND u.actual_name LIKE CONCAT('%', :searchValue, '%') \n");
            if (search.getSearchType().equals("shopName")) sql.append("    AND s.name LIKE CONCAT('%', :searchValue, '%') \n");
            if (search.getSearchType().equals("stationName")) sql.append("    AND s.name LIKE CONCAT('%', :searchValue, '%') \n");
        }

        if (StringUtils.hasText(search.getStartDate()) && StringUtils.hasText(search.getEndDate())) {
            sql.append("    AND po.order_date BETWEEN :startDate AND :endDate \n");
        }
        if (StringUtils.hasText(search.getOrderType())) {
            sql.append("    AND pog.order_type = :orderType \n");
        }

        sql.append("GROUP BY s.idx \n");

        Query query = entityManager.createNativeQuery(sql.toString());

        query.setParameter("productId", idx);
        if (StringUtils.hasText(search.getSearchType()) && StringUtils.hasText(search.getSearchValue())) query.setParameter("searchValue", search.getSearchValue());
        if (StringUtils.hasText(search.getStartDate()) && StringUtils.hasText(search.getEndDate())) {
            query.setParameter("startDate", search.getStartDate());
            query.setParameter("endDate", search.getEndDate());
        }
        if (search.getOrderType()!=null) {
            query.setParameter("orderType", search.getOrderType());   
        }

        List<Object[]> resultList = query.getResultList();
        for (Object[] row : resultList) {
            int index = 0;
            AdmProductOrderDto.detailByProduct data = new AdmProductOrderDto.detailByProduct();
            
            if(search.getOrderType().equals(ProductOrderType.PICKUP.toString())) {
                if (row[index] != null)data.setShopIdx(Integer.parseInt(row[index].toString()));
                if (row[++index] != null) data.setShopName(row[index].toString());
            } else if(search.getOrderType().equals(ProductOrderType.STATION.toString())) {
                if (row[index] != null) data.setStationIdx(Integer.parseInt(row[index].toString()));
                if (row[++index] != null) data.setStationName(row[index].toString());
            }
            
            if (row[++index] != null) data.setTotalProductNum(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setTotalAmount(Integer.parseInt(row[index].toString()));
            datas.add(data);
        }
        return datas;
    }

    public List<listByShop> listByShop(AdmProductOrderSearch search, Pageable pageable) {
        List<AdmProductOrderDto.listByShop> datas = new ArrayList<AdmProductOrderDto.listByShop>();

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT s.idx, \n");
        sql.append("    s.name, \n");
        sql.append("    s.address, \n");
        sql.append("    s.address_detail, \n");
        if(search.getOrderType().equals(ProductOrderType.PICKUP.toString())) {
            sql.append("    s.tel, \n");
        }
            sql.append("    SUM(po.product_num), \n");
        sql.append("    SUM(po.amount) \n");
        sql.append("FROM product_order po \n");
        sql.append("LEFT JOIN product_order_group pog ON pog.idx = po.group_id \n");
        sql.append("LEFT JOIN product p ON p.idx = po.product_id \n");
        sql.append("LEFT JOIN user u ON pog.user_uid = u.uid \n");
        if(search.getOrderType().equals(ProductOrderType.PICKUP.toString())) {
            sql.append("LEFT JOIN shop s ON s.idx = pog.shop_id \n");
        } else if(search.getOrderType().equals(ProductOrderType.STATION.toString())) {
            sql.append("LEFT JOIN station s ON s.idx = pog.station_id \n");
        }
        sql.append("WHERE pog.payment_status = 1 AND po.order_status > -1 \n");
        sql.append("    AND pog.user_uid IS NOT NULL\n");
        if (StringUtils.hasText(search.getSearchType()) && StringUtils.hasText(search.getSearchValue())) {
            if (search.getSearchType().equals("productName")) sql.append("    AND po.product_name LIKE CONCAT('%', :searchValue, '%') \n");
            if (search.getSearchType().equals("actualName")) sql.append("    AND u.actual_name LIKE CONCAT('%', :searchValue, '%') \n");
            if (search.getSearchType().equals("shopName")) sql.append("    AND s.name LIKE CONCAT('%', :searchValue, '%') \n");
        }

        if (StringUtils.hasText(search.getStartDate()) && StringUtils.hasText(search.getEndDate())) {
            sql.append("    AND po.order_date BETWEEN :startDate AND :endDate \n");
        }
        if (StringUtils.hasText(search.getOrderType())) {
            sql.append("    AND pog.order_type = :orderType \n");
        }

        sql.append("GROUP BY s.idx \n");

        if(search.getExcelType() == null) sql.append("LIMIT :startLimit, :endLimit");

        Query query = entityManager.createNativeQuery(sql.toString());

        if(search.getExcelType() == null) {
            query.setParameter("startLimit", pageable.getPageNumber() * pageable.getPageSize());
            query.setParameter("endLimit", pageable.getPageSize());
        }

        if (StringUtils.hasText(search.getSearchType()) && StringUtils.hasText(search.getSearchValue())) query.setParameter("searchValue", search.getSearchValue());
        if (StringUtils.hasText(search.getStartDate()) && StringUtils.hasText(search.getEndDate())) {
            query.setParameter("startDate", search.getStartDate());
            query.setParameter("endDate", search.getEndDate());
        }
        if (search.getOrderType()!=null) {
            query.setParameter("orderType", search.getOrderType());   
        }

        List<Object[]> resultList = query.getResultList();
        for (Object[] row : resultList) {
            int index = 0;
            AdmProductOrderDto.listByShop data = new AdmProductOrderDto.listByShop();

            if(search.getOrderType().equals(ProductOrderType.PICKUP.toString())) {
                if (row[index] != null) data.setShopId(Integer.parseInt(row[index].toString()));
                if (row[++index] != null) data.setShopName(row[index].toString());
            } else if(search.getOrderType().equals(ProductOrderType.STATION.toString())) {
                if (row[index] != null) data.setStationId(Integer.parseInt(row[index].toString()));
                if (row[++index] != null) data.setStationName(row[index].toString());
            }
            
            if (row[++index] != null) data.setAddress(row[index].toString());
            if (row[++index] != null) data.setAddressDetail(row[index].toString());
            if(search.getOrderType().equals(ProductOrderType.PICKUP.toString())) {
                if (row[++index] != null) data.setShopTel(row[index].toString());
            }
            if (row[++index] != null) data.setTotalProductNum(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setTotalAmount(Integer.parseInt(row[index].toString()));
            datas.add(data);
        }
        return datas;
    }

    public int countByShop(AdmProductOrderSearch search) {
        List<AdmProductOrderDto.listByShop> datas = new ArrayList<AdmProductOrderDto.listByShop>();

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(DISTINCT(s.idx)) \n");
        sql.append("FROM product_order po \n");
        sql.append("LEFT JOIN product_order_group pog ON pog.idx = po.group_id \n");
        sql.append("LEFT JOIN product p ON p.idx = po.product_id \n");
        sql.append("LEFT JOIN user u ON pog.user_uid = u.uid \n");
        sql.append("LEFT JOIN shop s ON s.idx = pog.shop_id \n");
        sql.append("WHERE pog.payment_status = 1 AND po.order_status > -1 \n");
        sql.append("    AND pog.user_uid IS NOT NULL\n");
        if (StringUtils.hasText(search.getSearchType()) && StringUtils.hasText(search.getSearchValue())) {
            if (search.getSearchType().equals("productName")) sql.append("    AND po.product_name LIKE CONCAT('%', :searchValue, '%') \n");
            if (search.getSearchType().equals("actualName")) sql.append("    AND u.actual_name LIKE CONCAT('%', :searchValue, '%') \n");
            if (search.getSearchType().equals("shopName")) sql.append("    AND s.name LIKE CONCAT('%', :searchValue, '%') \n");
        }

        if (StringUtils.hasText(search.getStartDate()) && StringUtils.hasText(search.getEndDate())) {
            sql.append("    AND po.order_date BETWEEN :startDate AND :endDate \n");
        }
        if (StringUtils.hasText(search.getOrderType())) {
            sql.append("    AND pog.order_type = :orderType \n");
        }

        Query query = entityManager.createNativeQuery(sql.toString());
        if (StringUtils.hasText(search.getSearchType()) && StringUtils.hasText(search.getSearchValue())) query.setParameter("searchValue", search.getSearchValue());
        if (StringUtils.hasText(search.getStartDate()) && StringUtils.hasText(search.getEndDate())) {
            query.setParameter("startDate", search.getStartDate());
            query.setParameter("endDate", search.getEndDate());
        }
        if (search.getOrderType()!=null) {
            query.setParameter("orderType", search.getOrderType());   
        }

        return ((BigInteger) query.getSingleResult()).intValue();
    }

    public List<AdmProductOrderDto.detailByShop> detailByShop(Integer idx, AdmProductOrderSearch search, ProductOrderType type) {
        List<AdmProductOrderDto.detailByShop> datas = new ArrayList<AdmProductOrderDto.detailByShop>();

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT p.idx, \n");
        sql.append("    p.name, \n");
        sql.append("    SUM(po.product_num), \n");
        sql.append("    SUM(po.amount), \n");
        sql.append("    SUM( \n");
        sql.append("        case \n");

        if(type == ProductOrderType.PICKUP) sql.append("when (po.receive_status = 0) then po.product_num \n");
        else if(type == ProductOrderType.STATION) sql.append("when (po.pickup_status = 0) then po.product_num \n");

        sql.append("            else 0 \n");
        sql.append("        end \n");
        sql.append("    ) as hold_count, \n");
        sql.append("    SUM( \n");
        sql.append("        case \n");

        if(type == ProductOrderType.PICKUP) sql.append("when (po.receive_status = 1) then po.product_num \n");
        else if(type == ProductOrderType.STATION) sql.append("when (po.pickup_status = 1) then po.product_num \n");

        sql.append("            else 0 \n");
        sql.append("        end \n");
        sql.append("    ) as delivery_count, \n");
        sql.append("    SUM( \n");
        sql.append("        case \n");
        
        if(type == ProductOrderType.PICKUP) sql.append("when (po.receive_status = 2) then po.product_num \n");
        else if(type == ProductOrderType.STATION) sql.append("when (po.pickup_status = 2) then po.product_num \n");

        sql.append("            else 0 \n");
        sql.append("        end \n");
        sql.append("    ) as done_count \n");
        sql.append("FROM product_order po \n");
        sql.append("LEFT JOIN product_order_group pog ON pog.idx = po.group_id \n");
        sql.append("LEFT JOIN product p ON p.idx = po.product_id \n");
        if(type == ProductOrderType.PICKUP){
            sql.append("LEFT JOIN shop s ON s.idx = pog.shop_id \n");
        } else if(type == ProductOrderType.STATION){
            sql.append("LEFT JOIN station s ON s.idx = pog.station_id \n");
        }

        sql.append("LEFT JOIN user u ON pog.user_uid = u.uid \n");
        if(type == ProductOrderType.PICKUP){
            sql.append("WHERE pog.payment_status = 1 AND pog.shop_id = :shopId And po.order_status != -1 \n");
        } else if(type == ProductOrderType.STATION){
            sql.append("WHERE pog.payment_status = 1 AND pog.station_id = :stationId And po.order_status != -1 \n");
        }
        if (StringUtils.hasText(search.getSearchType()) && StringUtils.hasText(search.getSearchValue())) {
            if (search.getSearchType().equals("productName")) sql.append("    AND po.product_name LIKE CONCAT('%', :searchValue, '%') \n");
            if (search.getSearchType().equals("actualName")) sql.append("    AND u.actual_name LIKE CONCAT('%', :searchValue, '%') \n");
            if (search.getSearchType().equals("shopName")) sql.append("    AND s.name LIKE CONCAT('%', :searchValue, '%') \n");
        }

        if (StringUtils.hasText(search.getStartDate()) && StringUtils.hasText(search.getEndDate())) {
            sql.append("    AND po.order_date BETWEEN :startDate AND :endDate \n");
        }
        if (StringUtils.hasText(search.getOrderType())) {
            sql.append("    AND pog.order_type = :orderType \n");
        }

        sql.append("GROUP BY s.idx, p.idx \n");

        Query query = entityManager.createNativeQuery(sql.toString());

        if(type == ProductOrderType.PICKUP){
            query.setParameter("shopId", idx);
        } else if(type == ProductOrderType.STATION){
            query.setParameter("stationId", idx);
        }
        if (StringUtils.hasText(search.getSearchType()) && StringUtils.hasText(search.getSearchValue())) query.setParameter("searchValue", search.getSearchValue());
        if (StringUtils.hasText(search.getStartDate()) && StringUtils.hasText(search.getEndDate())) {
            query.setParameter("startDate", search.getStartDate());
            query.setParameter("endDate", search.getEndDate());
        }
        if (search.getOrderType()!=null) {
            query.setParameter("orderType", search.getOrderType());   
        }

        List<Object[]> resultList = query.getResultList();
        for (Object[] row : resultList) {
            int index = 0;
            AdmProductOrderDto.detailByShop data = new AdmProductOrderDto.detailByShop();
            data.setProductId(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setProductName(row[index].toString());
            if (row[++index] != null) data.setTotalProductNum(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setTotalAmount(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setTotalHoldCount(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setTotalDeliveryCount(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setTotalDoneCount(Integer.parseInt(row[index].toString()));
            datas.add(data);
        }
        return datas;
    }

    public List<Integer> getProductOrderIdxList(List<Integer> idList, AdmProductOrderSearch search) {
        List<Integer> idxList = new ArrayList<Integer>();

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT po.idx, po.receive_status \n");
        sql.append("FROM product_order po \n");
        sql.append("LEFT JOIN product_order_group pog ON pog.idx = po.group_id \n");
        sql.append("LEFT JOIN product p ON p.idx = po.product_id \n");
        
        if(search.getOrderType().equals(ProductOrderType.PICKUP.toString())) sql.append("LEFT JOIN shop s ON s.idx = pog.shop_id \n");
        else if(search.getOrderType().equals(ProductOrderType.STATION.toString())) sql.append("LEFT JOIN station s ON s.idx = pog.station_id \n");

        sql.append("LEFT JOIN user u ON pog.user_uid = u.uid \n");
        
        if(search.getOrderType().equals(ProductOrderType.PICKUP.toString())) sql.append("WHERE pog.payment_status = 1 AND po.order_status > -1 AND pog.shop_id IN :shopIdList \n");
        else if(search.getOrderType().equals(ProductOrderType.STATION.toString())) sql.append("WHERE pog.payment_status = 1 AND po.order_status > -1 AND pog.station_id IN :stationIdList \n");
        sql.append("    AND pog.user_uid IS NOT NULL\n");

        if (StringUtils.hasText(search.getSearchType()) && StringUtils.hasText(search.getSearchValue())) {
            if (search.getSearchType().equals("productName")) sql.append("    AND po.product_name LIKE CONCAT('%', :searchValue, '%') \n");
            if (search.getSearchType().equals("actualName")) sql.append("    AND u.actual_name LIKE CONCAT('%', :searchValue, '%') \n");
            if (search.getSearchType().equals("shopName")) sql.append("    AND s.name LIKE CONCAT('%', :searchValue, '%') \n");
        }

        if (StringUtils.hasText(search.getStartDate()) && StringUtils.hasText(search.getEndDate())) {
            sql.append("    AND po.order_date BETWEEN :startDate AND :endDate \n");
        }
        if (StringUtils.hasText(search.getOrderType())) {
            sql.append("    AND pog.order_type = :orderType \n");
        }

        Query query = entityManager.createNativeQuery(sql.toString());

        if(search.getOrderType().equals(ProductOrderType.PICKUP.toString())) query.setParameter("shopIdList", idList);
        else if(search.getOrderType().equals(ProductOrderType.STATION.toString())) query.setParameter("stationIdList", idList);

        if (StringUtils.hasText(search.getSearchType()) && StringUtils.hasText(search.getSearchValue())) query.setParameter("searchValue", search.getSearchValue());
        if (StringUtils.hasText(search.getStartDate()) && StringUtils.hasText(search.getEndDate())) {
            query.setParameter("startDate", search.getStartDate());
            query.setParameter("endDate", search.getEndDate());
        }
        if (search.getOrderType()!=null) {
            query.setParameter("orderType", search.getOrderType());   
        }

        List<Object[]> resultList = query.getResultList();
        for (Object[] row : resultList) {
            idxList.add(Integer.parseInt(row[0].toString()));
        }
        return idxList;
    }

    public List<AdmProductOrderDto.listForFCM> selectProductOrderListForFcm(List<Integer> productIdxList, ProductOrderType type) {
        StringBuffer sql = new StringBuffer();
        sql.append("select po.group_id, po.week_num, po.day_num, pog.user_uid, s.name, po.pickup_time \n");
        sql.append("from product_order po \n");
        sql.append("LEFT JOIN product_order_group pog on po.group_id = pog.idx \n");
        
        if(type == ProductOrderType.PICKUP) sql.append("LEFT JOIN shop s on pog.shop_id = s.idx \n");
        else if(type == ProductOrderType.STATION) sql.append("LEFT JOIN station s on pog.station_id = s.idx \n");

        if(type == ProductOrderType.PICKUP){
            sql.append("WHERE po.pickup_status != 1 AND po.idx IN :productIdxList \n");
        } else if(type == ProductOrderType.STATION){
            sql.append("WHERE po.idx IN :productIdxList \n");
        }
        sql.append("group by po.group_id, po.week_num, po.day_num, pog.user_uid, s.name, po.pickup_time \n");

        Query query = entityManager.createNativeQuery(sql.toString());

        query.setParameter("productIdxList", productIdxList);
       
        List<AdmProductOrderDto.listForFCM> datas = new ArrayList<AdmProductOrderDto.listForFCM>();
        List<Object[]> resultList = query.getResultList();
        for (Object[] row : resultList) {
            int index = 0;
            AdmProductOrderDto.listForFCM data = new AdmProductOrderDto.listForFCM();
            if (row[index] != null) data.setGroupId(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setWeekNum(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setDayNum(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setUserUid(row[index].toString());
            if (row[++index] != null) data.setName(row[index].toString());
            if (row[++index] != null) data.setPickupTime(row[index].toString());
            datas.add(data);
        }
        return datas;
    }
    /*
    public List<AdmProductOrderDto.list> list(Pageable pageable) {
        List<AdmProductOrderDto.list> datas = new ArrayList<AdmProductOrderDto.list>();

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT po.idx, \n");
        sql.append("    u.actual_name, \n");
        sql.append("    s.name as shop_name, \n");
        sql.append("    po.amount, \n");
        sql.append("    po.product_num, \n");
        sql.append("    date_format( \n");
        sql.append("        date_add( \n");
        sql.append("            ADDDATE( \n");
        sql.append("                pog.create_date, \n");
        sql.append("                -WEEKDAY(pog.create_date) + (po.week_num - 1) \n");
        sql.append("            ), \n");
        sql.append("            INTERVAL po.week_num week \n");
        sql.append("        ) \n");
        sql.append("    , '%Y-%m-%d') as pickup_date, \n");
        sql.append("    po.week_num, \n");
        sql.append("    u.concat_number, \n");
        sql.append("    po.pickup_status, \n");
        sql.append("    pog.create_date, \n");
        sql.append("    po.product_name \n");
        sql.append("FROM product_order po \n");
        sql.append("LEFT JOIN product_order_group pog ON pog.idx = po.group_id \n");
        sql.append("LEFT JOIN shop s ON pog.shop_id = s.idx \n");
        sql.append("LEFT JOIN user u ON u.uid = pog.user_uid \n");
        sql.append("ORDER BY po.idx DESC \n");
        sql.append("LIMIT :startLimit, :endLimit");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("startLimit", pageable.getPageNumber() * pageable.getPageSize());
        query.setParameter("endLimit", pageable.getPageSize());

        List<Object[]> resultList = query.getResultList();
        for (Object[] row : resultList) {
            int index = 0;
            AdmProductOrderDto.list data = new AdmProductOrderDto.list();
            data.setIdx(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setActualName(row[index].toString());
            if (row[++index] != null) data.setShopName(row[index].toString());
            if (row[++index] != null) data.setAmount(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setTotalProductNum(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setPickupDate(row[index].toString());
            if (row[++index] != null) data.setWeekNum(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setPhone(row[index].toString());
            if (row[++index] != null) data.setPickupStatus(row[index].toString().equals("1"));
            if (row[++index] != null) data.setOrderDate(row[index].toString());
            if (row[++index] != null) data.setProductName(row[index].toString());
            datas.add(data);
        }

        return datas;
    }

    public int count(Pageable pageable) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(po.idx) \n");
        sql.append("FROM product_order po \n");
        sql.append("LEFT JOIN product_order_group pog ON pog.idx = po.group_id \n");
        sql.append("LEFT JOIN shop s ON pog.shop_id = s.idx \n");
        sql.append("LEFT JOIN user u ON u.uid = pog.user_uid \n");
        sql.append("ORDER BY po.idx DESC \n");

        Query query = entityManager.createNativeQuery(sql.toString());

        return ((BigInteger) query.getSingleResult()).intValue();
    }
    */
}
