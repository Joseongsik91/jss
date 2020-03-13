package com.oneplat.oap.mgmt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class OapMgmtApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(OapMgmtApplication.class, args);
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(OapMgmtApplication.class);
	}

}
