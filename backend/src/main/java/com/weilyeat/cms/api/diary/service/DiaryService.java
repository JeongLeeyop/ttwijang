package com.ttwijang.cms.api.diary.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.remoting.soap.SoapFaultException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ttwijang.cms.api.diary.dto.DiaryDto;
import com.ttwijang.cms.api.diary.dto.DiaryDto.mealItemAddByHistory;
import com.ttwijang.cms.api.diary.dto.DiarySearch;
import com.ttwijang.cms.api.diary.dto.DiaryStatisticsSearch;
import com.ttwijang.cms.api.diary.dto.mapper.DiaryMapper;
import com.ttwijang.cms.api.diary.dto.mapper.DiaryMealMapper;
import com.ttwijang.cms.api.diary.repository.DiaryMealRepository;
import com.ttwijang.cms.api.diary.repository.DiaryRepository;
import com.ttwijang.cms.api.diary.repository.query.DiaryMealQuery;
import com.ttwijang.cms.api.food.repository.FoodRepository;
import com.ttwijang.cms.api.point.repository.PointHistoryRepository;
import com.ttwijang.cms.api.point.service.PointHistoryService;
import com.ttwijang.cms.api.product.repository.ProductOrderRepository;
import com.ttwijang.cms.api.push_alarm.dto.PushAlarmDto;
import com.ttwijang.cms.api.push_alarm.service.PushAlarmService;
import com.ttwijang.cms.api.user.dto.UserFcmToken;
import com.ttwijang.cms.api.user.repository.UserFcmTokenRepository;
import com.ttwijang.cms.api.user.repository.UserRepository;
import com.ttwijang.cms.common.exception.BadRequestException;
import com.ttwijang.cms.common.exception.NotFoundException;
import com.ttwijang.cms.common.exception.code.BadRequest;
import com.ttwijang.cms.common.exception.code.NotFound;
import com.ttwijang.cms.entity.Diary;
import com.ttwijang.cms.entity.DiaryMeal;
import com.ttwijang.cms.entity.Food;
import com.ttwijang.cms.entity.Product;
import com.ttwijang.cms.entity.ProductOrder;
import com.ttwijang.cms.entity.User;
import com.ttwijang.cms.fcm.model.PushNotificationRequest;
import com.ttwijang.cms.fcm.service.PushNotificationService;
import com.ttwijang.cms.oauth.SinghaUser;

import lombok.AllArgsConstructor;

public interface DiaryService {
    DiaryDto.physicalConditionDetail physicalConditionDetail(SinghaUser authUser, DiarySearch search); // 체성분상태
    void savePhysicalCondition(SinghaUser authUser, DiaryDto.physicalConditionSave saveDto); // 체성분 상태 저장
    void savePurposePhysical(SinghaUser authUser, DiaryDto.purposePhysicalSave saveDto); // 목표 체성분 저장

    DiaryDto.detail detail(SinghaUser authUser, DiarySearch search); // 다이어리
    void addDiaryMeal(SinghaUser authUser, DiaryDto.mealItemAdd addDto); // 다이어리 음식 추가 -> 직접기록
    void deleteDiaryMeal(Long idx, SinghaUser authUser); // 다이어리 음식 제거

    DiaryDto.mealStatistics mealStatistics(SinghaUser authUser, DiaryStatisticsSearch search); // 체성분 기록
    void addDiaryMealByFood(SinghaUser authUser, DiaryDto.mealItemAddByFood addDto); // 다이어리 음식추가 -> 음식검색
    void addDiaryMealByHistory(SinghaUser authUser, DiaryDto.mealItemAddByHistory addDto); // 다이어리 음식추가 -> 음식검색
    void addDiaryMealByOrder(SinghaUser authUser, DiaryDto.mealItemAddByOrder addDto);
}

@Service
@AllArgsConstructor
class DiaryServiceImpl implements DiaryService {
    private final DiaryRepository diaryRepository;
    private final DiaryMealRepository diaryMealRepository;
    private final DiaryMealQuery diaryMealQuery;
    private final FoodRepository foodRepository;
    private final PointHistoryService pointHistoryService;
    private final PointHistoryRepository pointHistoryRepository;
    private final UserFcmTokenRepository userFcmTokenRepository;
    private final UserRepository userRepository;
    private final ProductOrderRepository orderRepository;
    
    @Autowired
    PushAlarmService pushAlarmService;
    
