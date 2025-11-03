package com.weilyeat.cms.api.product.service;

import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import com.weilyeat.cms.api.product.dto.AdmProductOrderDto;
import com.weilyeat.cms.api.product.dto.AdmProductOrderDto.updateReceiveStatus;
import com.weilyeat.cms.api.product.dto.ProductOrderGroupDto;
import com.weilyeat.cms.api.product.dto.mapper.AdmProductOrderMapper;
import com.weilyeat.cms.api.product.repository.AdmProductOrderRepository;
import com.weilyeat.cms.api.product.repository.query.AdmProductOrderQuery;
import com.weilyeat.cms.api.product.repository.query.ProductOrderQuery;
import com.weilyeat.cms.api.product.repository.search.AdmProductOrderSearch;
import com.weilyeat.cms.api.push_alarm.dto.PushAlarmDto;
import com.weilyeat.cms.api.push_alarm.service.PushAlarmService;
import com.weilyeat.cms.api.station.repository.StationRepository;
import com.weilyeat.cms.api.user.dto.UserFcmToken;
import com.weilyeat.cms.api.user.exception.UserNotFoundException;
import com.weilyeat.cms.api.user.repository.UserFcmTokenRepository;
import com.weilyeat.cms.common.exception.NotFoundException;
import com.weilyeat.cms.common.exception.code.NotFound;
import com.weilyeat.cms.entity.ProductOrder;
import com.weilyeat.cms.entity.ProductOrderGroup;
import com.weilyeat.cms.entity.ProductOrderType;
import com.weilyeat.cms.entity.User;
import com.weilyeat.cms.fcm.model.PushNotificationRequest;
import com.weilyeat.cms.fcm.service.PushNotificationService;
import com.weilyeat.cms.oauth.SinghaUser;

import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import lombok.AllArgsConstructor;

public interface AdmProductOrderService {
    Page<AdmProductOrderDto.list> list(AdmProductOrderSearch search, Pageable pageable);
    AdmProductOrderDto.detail detail(Integer idx);
    
    Page<AdmProductOrderDto.listByDay> listByDay(AdmProductOrderSearch search, Pageable pageable, SinghaUser authUser);
    List<AdmProductOrderDto.detailByDay> detailByDay(Integer idx);
    
    Page<AdmProductOrderDto.listByProduct> listByProduct(AdmProductOrderSearch search, Pageable pageable);
    List<AdmProductOrderDto.detailByProduct> detailByProduct(Integer idx, AdmProductOrderSearch search);
    
    Page<AdmProductOrderDto.listByShop> listByShop(AdmProductOrderSearch search, Pageable pageable);
    
    List<AdmProductOrderDto.detailByShop> detailByShop(Integer idx, AdmProductOrderSearch search);
    List<AdmProductOrderDto.detailByShop> detailByStation(Integer idx, AdmProductOrderSearch search);
    
    void updateStatus(Integer idx, AdmProductOrderDto.updateStatus dto);
    void updateStatusByDay(Integer idx, AdmProductOrderDto.updateStatus dto);
    AdmProductOrderDto.totalStatistics getTotalStatistics(AdmProductOrderSearch search);
    void updateReceiveStatus(updateReceiveStatus dto, AdmProductOrderSearch search);
    
    void excel(AdmProductOrderSearch search, Pageable pageable, HttpServletResponse response, SinghaUser authUser);
}

@Service
@AllArgsConstructor
class AdmProductOrderServiceImpl implements AdmProductOrderService {
    private final AdmProductOrderRepository orderRepository;
    private final AdmProductOrderQuery orderQuery;
    private final AdmProductOrderGroupService orderGroupService;
    private final StationRepository stationRepository;
    
    private final UserFcmTokenRepository userFcmTokenRepository;
    private final PushNotificationService pushNotificationService;
    private final PushAlarmService pushAlarmService;
    private final ProductOrderQuery productOrderQuery;


    @Override
    public Page<AdmProductOrderDto.list> list(AdmProductOrderSearch search, Pageable pageable) {
        return orderRepository.findAll(search.search(), pageable).map(x -> AdmProductOrderMapper.INSTANCE.entitiyToListDto(x));
    }

