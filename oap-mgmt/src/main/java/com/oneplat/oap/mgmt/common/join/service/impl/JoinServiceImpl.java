package com.oneplat.oap.mgmt.common.join.service.impl;

import com.oneplat.oap.mgmt.common.join.service.JoinService;
import com.oneplat.oap.mgmt.setting.admin.model.Role;
import com.oneplat.oap.mgmt.setting.operator.mapper.LoginHistoryMapper;
import com.oneplat.oap.mgmt.setting.operator.mapper.OperatorMapper;
import com.oneplat.oap.mgmt.setting.operator.model.Operator.CreateOperator;
import com.oneplat.oap.mgmt.setting.operator.model.OperatorCode;
import com.oneplat.oap.mgmt.setting.operator.service.OperatorService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JoinServiceImpl implements JoinService{
    
    @Autowired
    OperatorMapper oprMapper;
    
    @Autowired
    OperatorService oprService;

    @Autowired
    LoginHistoryMapper loginHistoryMapper;
    
    @Override
    public int searchIdCount(String id) {
        // TODO Auto-generated method stub
        return oprMapper.getLoginIdCount(id);
    }

    @Override
    @Transactional
    public CreateOperator createOperator(CreateOperator createOperator) {
        // TODO Auto-generated method stub
        
        createOperator.setOperatorStateCode(OperatorCode.MC_OPR_STATE_STAND_BY); // 가입신청대기
        
        /*List<Role> roleSelectList = createOperator.getRoleSelectList();
        boolean isCustomerInfoTreat = false;
        boolean isSellerInfoTreat = false;
        StringBuffer sb = new StringBuffer();
        if(roleSelectList!=null && roleSelectList.size()>0){
            // 고객정보 취급대상 내용을 조회하여 자동 설정하도록함
            List<Role> roleInfoList = oprMapper.selectRoleInfoInfoList(roleSelectList);
            if(roleInfoList!=null && roleInfoList.size()>0){
                for(Role role : roleInfoList){
                    sb.append(StringUtils.isNotEmpty(sb.toString())?", ":"");
                    sb.append(role.getRoleName());
                    if(role.getCustomerInfoTreatYn()) isCustomerInfoTreat = true;
                    if(role.getSellerInfoTreatYn()) isSellerInfoTreat = true;
                    
                    if(isCustomerInfoTreat && isSellerInfoTreat) break;
                }
            }
        }*/
        createOperator.setCustomerInfoTreatYn(false);
        createOperator.setSellerInfoTreatYn(false);
        oprService.createOperator(createOperator);
        
        // 담당자 가입 신청승인 요청 메일(to 관리자)
        oprService.sendApprovalReqeustEamil(createOperator);

        return createOperator;
    }

}