    @Autowired
    PushNotificationService pushNotificationService;

    @Override
    public DiaryDto.physicalConditionDetail physicalConditionDetail(SinghaUser authUser, DiarySearch search) {
        User user = authUser.getUser();
        LocalDate searchDate = LocalDate.parse(search.getSearchDate());
        Optional<Diary> optional = diaryRepository.findByUserUidAndDiaryDate(user.getUid(), searchDate);
        if (optional.isPresent()) {
            return DiaryMapper.INSTANCE.entityToPhysicalConditionDetail(optional.get());
        } else {
            Optional<Diary> optional2 = diaryRepository.findByUserUidOrderByDiaryDateDesc(user.getUid());
            if (optional2.isPresent()) {
                return DiaryMapper.INSTANCE.entityToPhysicalConditionDetail(optional2.get());
            }
        }
        Diary entity = new Diary();
        if (StringUtils.hasText(authUser.getUser().getWeight())) {
            entity.setWeight(Float.parseFloat(authUser.getUser().getWeight()));
        } else {
            entity.setWeight(0f);    
        }
        entity.setMuscleWeight(0f);
        entity.setFatRate(0f);
        return DiaryMapper.INSTANCE.entityToPhysicalConditionDetail(entity);
    }

    @Override
    public void savePhysicalCondition(SinghaUser authUser, DiaryDto.physicalConditionSave saveDto) {
        Diary entity = null;
        User user = authUser.getUser();
        Optional<Diary> optional = diaryRepository.findByUserUidAndDiaryDate(user.getUid(), saveDto.getDiaryDate());
        if (optional.isPresent()) {
            entity = DiaryMapper.INSTANCE.saveDtoToEntity(saveDto, optional.get());
        } else {
            entity = DiaryMapper.INSTANCE.saveDtoToEntity(saveDto);
            Optional<Diary> optional2 = diaryRepository.getLastPurpose(user.getUid());
            if (optional2.isPresent()) {
                Diary lastPurposeDiary = optional2.get();
                entity.setPurposeFatRate(lastPurposeDiary.getPurposeFatRate());
                entity.setPurposeMuscleWeight(lastPurposeDiary.getPurposeMuscleWeight());
                entity.setPurposeWeight(lastPurposeDiary.getPurposeWeight());
            }
        }
        entity.setUserUid(user.getUid());
        diaryRepository.save(entity);

        User userEntity = userRepository.findById(user.getUid()).orElseThrow(() -> new NotFoundException(NotFound.USER));
        userEntity.setWeight(saveDto.getWeight().toString());
        userRepository.save(userEntity);
    }

    @Override
    public void savePurposePhysical(SinghaUser authUser, DiaryDto.purposePhysicalSave saveDto) {
        Diary entity = null;
        User user = authUser.getUser();
        Optional<Diary> optional = diaryRepository.findByUserUidAndDiaryDate(user.getUid(), saveDto.getDiaryDate());
        if (optional.isPresent()) {
            entity = DiaryMapper.INSTANCE.purposeSaveDtoToEntity(saveDto, optional.get());
        } else {
            entity = DiaryMapper.INSTANCE.purposeSaveDtoToEntity(saveDto);
            Optional<Diary> optional2 = diaryRepository.getLastPhysicalCondition(user.getUid());
            if (optional2.isPresent()) {
                Diary lastPurposeDiary = optional2.get();
                entity.setFatRate(lastPurposeDiary.getFatRate());
                entity.setMuscleWeight(lastPurposeDiary.getMuscleWeight());
                entity.setWeight(lastPurposeDiary.getWeight());
            }
        }
        entity.setUserUid(user.getUid());
        diaryRepository.save(entity);
        
        User userEntity = userRepository.findById(user.getUid()).orElseThrow(() -> new NotFoundException(NotFound.USER));
        userEntity.setGoalDate(saveDto.getGoalDate());
        userEntity.setGoalWeight(saveDto.getPurposeWeight().toString());
        userRepository.save(userEntity);
    }

    @Override
    public DiaryDto.detail detail(SinghaUser authUser, DiarySearch search) {
        return diaryMealQuery.detail(authUser.getUser().getUid(), search);
    }

