package com.ttwijang.cms.api.attached_file;

import java.beans.JavaBean;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "attached-file")
@Getter @Setter
public class AttachedFileProperties {
	private String uploadDirectory;
}
