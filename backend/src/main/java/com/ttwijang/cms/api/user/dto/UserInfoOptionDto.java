package com.ttwijang.cms.api.user.dto;

import lombok.Data;

public class UserInfoOptionDto {
	@Data
	public static class Save {
		private String uid;
		private String label;
	}
}
