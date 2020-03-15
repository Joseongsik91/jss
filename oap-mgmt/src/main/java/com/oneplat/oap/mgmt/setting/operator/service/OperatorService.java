package com.oneplat.oap.mgmt.setting.operator.service;

import java.util.List;

import com.oneplat.oap.core.model.SearchRequest;
import com.oneplat.oap.mgmt.setting.operator.model.Operator;
import com.oneplat.oap.mgmt.setting.operator.model.Operator.CreateOperator;
import com.oneplat.oap.mgmt.setting.operator.model.Operator.ModifyOperator;
import com.oneplat.oap.mgmt.setting.operator.model.Operator.OperatorApprove;
import com.oneplat.oap.mgmt.setting.operator.model.Operator.OperatorDetail;
import com.oneplat.oap.mgmt.setting.operator.model.Operator.ResetOperatorPassword;
import com.oneplat.oap.mgmt.setting.operator.model.OperatorCode;
import com.oneplat.oap.mgmt.setting.operator.model.ResponseLoginHistory;
import com.oneplat.oap.mgmt.setting.operator.model.ResponseOperator;
import com.oneplat.oap.mgmt.setting.operator.model.enums.OperatorGroup;

public interface OperatorService {

    ResponseOperator searchOperatorList(SearchRequest searchRequest);

    List<Operator> searchOperatorListExcel(SearchRequest searchRequest);

    ResponseOperator searchApprovalStandByList(SearchRequest searchRequest);

    int getCount(String stateCode);

    OperatorDetail searchOperator(Long oprNumber);

    OperatorDetail searchOperator(String loginId);

    String searchAutoMakePassword();

    CreateOperator createOperator(CreateOperator createOpr);

    int modifyOperator(ModifyOperator modifyOpr);

    int modifyOperatorApprove(OperatorApprove modifyOpr);

    int removeOperator(Long operatorNumber);

    ResponseLoginHistory searchLoginHistoryList(SearchRequest searchRequest);

    void modifyOperatorPassword(ResetOperatorPassword passwordModel);

    public List<Long> getOperatorNumberListByOperatorGroup(OperatorGroup operatorGroup);

    void sendApprovalReqeustEamil(CreateOperator createOperator);

}
