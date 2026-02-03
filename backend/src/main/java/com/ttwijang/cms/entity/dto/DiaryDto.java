// package com.ttwijang.cms.entity.dto;

// import java.time.LocalDateTime;

// import org.springframework.format.annotation.DateTimeFormat;

// import lombok.Data;

// public class DiaryDto {

//     @Data
//     public static class Detail {
//         private Long id;
//         private LocalDateTime date;
//         private String weight;
//         private String muscleRate;
//         private String fatRate;
//         private Long accountId;
//     }
//     @Data
//     public static class List {
//         private Long id;
//         private LocalDateTime date;
//         private String weight;
//         private String muscleRate;
//         private String fatRate;
//         private Long accountId;

//         private java.util.List<DiaryFoodDto.Detail> diaryFoodList;

//     }
//     @Data
//     public static class Add {
//         @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//         private LocalDateTime date;

//         private String weight;
//         private String muscleRate;
//         private String fatRate;
//         private Long accountId;
//     }

// @Data
// public static class Update {
//     private Long id;
//     private String weight;
//     private String muscleRate;
//     private String fatRate;
// }
// }