    @Transactional
    @Override
    public void addDiaryMeal(SinghaUser authUser, DiaryDto.mealItemAdd addDto) {
        if (addDto.getDiaryDate().equals(LocalDate.now().toString())) {
            if(savePoint(authUser.getUser(), addDto.getType())){
                sendPushAlarm(authUser.getUser(), addDto);
            }
        }

        DiaryMeal entity = DiaryMealMapper.INSTANCE.addDtoToEntity(addDto);
        entity.setUserUid(authUser.getUser().getUid());
        diaryMealRepository.save(entity);
    }

    @Override
    public void deleteDiaryMeal(Long idx, SinghaUser authUser) {
        DiaryMeal entity = diaryMealRepository.findById(idx).orElseThrow(() -> new NotFoundException(NotFound.DIARY_MEAL));
        if (!entity.getUserUid().equals(authUser.getUser().getUid())) throw new BadRequestException(BadRequest.NOT_MINE);
        diaryMealRepository.delete(entity);
    }

    @Override
    public DiaryDto.mealStatistics mealStatistics(SinghaUser authUser, DiaryStatisticsSearch search) {
        DiaryDto.mealStatistics dto = diaryMealQuery.mealStatistics(authUser.getUser().getUid(), search);
        dto = diaryMealQuery.setNutrientStandard(dto, authUser.getUser().getUid(), search);
        return dto;
    }

    @Transactional
    @Override
    public void addDiaryMealByFood(SinghaUser authUser, DiaryDto.mealItemAddByFood addDto) {
        if (addDto.getDiaryDate().equals(LocalDate.now().toString())) {
            if(savePoint(authUser.getUser(), addDto.getType())){
                sendPushAlarm(authUser.getUser(), addDto);
            }
        }

        DiaryMeal entity = DiaryMealMapper.INSTANCE.addDtoByFoodToEntity(addDto);
        entity.setUserUid(authUser.getUser().getUid());
        
        Food food = foodRepository.findById(addDto.getFoodIdx()).orElseThrow(() -> new NotFoundException(NotFound.FOOD));
        entity.setMenuName(food.getName());
        if (food.getWeightType() != null) entity.setAmountUnit(food.getWeightType());
        
        if (food.getKcal() != null) entity.setCalorie((float) (Math.round(food.getKcal() / food.getWeightOnce() * addDto.getWeight() * 100) / 100.0));
        if (food.getCarbohydrate() != null) entity.setCarbohydrate((float) (Math.round(food.getCarbohydrate() / food.getWeightOnce() * addDto.getWeight() * 100) / 100.0));
        if (food.getProtein() != null) entity.setProtein((float) (Math.round(food.getProtein() / food.getWeightOnce() * addDto.getWeight() * 100) / 100.0));
        if (food.getFat() != null) entity.setFat((float) (Math.round(food.getFat() / food.getWeightOnce() * addDto.getWeight() * 100) / 100.0));
        if (food.getNatrium() != null) entity.setSodium((float) (Math.round(food.getNatrium() / food.getWeightOnce() * addDto.getWeight() * 100) / 100.0));
        if (food.getSugar() != null) entity.setSugar((float) (Math.round(food.getSugar() / food.getWeightOnce() * addDto.getWeight() * 100) / 100.0));
        if (food.getStFattyAcid() != null) entity.setSaturatedFattyAcids((float) (Math.round(food.getStFattyAcid() / food.getWeightOnce() * addDto.getWeight() * 100) / 100.0));
        if (food.getVitaminC() != null) entity.setVitaminC((float) (Math.round(food.getVitaminC() / food.getWeightOnce() * addDto.getWeight() * 100) / 100.0));
        if (food.getTransFat() != null) entity.setTransFat((float) (Math.round(food.getTransFat() / food.getWeightOnce() * addDto.getWeight() * 100) / 100.0));
        if (food.getCholesterol() != null) entity.setCholesterol((float) (Math.round(food.getCholesterol() / food.getWeightOnce() * addDto.getWeight() * 100) / 100.0));
        if (food.getCalcium() != null) entity.setCalcium((float) (Math.round(food.getCalcium() / food.getWeightOnce() * addDto.getWeight() * 100) / 100.0));
        if (food.getIron2() != null) entity.setIron((float) (Math.round(food.getIron2() / food.getWeightOnce() * addDto.getWeight() * 100) / 100.0));
        if (food.getPotassium() != null) entity.setPotassium((float) (Math.round(food.getPotassium() / food.getWeightOnce() * addDto.getWeight() * 100) / 100.0));
        if (food.getDietaryFiber() != null) entity.setDietaryFiber((float) (Math.round(food.getDietaryFiber() / food.getWeightOnce() * addDto.getWeight() * 100) / 100.0));

        diaryMealRepository.save(entity);
    }