    @Override
    public AdmProductOrderDto.detail detail(Integer idx) {
        ProductOrder entity = orderRepository.findById(idx).orElseThrow(() -> new NotFoundException(NotFound.ORDER));
        return AdmProductOrderMapper.INSTANCE.entitiyToDetailDto(entity);
    }
    
    @Override
    public Page<AdmProductOrderDto.listByDay> listByDay(AdmProductOrderSearch search, Pageable pageable, @AuthenticationPrincipal SinghaUser authUser) {
        if (authUser != null) {
            if (authUser.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_SHOP_ADMIN"))) {
                User user = authUser.getUser();
                search.setShopId(user.getShopIdx());
            }
        }
        List<AdmProductOrderDto.listByDay> list = orderQuery.listByDay(search, pageable);
        int count = orderQuery.countByDay(search);
        return new PageImpl<>(list, pageable, count);
    }

    @Override
    public List<AdmProductOrderDto.detailByDay> detailByDay(Integer idx) {
        List<ProductOrder> list = orderRepository.detailByDay(idx);
        //존재하지 않는 stationId 제거
        list.stream().forEach(x -> {
            if(x.getGroup().getOrderType().equals(ProductOrderType.STATION) && !stationRepository.findById(x.getGroup().getStationId()).isPresent()) {
                x.getGroup().setStationId(null);
                x.getGroup().setStation(null);
            }
        });
        return list.stream().map(x -> AdmProductOrderMapper.INSTANCE.entitiyToListDetailByDayDto(x)).collect(Collectors.toList());
    }

    @Override
    public Page<AdmProductOrderDto.listByProduct> listByProduct(AdmProductOrderSearch search, Pageable pageable) {
        List<AdmProductOrderDto.listByProduct> list = orderQuery.listByProduct(search, pageable);
        int count = orderQuery.countByProduct(search);
        return new PageImpl<>(list, pageable, count);
    }

    @Override
    public List<AdmProductOrderDto.detailByProduct> detailByProduct(Integer idx, AdmProductOrderSearch search) {
        return orderQuery.detailByProduct(idx, search);
    }
    
    @Override
    public Page<AdmProductOrderDto.listByShop> listByShop(AdmProductOrderSearch search, Pageable pageable) {
        List<AdmProductOrderDto.listByShop> list = orderQuery.listByShop(search, pageable);
        int count = orderQuery.countByShop(search);
        return new PageImpl<>(list, pageable, count);
    }

    @Override
    public List<AdmProductOrderDto.detailByShop> detailByShop(Integer idx, AdmProductOrderSearch search) {
        return orderQuery.detailByShop(idx, search, ProductOrderType.PICKUP);
    }
    
    @Override
    public List<AdmProductOrderDto.detailByShop> detailByStation(Integer idx, AdmProductOrderSearch search) {
        return orderQuery.detailByShop(idx, search, ProductOrderType.STATION);
    }

    @Override
    @Transactional
    public void updateStatus(Integer idx, AdmProductOrderDto.updateStatus dto) {
        ProductOrder entity = orderRepository.findById(idx).orElseThrow(() -> new NotFoundException(NotFound.ORDER));
        entity.setPickupStatus(dto.getPickupStatus());
        orderRepository.save(entity);
        ProductOrderGroup orderGroup = entity.getGroup();
        
        if(orderGroup.getOrderType() == ProductOrderType.STATION) {
        String title= "";
        if(dto.getPickupStatus() == 1) 
        title = "상품이 배송지로 출발했습니다.";
        else if(dto.getPickupStatus() == 2) 
        title = "상품이 배송지에 도착했습니다.";
        
        if(dto.getPickupStatus() != 0){
            Optional<UserFcmToken> fcmToken2 = userFcmTokenRepository.findById(orderGroup.getUserUid());
            if(fcmToken2.isPresent()){
                UserFcmToken fcmToken = fcmToken2.get();
                String content = "배송지 : " +orderGroup.getStation().getName();
                String link = "/delivery/"+orderGroup.getIdx();
                PushNotificationRequest pushRequest = new PushNotificationRequest(title,content,link,fcmToken.getToken(),null);
                pushNotificationService.sendPushNotificationToToken(pushRequest);
                // 푸쉬알람 저장
                PushAlarmDto.Add pushAlarmDto = new PushAlarmDto.Add();
                pushAlarmDto.setUserUid(orderGroup.getUserUid());
                pushAlarmDto.setTitle(title);
                pushAlarmDto.setContent(content);
                pushAlarmDto.setLink(link);
                pushAlarmDto.setUserUidList(null);
                pushAlarmService.add(pushAlarmDto);
            }
        }
    }

        // 주문 상태 변경
        Integer orderStatus = null;
        if(orderGroup.getOrderType() == ProductOrderType.PICKUP) 
            orderStatus = orderGroupService.findOrderStatus(entity.getGroupId());
        else if(orderGroup.getOrderType() == ProductOrderType.STATION) 
            orderStatus = orderGroupService.findOrderStatus2(entity.getGroupId());
        
        if(orderStatus == null){
            new Exception("주문상태 조회 실패");
        } else {
            ProductOrderGroupDto.updateStatus productGroupDto = new ProductOrderGroupDto.updateStatus();
            productGroupDto.setOrderStatus(orderStatus);
            orderGroupService.updateStatus(entity.getGroupId(), productGroupDto);
        }
    }
    
