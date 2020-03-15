package com.oneplat.oap.mgmt.config;

import java.util.Collection;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.google.common.collect.ImmutableList;
import com.oneplat.oap.core.model.MyFilterSecurityMetadataSource;
import com.oneplat.oap.core.web.filter.CORSFilter;
import com.oneplat.oap.core.web.filter.CustomAccessDecisionManager;
import com.oneplat.oap.mgmt.common.web.handler.CustomAccessDeniedHandler;
import com.oneplat.oap.mgmt.common.web.handler.CustomAccessDenyEntryPoint;
import com.oneplat.oap.mgmt.common.web.handler.CustomAuthenticationProvider;
import com.oneplat.oap.mgmt.common.web.handler.LoginFailureHandler;
import com.oneplat.oap.mgmt.common.web.handler.LoginOutSuccessHandler;
import com.oneplat.oap.mgmt.common.web.handler.LoginSuccessHandler;

/**
 * TODO Spring Security 설정 처리 
 *
 * @author mike Ryu, BD Apis
 * @date 2015. 3. 09
 * @version 1.0
 */

@Configuration
@EnableWebSecurity
@Import(DatabaseConfig.class)
@PropertySource("classpath:system.properties")
public class CustomWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
    @Autowired 
    private LoginSuccessHandler loginSuccessHandler;
    @Autowired 
    private LoginFailureHandler loginFailureHandler;
    @Autowired
    private LoginOutSuccessHandler loginOutSuccessHandler;
    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;
    
    @Autowired 
    private CustomAccessDeniedHandler accessDeniedHandler;
    //@Autowired UserDetailsService userDetailsService;
    
    @Autowired
    Environment env;
    
    @Autowired PasswordEncoder passwordEncoder;
    
    @Bean
    protected SessionRegistry sessionRegistryImpl()
    {
        return new SessionRegistryImpl();
    }
//    @Override
//    protected UserDetailsService userDetailsService() {
//        return userDetailsService;
//    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        .sessionManagement()
            //.invalidSessionUrl("/login") // session-management@invalid-session-url
            //.sessionAuthenticationErrorUrl("/login") // session-management@session-authentication-error-url
            .maximumSessions(3) // session-management/concurrency-control@max-sessions 동시 접속 1명
            .maxSessionsPreventsLogin(false) // session-management/concurrency-control@error-if-maximum-exceeded
            .expiredUrl("/expired-session") // session-management/concurrency-control@expired-url
            .sessionRegistry(sessionRegistryImpl()); // session-management/concurrency-control@session-registry-ref
        http
            .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(customAccessDenyEntryPoint())
                .and()
            .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/auth_check")
                .usernameParameter("loginId")
                .passwordParameter("password")
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailureHandler)
                //.defaultSuccessUrl("/index")
                //.failureUrl("/login?fail=true")
                .permitAll()
                .and()
            .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(loginOutSuccessHandler);
            
        if(env.getProperty("system.auth.checkYn").equals("Y")){
            http.authorizeRequests()
            .antMatchers("/error-login", "/login", "/favicon.ico", "/resources/**", "/publish/**", "/analytics/log/**", "/common/join/**").permitAll()
            .antMatchers( "/**").hasRole("USER")
            .anyRequest().authenticated()
            .and()
//            .addFilterAfter(filterSecurityInterceptor(), FilterSecurityInterceptor.class)
//            .addFilter(new CORSFilter())
//            .addFilterAfter(new CORSFilter(), BasicAuthenticationFilter.class)
            .headers().frameOptions().disable()
            .and().csrf().disable().cors();
        }else{
            http.authorizeRequests()
            .antMatchers("/**").permitAll()
            //.antMatchers( "/backoffice/**").hasAnyRole("OP","DEV")
            .anyRequest().authenticated()
            .and()
            .headers().frameOptions().disable()
            .and().csrf().disable().cors();
            
        }
        
    }
    
    @Bean
    public AuthenticationEntryPoint customAccessDenyEntryPoint() {
        CustomAccessDenyEntryPoint customAuthEntryPoint = new CustomAccessDenyEntryPoint();
        return customAuthEntryPoint;
    }

    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.authenticationProvider(customAuthenticationProvider);
    }
//    @Autowired
//    protected void configureGlobal(AuthenticationManagerBuilder auth)
//            throws Exception {
//        auth.userDetailsService(userDetailsService())
//        .passwordEncoder(passwordEncoder);
//    }
    
    // Only necessary to have access to verify the AuthenticationManager
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
    @Bean
    public FilterSecurityInterceptor filterSecurityInterceptor() throws Exception{
        
        // FilterSecurityInterceptor
        FilterSecurityInterceptor filterSecurityInterceptor = new FilterSecurityInterceptor();
        filterSecurityInterceptor.setAuthenticationManager(authenticationManagerBean());
        filterSecurityInterceptor.setAccessDecisionManager(new CustomAccessDecisionManager());
        // SecurityExpressionHandler
        SecurityExpressionHandler<FilterInvocation> securityExpressionHandler = new DefaultWebSecurityExpressionHandler();

        LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> map = new LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>();
        MyFilterSecurityMetadataSource ms = new MyFilterSecurityMetadataSource();
        filterSecurityInterceptor.setSecurityMetadataSource(ms);
        try {
            filterSecurityInterceptor.afterPropertiesSet();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filterSecurityInterceptor;
    }
    
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web
//            .ignoring()
//            .antMatchers("/resource/**");
//        web.securityInterceptor(filterSecurityInterceptor());
//    }
     
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(ImmutableList.of("*"));
        configuration.setAllowedMethods(ImmutableList.of("HEAD",
                "GET", "POST", "PUT", "DELETE", "PATCH"));
        // setAllowCredentials(true) is important, otherwise:
        // The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'.
        configuration.setAllowCredentials(true);
        // setAllowedHeaders is important! Without it, OPTIONS preflight request
        // will fail with 403 Invalid CORS request
        configuration.setAllowedHeaders(ImmutableList.of("Authorization", "Cache-Control", "Content-Type"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
