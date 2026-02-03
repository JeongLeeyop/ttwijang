package com.ttwijang.cms.api.diary.repository.query;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;

import com.ttwijang.cms.api.diary.dto.DiaryDto;
import com.ttwijang.cms.api.diary.dto.DiarySearch;
import com.ttwijang.cms.api.diary.dto.DiaryStatisticsSearch;

@Component
public class DiaryMealQuery {
    @PersistenceContext
    private EntityManager entityManager;

    public DiaryDto.detail detail(String userUid, DiarySearch search) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT dm.diary_date, \n "); // 0
        sql.append("    dm.idx, \n "); // 1
        sql.append("    dm.type, \n "); // 2
        sql.append("    dm.menu_name, \n "); // 3
        sql.append("    dm.amount, \n "); // 4
        sql.append("    dm.amount_unit, \n "); // 5
        sql.append("    dm.calorie, \n "); // 6
        sql.append("    dm.carbohydrate, \n "); // 7
        sql.append("    dm.protein, \n "); // 8
        sql.append("    dm.fat, \n "); // 9
        sql.append("    dm.sodium, \n "); // 10
        sql.append("    dm.sugar, \n "); // 11
        sql.append("    dm.saturated_fatty_acids, \n "); // 12
        sql.append("    dm.vitamin_c, \n "); // 13
        sql.append("    dm.trans_fat, \n "); // 14
        sql.append("    dm.cholesterol, \n "); // 15
        sql.append("    dm.calcium, \n "); // 16
        sql.append("    dm.iron, \n "); // 17
        sql.append("    dm.potassium, \n "); // 18
        sql.append("    dm.dietary_fiber \n "); // 19
        sql.append("FROM diary_meal dm \n ");
        sql.append("WHERE dm.user_uid = :userUid AND dm.diary_date = :searchDate \n ");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("userUid", userUid);
        query.setParameter("searchDate", search.getSearchDate());

        DiaryDto.detail dto = new DiaryDto.detail();

        List<Object[]> resultList = query.getResultList();
        for (Object[] row : resultList) {
            int index = 0;
            DiaryDto.mealItemDetail item = new DiaryDto.mealItemDetail();
            item.setIdx(Long.parseLong(row[++index].toString())); // 1
            if (row[++index] != null) item.setType(Integer.parseInt(row[index].toString())); // 2
            if (row[++index] != null) item.setMenuName(row[index].toString()); // 3
            if (row[++index] != null) item.setAmount(Float.parseFloat(row[index].toString())); // 4
            if (row[++index] != null) item.setAmountUnit(row[index].toString()); // 5
            if (row[++index] != null) item.setCalorie(Float.parseFloat(row[index].toString())); // 6
            if (row[++index] != null) item.setCarbohydrate(Float.parseFloat(row[index].toString())); // 7
            if (row[++index] != null) item.setProtein(Float.parseFloat(row[index].toString())); // 8
            if (row[++index] != null) item.setFat(Float.parseFloat(row[index].toString())); // 9
            if (row[++index] != null) item.setSodium(Float.parseFloat(row[index].toString())); // 10
            if (row[++index] != null) item.setSugar(Float.parseFloat(row[index].toString())); // 11
            if (row[++index] != null) item.setSaturatedFattyAcids(Float.parseFloat(row[index].toString())); // 12
            if (row[++index] != null) item.setVitaminC(Float.parseFloat(row[index].toString())); // 13
            if (row[++index] != null) item.setTransFat(Float.parseFloat(row[index].toString())); // 14
            if (row[++index] != null) item.setCholesterol(Float.parseFloat(row[index].toString())); // 15
            if (row[++index] != null) item.setCalcium(Float.parseFloat(row[index].toString())); // 16
            if (row[++index] != null) item.setIron(Float.parseFloat(row[index].toString())); // 17
            if (row[++index] != null) item.setPotassium(Float.parseFloat(row[index].toString())); // 18
            if (row[++index] != null) item.setDietaryFiber(Float.parseFloat(row[index].toString())); // 19

            if (item.getType() == 1) {
                dto.getBreakfast().setTotalCalorie(dto.getBreakfast().getTotalCalorie() + item.getCalorie());
                dto.getBreakfast().getMealList().add(item);
            }
            if (item.getType() == 2) {
                dto.getLunch().setTotalCalorie(dto.getLunch().getTotalCalorie() + item.getCalorie());
                dto.getLunch().getMealList().add(item);
            }
            if (item.getType() == 3) {
                dto.getDinner().setTotalCalorie(dto.getDinner().getTotalCalorie() + item.getCalorie());
                dto.getDinner().getMealList().add(item);
            }
        }

