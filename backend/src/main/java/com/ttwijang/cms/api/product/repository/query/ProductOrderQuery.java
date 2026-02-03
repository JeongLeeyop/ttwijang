package com.ttwijang.cms.api.product.repository.query;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;

import com.ttwijang.cms.api.product.dto.ProductOrderDto;
import com.ttwijang.cms.api.product.dto.ShopAdmProductOrderDto;

@Component
public class ProductOrderQuery {
    @PersistenceContext
    private EntityManager entityManager;

    public int getTotalPrice(Map<Long, ProductOrderDto.orderChildProduct> params) {
        StringBuffer sql = new StringBuffer();
        
        sql.append("SELECT \n");
        sql.append("    SUM( \n");
        sql.append("        case \n");
        for( Map.Entry<Long, ProductOrderDto.orderChildProduct> entry : params.entrySet() ){
            sql.append("when (p.idx = ");
            sql.append(entry.getKey());
            sql.append(") then p.price * ");
            sql.append(entry.getValue().getCount());
            sql.append("\n");
        }
        sql.append("            else 0 \n");
        sql.append("        end \n");
        sql.append("    ) \n");
        sql.append("FROM product p \n");
        sql.append("WHERE p.idx IN :idxList \n");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("idxList", params.keySet());

        Object result = query.getSingleResult();
        if (result == null) {
            return 0;
        }
        return Integer.parseInt(result.toString());
    }

