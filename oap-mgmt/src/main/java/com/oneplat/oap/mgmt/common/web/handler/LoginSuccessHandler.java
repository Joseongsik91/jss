package com.oneplat.oap.mgmt.common.web.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oneplat.oap.core.model.AbstractObject;
import com.oneplat.oap.core.mybatis.enums.YesNoType;
import com.oneplat.oap.mgmt.common.model.AuthenticationInjector;
import com.oneplat.oap.mgmt.login.model.LoginOperator;
import com.oneplat.oap.mgmt.login.service.LoginService;
import com.oneplat.oap.mgmt.setting.operator.model.LoginHistory;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    protected MessageSourceAccessor accessor;
    @Autowired LoginService loginService;
    @Autowired 
    AuthenticationInjector authenticationInjector;
    @ResponseStatus(HttpStatus.OK)
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth) throws IOException, ServletException
    {
    		//로그인 성공했을때 로그인 히스토리에 정보 집어넣는 과정입니다. 2020.03.05 조성식
    		final Logger logger = LoggerFactory.getLogger(CustomAuthenticationProvider.class);
    		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    		LoginHistory loginHistory = new LoginHistory();
    		LoginOperator loginOperator = new LoginOperator();
    		AbstractObject model = new LoginHistory();
    		loginOperator = loginService.searchLoginOperator(auth.getName());
    		String insideYn = request.getParameter("insideYn")==null?"N":(String)request.getParameter("insideYn");
    		loginHistory.setOperatorNumber(loginOperator.getOperatorNumber());
            loginHistory.setLoginId(auth.getName());
            loginHistory.setInsideYn(YesNoType.valueOf(insideYn));
            loginHistory.setAccessIp4Addr(request.getRemoteAddr());
            model = loginHistory;
            try{
            	if(authentication == null || "anonymousUser".equals(auth.getName())) {
            		model.setCreateId("backoffice");
            		model.setModifyId("backoffice");
            	}else {
            		model.setCreateId(auth.getName());
            		model.setModifyId(auth.getName());
            		model.getCreateId();
            	}
//                authenticationInjector.setAuthentication(loginHistory);
                loginService.createLoginHistory((LoginHistory) model);
            }catch(Exception e){
                e.printStackTrace();
                logger.error("insert LoginHistory Error!!");
            }
            //로그인 성공했을때 로그인 히스토리에 정보 집어넣는 과정입니다.
            
            
            ObjectMapper mapper = new ObjectMapper();
            ModelMap mm = new ModelMap();
            mm.put("data",auth.getPrincipal());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(mapper.writeValueAsString(mm));
            response.getWriter().flush();
            response.getWriter().close();
    }
}