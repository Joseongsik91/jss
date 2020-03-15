package com.oneplat.oap.mgmt.common.web.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpStatus;
import com.oneplat.oap.core.exception.ExceptionControllerAdvice.ExceptionVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginOutSuccessHandler implements LogoutSuccessHandler
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    protected MessageSourceAccessor accessor;
    
    
    @Override
    public void onLogoutSuccess(HttpServletRequest request,
            HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        ObjectMapper mapper = new ObjectMapper();
        // TODO Auto-generated method stub
        response.setContentType("application/json;charset=UTF-8");

        response.setStatus(HttpStatus.SC_ACCEPTED);
        response.getWriter().write(mapper.writeValueAsString(new ExceptionVo("0", "OK")));
        response.getWriter().flush();
        response.getWriter().close();
        
    }
}