        return dto;
    }

    public DiaryDto.mealStatistics mealStatistics(String userUid, DiaryStatisticsSearch search) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT \n ");
        sql.append("    SUM(dm.amount), \n "); // 0
        sql.append("    SUM(dm.calorie), \n "); // 1
        sql.append("    SUM(dm.carbohydrate), \n "); // 2
        sql.append("    SUM(dm.protein), \n "); // 3
        sql.append("    SUM(dm.fat), \n "); // 4
        sql.append("    SUM(dm.sodium), \n "); // 5
        sql.append("    SUM(dm.sugar), \n "); // 6
        sql.append("    SUM(dm.saturated_fatty_acids), \n "); // 7
        sql.append("    SUM(dm.vitamin_c), \n "); // 8
        sql.append("    SUM(dm.trans_fat), \n "); // 9
        sql.append("    SUM(dm.cholesterol), \n "); // 10
        sql.append("    SUM(dm.calcium), \n "); // 11
        sql.append("    SUM(dm.iron), \n "); // 12
        sql.append("    SUM(dm.potassium), \n "); // 13
        sql.append("    SUM(dm.dietary_fiber) \n "); // 14
        sql.append("FROM diary_meal dm \n ");
        sql.append("WHERE dm.user_uid = :userUid AND dm.diary_date = :searchDate \n ");
        if (search.getType() > 0) sql.append("  AND dm.type = :type \n");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("userUid", userUid);
        query.setParameter("searchDate", search.getDiaryDate());
        if (search.getType() > 0) query.setParameter("type", search.getType());

        DiaryDto.mealStatistics dto = new DiaryDto.mealStatistics();
        dto.setDiaryDate(search.getDiaryDate());

        List<Object[]> resultList = query.getResultList();
        for (Object[] row : resultList) {
            int index = 0;
            if (row[index] != null) dto.setAmount(Float.parseFloat(row[index].toString())); // 0
            if (row[++index] != null) dto.setCalorie(Float.parseFloat(row[index].toString())); // 1
            if (row[++index] != null) dto.setCarbohydrate(Float.parseFloat(row[index].toString())); // 2
            if (row[++index] != null) dto.setProtein(Float.parseFloat(row[index].toString())); // 3
            if (row[++index] != null) dto.setFat(Float.parseFloat(row[index].toString())); // 4
            if (row[++index] != null) dto.setSodium(Float.parseFloat(row[index].toString())); // 5
            if (row[++index] != null) dto.setSugar(Float.parseFloat(row[index].toString())); // 6
            if (row[++index] != null) dto.setSaturatedFattyAcids(Float.parseFloat(row[index].toString())); // 7
            if (row[++index] != null) dto.setVitaminC(Float.parseFloat(row[index].toString())); // 8
            if (row[++index] != null) dto.setTransFat(Float.parseFloat(row[index].toString())); // 9
            if (row[++index] != null) dto.setCholesterol(Float.parseFloat(row[index].toString())); // 10
            if (row[++index] != null) dto.setCalcium(Float.parseFloat(row[index].toString())); // 11
            if (row[++index] != null) dto.setIron(Float.parseFloat(row[index].toString())); // 12
            if (row[++index] != null) dto.setPotassium(Float.parseFloat(row[index].toString())); // 13
            if (row[++index] != null) dto.setDietaryFiber(Float.parseFloat(row[index].toString())); // 14
        }

        return dto;
    }

    public DiaryDto.mealStatistics setNutrientStandard(DiaryDto.mealStatistics dto, String userUid, DiaryStatisticsSearch search) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT \n ");
        sql.append("    case \n");
        sql.append("        when(u.gender = 0) then \n");
        sql.append("            66 + (13.7 * IFNULL(d.weight, u.weight)) + (5 * u.height) - (6.8 * IFNULL((SELECT YEAR(:searchDate) - LEFT(u.birth, 4)), 30)) \n");
        sql.append("            * case \n");
        sql.append("                when(u.activity_level = 2) then 1.11 \n");
        sql.append("                when(u.activity_level = 3) then 1.25 \n");
        sql.append("                when(u.activity_level = 4) then 1.48 \n");
        sql.append("                else 1 \n");
        sql.append("            end \n");
        sql.append("            * case \n");
        sql.append("                when(u.diet_purpose = '다이어트') then 0.8 \n");
        sql.append("                when(u.diet_purpose = '체중증량') then 1.2 \n");
        sql.append("                else 1 \n");
        sql.append("            end \n");
        sql.append("        when(u.gender = 1) then 655 + (9.6 * IFNULL(d.weight, u.weight)) + (1.7 * u.height) - (4.7 * IFNULL((SELECT YEAR(:searchDate) - LEFT(u.birth, 4)), 30)) \n");
        sql.append("            * case \n");
        sql.append("                when(u.activity_level = 2) then 1.12 \n");
        sql.append("                when(u.activity_level = 3) then 1.27 \n");
        sql.append("                when(u.activity_level = 4) then 1.45 \n");
        sql.append("                else 1 \n");
        sql.append("            end \n");
        sql.append("            * case \n");
        sql.append("                when(u.diet_purpose = '다이어트') then 0.8 \n");
        sql.append("                when(u.diet_purpose = '체중증량') then 1.2 \n");
        sql.append("                else 1 \n");
        sql.append("            end \n");
        sql.append("        else ns.calorie \n");
        sql.append("    end as standard_calorie, \n"); // 0
        //sql.append("   ns.carbohydrate, \n "); // 1
        //sql.append("   ns.protein, \n "); // 2
        //sql.append("   ns.fat, \n "); // 3
        sql.append("   ns.sodium, \n "); // 4
        sql.append("   ns.sugar, \n "); // 5
        sql.append("   ns.saturated_fatty_acids, \n "); // 6
        sql.append("   ns.vitamin_c, \n "); // 7
        sql.append("   ns.trans_fat, \n "); // 8
        sql.append("   ns.cholesterol, \n "); // 9
        sql.append("   ns.calcium, \n "); // 10
        sql.append("   ns.iron, \n "); // 11
        sql.append("   ns.potassium, \n "); // 12
        sql.append("   ns.dietary_fiber \n "); // 13
        sql.append("FROM nutrient_standard ns \n ");
        sql.append("LEFT JOIN user u ON \n");
        sql.append("ns.gender = u.gender AND \n");
        sql.append("IFNULL((SELECT YEAR(:searchDate) - LEFT(u.birth, 4)), 30) BETWEEN ns.age_start AND ns.age_end \n");
        sql.append("LEFT JOIN diary d ON d.idx = (SELECT idx FROM diary d WHERE user_uid = :userUid AND diary_date >= :searchDate ORDER BY idx LIMIT 1) \n");
        sql.append("WHERE u.uid = :userUid");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("userUid", userUid);
        query.setParameter("searchDate", search.getDiaryDate());

        List<Object[]> resultList = query.getResultList();
        for (Object[] row : resultList) {
            int index = 0;
            if (row[index] != null) dto.setStandardCalorie(Float.parseFloat(row[index].toString())); // 1
            dto.setStandardCarbohydrate(Math.round(dto.getStandardCalorie() * 0.5 / 4) * 1f); // 2
            dto.setStandardProtein(Math.round(dto.getStandardCalorie() * 0.3 / 4) * 1f); // 3
            dto.setStandardFat(Math.round(dto.getStandardCalorie() * 0.2 / 9) * 1f); // 4
            // if (row[++index] != null) dto.setStandardCarbohydrate(Float.parseFloat(row[index].toString())); // 2
            // if (row[++index] != null) dto.setStandardProtein(Float.parseFloat(row[index].toString())); // 3
            // if (row[++index] != null) dto.setStandardFat(Float.parseFloat(row[index].toString())); // 4
            if (row[++index] != null) dto.setStandardSodium(Float.parseFloat(row[index].toString())); // 5
            if (row[++index] != null) dto.setStandardSugar(Float.parseFloat(row[index].toString())); // 6
            if (row[++index] != null) dto.setStandardSaturatedFattyAcids(Float.parseFloat(row[index].toString())); // 7
            if (row[++index] != null) dto.setStandardVitaminC(Float.parseFloat(row[index].toString())); // 8
            if (row[++index] != null) dto.setStandardTransFat(Float.parseFloat(row[index].toString())); // 9
            if (row[++index] != null) dto.setStandardCholesterol(Float.parseFloat(row[index].toString())); // 10
            if (row[++index] != null) dto.setStandardCalcium(Float.parseFloat(row[index].toString())); // 11
            if (row[++index] != null) dto.setStandardIron(Float.parseFloat(row[index].toString())); // 12
            if (row[++index] != null) dto.setStandardPotassium(Float.parseFloat(row[index].toString())); // 13
            if (row[++index] != null) dto.setStandardDietaryFiber(Float.parseFloat(row[index].toString())); // 14
        }

        return dto;
    }
}
