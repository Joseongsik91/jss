package com.oneplat.oap.mgmt.setting.operator.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oneplat.oap.core.exception.AlreadySendAuthException;
import com.oneplat.oap.mgmt.common.model.AuthenticationInjector;
import com.oneplat.oap.mgmt.setting.operator.mapper.OperatorAuthMapper;
import com.oneplat.oap.mgmt.setting.operator.model.OperatorAuth;
import com.oneplat.oap.mgmt.setting.operator.service.OperatorAuthService;

@Service
public class OperatorAuthServiceImpl implements OperatorAuthService{
    
    @Autowired
    private OperatorAuthMapper operatorAuthMapper;
    
    @Autowired
    private AuthenticationInjector authenticationInjector;
    
    @Override
    public OperatorAuth getOperatorAuth(Long operatorNumber){
        return operatorAuthMapper.selectOperatorAuth(operatorNumber);
    }
    @Override
    public int getOperatorAuthCnt(Long operatorNumber){
        return operatorAuthMapper.selectOperatorAuthCnt(operatorNumber);
    }
    @Override
    public void createOperatorAuth(OperatorAuth operatorAuth){
        if(operatorAuthMapper.selectOperatorAuthCnt(operatorAuth.getOperatorNumber())>0){
            throw new AlreadySendAuthException();
        }
        authenticationInjector.setAuthentication(operatorAuth);
        operatorAuthMapper.insertOperatorAuth(operatorAuth);
    }

}