    @Override
    @Transactional
    public void updateStatusByDay(Integer idx, AdmProductOrderDto.updateStatus dto) {
        List<ProductOrder> entityList = orderRepository.detailByDay(idx);
        if(entityList.size() < 1) throw new NotFoundException(NotFound.ORDER);

        for(ProductOrder entity: entityList) {
        ProductOrderGroup orderGroup = entity.getGroup();

        entity.setPickupStatus(dto.getPickupStatus());
        orderRepository.save(entity);

        // 주문 상태 변경
        Integer orderStatus = null;
        if(orderGroup.getOrderType() == ProductOrderType.PICKUP) 
            orderStatus = orderGroupService.findOrderStatus(entity.getGroupId());
        else if(orderGroup.getOrderType() == ProductOrderType.STATION) 
            orderStatus = orderGroupService.findOrderStatus2(entity.getGroupId());
        
        if(orderStatus == null){
            new Exception("주문상태 조회 실패");
        } else {
            ProductOrderGroupDto.updateStatus productGroupDto = new ProductOrderGroupDto.updateStatus();
            productGroupDto.setOrderStatus(orderStatus);
            orderGroupService.updateStatus(entity.getGroupId(), productGroupDto);
        }
     }

     if(entityList.get(0) != null) {
        ProductOrderGroup orderGroup = entityList.get(0).getGroup();
        if(orderGroup.getOrderType() == ProductOrderType.STATION) {
        String title= "";
        if(dto.getPickupStatus() == 1) 
        title = "상품이 배송지로 출발했습니다.";
        else if(dto.getPickupStatus() == 2) 
        title = "상품이 배송지에 도착했습니다.";
        
        if(dto.getPickupStatus() != 0){
            Optional<UserFcmToken> fcmToken2 = userFcmTokenRepository.findById(orderGroup.getUserUid());
            if(fcmToken2.isPresent()){
                UserFcmToken fcmToken = fcmToken2.get();
                String content = "배송지 : " +orderGroup.getStation().getName();
                String link = "/delivery/"+orderGroup.getIdx();
                PushNotificationRequest pushRequest = new PushNotificationRequest(title,content,link,fcmToken.getToken(),null);
                pushNotificationService.sendPushNotificationToToken(pushRequest);
                // 푸쉬알람 저장
                PushAlarmDto.Add pushAlarmDto = new PushAlarmDto.Add();
                pushAlarmDto.setUserUid(orderGroup.getUserUid());
                pushAlarmDto.setTitle(title);
                pushAlarmDto.setContent(content);
                pushAlarmDto.setLink(link);
                pushAlarmDto.setUserUidList(null);
                pushAlarmService.add(pushAlarmDto);
            }
        }
     }
    }
    }

   @Override
    public AdmProductOrderDto.totalStatistics getTotalStatistics(AdmProductOrderSearch search) {
        return orderQuery.getTotalStatistics(search);
    }

