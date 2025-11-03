package com.weilyeat.cms.entity.dto;

import lombok.Data;
							

public class OrderItemDto{		

    @Data
    public static class Detail{
        private Long id;
        private Long orderId;
        private int qty;
        private ItemsDto.Detail Items;
    }    

    @Data
    public static class Add{
        private Long orderId;
        private Long itemId;
        private int qty;
    }

    @Data
    public static class clientDetail {
        private int qty;
        private String itemName;
        private int price;
    }

}