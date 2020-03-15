package com.oneplat.oap.mgmt.common.join.service;


import com.oneplat.oap.mgmt.setting.operator.model.Operator.CreateOperator;

public interface JoinService {

    int searchIdCount(String id);
    CreateOperator createOperator(CreateOperator createOperator);
    
}