    @Transactional
    @Override
    public void updateReceiveStatus(AdmProductOrderDto.updateReceiveStatus dto, AdmProductOrderSearch search) {
        
        // 배달상태
        List<Integer> productIdxList = orderQuery.getProductOrderIdxList(dto.getIdxList(), search);

        if(search.getOrderType().equals(ProductOrderType.PICKUP.toString())) {
            orderRepository.updateReceiveStatusByIdx(dto.getReceiveStatus(), productIdxList);
        } else if(search.getOrderType().equals(ProductOrderType.STATION.toString())) {
            orderRepository.updatePickupStatusByIdx(dto.getReceiveStatus(), productIdxList);
        }
        
        if(search.getOrderType().equals(ProductOrderType.PICKUP.toString())) {
            if(dto.getReceiveStatus()==2){
            List<AdmProductOrderDto.listForFCM> fcmList = orderQuery.selectProductOrderListForFcm(productIdxList, ProductOrderType.PICKUP);
            for(AdmProductOrderDto.listForFCM fcmDto : fcmList){
                UserFcmToken fcmToken = userFcmTokenRepository.findById(fcmDto.getUserUid()).orElseThrow(() -> new UserNotFoundException("잘못된 접근입니다."));
                String title = "상품이 픽업매장에 도착했습니다.";
                String content = fcmDto.getName()+"\n픽업시간 : " + fcmDto.getPickupTime();
                String link = "/delivery/"+fcmDto.getGroupId();

                PushNotificationRequest pushRequest = new PushNotificationRequest(title,content,link,fcmToken.getToken(),null);
                pushNotificationService.sendPushNotificationToToken(pushRequest);

                // 푸쉬알람 저장
                PushAlarmDto.Add pushAlarmDto = new PushAlarmDto.Add();
                pushAlarmDto.setUserUid(fcmDto.getUserUid());
                pushAlarmDto.setTitle(title);
                pushAlarmDto.setContent(content);
                pushAlarmDto.setLink(link);
                pushAlarmDto.setUserUidList(null);
                pushAlarmService.add(pushAlarmDto);
             }
            }
        } else if(search.getOrderType().equals(ProductOrderType.STATION.toString())) {
            if(dto.getReceiveStatus()==1){
                List<AdmProductOrderDto.listForFCM> fcmList = orderQuery.selectProductOrderListForFcm(productIdxList,ProductOrderType.STATION);
                for(AdmProductOrderDto.listForFCM fcmDto : fcmList){
                    UserFcmToken fcmToken = userFcmTokenRepository.findById(fcmDto.getUserUid()).orElseThrow(() -> new UserNotFoundException("잘못된 접근입니다."));
                    String title = "상품이 거점으로 출발하였습니다.";
                    String content = "배송지 : "+fcmDto.getName();
                    String link = "/delivery/"+fcmDto.getGroupId();
    
                    PushNotificationRequest pushRequest = new PushNotificationRequest(title,content,link,fcmToken.getToken(),null);
                    pushNotificationService.sendPushNotificationToToken(pushRequest);
    
                    // 푸쉬알람 저장
                    PushAlarmDto.Add pushAlarmDto = new PushAlarmDto.Add();
                    pushAlarmDto.setUserUid(fcmDto.getUserUid());
                    pushAlarmDto.setTitle(title);
                    pushAlarmDto.setContent(content);
                    pushAlarmDto.setLink(link);
                    pushAlarmDto.setUserUidList(null);
                    pushAlarmService.add(pushAlarmDto);
                 }
            } else if(dto.getReceiveStatus()==2){
                List<AdmProductOrderDto.listForFCM> fcmList = orderQuery.selectProductOrderListForFcm(productIdxList,ProductOrderType.STATION);
                for(AdmProductOrderDto.listForFCM fcmDto : fcmList){
                    UserFcmToken fcmToken = userFcmTokenRepository.findById(fcmDto.getUserUid()).orElseThrow(() -> new UserNotFoundException("잘못된 접근입니다."));
                    String title = "상품이 거점에 도착했습니다.";
                    String content = "배송지 : "+fcmDto.getName();
                    String link = "/delivery/"+fcmDto.getGroupId();
    
                    PushNotificationRequest pushRequest = new PushNotificationRequest(title,content,link,fcmToken.getToken(),null);
                    pushNotificationService.sendPushNotificationToToken(pushRequest);
    
                    // 푸쉬알람 저장
                    PushAlarmDto.Add pushAlarmDto = new PushAlarmDto.Add();
                    pushAlarmDto.setUserUid(fcmDto.getUserUid());
                    pushAlarmDto.setTitle(title);
                    pushAlarmDto.setContent(content);
                    pushAlarmDto.setLink(link);
                    pushAlarmDto.setUserUidList(null);
                    pushAlarmService.add(pushAlarmDto);
                 }
            }    
        }
        //주문 상태 변경
            orderGroupService.updateReceiveStatusByProductList(productIdxList, ProductOrderType.valueOf(search.getOrderType()));
    }

