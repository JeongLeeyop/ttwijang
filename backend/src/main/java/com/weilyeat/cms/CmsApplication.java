package com.ttwijang.cms;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.ttwijang.cms.api.attached_file.AttachedFileProperties;

// @EnableJpaAuditing
@EnableConfigurationProperties({AttachedFileProperties.class })
@EnableScheduling
@SpringBootApplication
public class CmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CmsApplication.class, args);
	}

	@PostConstruct
    public void setDefaultTimeZone() {
      	TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }

}
