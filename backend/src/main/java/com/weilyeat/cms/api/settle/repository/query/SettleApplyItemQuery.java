package com.weilyeat.cms.api.settle.repository.query;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;

import com.weilyeat.cms.api.settle.dto.AdmSettleApplyDto;
import com.weilyeat.cms.api.settle.dto.ShopAdmSettleApplyDto;

@Component
public class SettleApplyItemQuery {
    @PersistenceContext
    private EntityManager entityManager;
    
    public List<AdmSettleApplyDto.item> list(Integer applyIdx) {
        List<AdmSettleApplyDto.item> datas = new ArrayList<AdmSettleApplyDto.item>();

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT p.name, \n");
        sql.append("    po.amount, \n");
        sql.append("    po.product_num, \n");
        sql.append("    po.week_num, \n");
        sql.append("    po.day_num, \n");
        sql.append("date_format(pog.create_date, '%Y-%m-%d') as order_date, \n");
        sql.append("date_format( \n");
        sql.append("    date_add( \n");
        sql.append("        ADDDATE( \n");
        sql.append("            pog.create_date, \n");
        sql.append("            -WEEKDAY(pog.create_date) + (po.week_num - 1) \n");
        sql.append("        ), \n");
        sql.append("        INTERVAL po.week_num week \n");
        sql.append("    ) \n");
        sql.append(", '%Y-%m-%d') as pickup_date \n");
        sql.append("FROM settle_apply_item sai \n");
        sql.append("LEFT JOIN product_order po ON po.idx = sai.order_idx \n");
        sql.append("LEFT JOIN product_order_group pog ON pog.idx = po.group_id \n");
        sql.append("LEFT JOIN product p ON p.idx = po.product_id \n");
        sql.append("WHERE sai.apply_idx = :applyIdx \n");
        sql.append("ORDER BY pog.create_date, po.week_num, po.day_num \n");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("applyIdx", applyIdx);

        List<Object[]> resultList = query.getResultList();
        for (Object[] row : resultList) {
            int index = 0;
            AdmSettleApplyDto.item data = new AdmSettleApplyDto.item();
            if (row[index] != null) data.setProductName(row[index].toString());
            if (row[++index] != null) data.setAmount(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setProductNum(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setWeekNum(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setDayNum(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setOrderDate(row[index].toString());
            if (row[++index] != null) data.setPickupDate(row[index].toString());
            datas.add(data);
        }
        return datas;
    }

    public List<ShopAdmSettleApplyDto.item> listByShopAdmin(Integer applyIdx) {
        List<ShopAdmSettleApplyDto.item> datas = new ArrayList<ShopAdmSettleApplyDto.item>();

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT p.name, \n");
        sql.append("    po.amount, \n");
        sql.append("    po.product_num, \n");
        sql.append("    po.week_num, \n");
        sql.append("    po.day_num, \n");
        sql.append("date_format(pog.create_date, '%Y-%m-%d') as order_date, \n");
        sql.append("date_format( \n");
        sql.append("    date_add( \n");
        sql.append("        ADDDATE( \n");
        sql.append("            pog.create_date, \n");
        sql.append("            -WEEKDAY(pog.create_date) + (po.week_num - 1) \n");
        sql.append("        ), \n");
        sql.append("        INTERVAL po.week_num week \n");
        sql.append("    ) \n");
        sql.append(", '%Y-%m-%d') as pickup_date \n");
        sql.append("FROM settle_apply_item sai \n");
        sql.append("LEFT JOIN product_order po ON po.idx = sai.order_idx \n");
        sql.append("LEFT JOIN product_order_group pog ON pog.idx = po.group_id \n");
        sql.append("LEFT JOIN product p ON p.idx = po.product_id \n");
        sql.append("WHERE sai.apply_idx = :applyIdx \n");
        sql.append("ORDER BY pog.create_date, po.week_num, po.day_num \n");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("applyIdx", applyIdx);

        List<Object[]> resultList = query.getResultList();
        for (Object[] row : resultList) {
            int index = 0;
            ShopAdmSettleApplyDto.item data = new ShopAdmSettleApplyDto.item();
            if (row[index] != null) data.setProductName(row[index].toString());
            if (row[++index] != null) data.setAmount(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setProductNum(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setWeekNum(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setDayNum(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setOrderDate(row[index].toString());
            if (row[++index] != null) data.setPickupDate(row[index].toString());
            datas.add(data);
        }
        return datas;
    }
}
