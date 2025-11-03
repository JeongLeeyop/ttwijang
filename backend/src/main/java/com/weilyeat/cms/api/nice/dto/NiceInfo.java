package com.weilyeat.cms.api.nice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class NiceInfo {
	private String name;
	private String phone;
	
	public NiceInfo(String name, String phone) {
		this.name = name;
		this.phone = phone;
	}
}
