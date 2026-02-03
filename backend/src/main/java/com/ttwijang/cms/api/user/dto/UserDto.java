package com.ttwijang.cms.api.user.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.ttwijang.cms.entity.UserRole;
import com.ttwijang.cms.oauth.soical.OAuth2UserInfo;
import com.ttwijang.cms.oauth.soical.SocialType;

import lombok.Data;

public class UserDto {

    @Data
    public static class Detail {
        private String uid;
        private String userId;
        private String actualName;
        private String concatNumber;
        private String email;
        private LocalDate birth;
        private boolean enabled;
        private String address;
        private String addressDetail;
        private String lat;        
        private String lon;
        private Integer gender;
        private boolean dietExperience;
        private String height;
        private String weight;
        private String goalWeight;
        private LocalDate goalDate;
        private Integer activityLevel;
        private String dietPurpose;
        private String dietPrecaution;
        private Integer shopIdx;
        private List<String> roles = new ArrayList<String>();
        private LocalDateTime createDate;

        public void setRoles(List<UserRole> userRoles) {
            this.roles = new ArrayList<String>();
            userRoles.forEach(data -> {
                this.roles.add(data.getRole().getRoleCode());
            });
        }
    }

    @Data
    public static class Simple {
        private String uid;
        private String userId;
        private String actualName;
        private String concatNumber;
    }

    @Data
    public static class Page {
        private String uid;
        private String userId;
        private String actualName;
        private String nickName;
        private String email;
        private LocalDate birth;
        private String concatNumber;
        private boolean enabled;
        private boolean missionInquiryStatus;
        private String address;
        private String addressDetail;
        private String lat;        
        private String lon;
        private Integer gender;
        private String provider;
        private List<String> roles = new ArrayList<String>();
        private LocalDateTime createDate;

        public void setRoles(List<UserRole> userRoles) {
            this.roles = new ArrayList<String>();
            userRoles.forEach(data -> {
                this.roles.add(data.getRole().getRoleName());
            });
        }
    }

    @Data
    public static class JoinAdmin {
        @NotBlank(message = "아이디를 입력하세요.")
        @Pattern(regexp = "^[a-z0-9_-]*$", message = "아이디는 영문 소문자, 숫자, 특수문자('-', '_')만 사용 가능 합니다.")
        @Size(min = 4, max = 20, message = "아이디는 최소4자, 최대20자 입니다.")
        private String userId;
        
        @NotBlank(message = "패스워드를 입력하세요.")
        @Size(min = 8, max = 20, message = "패스워드는 최소8자, 최대20자 입니다.")
        private String userPassword;

        @NotBlank(message = "성명을 입력하세요.")
        private String actualName;
        
        @NotBlank(message = "연락처를 입력하세요.")
        private String concatNumber;

        private LocalDate birth;

        @Email(message = "이메일 형식으로 입력하세요.")
        private String email;

        @NotBlank(message = "주소를 입력하세요.")
        private String address;

        @NotBlank(message = "상세주소를 입력하세요.")
        private String addressDetail;

        private String lat;        
        private String lon;

        @NotNull(message = "성별을 선택하세요.")
        private Integer gender;

        private List<String> roles = new ArrayList<String>();
    }
    
    @Data
    public static class Update {
        private String userPassword;
        
        @NotBlank(message = "성명을 입력하세요.")
        private String actualName;
        
        @NotBlank(message = "연락처를 입력하세요.")
        private String concatNumber;

        private LocalDate birth;

        @Email(message = "이메일 형식으로 입력하세요.")
        private String email;

        @NotBlank(message = "주소를 입력하세요.")
        private String address;

        @NotBlank(message = "상세주소를 입력하세요.")
        private String addressDetail;

        private String lat;        
        private String lon;

        @NotNull(message = "성별을 선택하세요.")
        private Integer gender;

        private boolean dietExperience;
        private String height;
        private String weight;
        private String goalWeight;
        private LocalDate goalDate;
        private Integer activityLevel;
        private String dietPurpose;
        private String dietPrecaution;

        private List<String> roles = new ArrayList<String>();
    }

    @Data
    public static class JoinSoicalUser {
        private String userId;
        private String actualName;
        private SocialType provider;
        private String providerId;
        private boolean enabled;
        private boolean notLocked;
        
        public JoinSoicalUser(OAuth2UserInfo userInfo) {
            this.userId = userInfo.getId();
            this.provider = userInfo.getSocialType();
            this.providerId = userInfo.getId();
            this.actualName = userInfo.getName();
            this.enabled = true;
            this.notLocked = true;
        }
    }

    @Data
    public static class ClientUpdate {
        @Email(message = "이메일 형식으로 입력하세요.")
        private String email;
    }

    @Data
    public static class ClientPasswordUpdate {
        @NotBlank(message = "패스워드는 필수 값입니다.")
        @Size(min = 8, max = 20, message = "패스워드는 최소8자, 최대20자 입니다.")
        private String userPassword;

        @Email(message = "이메일 형식으로 입력하세요.")
        private String email;
    }

    @Data
    public static class FindPassword {
        private String email;
        private String name;
        private String phone;
    }

    @Data
    public static class manager {
        private String uid;
        private String userId;
        private String actualName;
        private String concatNumber;
        private String email;
        private LocalDate birth;
        private LocalDateTime createDate;
        private String shopName;
        private Integer shopIdx;
    }

    @Data
    public static class addManager {
        @NotBlank(message = "아이디를 입력하세요.")
        @Pattern(regexp = "^[a-z0-9_-]*$", message = "아이디는 영문 소문자, 숫자, 특수문자('-', '_')만 사용 가능 합니다.")
        @Size(min = 4, max = 20, message = "아이디는 최소4자, 최대20자 입니다.")
        private String userId;
        
        @NotBlank(message = "패스워드를 입력하세요.")
        @Size(min = 8, max = 20, message = "패스워드는 최소8자, 최대20자 입니다.")
        private String userPassword;

        @NotBlank(message = "성명을 입력하세요.")
        private String actualName;
        
        @NotBlank(message = "연락처를 입력하세요.")
        private String concatNumber;

        private LocalDate birth;

        @Email(message = "이메일 형식으로 입력하세요.")
        private String email;

        private String address;

        private String addressDetail;

        private Integer gender;

        @NotNull(message = "상점 고유값을 입력하세요.")
        private Integer shopIdx;
    }

    @Data
    public static class updateManager {
        private String userPassword;

        @NotBlank(message = "성명을 입력하세요.")
        private String actualName;
        
        @NotBlank(message = "연락처를 입력하세요.")
        private String concatNumber;

        private LocalDate birth;

        @Email(message = "이메일 형식으로 입력하세요.")
        private String email;

        private String address;

        private String addressDetail;

        private Integer gender;

        @NotNull(message = "상점 고유값을 입력하세요.")
        private Integer shopIdx;
    }
}
