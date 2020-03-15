package com.oneplat.oap.mgmt.exception;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.oneplat.oap.core.exception.ServiceException;
import com.oneplat.oap.mgmt.common.config.ApiConstants;
import com.oneplat.oap.core.exception.ExceptionControllerAdvice;

@ControllerAdvice
public class CustomExceptionControllerAdvice extends ExceptionControllerAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomExceptionControllerAdvice.class);
    
    @Autowired
    MessageSourceAccessor msa;
    
    @ExceptionHandler({ServiceException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody ExceptionVo handleCacheInterfaceException(HttpServletResponse response, ServiceException e) {
        //LOGGER.debug("[CustomExceptionControllerAdvice] handleCacheInterfaceException : {} ", e.getCode());
        LOGGER.error("[CustomExceptionControllerAdvice] Exception has caught.", e);
        if(ApiConstants.DATA_INTERFACE_FAIL.equals(e.getCode())) {
            return new ExceptionVo(e.getCode(), e.getArgs()[0]+"");
        }else {
            return handleUnknownException(response, e);
        }
    }
}
