package com.oneplat.oap.mgmt.setting.operator.service;

import com.oneplat.oap.mgmt.setting.operator.model.OperatorAuth;

public interface OperatorAuthService {

    OperatorAuth getOperatorAuth(Long operatorNumber);
    int getOperatorAuthCnt(Long operatorNumber);
    void createOperatorAuth(OperatorAuth operatorAuth);

}
