package com.oneplat.oap.mgmt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
//@EnableConfigurationProperties({
//	FileStorageProperties.class,
//	AdminImageFileStorageProperties.class
//})
public class OapApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(OapApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(OapApplication.class);
	}
}