    @Override
    public void excel(AdmProductOrderSearch search, Pageable pageable, HttpServletResponse response, SinghaUser authUser) {
        List<AdmProductOrderDto.listByDay> DayList = null;
        List<AdmProductOrderDto.listByProduct> ProductList = null;
        List<AdmProductOrderDto.listByShop> ShopList = null;

        if(search.getExcelType().equals("day")) {
            if (authUser != null) {
                if (authUser.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_SHOP_ADMIN"))) {
                    User user = authUser.getUser();
                    search.setShopId(user.getShopIdx());
                }
            }
            DayList = orderQuery.listByDay(search, pageable);
        } else if (search.getExcelType().equals("product")) {
            ProductList = orderQuery.listByProduct(search, pageable);
        } else if (search.getExcelType().equals("shop")) { 
            ShopList = orderQuery.listByShop(search, pageable);
        }

        Workbook wb = new XSSFWorkbook();
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        Sheet sheet = wb.createSheet("주문내역");
        Row row = null;
        Cell cell = null;
        int rowNum = 0;
         
        // 빨간 글씨
        CellStyle redColorStyle = wb.createCellStyle();
        Font redFont = wb.createFont();
        redFont.setColor(HSSFColorPredefined.RED.getIndex());
        redColorStyle.setFont(redFont);

        // 헤더 스타일
        CellStyle headerStyle = wb.createCellStyle();
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setFillForegroundColor(HSSFColorPredefined.PALE_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setWrapText(true);

        sheet.setColumnWidth(1, 4500);
        sheet.setColumnWidth(2, 6000);
        sheet.setColumnWidth(3, 3500);
        sheet.setColumnWidth(4, 3500);
        sheet.setColumnWidth(5, 7500);
        sheet.setColumnWidth(6, 4500);
        sheet.setColumnWidth(7, 4500);
        sheet.setColumnWidth(8, 4500);
        sheet.setColumnWidth(9, 3000);
        sheet.setColumnWidth(10, 4000);
        sheet.setColumnWidth(11, 4000);
        sheet.setColumnWidth(12, 4000);
        sheet.setColumnWidth(13, 4000);
        sheet.setColumnWidth(14, 4000);
        sheet.setColumnWidth(15, 4000);

        if(search.getExcelType().equals("day")) {
            sheet.setColumnWidth(3, 12000);
            sheet.setColumnWidth(5, 4000);
            sheet.setColumnWidth(9, 6000);
            if(search.getOrderType().equals(ProductOrderType.PICKUP.toString())) {
                sheet.setColumnWidth(3, 4000);
                sheet.setColumnWidth(11, 10000);
            }

        } else if (search.getExcelType().equals("product")) {
            sheet.setColumnWidth(4, 8000);
            sheet.setColumnWidth(5, 4000);
            sheet.setColumnWidth(8, 15000);
        } else if (search.getExcelType().equals("shop")) {
            sheet.setColumnWidth(6, 4000);
            sheet.setColumnWidth(8, 15000);
        }

        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.KOREA);

        // 예약자 목록 헤더
        int rowIndex = 0;
        List<String> headers = new ArrayList<String>();

        if(search.getOrderType().equals(ProductOrderType.PICKUP.toString())){
            if(search.getExcelType().equals("day")) {
                headers = Arrays.asList("번호", "이름", " 픽업매장", "결제금액", "상품수량", "픽업날짜", "픽업시간", "연락처", "상태", "주문날짜","주차","상품명","수량","가격");
            } else if (search.getExcelType().equals("product")) {
                headers = Arrays.asList("번호", "상품명", "주문 수량", "결제금액", "상품명", "수량", "총 가격");
            } else if (search.getExcelType().equals("shop")) { 
                headers = Arrays.asList("번호", "거점명", "거점위치", "주문 수량", "결제금액","상점명", "수량", "총 가격", "상태");
            }
        } else if(search.getOrderType().equals(ProductOrderType.STATION.toString())){
            if(search.getExcelType().equals("day")) {
                headers = Arrays.asList("번호", "이름", "배송지", "주소", "결제금액", "상품수량", "배송날짜", "연락처", "상태", "주문날짜", "현관 비밀번호", "주차","상품명","수량","가격");
            } else if (search.getExcelType().equals("product")) {
                headers = Arrays.asList("번호", "상품명", "주문 수량", "결제금액", "상품명", "수량", "총 가격");
            } else if (search.getExcelType().equals("shop")) { 
                headers = Arrays.asList("번호", "거점명", "거점위치", "주문 수량", "결제금액","거점명", "수량", "총 가격", "상태");
            }
        }
        
        row = sheet.createRow(rowNum++);
        for (String header : headers) {
            cell = row.createCell(rowIndex++);
            cell.setCellValue(header);
            cell.setCellStyle(headerStyle);
        }

        // 예약자 목록 바디
        int dataIndex = 1;
        int prvDayIdx = -1;

        if(search.getExcelType().equals("day")) {
            for (AdmProductOrderDto.listByDay data : DayList) {

                int index = 0;
                row = sheet.createRow(rowNum++);
                
                if(prvDayIdx != data.getDayId()){
                    row = sheet.createRow(rowNum++);

                    cell = row.createCell(index++); // 0
                    cell.setCellValue(dataIndex);
        
                    cell = row.createCell(index++); // 1
                    cell.setCellValue(data.getName());
        
                    cell = row.createCell(index++); // 2
                    if(search.getOrderType().equals(ProductOrderType.PICKUP.toString())){
                        cell.setCellValue(data.getShopName());
                    }else if(search.getOrderType().equals(ProductOrderType.STATION.toString())){
                        cell.setCellValue(data.getStationName());
                    }
        
                    if(search.getOrderType().equals(ProductOrderType.STATION.toString())){
                    cell = row.createCell(index++); // 3
                        if(data.getAddress() == null) data.setAddress("");
                        if(data.getAddressDetail() == null) data.setAddressDetail("");
                        cell.setCellValue(data.getAddress() + " " + data.getAddressDetail());
                    }

                    cell = row.createCell(index++); // 4 (3)
                    cell.setCellValue(numberFormat.format(data.getAmount())+"원");

                    cell = row.createCell(index++); // 5 (4)
                    cell.setCellValue(data.getProductNum()+"개");

                    cell = row.createCell(index++); // 6 (5)
                    cell.setCellValue(data.getPickupDate());

                    if(search.getOrderType().equals(ProductOrderType.PICKUP.toString())){
                        cell = row.createCell(index++);
                        cell.setCellValue(data.getPickupTime());
                    }

                    cell = row.createCell(index++); // 7
                    cell.setCellValue(data.getNumber());

                    cell = row.createCell(index++); // 8
                    if (data.getPickupStatus() == 0) cell.setCellValue("배송대기");
                    if (data.getPickupStatus() == 1) cell.setCellValue("배송중");
                    if (data.getPickupStatus() == 2) cell.setCellValue("배송완료");

                    cell = row.createCell(index++); // 9
                    cell.setCellValue(data.getCreateDate());
                    
                    if(search.getOrderType().equals(ProductOrderType.STATION.toString())){
                        cell = row.createCell(index++); // 10
                        cell.setCellValue(data.getMemo()); 
                    }
                    
                    cell = row.createCell(index++); // 11 (10)
                    cell.setCellValue(data.getWeekNum()+"주차"); 
                    
                    // 하위 컬럼에 index를 카운터 하지 않음
                    dataIndex++;
                    
                }

                prvDayIdx = data.getDayId();

                if(search.getOrderType().equals(ProductOrderType.PICKUP.toString())) index = 11;
                else if(search.getOrderType().equals(ProductOrderType.STATION.toString())) index = 12;

                cell = row.createCell(index++); // 12 (11)
                cell.setCellValue(data.getProductName()); 
                cell = row.createCell(index++); // 13 (12)
                cell.setCellValue(data.getProductNum2()+"개"); 
                cell = row.createCell(index++); // 14 (13)
                cell.setCellValue(numberFormat.format(data.getProductPrice())+"원"); 

            }   
        } else if (search.getExcelType().equals("product")) {
            for (AdmProductOrderDto.listByProduct data : ProductList) {
                row = sheet.createRow(rowNum++);
                cell = row.createCell(0);
                cell.setCellValue(dataIndex);
    
                cell = row.createCell(1);
                cell.setCellValue(data.getProductName());
    
                cell = row.createCell(2);
                cell.setCellValue(data.getTotalProductNum()+"개");
    
                cell = row.createCell(3);
                cell.setCellValue(numberFormat.format(data.getTotalAmount())+"원");

                List<AdmProductOrderDto.detailByProduct> list = orderQuery.detailByProduct(data.getProductId(), search);
                for (AdmProductOrderDto.detailByProduct data2: list){
                    cell = row.createCell(4);

                    if(search.getOrderType().equals(ProductOrderType.PICKUP.toString())) {
                        cell.setCellValue(data2.getShopName()); 
                    }
                    else if(search.getOrderType().equals(ProductOrderType.STATION.toString())) {
                        cell.setCellValue(data2.getStationName()); 
                    }

                    cell = row.createCell(5);
                    cell.setCellValue(data2.getTotalProductNum()+"개"); 
                    cell = row.createCell(6);
                    cell.setCellValue(numberFormat.format(data2.getTotalAmount())+"원"); 
                    row = sheet.createRow(rowNum++);
                }
                dataIndex++;
            }   
        } else if (search.getExcelType().equals("shop")) { 
            for (AdmProductOrderDto.listByShop data : ShopList) {
                row = sheet.createRow(rowNum++);
                cell = row.createCell(0);
                cell.setCellValue(dataIndex);
    
                cell = row.createCell(1);
                cell.setCellValue(data.getStationName());
    
                cell = row.createCell(2);
                if(data.getAddress() == null) data.setAddress("");
                if(data.getAddressDetail() == null) data.setAddressDetail("");
                cell.setCellValue(data.getAddress() + " " + data.getAddressDetail());
    
                cell = row.createCell(3);
                cell.setCellValue(data.getTotalProductNum()+"개");
                cell = row.createCell(4);
                cell.setCellValue(numberFormat.format(data.getTotalAmount())+"원");
                
                List<AdmProductOrderDto.detailByShop> list = null;
                if(search.getOrderType().equals(ProductOrderType.PICKUP.toString())) {
                    list = orderQuery.detailByShop(data.getShopId(), search, ProductOrderType.PICKUP);
                } else if (search.getOrderType().equals(ProductOrderType.STATION.toString())) {
                    list = orderQuery.detailByShop(data.getStationId(), search, ProductOrderType.STATION);
                }
                
                for (AdmProductOrderDto.detailByShop data2: list){
                    cell = row.createCell(5);
                    cell.setCellValue(data2.getProductName()); 
                    cell = row.createCell(6);
                    cell.setCellValue(data2.getTotalProductNum()+"개"); 
                    cell = row.createCell(7);
                    cell.setCellValue(numberFormat.format(data2.getTotalAmount())+"원"); 
                    cell = row.createCell(8);
                    cell.setCellValue("주문접수: " + data2.getTotalHoldCount() + "개 | 배달중: " + data2.getTotalDeliveryCount() + "개 | 배달완료: " + data2.getTotalDoneCount()); 
                    row = sheet.createRow(rowNum++);
                }
                dataIndex++;
            }   
        }
        
        // Excel File Output
        try {
            String fileName = URLEncoder.encode("웨일리잇_주문_내역.xls", "utf-8");
            response.setContentType("ms-vnd/excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            wb.write(response.getOutputStream());
            wb.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("엑셀 출력에 실패했습니다.");
        }
    }
    
}
