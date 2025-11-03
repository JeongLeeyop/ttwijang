package com.ttwijang.cms.api.shop.dto;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

public class AdmShopPickupTimeDto {
    @Data
    public static class detail {
        private List<LocalTime> mon = new ArrayList<LocalTime>();
        private List<LocalTime> tue = new ArrayList<LocalTime>();
        private List<LocalTime> wed = new ArrayList<LocalTime>();
        private List<LocalTime> thu = new ArrayList<LocalTime>();
        private List<LocalTime> fri = new ArrayList<LocalTime>();
        private List<LocalTime> sat = new ArrayList<LocalTime>();
        private List<LocalTime> sun = new ArrayList<LocalTime>();
    }

    @Data
    public static class save {
        private List<LocalTime> mon = new ArrayList<LocalTime>();
        private List<LocalTime> tue = new ArrayList<LocalTime>();
        private List<LocalTime> wed = new ArrayList<LocalTime>();
        private List<LocalTime> thu = new ArrayList<LocalTime>();
        private List<LocalTime> fri = new ArrayList<LocalTime>();
        private List<LocalTime> sat = new ArrayList<LocalTime>();
        private List<LocalTime> sun = new ArrayList<LocalTime>();
    }
}
