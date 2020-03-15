package com.oneplat.oap.mgmt.login.service;

import com.oneplat.oap.mgmt.login.model.LoginOperator;
import com.oneplat.oap.mgmt.setting.operator.model.LoginHistory;

public interface LoginService {
    LoginOperator searchLoginOperator(String loginId);
    
    public void  modifyLoginOperator(LoginOperator loginOperator);
    
    public void createLoginHistory(LoginHistory loginHistory);
}
