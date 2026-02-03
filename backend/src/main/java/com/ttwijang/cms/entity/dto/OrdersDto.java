package com.ttwijang.cms.entity.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

							

public class OrdersDto{			
    
    @Data
    public static class Detail{
        private Long id;
        private String round;
        private String status;
        private String cancelMemo;
        private String cancelledDate;
        private java.util.List<OrderItemDto.Detail> orderItemList;
    }

    @Data
    public static class List{
        private Long id;
        private String round;
        private String status;
        private String cancelMemo;
        private String cancelledDate;
        private java.util.List<OrderItemDto.Detail> orderItemList;
    }
    
    @Data
    public static class Add{
        private Long customDietId;
        private String round;
        private String status;
        private java.util.List<OrderItemDto.Add> addOrderItemList;
    }

    @Data
    public static class clientDetail {
        private Long id;
        private String round;
        private String startDate;
        private String endDate;
        private java.util.List<OrderItemDto.clientDetail> orderItemList = new ArrayList<OrderItemDto.clientDetail>();
    }
   
    
    //FK	id	customDietId	customDiet		N		
}