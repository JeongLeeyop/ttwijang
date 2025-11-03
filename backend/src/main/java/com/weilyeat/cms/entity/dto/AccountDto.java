package com.weilyeat.cms.entity.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;

import lombok.Data;


public class AccountDto {

    
    @Data
	public static class Detail {
    private Long id;
    private String authority;
    
    private String name;
    private LocalDate birth;
    private String gender;
    private String tel;
    private String email;
    
    private String dietCheck;
    private String height;
    private String weight;
    private String destWeight;

    //다이어리
    private String destMuscleRate;
    private String destFatRate;

    private LocalDate destDate;
    private String activityLevel;
    private String dietPurpose;
    private String dietPrecaution;

    private LocalDateTime createdDate;
    private String point;

    private boolean withdrawStatus;
    private LocalDateTime withdrawDate;

    private int postCode;
    private String oldAddress;
    private String address;
    private String addressDetail;
    private String addressSido;

    // private String status;

    //FK
    //id	file_id	썸네일		N	
    }	


	@Data
	public static class Add {
    private Long id;
    private String authority;
    
    @NotBlank(message = "이름은 필수 값입니다.")
    private String name;
    private LocalDate birth;
    private String gender;
    private String tel;
    @NotBlank(message = "이메일은 필수 값입니다.")
    private String email;
    
    @NotBlank(message = "패스워드는 필수 값입니다.")
    @Size(min = 8, max = 20, message = "패스워드는 최소8자, 최대20자 입니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$", message = "패스워드는 영문과 특수문자 숫자를 포함해야 합니다.")
    private String password;
    
    private String dietCheck;
    private String height;
    private String weight;
    private String destWeight;

    private String destMuscleRate;
    private String destFatRate;

    private LocalDate destDate;
    private String activityLevel;
    private String dietPurpose;
    private String dietPrecaution;

    private LocalDateTime createdDate;
    
    private boolean withdrawStatus;

    private Integer point;
    private Integer postCode;

    private String oldAddress;
    private String address;
    private String addressDetail;
    private String addressSido;
    // private String status;

    //FK
    //id	file_id	썸네일		N	
    }	

    
    

    @Data
	public static class Diet  {
        private String dietCheck;
        private String height;
        private String weight;
        private String destWeight;
        private LocalDate destDate;
        private String activityLevel;
        private String dietPurpose;
        private String dietPrecaution;
    }

    @Data
	public static class Dest  {
        private Long id;
        private String destWeight;
        private String destMuscleRate;
        private String destFatRate;
    }
}


