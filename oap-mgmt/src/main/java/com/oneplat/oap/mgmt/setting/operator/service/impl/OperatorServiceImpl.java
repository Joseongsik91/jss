package com.oneplat.oap.mgmt.setting.operator.service.impl;

import com.oneplat.oap.core.exception.NotFoundException;
import com.oneplat.oap.core.model.SearchRequest;
import com.oneplat.oap.core.util.DateHelper;
import com.oneplat.oap.core.util.MaskingString;
import com.oneplat.oap.core.util.RandomStringBuilder;
import com.oneplat.oap.mgmt.common.email.model.MailRequest;
import com.oneplat.oap.mgmt.common.email.model.enums.EmailTemplateCode;
import com.oneplat.oap.mgmt.common.email.service.EmailSendService;
import com.oneplat.oap.mgmt.common.model.AuthenticationInjector;
import com.oneplat.oap.mgmt.setting.admin.mapper.GroupManagementMapper;
import com.oneplat.oap.mgmt.setting.admin.model.OperatorRole;
import com.oneplat.oap.mgmt.setting.admin.model.Role;
import com.oneplat.oap.mgmt.setting.operator.mapper.LoginHistoryMapper;
import com.oneplat.oap.mgmt.setting.operator.mapper.OperatorMapper;
import com.oneplat.oap.mgmt.setting.operator.model.*;
import com.oneplat.oap.mgmt.setting.operator.model.Operator.*;
import com.oneplat.oap.mgmt.setting.operator.model.enums.OperatorGroup;
import com.oneplat.oap.mgmt.setting.operator.service.OperatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class OperatorServiceImpl implements OperatorService{
    
    @Autowired
    OperatorMapper oprMapper;

    @Autowired
    LoginHistoryMapper loginHistoryMapper;
    
    @Autowired
    PasswordEncoder endcoder;
    
    @Autowired 
    AuthenticationInjector authenticationInjector;
    
    @Autowired
    GroupManagementMapper roleMapper;
    
    @Autowired
    MessageSourceAccessor msa;

    @Autowired
    private Environment env;

    @Autowired
    private EmailSendService emailSendService;
    
    @Override
    public ResponseOperator searchOperatorList(SearchRequest searchRequest) {
        // TODO Auto-generated method stub
        ResponseOperator responseOperator = new ResponseOperator();

        // 전체 담당자 목록
        List<Operator> list = oprMapper.selectOprList(searchRequest);
        responseOperator.setData(list);
        
        // 전체담당자수
        if(searchRequest.getPageInfo()!=null){
            searchRequest.getPageInfo().setResultCount(list.size());
			searchRequest.getPageInfo().setTotalCount(oprMapper.getOprListCount(searchRequest));
        }
        
        return responseOperator;
    }

    @Override
    public List<Operator> searchOperatorListExcel(SearchRequest searchRequest) {
        // TODO Auto-generated method stub
        return oprMapper.selectOprList(searchRequest);
    }
    
    @Override
    public int getCount(String stateCode){
        return oprMapper.getOprCount(stateCode);
    }

    @Override
    public ResponseOperator searchApprovalStandByList(SearchRequest searchRequest) {
        // TODO Auto-generated method stub
        ResponseOperator responseOpr = new ResponseOperator();

        // 승인대기/반려 목록
        List<Operator> list = oprMapper.selectApprovalStandByList(searchRequest);
        responseOpr.setData(list);
        
        // 승인대기/반려 목록 전체건수
        if(searchRequest.getPageInfo()!=null){
            searchRequest.getPageInfo().setResultCount(list.size());
			searchRequest.getPageInfo().setTotalCount(oprMapper.getApprovalStandByListCount(searchRequest));
        }

        return responseOpr;
    }

    @Override
    public OperatorDetail searchOperator(Long operatorNumber) {
        // TODO Auto-generated method stub
        Operator operator = new Operator();
        operator.setOperatorNumber(operatorNumber);
        
        return oprMapper.selectOperator(operator);
    }

    @Override
    public OperatorDetail searchOperator(String loginId) {
        // TODO Auto-generated method stub
        Operator operator = new Operator();
        operator.setLoginId(loginId);
        
        return oprMapper.selectOperator(operator);
    }


    @Override
    public String searchAutoMakePassword() {
        // TODO Auto-generated method stub
         return new RandomStringBuilder().setLength(16).build();
    }

    @Override
    @Transactional
    public CreateOperator createOperator(CreateOperator createOperator) {
        // TODO Auto-generated method stub
        // [jira:UPPP-113] 
        // - 관리자에 의한 수동등록시 회원상태는 정상으로 처리
        // - 회원상태가 반려인 아이디의 경우 동일 아이디로 재등록이 가능하도록 처리 
        
        // 아이디 중복여부 확인(반려,삭제 아이디 제외)
        if(oprMapper.getLoginIdCount(createOperator.getLoginId())>0){
            throw new ValidationException("duple");
        }
        
        // 비밀번호 암호화
        String password = createOperator.getLoginPassword();
        String encodingPassword = endcoder.encode(password);
        createOperator.setLoginPassword(encodingPassword);
        
        // MC_OPR INSERT(관리자에 의한 수동등록시 회원상태는 정상으로 처리)
        authenticationInjector.setAuthentication(createOperator);
        int resultCount = oprMapper.createOperator(createOperator);
        
        // MC_OPR_ROLE(담당자-그룹) INSERT(관리자에 의한 수동등록시 회원상태는 정상으로 처리)
        List<Role> roleList = createOperator.getRoleSelectList();
        if(roleList!=null){
            resultCount += oprMapper.insertOprRoleList(createOperator);
        }
        
        // MC_OPR_PERSON_INFO(담당자-개인정보) INSERT
        Long operatorNumber = createOperator.getOperatorNumber();
        String createId = createOperator.getCreateId();
       
        List<OperatorPersonInfo> oprPersonInfoList = new ArrayList<OperatorPersonInfo>();
        
        List<String> managerList = createOperator.getManagerPersonInfoCheckedList();
            
        if(managerList != null){
            for(String personInfoTreatCode : managerList){
                OperatorPersonInfo info = new OperatorPersonInfo();
                info.setPersonInfoSectionCode(OperatorCode.MC_PERSON_INFO_SECTION_03.getCode());
                info.setPersonInfoTreatCode(personInfoTreatCode);
                info.setOperatorNumber(operatorNumber);
                info.setCreateId(createId);
                oprPersonInfoList.add(info);
            }
        }
        
        // 개인정보취급 목록 저장
        if(oprPersonInfoList != null && oprPersonInfoList.size() > 0){
            resultCount += oprMapper.insertOprPersonInfoList(oprPersonInfoList);
        }


        /**
         * SunHaLee
         * Create Join Mail
         * */

        HashMap<String, Object> map = new HashMap<String, Object>();
        if(resultCount != 0 && !createOperator.getOperatorStateCode().getCode().equals("MC_OPR_STATE_01")){
            map.put("name", createOperator.getOperatorName());
            map.put("id", createOperator.getLoginId());
            map.put("tempPassword", password);
            String[] receiverEmails = new String[]{createOperator.getEmail()};
            MailRequest mailRequest = new MailRequest(EmailTemplateCode.OPERATOR_JOIN_CREATE, receiverEmails, map);
            emailSendService.send(mailRequest);
        }

        return createOperator;
    }

    @Override
    @Transactional
    public int modifyOperator(ModifyOperator modifyOpr) {
        // TODO Auto-generated method stub
        // 변경내용이 있는지 확인해야함
        Operator operator = new Operator();
        operator.setOperatorNumber(modifyOpr.getOperatorNumber());

        OperatorDetail oprDetail = oprMapper.selectOperator(operator);
        int resultCount = 0;
        String changePassword = modifyOpr.getLoginPassword();
        
        if(oprDetail==null){
            throw new NotFoundException();
        }else{
            authenticationInjector.setAuthentication(modifyOpr);
            Long oprNumber = modifyOpr.getOperatorNumber();
            String createId = modifyOpr.getCreateId();
            
            // 대기이외에 승인/반려 요청이 들어 올 경우 에러처리
            if(!OperatorCode.MC_OPR_STATE_STAND_BY.equals(oprDetail.getOperatorStateCode()) && ( OperatorCode.MC_OPR_STATE_REJECT.equals(modifyOpr.getOperatorStateCode())
                                                                                    || OperatorCode.MC_OPR_STATE_NORMAL.equals(modifyOpr.getOperatorStateCode()) ) ){
                if(oprDetail.getOperatorStateCode()!=modifyOpr.getOperatorStateCode()){
                    // TODO 에러유형 변경
                    throw new NotFoundException();
                }
            }
           
            if(!"".equals(modifyOpr.getLoginPassword())
            		|| !modifyOpr.getLoginId().equals(oprDetail.getLoginId())//loginid가 같거나
            		|| !modifyOpr.getOperatorName().equals(oprDetail.getOperatorName())//
                    || !modifyOpr.getDept().equals(oprDetail.getDept())
                    || !modifyOpr.getPosition().equals(oprDetail.getPosition())
                    || !modifyOpr.getEmail().equals(oprDetail.getEmail())
                    || !modifyOpr.getCellPhoneNum().equals(oprDetail.getCellPhoneNum())
                    || !modifyOpr.getExtensionPhoneNumber().equals(oprDetail.getExtensionPhoneNumber())
                    || !modifyOpr.getNote().equals(oprDetail.getNote())
                    || !modifyOpr.getAcountLockYn().equals(oprDetail.getAcountLockYn())
                    || !modifyOpr.getNickName().equals(oprDetail.getNickName())
                    
                    || ( modifyOpr.getOperatorStateCode()!=null && ( OperatorCode.MC_OPR_STATE_REJECT.equals(modifyOpr.getOperatorStateCode())
                                                                || OperatorCode.MC_OPR_STATE_NORMAL.equals(modifyOpr.getOperatorStateCode()) ) ))
                    {
                
                // 반려시 사유필수
                if(modifyOpr.getOperatorStateCode()!=null && OperatorCode.MC_OPR_STATE_REJECT.equals(modifyOpr.getOperatorStateCode()) ){
                    if(StringUtils.isEmpty(modifyOpr.getOperatorStateDesc())){
                        throw new NotFoundException();
                    }
                }
                
                // 비번 변경시 MC_PASSWD_HIST UPDATE
                if(!StringUtils.isEmpty(changePassword)){
                    //암호화
                    String encodingPassword = endcoder.encode(changePassword);
                    modifyOpr.setLoginPassword(encodingPassword);
                    
                    resultCount += oprMapper.updatePasswdHist(modifyOpr);
                    resultCount += oprMapper.insertPasswdHist(modifyOpr);

                    /**
                     * SunHaLee
                     * Modify Password Mail
                     * */
                    if(modifyOpr.getNewPasswordCheck() == null || modifyOpr.getNewPasswordCheck() == ""){
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        if(resultCount != 0){
                            map.put("name", modifyOpr.getOperatorName());
                            map.put("tempPassword", changePassword);
                            String[] receiverEmails = new String[]{modifyOpr.getEmail()};
                            MailRequest mailRequest = new MailRequest(EmailTemplateCode.OPERATOR_JOIN_PASSWORD, receiverEmails, map);
                            emailSendService.send(mailRequest);
                        }
                    }
                }
            
                // MC_OPR UPDATE
                resultCount += oprMapper.updateOperator(modifyOpr);

                // MC_OPR_HIST UPDATE
                resultCount += oprMapper.updateOprHist(modifyOpr);
                
                // MC_OPR_HIST INSERT
                resultCount += oprMapper.insertOprHist(oprNumber);
                
            }

            // 담당업무(List기준으로 db에서 기존보유여부에 따라 갱신됨)
            resultCount += oprMapper.removeOprRoleList(modifyOpr);
            
            List<Role> roleList = modifyOpr.getRoleSelectList();
            if(roleList!=null && roleList.size()>0){
                // 등록
                CreateOperator createOperator = new CreateOperator();
                createOperator.setCreateId(createId);
                createOperator.setOperatorNumber(oprNumber);
                createOperator.setRoleSelectList(roleList);
                resultCount += oprMapper.insertOprRoleList(createOperator);
            }
           
            List<OperatorPersonInfo> oprPersonInfoList = new ArrayList<OperatorPersonInfo>();
            
            List<String> managerList = modifyOpr.getManagerPersonInfoCheckedList();
                
            if(managerList != null && managerList.size()>0){
                for(String personInfoTreatCode : managerList){
                    OperatorPersonInfo info = new OperatorPersonInfo();
                    info.setPersonInfoSectionCode(OperatorCode.MC_PERSON_INFO_SECTION_03.getCode());
                    info.setPersonInfoTreatCode(personInfoTreatCode);
                    info.setOperatorNumber(oprNumber);
                    info.setCreateId(createId);
                    oprPersonInfoList.add(info);
                }
            }
            
            // 개인정보취급코드의 변경 이력 업데이트건이 있을 경우 이력등록 및 업데이트
            if(oprPersonInfoList != null && oprPersonInfoList.size() > 0){
                // 조건에 ON DUPLICATE KEY UPDATE 가 추가되어 데이타가 있는 경우 갱신되지 않는다
                modifyOpr.setOprPersonInfoList(oprPersonInfoList);
                resultCount += oprMapper.updateOprPersonInfoList(modifyOpr);
                resultCount += oprMapper.insertOprPersonInfoList(oprPersonInfoList);
            }else{
                oprMapper.removeOprPersonInfoList(modifyOpr);
            }

            //반려 메일 전송
            if(modifyOpr.getOperatorStateCode()!=null && OperatorCode.MC_OPR_STATE_REJECT.equals(modifyOpr.getOperatorStateCode()) ){
                //승인 메일 전송
                HashMap<String,Object> map = new HashMap<String,Object>();
                map.put("name", modifyOpr.getOperatorName());
                map.put("rejectReson", modifyOpr.getOperatorStateDesc());
                String[] receiverEmails = new String[]{modifyOpr.getEmail()};
                MailRequest mailRequest = new MailRequest(EmailTemplateCode.OPERATOR_JOIN_REJECT, receiverEmails, map);
                emailSendService.send(mailRequest);
            }else if(modifyOpr.getOperatorStateCode()!=null && OperatorCode.MC_OPR_STATE_NORMAL.equals(modifyOpr.getOperatorStateCode()) ){
                //승인 메일 전송
                HashMap<String,Object> map = new HashMap<String,Object>();
                map.put("name", modifyOpr.getOperatorName());
                String[] receiverEmails = new String[]{modifyOpr.getEmail()};
                MailRequest mailRequest = new MailRequest(EmailTemplateCode.OPERATOR_JOIN_COMPLETE, receiverEmails, map);
                emailSendService.send(mailRequest);
            }
        }
        
        return resultCount;
    }
    
    @Override
    public int modifyOperatorApprove(OperatorApprove modifyOprApprove) {
        // TODO Auto-generated method stub
        authenticationInjector.setAuthentication(modifyOprApprove);
        Operator operator = new Operator();
        operator.setOperatorNumber(modifyOprApprove.getOperatorNumber());

        OperatorDetail oprDetail = oprMapper.selectOperator(operator);
        if(!OperatorCode.MC_OPR_STATE_STAND_BY.equals(oprDetail.getOperatorStateCode())){
            throw new NotFoundException();
        }
            
        // 반려시 사유필수
        if(modifyOprApprove.getOperatorStateCode()!=null && OperatorCode.MC_OPR_STATE_REJECT.equals(modifyOprApprove.getOperatorStateCode()) ){
            if(StringUtils.isEmpty(modifyOprApprove.getOperatorStateDesc())){
                throw new NotFoundException();
            }
        }
        
        ModifyOperator modifyOpr = new ModifyOperator();
        modifyOpr.setOperatorNumber(modifyOpr.getOperatorNumber());
        authenticationInjector.setAuthentication(modifyOprApprove);
        
        // MC_OPR UPDATE
        int resultCount = oprMapper.updateOperatorApprove(modifyOpr);
        
        // MC_OPR_HIST UPDATE
        resultCount += oprMapper.updateOprHist(modifyOpr);
        
        // MC_OPR_HIST INSERT
        resultCount += oprMapper.insertOprHist(modifyOpr.getOperatorNumber());
        
        return resultCount;
    }

    @Override
    @Transactional
    public int removeOperator(Long operatorNumber) {
        // TODO Auto-generated method stub
        ModifyOperator modifyOpr = new ModifyOperator();
        modifyOpr.setOperatorNumber(operatorNumber);
        authenticationInjector.setAuthentication(modifyOpr);
        // MC_OPR_PERSON_INFO UPDATE
        int resultCount = oprMapper.removeOprPersonInfoList(modifyOpr);
        
        // MC_OPR_ROLE UPDATE
        resultCount += oprMapper.removeOprRoleList(modifyOpr);

        // MC_OPR_HIST UPDATE
        resultCount += oprMapper.updateOprHist(modifyOpr);
        
        // MC_OPR_HIST INSERT
        resultCount += oprMapper.insertOprHist(modifyOpr.getOperatorNumber());

        // MC_OPR UPDATE
        modifyOpr.setOperatorStateCode(OperatorCode.MC_OPR_STATE_DELETE);
        modifyOpr.setOperatorStateDesc("관리자 삭제");
        resultCount += oprMapper.removeOperator(modifyOpr);
        
        return resultCount;
    }

    @Override
    public ResponseLoginHistory searchLoginHistoryList(SearchRequest searchRequest) {
        // TODO Auto-generated method stub
        ResponseLoginHistory responseLoginHistory = new ResponseLoginHistory();

        // 전체 담당자 목록
        List<LoginHistory> list = loginHistoryMapper.selectLoginHistoryList(searchRequest);
        responseLoginHistory.setData(list);
        
        // 전체담당자수
        if(searchRequest.getPageInfo()!=null){
            searchRequest.getPageInfo().setTotalCount(loginHistoryMapper.getLoginHistoryListCount(searchRequest));
            searchRequest.getPageInfo().setResultCount(list==null?0:list.size());
        }
        
        return responseLoginHistory;
    }
    
    @Override
    @Transactional
    public void modifyOperatorPassword(ResetOperatorPassword passwordModel) {
        // TODO Auto-generated method stub
        String encodingPassword = endcoder.encode(passwordModel.getLoginPassword());
        passwordModel.setLoginPassword(encodingPassword);
        
        ModifyOperator modifyOpr = new ModifyOperator();
        authenticationInjector.setAuthentication(modifyOpr);
        modifyOpr.setOperatorNumber(passwordModel.getOperatorNumber());
        oprMapper.updatePasswdHist(modifyOpr);
        oprMapper.insertPasswdHist(modifyOpr);

        // MC_OPR UPDATE
        authenticationInjector.setAuthentication(passwordModel);
        oprMapper.updateOperatorPassword(passwordModel);
       
    }

    @Override
    public List<Long> getOperatorNumberListByOperatorGroup(OperatorGroup operatorGroup) {
        return oprMapper.selectOperatorListByGroupCode(operatorGroup);
    }

    @Override
    public void sendApprovalReqeustEamil(CreateOperator createOperator) {
        String platformNumber = env.getProperty("platform.role.number");
        if(!StringUtils.isEmpty(platformNumber)){
            String createDateTime = DateHelper.getCurrentDatetime("yyyy-MM-dd HH:mm:ss");

            StringBuffer sb = new StringBuffer();
            List<Role> roleList = createOperator.getRoleSelectList();
            if(roleList!=null && roleList.size()>0){
                for(Role role : roleList){
                    sb.append(!StringUtils.isEmpty(sb.toString())?", ":"");
                    sb.append(role.getRoleName());
                }
            }

            List<OperatorRole> operatorList = roleMapper.selectOprRoleList(Long.parseLong(platformNumber));
            HashMap<String,Object> map = new HashMap<String,Object>();
            if(operatorList!=null && operatorList.size()>0){
                map.put("name", createOperator.getOperatorName());
                map.put("id", MaskingString.getMaskedString(createOperator.getLoginId(), "id"));
                map.put("department", createOperator.getDept());
                map.put("position", createOperator.getPosition());
                map.put("createDate", createDateTime);
                map.put("chargeTask", sb.toString());
                String[] receiverEmails = new String[operatorList.size()];
                int index = 0;
                for(OperatorRole receiverOperator : operatorList){
                    if(!StringUtils.isEmpty(receiverOperator.getEmail())){
                        receiverEmails[index] = receiverOperator.getEmail();
                        index++;
                    }
                }
                MailRequest mailRequest = new MailRequest(EmailTemplateCode.OPERATOR_JOIN_REQUEST, receiverEmails, map);
                emailSendService.send(mailRequest);
            }
        }
    }

}
