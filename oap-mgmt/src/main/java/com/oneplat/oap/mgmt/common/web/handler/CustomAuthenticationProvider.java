package com.oneplat.oap.mgmt.common.web.handler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.oneplat.oap.core.exception.ServiceException;
import com.oneplat.oap.core.mybatis.enums.YesNoType;
import com.oneplat.oap.mgmt.common.model.AuthenticationInjector;
import com.oneplat.oap.mgmt.login.model.LoginOperator;
import com.oneplat.oap.mgmt.login.model.LoginUser;
import com.oneplat.oap.mgmt.login.service.LoginService;
import com.oneplat.oap.mgmt.setting.operator.model.LoginHistory;
import com.oneplat.oap.mgmt.setting.operator.model.OperatorCode;
import com.oneplat.oap.mgmt.setting.operator.service.OperatorService;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationProvider.class);

    @Autowired OperatorService operatorService;
    @Autowired LoginService loginService;
    
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    MessageSourceAccessor msa;
    
    @Autowired 
    AuthenticationInjector authenticationInjector;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String operatorname = authentication.getName();
        String password = (String) authentication.getCredentials();
        
        
        
        String insideYn = request.getParameter("insideYn")==null?"N":(String)request.getParameter("insideYn");
        Collection<? extends GrantedAuthority> authorities;
        LoginOperator loginOperator = new LoginOperator();
        if(insideYn.equals("Y")){
            /*
             * To do Something
             */
        }
        loginOperator = loginService.searchLoginOperator(operatorname);
        
        if (loginOperator == null)
            throw new UsernameNotFoundException(msa.getMessage(ServiceException.USER_NOT_FOUND_ERROR_CODE).split(";")[1]);
        
        logger.info("operatorname : " + operatorname + " / password : " + password);
        logger.info("operatorname : " + loginOperator.getLoginId() + " / password : " + loginOperator.getLoginPassword());
        
        if (!passwordEncoder.matches(password,loginOperator.getLoginPassword())){
        	
            
            int failCount = loginOperator.getLoginFailCount();
            if(failCount<10){
            	failCount = loginOperator.getLoginFailCount() + 1 ;
            	loginOperator.setLoginFailCount(failCount);
            	loginService.modifyLoginOperator(loginOperator);
            }
            if(failCount >= 10){ //로그인 실패10회이상
            	loginOperator.setAcountLockYn("Y");
            	loginService.modifyLoginOperator(loginOperator);
                if(OperatorCode.MC_OPR_ACCOUNT_LOCK.getCode().equals(loginOperator.getAcountLockYn()))
                	throw new LockedException(msa.getMessage(ServiceException.LOCK_ID_ERROR_CODE).split(";")[1]);
                else
                	throw new DisabledException(msa.getMessage(ServiceException.DISABLED_ID_ERROR_CODE).split(";")[1]);
            }
            Object[] args = {failCount};
            throw new BadCredentialsException(msa.getMessage(ServiceException.NOT_MATCH_PASSWORD_ERROR_CODE, args).split(";")[1]);
        }else{
        	//관리자 승인 안한 계정일 경우 에러 메세지
        	if(!loginService.searchLoginOperator(loginOperator.getLoginId()).getOperatorStateCode().equals("MC_OPR_STATE_04")) {
        		throw new DisabledException(msa.getMessage(ServiceException.DISABLED_ID_ERROR_CODE).split(";")[1]);
        	}
            if(loginOperator.getLoginFailCount()>=10){
                throw new LockedException(msa.getMessage(ServiceException.LOCK_ID_ERROR_CODE).split(";")[1]);
            }else{
            	if(loginOperator.getLoginFailCount()!=0){
            		loginOperator.setLoginFailCount(0);
                    loginService.modifyLoginOperator(loginOperator);
            	}
            }
        }
        
        authorities = getAuthorities();
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;        
        boolean enabled = (OperatorCode.MC_OPR_STATE_NORMAL.getCode().equals(loginOperator.getOperatorStateCode()) ?true:false);
        LoginHistory loginHistory = new LoginHistory();
        Date now = new Date();
        loginHistory.setOperatorNumber(loginOperator.getOperatorNumber());
        loginHistory.setLoginId(loginOperator.getLoginId());
        loginHistory.setInsideYn(YesNoType.valueOf(insideYn));
        loginHistory.setAccessIp4Addr(request.getRemoteAddr());
        try{
            authenticationInjector.setAuthentication(loginHistory);
            loginService.createLoginHistory(loginHistory);
        }catch(Exception e){
            e.printStackTrace();
            logger.error("insert LoginHistory Error!!");
        }
        
        
        return new UsernamePasswordAuthenticationToken(new LoginUser(
                String.valueOf(loginOperator.getLoginId()), 
                loginOperator.getLoginPassword(),
                enabled,
                accountNonExpired,
                credentialsNonExpired,
                accountNonLocked,
                getAuthorities(),
                new ArrayList<String>(),
                loginOperator.getOperatorNumber(),
                loginOperator.getLoginId(), 
                loginOperator.getOperatorName()
                ), password, authorities);
            
    }
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> auth = new ArrayList<GrantedAuthority>();
        
            auth.add(new SimpleGrantedAuthority("ROLE_USER"));
        
        return auth;
    }
    @Override
    public boolean supports(Class<?> arg0) {
        return true;
    }
}