    @Transactional
    @Override
    public void addDiaryMealByHistory(SinghaUser authUser, mealItemAddByHistory addDto) {
        if (addDto.getDiaryDate().equals(LocalDate.now().toString())) {
            if(savePoint(authUser.getUser(), addDto.getType())){
                sendPushAlarm(authUser.getUser(), addDto);
            }
        }

        DiaryMeal history = diaryMealRepository.findById(addDto.getMealIdx()).orElseThrow(() -> new NotFoundException(NotFound.DIARY_MEAL));
        if (!history.getUserUid().equals(authUser.getUser().getUid())) throw new BadRequestException(BadRequest.NOT_MINE);

        DiaryMeal entity = new DiaryMeal();
        entity.setUserUid(authUser.getUser().getUid());
        entity.setAmount(addDto.getWeight());
        entity.setDiaryDate(LocalDate.parse(addDto.getDiaryDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        entity.setType(addDto.getType());
        entity.setMenuName(history.getMenuName());
        entity.setAmountUnit(history.getAmountUnit());

        if (history.getCalorie() != null) entity.setCalorie((float) (Math.round(history.getCalorie() / history.getAmount() * addDto.getWeight() * 100) / 100.0));
        if (history.getCarbohydrate() != null) entity.setCarbohydrate((float) (Math.round(history.getCarbohydrate() / history.getAmount() * addDto.getWeight() * 100) / 100.0));
        if (history.getProtein() != null) entity.setProtein((float) (Math.round(history.getProtein() / history.getAmount() * addDto.getWeight() * 100) / 100.0));
        if (history.getFat() != null) entity.setFat((float) (Math.round(history.getFat() / history.getAmount() * addDto.getWeight() * 100) / 100.0));
        if (history.getSodium() != null) entity.setSodium((float) (Math.round(history.getSodium() / history.getAmount() * addDto.getWeight() * 100) / 100.0));
        if (history.getSugar() != null) entity.setSugar((float) (Math.round(history.getSugar() / history.getAmount() * addDto.getWeight() * 100) / 100.0));
        if (history.getSaturatedFattyAcids() != null) entity.setSaturatedFattyAcids((float) (Math.round(history.getSaturatedFattyAcids() / history.getAmount() * addDto.getWeight() * 100) / 100.0));
        if (history.getVitaminC() != null) entity.setVitaminC((float) (Math.round(history.getVitaminC() / history.getAmount() * addDto.getWeight() * 100) / 100.0));
        if (history.getTransFat() != null) entity.setTransFat((float) (Math.round(history.getTransFat() / history.getAmount() * addDto.getWeight() * 100) / 100.0));
        if (history.getCholesterol() != null) entity.setCholesterol((float) (Math.round(history.getCholesterol() / history.getAmount() * addDto.getWeight() * 100) / 100.0));
        if (history.getCalcium() != null) entity.setCalcium((float) (Math.round(history.getCalcium() / history.getAmount() * addDto.getWeight() * 100) / 100.0));
        if (history.getIron() != null) entity.setIron((float) (Math.round(history.getIron() / history.getAmount() * addDto.getWeight() * 100) / 100.0));
        if (history.getPotassium() != null) entity.setPotassium((float) (Math.round(history.getPotassium() / history.getAmount() * addDto.getWeight() * 100) / 100.0));
        if (history.getDietaryFiber() != null) entity.setDietaryFiber((float) (Math.round(history.getDietaryFiber() / history.getAmount() * addDto.getWeight() * 100) / 100.0));

        diaryMealRepository.save(entity);
    }

    /**
     * 매일 다이어리 작성 시 10포인트 지급
     * 아침, 점심, 저녁 -> 하루 최대 30포인트
     * 악용할 수 있어서 언제껄 쓰더라도 다이어리에 음식 추가하는 날짜 기준으로 잡음
     */
    private boolean savePoint(User user, int type) {
        StringBuilder sb = new StringBuilder();
        String mealType = type == 1 ? "아침" : type == 2 ? "점심" : "저녁";
        sb.append("다이어리 작성");
        sb.append("(");
        sb.append(mealType);
        sb.append(")");
        int count = pointHistoryRepository.countTodayByUserUidAndReason(user.getUid(), sb.toString());
        if (count < 1) {
            pointHistoryService.addPoint(10, sb.toString(), user.getUid());
            return true;
        }
        return false;
    }


    private void sendPushAlarm(User user, Object obj) {

        String mealType = null;
        String content = null;
        String link = null;

        if(obj.getClass() == DiaryDto.mealItemAddByFood.class){
            DiaryDto.mealItemAddByFood addDto = (DiaryDto.mealItemAddByFood) obj;
            mealType = addDto.getType() == 1 ? "아침" : addDto.getType() == 2 ? "점심" : "저녁";
            content = mealType+" 다이어리 작성으로 10포인트가 적립되셨습니다.";
            link = "/diary?diaryDate="+addDto.getDiaryDate();
        } else if (obj.getClass() == DiaryDto.mealItemAddByHistory.class){
            DiaryDto.mealItemAddByHistory addDto = (DiaryDto.mealItemAddByHistory) obj;
            mealType = addDto.getType() == 1 ? "아침" : addDto.getType() == 2 ? "점심" : "저녁";
            content = mealType+" 다이어리 작성으로 10포인트가 적립되셨습니다.";
            link = "/diary?diaryDate="+addDto.getDiaryDate();
        } else if (obj.getClass() == DiaryDto.mealItemAdd.class){
            DiaryDto.mealItemAdd addDto = (DiaryDto.mealItemAdd) obj;
            mealType = addDto.getType() == 1 ? "아침" : addDto.getType() == 2 ? "점심" : "저녁";
            content = mealType+" 다이어리 작성으로 10포인트가 적립되셨습니다.";
            link = "/diary?diaryDate="+addDto.getDiaryDate();
        } else if (obj.getClass() == DiaryDto.mealItemAddByOrder.class){
            DiaryDto.mealItemAddByOrder addDto = (DiaryDto.mealItemAddByOrder) obj;
            mealType = addDto.getType() == 1 ? "아침" : addDto.getType() == 2 ? "점심" : "저녁";
            content = mealType+" 다이어리 작성으로 10포인트가 적립되셨습니다.";
            link = "/diary?diaryDate="+addDto.getDiaryDate();
        }

        String title = "금일의 다이어리 작성 보상이 도착했습니다.";

        UserFcmToken fcmToken = userFcmTokenRepository.findById(user.getUid()).orElse(null);
        // List<UserFcmToken> fcmToken = userFcmTokenRepository.findAll().orElse(null);

        // 토큰이 있으면 FCM 알림 전송
        if(fcmToken!=null){
            PushNotificationRequest pushRequest = new PushNotificationRequest(title,content,link,fcmToken.getToken(),null);
            pushNotificationService.sendPushNotificationToToken(pushRequest);
        }

        // 푸쉬알람 저장
        PushAlarmDto.Add pushAlarmDto = new PushAlarmDto.Add();
        pushAlarmDto.setUserUid(user.getUid());
        pushAlarmDto.setTitle(title);
        pushAlarmDto.setContent(content);
        pushAlarmDto.setLink(link);
        pushAlarmDto.setUserUidList(null);
        pushAlarmService.add(pushAlarmDto);
    }

    @Override
    public void addDiaryMealByOrder(SinghaUser authUser, DiaryDto.mealItemAddByOrder addDto) {
        ProductOrder order = orderRepository.findById(addDto.getOrderIdx()).orElseThrow(() -> new NotFoundException(NotFound.ORDER));
        if (!order.getGroup().getUserUid().equals(authUser.getUser().getUid())) throw new BadRequestException(BadRequest.NOT_MINE);
        Product product = order.getProduct();

        DiaryMeal entity = DiaryMealMapper.INSTANCE.productToEntity(product);
        entity.setType(addDto.getType());
        entity.setDiaryDate(LocalDate.parse(addDto.getDiaryDate()));
        entity.setMenuName(product.getName());
        entity.setAmountUnit("g");
        entity.setUserUid(authUser.getUser().getUid());
        entity.setVitaminC(0f);
        entity.setTransFat(0f);
        entity.setCholesterol(0f);
        entity.setCalcium(0f);
        entity.setIron(0f);
        entity.setPotassium(0f);
        entity.setDietaryFiber(0f);
        diaryMealRepository.save(entity);

        if (addDto.getDiaryDate().equals(LocalDate.now().toString())) {
            if(savePoint(authUser.getUser(), addDto.getType())){
                sendPushAlarm(authUser.getUser(), addDto);
            }
        }
    }

}
