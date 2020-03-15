package com.oneplat.oap.mgmt.login.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oneplat.oap.core.exception.NotFoundException;
import com.oneplat.oap.mgmt.login.mapper.LoginOperatorMapper;
import com.oneplat.oap.mgmt.login.model.LoginOperator;
import com.oneplat.oap.mgmt.login.service.LoginService;
import com.oneplat.oap.mgmt.setting.operator.model.LoginHistory;

@Service
public class LoginServiceImpl implements LoginService {

    private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);
    
    @Autowired LoginOperatorMapper loginOperatorMapper;
    

    @Override
    public LoginOperator searchLoginOperator(String loginId) {
        // TODO Auto-generated method stub
        return loginOperatorMapper.selectLoginOperator(loginId);
    }
    
    @Override
    public void  modifyLoginOperator(LoginOperator loginOperator) {
        // TODO Auto-generated method stub
        if(loginOperatorMapper.updateOperatorLoginInfo(loginOperator) ==0){
            throw new NotFoundException();
        }
    }
    
    @Override
    public void createLoginHistory(LoginHistory loginHistory) {
    	loginOperatorMapper.insertLoginHistory(loginHistory);
    }

    
}
