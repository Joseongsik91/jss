package com.oneplat.oap.mgmt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import java.util.Properties;

@Configuration
@PropertySource("classpath:config.properties")
public class CommonContext {
    @Autowired
    private Environment env;
    /**
     * Ant Path Matcher
     * @return
     **/
    @Bean
    public PathMatcher antPathMater() {
        return new AntPathMatcher();
    }


    /**
     * Multipart Resolver
     * @return
     **/
    @Bean
    public CommonsMultipartResolver filterMultipartResolver() {
        CommonsMultipartResolver cmr = new CommonsMultipartResolver();
        cmr.setMaxUploadSize(100000000);
        return cmr;
    }

	

    /**
     * Configuration Properties
     * SpEL 사용 <util:properties id="config" location="classpath:/conf/config.properties" />
     **/
    @Bean
    public PropertiesFactoryBean config() {
        PropertiesFactoryBean pfb = new PropertiesFactoryBean();
        pfb.setLocation(new ClassPathResource("system.properties"));
        return pfb;
    }
	
    /**
     * Configuration Properties
     * SpEL 사용 <util:properties id="config" location="classpath:/conf/config.properties" />
     */
    @Bean
    public PropertiesFactoryBean systemConfig() {
        PropertiesFactoryBean pfb = new PropertiesFactoryBean();
        pfb.setLocation(new ClassPathResource("system.properties"));
        return pfb;
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    /*OSDF 연동을 위한 REST Template 등록*/
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(clientHttpRequestFactory());
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(2000);
        factory.setConnectTimeout(2000);
        return factory;
    }

    @Bean
    public JavaMailSender mailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(env.getProperty("mail.host"));
        mailSender.setUsername(env.getProperty("mail.name"));
        mailSender.setPassword(env.getProperty("mail.password"));
        mailSender.setDefaultEncoding("UTF-8");
        mailSender.setPort(587);
        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.smtp.auth", true);
        mailSender.setJavaMailProperties(properties);
        return mailSender;
    }

}