    public ProductOrderDto.lastOrder getLastOrder(Integer orderGroupIdx) {
        ProductOrderDto.lastOrder data = new ProductOrderDto.lastOrder();

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT \n");
        sql.append("    date_format( \n");
        sql.append("        date_add( \n");
        sql.append("            ADDDATE( \n");
        sql.append("                pog.create_date, \n");
        sql.append("                -WEEKDAY(pog.create_date) + (po.week_num - 1) \n");
        sql.append("            ), \n");
        sql.append("            INTERVAL po.week_num week \n");
        sql.append("        ) \n");
        sql.append("    , '%Y-%m-%d') as pickup_date, \n");
        sql.append("    po.pickup_status \n");
        sql.append("FROM product_order po \n");
        sql.append("LEFT JOIN product_order_group pog ON po.group_id = pog.idx \n");
        sql.append("WHERE pog.idx = :orderGroupIdx \n");
        sql.append("ORDER BY pickup_date DESC \n");
        sql.append("LIMIT 1 \n");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("orderGroupIdx", orderGroupIdx);

        List<Object[]> resultList = query.getResultList();
        for (Object[] row : resultList) {
            int index = 0;
            if (row[index] != null) data.setPickupDate(LocalDate.parse(row[index].toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            if (row[++index] != null) data.setPickupStatus(Integer.parseInt(row[index].toString()) == 1);
        }
        return data;
    }

        public ProductOrderDto.orderStatus getOrderStatus(Integer orderGroupIdx) {
        ProductOrderDto.orderStatus data = new ProductOrderDto.orderStatus();

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT \n");
        sql.append("(select count(*) from product_order where group_id = :orderGroupIdx and order_status != -1) as totalCnt,\n");
        sql.append("(select count(*) from product_order where group_id = :orderGroupIdx and pickup_status = 0 and order_status != -1) as pickupCnt, \n");
        sql.append("(select count(*) from product_order where group_id = :orderGroupIdx and receive_status = 0 and order_status != -1) as receiveCnt \n");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("orderGroupIdx", orderGroupIdx);

        List<Object[]> resultList = query.getResultList();
        for (Object[] row : resultList) {
            int index = 0;
            if (row[index] != null) data.setTotalCnt(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setPickupCnt(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setReceiveCnt(Integer.parseInt(row[index].toString()));
        }
        return data;
    }
        
    public ProductOrderDto.orderStatus getOrderStatus2(Integer orderGroupIdx) {
        ProductOrderDto.orderStatus data = new ProductOrderDto.orderStatus();

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT \n");
        sql.append("(select count(*) from product_order where group_id = :orderGroupIdx and order_status != -1) as totalCnt,\n");
        sql.append("(select count(*) from product_order where group_id = :orderGroupIdx and pickup_status = 0 and order_status != -1) as deliveryReadyCnt, \n");
        sql.append("(select count(*) from product_order where group_id = :orderGroupIdx and pickup_status = 1 and order_status != -1) as deliveryStartCnt, \n");
        sql.append("(select count(*) from product_order where group_id = :orderGroupIdx and pickup_status = 2 and order_status != -1) as deliveryCompleteCnt \n");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("orderGroupIdx", orderGroupIdx);

        List<Object[]> resultList = query.getResultList();
        for (Object[] row : resultList) {
            int index = 0;
            if (row[index] != null) data.setTotalCnt(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setDeliveryReadyCnt(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setDeliveryStartCnt(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setDeliveryCompleteCnt(Integer.parseInt(row[index].toString()));
        }
        return data;
    }

  
   public List<ProductOrderDto.dayCount> getOrderCountList(String startDate, String endDate, List<Integer> dayNumList, Integer shopIdx){

    List<ProductOrderDto.dayCount> datas = new ArrayList<ProductOrderDto.dayCount>();
    StringBuffer sql = new StringBuffer();
            sql.append("select\r\n");
            sql.append("        day_num as dayNum, sum(a.product_num) as count\r\n");
            sql.append("    from\r\n");
            sql.append("        product_order a\r\n");
            sql.append("    join\r\n");
            sql.append("        product_order_group b\r\n");
            sql.append("            on a.group_id = b.idx\r\n");
            sql.append("    where\r\n");
            sql.append("        a.receive_status != 2\r\n"); // 배달완료가 아님
            sql.append("        and a.pickup_status = 0\r\n"); // 픽업대기상태
            sql.append("        and a.payment_status = 1\r\n"); // 결제 완료
            sql.append("        and a.order_status not in (-1, 3)\r\n"); // 부분환불, 배달완료가 아닌 상태
            sql.append("        and a.start_date = :startDate and a.end_date = :endDate \r\n");
            sql.append("        and a.day_num IN :dayNumList \r\n");
            sql.append("        and b.shop_id = :shopIdx group by day_num");

    
        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setParameter("dayNumList", dayNumList);
        query.setParameter("shopIdx", shopIdx);

        List<Object[]> resultList = query.getResultList();

        ProductOrderDto.dayCount data = new ProductOrderDto.dayCount();
        
        for (Object[] row : resultList) {
            int index = 0;
            if (row[index] != null) data.setDayNum(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setCount(Integer.parseInt(row[index].toString()));
            datas.add(data);
        }

        return datas;
    }    


    public List<ProductOrderDto.groupOrderCount> getGroupOrderCount(Integer groupId){

    List<ProductOrderDto.groupOrderCount> datas = new ArrayList<ProductOrderDto.groupOrderCount>();
    StringBuffer sql = new StringBuffer();
        sql.append("select");
        sql.append(" b.start_date as startDate, b.end_date as endDate, b.week_num as weekNum, b.day_num as dayNum, SUM(b.product_num) as count");
        sql.append(" from "); // "from" 키워드 뒤에 공백 추가
        sql.append(" product_order b ");
        sql.append(" where b.group_id = :groupId");
        sql.append(" group by b.start_date, b.end_date, b.week_num, b.day_num");
    
        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("groupId", groupId);

        List<Object[]> resultList = query.getResultList();

        ProductOrderDto.groupOrderCount data = new ProductOrderDto.groupOrderCount();
        
        for (Object[] row : resultList) {
            int index = 0;
            if (row[index] != null) data.setStartDate(row[index].toString());
            if (row[++index] != null) data.setEndDate(row[index].toString());
            if (row[++index] != null) data.setWeekNum(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setDayNum(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setCount(Integer.parseInt(row[index].toString()));
            datas.add(data);
        }

        return datas;
    }    

}
