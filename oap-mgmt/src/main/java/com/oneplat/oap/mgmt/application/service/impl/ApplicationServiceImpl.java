package com.oneplat.oap.mgmt.application.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oneplat.oap.core.model.SearchRequest;
import com.oneplat.oap.mgmt.application.mapper.ApplicationMapper;
import com.oneplat.oap.mgmt.application.model.DashboardApplications;
import com.oneplat.oap.mgmt.application.model.DcApplication;
import com.oneplat.oap.mgmt.application.model.DcApplicationAuthKey;
import com.oneplat.oap.mgmt.application.model.DcApplicationKey;
import com.oneplat.oap.mgmt.application.model.DcApplicationScope;
import com.oneplat.oap.mgmt.application.model.DcApplicationService;
import com.oneplat.oap.mgmt.application.model.DcApplicationSla;
import com.oneplat.oap.mgmt.application.model.DcApplicationSlaRequest;
import com.oneplat.oap.mgmt.application.model.DcDeveloper;
import com.oneplat.oap.mgmt.application.model.DcHistoryManagement;
import com.oneplat.oap.mgmt.application.model.UUIDGenerator;
import com.oneplat.oap.mgmt.application.service.ApplicationService;
import com.oneplat.oap.mgmt.application.support.ApplicationCacheService;
import com.oneplat.oap.mgmt.common.model.AuthenticationInjector;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationServiceImpl.class);
    
    private static final String DC_HIST_MGMT_CD_APP = "DC_HIST_MGMT_01"; // Application
    private static final String DC_HIST_MGMT_CD_KEY = "DC_HIST_MGMT_02"; // Application Key
    private static final String DC_HIST_MGMT_CD_MSG_AUTH_KEY = "DC_HIST_MGMT_03"; // Application Message Auth Key
    
    @Autowired
    private ApplicationMapper applicationMapper;
    
    @Autowired
    private AuthenticationInjector authenticationInjector;
    
    @Autowired
    private Environment env;
    
    @Autowired
    private ApplicationCacheService applicationCacheService;

    @Override
    @Transactional
    public DcApplication createApplication(DcApplication application) {
        // TODO Auto-generated method stub
        LOGGER.debug("createApplication.application : {} ", application);
        
        //========== DC_APP (애플리케이션 등록)
        authenticationInjector.setAuthentication(application);
        applicationMapper.insertApplication(application);
        LOGGER.debug("applicationNumber : {} ", application.getApplicationNumber());

        
        //========== DC_HIST_MGMT (이력 관리 등록)
        DcHistoryManagement historyManagement = new DcHistoryManagement();
        historyManagement.setHistoryManagementCode(DC_HIST_MGMT_CD_APP);
        historyManagement.setHistoryManagementMemo("create");
        authenticationInjector.setAuthentication(historyManagement);
        applicationMapper.insertHistoryManagement(historyManagement);
        LOGGER.debug("historyManagementNumber : {} ", historyManagement.getHistoryManagementNumber());

        
        //========== DC_APP_HIST (애플리케이션 이력 등록)
        applicationMapper.insertApplicationHistory(application.getApplicationNumber(), historyManagement.getHistoryManagementNumber());
        
        
        //========== DC_APP_SVC (애플리케이션 서비스 등록)
        //MC 서비스 에 등록된 서비스 정보 조회하여 등록한다.
        List<DcApplicationService> serviceList = applicationMapper.selectServiceList(application); //애플리케이션-서비스에 매핑되어 있지 않은 신규 서비스 정보만 조회
        if(serviceList != null && serviceList.size() > 0) {
            for(DcApplicationService applicationService : serviceList) {
                LOGGER.debug("applicationService : {} ", applicationService);
                applicationService.setApplicationNumber(application.getApplicationNumber());
                authenticationInjector.setAuthentication(applicationService);
                applicationMapper.insertApplicationService(applicationService);
            }
        }
        
        //========== 애플리케이션 기본 정보 조회
        application = applicationMapper.selectApplicationInfo(application.getApplicationNumber());
        
        //========== Gateway 연동
        if("Y".equals(env.getProperty("system.interfaceYn"))) {
            LOGGER.debug("■■■■■■■■■■ Gateway 연동 [createApplication] ■■■■■■■■■■");
            applicationCacheService.createApplicationCacheData(application);
        }
        
        //========== SLA 정보 조회
        LOGGER.debug("selectApplicationSlaList..");
        application.setApplicationSlaList(applicationMapper.selectApplicationSlaList(application.getApplicationNumber()));
        
        return application;
    }
    
    
    @Override
    @Transactional
    public DcApplication modifyApplication(DcApplication application) {
        // TODO Auto-generated method stub
        LOGGER.debug("modifyApplication.application : {} ", application);
        
        //========== DC_APP (애플리케이션 수정)
        authenticationInjector.setAuthentication(application);
        applicationMapper.updateApplication(application);
        LOGGER.debug("========== applicationNumber : {} ", application.getApplicationNumber());
        
        
        //========== DC_HIST_MGMT (이력 관리 등록)
        DcHistoryManagement historyManagement = new DcHistoryManagement();
        historyManagement.setHistoryManagementCode(DC_HIST_MGMT_CD_APP);
        historyManagement.setHistoryManagementMemo("modify");
        authenticationInjector.setAuthentication(historyManagement);
        applicationMapper.insertHistoryManagement(historyManagement);
        LOGGER.debug("========== historyManagementNumber : {} ", historyManagement.getHistoryManagementNumber());

        
        //========== DC_APP_HIST (애플리케이션 이력 등록)
        //1.기존 이력 갱신
        LOGGER.debug("========== modifyId : {} ", application.getModifyId());
        applicationMapper.updateApplicationHistory(application.getApplicationNumber(), application.getModifyId());
        //2.신규 이력 추가
        applicationMapper.insertApplicationHistory(application.getApplicationNumber(), historyManagement.getHistoryManagementNumber());


        //========== DC_APP_SVC (애플리케이션 서비스 등록)
        //1.기존 서비스 갱신(사용하지 않는 서비스 종료일시 갱신)
        for(String serviceNumber : application.getServiceNumberList()) {
            LOGGER.debug("■■■■■■■■■■ serviceNumber: {} ", serviceNumber);
        }
        applicationMapper.updateApplicationService(application);

        
        //2.신규 서비스 추가(정책사용으로 설정된 신규 서비스만 추려서 조회)
        List<DcApplicationService> serviceList = applicationMapper.selectServiceList(application); //애플리케이션-서비스에 매핑되어 있지 않은 신규 서비스 정보만 조회
        for(DcApplicationService applicationService : serviceList) {
            LOGGER.debug("applicationService : {} ", applicationService);
            applicationService.setApplicationNumber(application.getApplicationNumber());
            authenticationInjector.setAuthentication(applicationService);
            applicationMapper.insertApplicationService(applicationService);
        }
        
        //========== 애플리케이션 기본 정보 조회
        application = applicationMapper.selectApplicationInfo(application.getApplicationNumber());
        
        //========== Gateway 연동
        if("Y".equals(env.getProperty("system.interfaceYn"))) {
            LOGGER.debug("■■■■■■■■■■ Gateway 연동 [modifyApplication] ■■■■■■■■■■");
            applicationCacheService.modifyApplicationCacheData(application);
        }
        
        //========== SLA 정보 조회
        LOGGER.debug("selectApplicationSlaList..");
        application.setApplicationSlaList(applicationMapper.selectApplicationSlaList(application.getApplicationNumber()));
        
        return application;
    }
    
    
    @Override
    public List<DcApplication> getApplicationList(SearchRequest searchRequest) {
        // TODO Auto-generated method stub
        searchRequest.setData();
        List<DcApplication> applicationList = applicationMapper.selectApplicationList(searchRequest);
        if(searchRequest.getPageInfo() != null) {
            searchRequest.getPageInfo().setTotalCount(applicationMapper.selectApplicationListCount(searchRequest));
            searchRequest.getPageInfo().setResultCount(applicationList.size());
        }
        return applicationList;
    }

    
    @Override
    public DcApplication getApplicationInfo(Long applicationNumber) {
        // TODO Auto-generated method stub
        return applicationMapper.selectApplicationInfo(applicationNumber);
    }
    
    @Override
    public DcApplication getApplicationDetail(Long applicationNumber) {
        // TODO Auto-generated method stub
        DcApplication application = new DcApplication();
        
        //기본정보
        application = applicationMapper.selectApplicationInfo(applicationNumber);
        
        //키정보
        List<DcApplicationKey> applicationKeyList = applicationMapper.selectApplicationKeyList(applicationNumber);
        if(applicationKeyList != null && applicationKeyList.size() > 0) {
            application.setApplicationKeyList(applicationKeyList);
        }
        
        //키정보(oauth)
        List<DcApplicationAuthKey> applicationAuthList = applicationMapper.selectApplicationAuthList(applicationNumber);
        if(applicationAuthList != null && applicationAuthList.size() > 0) {
            application.setApplicationAuthList(applicationAuthList);
        }
        
        //SLA정보
        List<DcApplicationSla> applicationSlaList = applicationMapper.selectApplicationSlaList(applicationNumber);
        if(applicationSlaList != null && applicationSlaList.size() > 0) {
            application.setApplicationSlaList(applicationSlaList);
        }

        //ScopeNumber정보
        application.setScopeNumberList(applicationMapper.selectApplicationScopeNumberList(applicationNumber));
        
        return application;
    }

    @Override
    @Transactional
    public DcApplication createApplicationKey(DcApplicationKey applicationKey) {
        // TODO Auto-generated method stub
        LOGGER.debug("createApplicationKey.applicationKey : {} ", applicationKey);

        /* 유형 Server Key, Browser Key 일때만 */
        if("DC_KEY_TYPE_01".equals(applicationKey.getKeyTypeCode()) || "DC_KEY_TYPE_02".equals(applicationKey.getKeyTypeCode() )){
            /* 모든 아이피 허용  typeLimitYn*/
            if("N".equals(applicationKey.getTypeLimitYn())){
                applicationKey.setKeyTypeAttributeValue("*");
            }
        }
        /* 키 생성 */
        
        UUIDGenerator uuidGenerator = new UUIDGenerator();
        applicationKey.setApplicationKey(uuidGenerator.createUUID());

        if(!applicationKey.getMsgEncryptionTypeCode().equals("") && null != applicationKey.getMsgEncryptionTypeCode()){
            applicationKey.setMsgEncryptionKey(uuidGenerator.createUUID());
        }

        //DC_APP_KEY (Insert)
        authenticationInjector.setAuthentication(applicationKey);
        applicationMapper.insertApplicationKey(applicationKey);
        
        //DC_HIST_MGMT (Insert)
        DcHistoryManagement historyManagement = new DcHistoryManagement();
        historyManagement.setHistoryManagementCode(DC_HIST_MGMT_CD_KEY);
        historyManagement.setHistoryManagementMemo("create");
        authenticationInjector.setAuthentication(historyManagement);
        applicationMapper.insertHistoryManagement(historyManagement);
        LOGGER.debug("historyManagementNumber : {} ", historyManagement.getHistoryManagementNumber());
        applicationKey.setHistoryManagementNumber(historyManagement.getHistoryManagementNumber());
        
        //DC_APP_KEY_HIST (Insert)
        applicationMapper.insertApplicationKeyHistory(applicationKey);
        
        
        //========== Gateway 연동
        if("Y".equals(env.getProperty("system.interfaceYn"))) {
            LOGGER.debug("■■■■■■■■■■ Gateway 연동 [createApplicationKey] ■■■■■■■■■■");
            applicationCacheService.createApplicationKeyCacheData(applicationKey);
        }
        
        
        //========== 키 정보 조회(전체)
        DcApplication application = new DcApplication();
        List<DcApplicationKey> applicationKeyList = applicationMapper.selectApplicationKeyList(applicationKey.getApplicationNumber());
        application.setApplicationKeyList(applicationKeyList);
        
        return application;
    }
    
    @Override
    @Transactional
    public DcApplication modifyApplicationKey(DcApplicationKey applicationKey) {
        // TODO Auto-generated method stub
        LOGGER.debug("modifyApplicationKey.applicationKey : {} ", applicationKey);
        
        /* 키 편집 */

        // 메세지 암호화 선택안함 여부 확인
        if(!"".equals(applicationKey.getMsgEncryptionTypeCode())){
            //메세지 암호화 키 존재 하지 않는 경우
            if(applicationKey.getMsgEncryptionKey() == null || applicationKey.getMsgEncryptionKey().equals("")){
                //메세지 암호화 사용 여부 체크
                if(!applicationKey.getMsgEncryptionTypeCode().equals("") && null != applicationKey.getMsgEncryptionTypeCode()){
                    UUIDGenerator uuidGenerator = new UUIDGenerator();
                    applicationKey.setMsgEncryptionKey(uuidGenerator.createUUID());
                }
            }
        } else {
            applicationKey.setMsgEncryptionKey("");
        }

        //DC_APP_KEY (Update)
        authenticationInjector.setAuthentication(applicationKey);
        applicationMapper.updateApplicationKey(applicationKey);
        
        
        //DC_HIST_MGMT (Insert)
        DcHistoryManagement historyManagement = new DcHistoryManagement();
        historyManagement.setHistoryManagementCode(DC_HIST_MGMT_CD_KEY);
        historyManagement.setHistoryManagementMemo("edit");
        authenticationInjector.setAuthentication(historyManagement);
        applicationMapper.insertHistoryManagement(historyManagement);
        LOGGER.debug("historyManagementNumber : {} ", historyManagement.getHistoryManagementNumber());
        applicationKey.setHistoryManagementNumber(historyManagement.getHistoryManagementNumber());
        
        //DC_APP_KEY_HIST (Insert)
        //1.기존 이력 갱신
        applicationMapper.updateApplicationKeyHistory(applicationKey);
        //2.신규 이력 추가
        applicationMapper.insertApplicationKeyHistory(applicationKey);
        
        
        //========== Gateway 연동
        if("Y".equals(env.getProperty("system.interfaceYn"))) {
            LOGGER.debug("■■■■■■■■■■ Gateway 연동 [modifyApplicationKey] ■■■■■■■■■■");
            applicationCacheService.modifyApplicationKeyCacheData(applicationKey);
        }
        
        
        //========== 키 정보 조회(전체)
        DcApplication application = new DcApplication();
        List<DcApplicationKey> applicationKeyList = applicationMapper.selectApplicationKeyList(applicationKey.getApplicationNumber());
        application.setApplicationKeyList(applicationKeyList);
        

        
        return application;
    }
    
    @Override
    @Transactional
    public DcApplication reissueApplicationKey(DcApplicationKey applicationKey) {
        // TODO Auto-generated method stub
        LOGGER.debug("reissueApplicationKey.applicationKey : {} ", applicationKey);
        
        /* 키 재발급 */
        
        UUIDGenerator uuidGenerator = new UUIDGenerator();
        applicationKey.setReissueApplicationKey(uuidGenerator.createUUID()); //재발급 키        
        
        //DC_APP_KEY (Update)
        authenticationInjector.setAuthentication(applicationKey);
        applicationMapper.reissueApplicationKey(applicationKey);
        
        
        //DC_HIST_MGMT (Insert)
        DcHistoryManagement historyManagement = new DcHistoryManagement();
        historyManagement.setHistoryManagementCode(DC_HIST_MGMT_CD_KEY);
        historyManagement.setHistoryManagementMemo("reissue");
        authenticationInjector.setAuthentication(historyManagement);
        applicationMapper.insertHistoryManagement(historyManagement);
        LOGGER.debug("historyManagementNumber : {} ", historyManagement.getHistoryManagementNumber());
        applicationKey.setHistoryManagementNumber(historyManagement.getHistoryManagementNumber());
        
        //DC_APP_KEY_HIST (Insert)
        //1.기존 이력 갱신
        applicationMapper.updateApplicationKeyHistory(applicationKey);
        //2.신규 이력 추가
        applicationMapper.insertApplicationKeyHistory(applicationKey);
        
        
        //========== 키 정보 조회(전체)
        DcApplication application = new DcApplication();
        List<DcApplicationKey> applicationKeyList = applicationMapper.selectApplicationKeyList(applicationKey.getApplicationNumber());
        application.setApplicationKeyList(applicationKeyList);
        
        //========== Gateway 연동
        if("Y".equals(env.getProperty("system.interfaceYn"))) {
            LOGGER.debug("■■■■■■■■■■ Gateway 연동 [reissueApplicationKey] ■■■■■■■■■■");
            for(DcApplicationKey keyData : applicationKeyList) {
                if(keyData.getKeyTypeCode().equals(applicationKey.getKeyTypeCode()) && keyData.getApplicationKeySequence() == applicationKey.getApplicationKeySequence() && keyData.getServiceGradeCode().equals(applicationKey.getServiceGradeCode())) {
                    
                    keyData.setApplicationNumber(applicationKey.getApplicationNumber());
                    keyData.setReissueApplicationKey(applicationKey.getReissueApplicationKey());
                    
                    applicationCacheService.reissueApplicationKeyCacheData(keyData);
                    break;
                }
            }
        }
        
        return application;
    }
    
    
    @Override
    @Transactional
    public DcApplication removeApplicationKey(DcApplicationKey applicationKey) {
        // TODO Auto-generated method stub
        LOGGER.debug("removeApplicationKey.applicationKey : {} ", applicationKey);

        /* 키 삭제 */
        
        //DC_APP_KEY (Update)
        authenticationInjector.setAuthentication(applicationKey);
        applicationMapper.deleteApplicationKey(applicationKey);
        
        
        //DC_HIST_MGMT (Insert)
        DcHistoryManagement historyManagement = new DcHistoryManagement();
        historyManagement.setHistoryManagementCode(DC_HIST_MGMT_CD_KEY);
        historyManagement.setHistoryManagementMemo("remove");
        authenticationInjector.setAuthentication(historyManagement);
        applicationMapper.insertHistoryManagement(historyManagement);
        LOGGER.debug("historyManagementNumber : {} ", historyManagement.getHistoryManagementNumber());        
        applicationKey.setHistoryManagementNumber(historyManagement.getHistoryManagementNumber());
        
        //DC_APP_KEY_HIST (Insert)
        //1.기존 이력 갱신
        applicationMapper.updateApplicationKeyHistory(applicationKey);
        //2.신규 이력 추가
        applicationMapper.insertApplicationKeyHistory(applicationKey);       
        
        
        //========== Gateway 연동
        if("Y".equals(env.getProperty("system.interfaceYn"))) {
            LOGGER.debug("■■■■■■■■■■ Gateway 연동 [removeApplicationKey] ■■■■■■■■■■");
            applicationCacheService.removeApplicationKeyCacheData(applicationKey);
        }
        
        
        //========== 키 정보 조회(전체)
        DcApplication application = new DcApplication();
        List<DcApplicationKey> applicationKeyList = applicationMapper.selectApplicationKeyList(applicationKey.getApplicationNumber());
        application.setApplicationKeyList(applicationKeyList);
        
        return application;
    }    
    

    @Override
    public void createApplicationSla(DcApplicationSla applicationSla) {
        // TODO Auto-generated method stub
        LOGGER.debug("createApplicationSla.applicationSla : {} ", applicationSla);
        
        //TODO 상용 or 테스트 중 선택해서 변경 하거나 상용+테스트 둘다 변경가능하다. 분기처리 필요함..
        
        //DC_APP_SLA (Update)
        authenticationInjector.setAuthentication(applicationSla);
//        applicationMapper.updateApplicationSla(applicationSla);
        
        //DC_APP_SLA (Insert)
        
        
    }

    @Override
    public void createApplicationSlaRequest(DcApplicationSlaRequest applicationSlaRequest) {
        // TODO Auto-generated method stub
        LOGGER.debug("createApplicationSlaRequest.applicationSlaRequest : {} ", applicationSlaRequest);
        
        /* 
         * 승인 상태일 경우만 DC_APP_SLA 등록
         * */
        
        //DC_APP_SLA_MODIFY_REQUEST
        applicationMapper.insertApplicationSlaRequest(applicationSlaRequest);
    }

    @Override
    public void createDeveloper(DcDeveloper developer) {
        // TODO Auto-generated method stub
        LOGGER.debug("createDeveloper.developer : {} ", developer);
        
        //DC_DEVELOPER
        applicationMapper.insertDeveloper(developer);
        
        //TODO 개발자 이력
    }


    @Override
    public List<DcApplicationService> getSearchServiceList(SearchRequest searchRequest) {
        // TODO Auto-generated method stub
        return applicationMapper.selectSearchServiceList(searchRequest);
    }


    @Override
    @Transactional
    public DcApplication modifyApplicationSla(DcApplicationSla applicationSla) {
        // TODO Auto-generated method stub
        LOGGER.debug("modifyApplicationSla.applicationSla : {} ", applicationSla);
        
        LOGGER.debug("ApplicationNumber : " + applicationSla.getApplicationNumber());
        LOGGER.debug("ServiceNumber : " + applicationSla.getServiceNumber());
        LOGGER.debug("ApiGroupNumber : " + applicationSla.getApiGroupNumber());
        
        //시작 일시 추출
        String beginDatetime = null;
        if(applicationSla.getCommerceServiceLimitQuantity() > 0 || applicationSla.getTestServiceLimitQuantity() > 0) {
            beginDatetime = applicationMapper.selectBeginDatetime();
            applicationSla.setBeginDatetime(beginDatetime);
        }
        LOGGER.debug("========== beginDatetime ::: " + applicationSla.getBeginDatetime());
        
        if(applicationSla.getCommerceServiceLimitQuantity() > 0) {
            
            authenticationInjector.setAuthentication(applicationSla);
            applicationSla.setServiceGradeCode("DC_SVC_GRADE_01"); //상용

            //DC_APP_SLA (Update)
            int cnt = applicationMapper.updateApplicationSla(applicationSla);
            LOGGER.debug("========== commerceCount: " + cnt);
            
            //DC_APP_SLA (Insert)
            applicationSla.setServiceLimitQuantity(applicationSla.getCommerceServiceLimitQuantity());
            applicationSla.setServiceCriteriaCode(applicationSla.getCommerceServiceCriteriaCode());
            applicationMapper.insertApplicationSla(applicationSla);
        }
        if(applicationSla.getTestServiceLimitQuantity() > 0) {
            
            authenticationInjector.setAuthentication(applicationSla);
            applicationSla.setServiceGradeCode("DC_SVC_GRADE_02"); //테스트
            
            //DC_APP_SLA (Update)
            int cnt = applicationMapper.updateApplicationSla(applicationSla);
            LOGGER.debug("========== testCount: " + cnt);
            
            //DC_APP_SLA (Insert)
            applicationSla.setServiceLimitQuantity(applicationSla.getTestServiceLimitQuantity());
            applicationSla.setServiceCriteriaCode(applicationSla.getTestServiceCriteriaCode());
            applicationMapper.insertApplicationSla(applicationSla);
        }
        
        //========== SLA 정보 조회
        DcApplication application = new DcApplication();
        List<DcApplicationSla> applicationSlaList = applicationMapper.selectApplicationSlaList(applicationSla.getApplicationNumber());
        application.setApplicationSlaList(applicationSlaList);

        //========== Gateway 연동
        if("Y".equals(env.getProperty("system.interfaceYn"))) {
            LOGGER.debug("■■■■■■■■■■ Gateway 연동 [modifyApplicationSla] ■■■■■■■■■■");
            for(DcApplicationSla slaData : applicationSlaList) {
                if(slaData.getServiceNumber().equals(applicationSla.getServiceNumber()) && slaData.getApiGroupNumber().equals(applicationSla.getApiGroupNumber())) {
                    slaData.setApplicationNumber(applicationSla.getApplicationNumber());
                    applicationCacheService.modifyApplicationSlaCacheData(slaData);
                    break;
                }
            }
        }
        
        return application;
    }


    @Override
    public List<DcApplication> getApplicationDetailHistoryList(Long applicationNumber) {
        // TODO Auto-generated method stub
        return applicationMapper.selectApplicationDetailHistoryList(applicationNumber);
    }


    @Override
    public List<DcApplicationSla> getApplicationSlaHistoryList(DcApplicationSla applicationSla) {
        // TODO Auto-generated method stub
        return applicationMapper.selectApplicationSlaHistoryList(applicationSla);
    }


    @Override
    @Transactional
    public DcApplication removeApplication(DcApplication application) {
        // TODO Auto-generated method stub
        
        LOGGER.debug("removeApplication.application : {} ", application);
        
        //========== DC_APP (애플리케이션 삭제)
        authenticationInjector.setAuthentication(application);
        applicationMapper.deleteApplication(application);
        LOGGER.debug("========== applicationNumber : {} ", application.getApplicationNumber());
        

        //========== DC_HIST_MGMT (이력 관리 등록)
        DcHistoryManagement historyManagement = new DcHistoryManagement();
        historyManagement.setHistoryManagementCode(DC_HIST_MGMT_CD_APP);
        historyManagement.setHistoryManagementMemo("remove");
        authenticationInjector.setAuthentication(historyManagement);
        applicationMapper.insertHistoryManagement(historyManagement);
        LOGGER.debug("========== historyManagementNumber : {} ", historyManagement.getHistoryManagementNumber());

        
        //========== DC_APP_HIST (애플리케이션 이력 등록)
        //1.기존 이력 갱신
        LOGGER.debug("========== modifyId : {} ", application.getModifyId());
        applicationMapper.updateApplicationHistory(application.getApplicationNumber(), application.getModifyId());
        //2.신규 이력 추가
        applicationMapper.insertApplicationHistory(application.getApplicationNumber(), historyManagement.getHistoryManagementNumber());
        

        //========== DC_APP_SVC (애플리케이션 서비스 삭제)
        //기존 서비스 갱신(종료일시 갱신)
        application.setServiceNumberList(null); //전체 삭제 필요
        applicationMapper.updateApplicationService(application);

        
        //========== DC_APP_SLA (애플리케이션 SLA 삭제)
        //기존 SLA 갱신(종료일시 갱신)
        DcApplicationSla applicationSla = new DcApplicationSla();
        applicationSla.setApplicationNumber(application.getApplicationNumber());
        authenticationInjector.setAuthentication(applicationSla);
        applicationMapper.removeApplicationSla(applicationSla);
        
        
        //========== Gateway 연동
        if("Y".equals(env.getProperty("system.interfaceYn"))) {
            LOGGER.debug("■■■■■■■■■■ Gateway 연동 [removeApplicationCacheData] ■■■■■■■■■■");
            applicationCacheService.removeApplicationCacheData(application);
        }
        
        return application;
    }


    @Override
    public DashboardApplications getDashboardApplications(SearchRequest searchRequest) {
        // TODO Auto-generated method stub
        searchRequest.setData();
        return applicationMapper.selectDashboardApplications(searchRequest);
    }


    @Override
    public List<DcApplicationScope> getSearchScopeList(Long applicationNumber) {
        // TODO Auto-generated method stub
        return applicationMapper.selectSearchScopeList(applicationNumber);
    }

    @Override
    public DcApplication reissueApplicationClientSecret(DcApplicationScope applicationScope) {

        DcApplicationAuthKey applicationAuthKey = new DcApplicationAuthKey();
        authenticationInjector.setAuthentication(applicationAuthKey);
        applicationAuthKey.setApplicationNumber(applicationScope.getApplicationNumber());
        applicationAuthKey.setKeyEndDatetime(applicationScope.getKeyEndDatetime());

        if("Y".equals(env.getProperty("system.interfaceYn"))) {
            LOGGER.debug("■■■■■■■■■■ OAuth 연동 [reissueApplicationClientSecret] ■■■■■■■■■■");
            String result = applicationCacheService.modifyApplicationSecretKeyCacheData(applicationScope);
            if(result != null) {
                try {
                    JSONObject body = new JSONObject(result);
                    String clientId = body.getString("clientId");
                    String clientSecret = body.getString("clientSecret");
                    applicationAuthKey.setClientId(clientId);
                    applicationAuthKey.setClientSecret(clientSecret);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }else {
                applicationAuthKey.setClientId("client_id-1234");
                applicationAuthKey.setClientSecret("client_secret-1234");
            }
            //기존 데이터 EndDateTime NOW()
            applicationMapper.updateApplicationAuthSecretKey(applicationAuthKey);
            //신규 데이터 등록
            applicationMapper.insertApplicationAuthKey(applicationAuthKey);
        }

        //========== OAuth 키 정보 조회(전체)
        DcApplication application = new DcApplication();
        List<DcApplicationAuthKey> applicationAuthList = applicationMapper.selectApplicationAuthList(applicationScope.getApplicationNumber());
        if(applicationAuthList != null && applicationAuthList.size() > 0) {
            application.setApplicationAuthList(applicationAuthList);
            applicationScope.setClientId(applicationAuthList.get(0).getClientId());
        }

        return application;
    }


    @Override
    public DcApplication createApplicationScope(DcApplicationScope applicationScope) {
        // TODO Auto-generated method stub
        LOGGER.debug("createApplicationScope.applicationScope : {} ", applicationScope);




        List<String> scopeContextList = new ArrayList<>();

        //DC_APP_SCOPE (Insert)
        authenticationInjector.setAuthentication(applicationScope);
        for(String scopeNumber : applicationScope.getScopeNumberList()) {
            scopeContextList.add(applicationMapper.selectScopeContext(scopeNumber));

            LOGGER.debug("========== scopeNumber : {} ", scopeNumber);
            applicationScope.setScopeNumber(Long.valueOf(scopeNumber));
            applicationMapper.insertApplicationScope(applicationScope);
        }
        
        DcApplicationAuthKey applicationAuthKey = new DcApplicationAuthKey();
        authenticationInjector.setAuthentication(applicationAuthKey);
        applicationAuthKey.setApplicationNumber(applicationScope.getApplicationNumber());
        applicationAuthKey.setKeyEndDatetime(applicationScope.getKeyEndDatetime());

        applicationScope.setScopeContextList(scopeContextList);
        //========== OAuth 연동
        if("Y".equals(env.getProperty("system.interfaceYn"))) {
            LOGGER.debug("■■■■■■■■■■ OAuth 연동 [createApplicationScopeCacheData] ■■■■■■■■■■");
            String result = applicationCacheService.createApplicationScopeCacheData(applicationScope);
            if(result != null) {
                try {
                    JSONObject body = new JSONObject(result);
                    String clientId = body.getString("clientId");
                    String clientSecret = body.getString("clientSecret");
                    applicationAuthKey.setClientId(clientId);
                    applicationAuthKey.setClientSecret(clientSecret);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }else {
            applicationAuthKey.setClientId("client_id-1234");
            applicationAuthKey.setClientSecret("client_secret-1234");
        }
        //DC_APP_AUTH_KEY (Insert)
        applicationMapper.insertApplicationAuthKey(applicationAuthKey);
        
        //========== OAuth 키 정보 조회(전체)
        DcApplication application = new DcApplication();
        List<DcApplicationAuthKey> applicationAuthList = applicationMapper.selectApplicationAuthList(applicationScope.getApplicationNumber());
        if(applicationAuthList != null && applicationAuthList.size() > 0) {
            application.setApplicationAuthList(applicationAuthList);
        }
        
        return application;
    }

    @Override
    public DcApplication modifyApplicationScope(DcApplicationScope applicationScope) {
        // TODO Auto-generated method stub
        LOGGER.debug("modifyApplicationScope.applicationScope : {} ", applicationScope);

        //DC_APP_SCOPE (Update)
        authenticationInjector.setAuthentication(applicationScope);

        List<String> scopeContextList = new ArrayList<>();

        applicationMapper.deleteApplicationScope(applicationScope);
        for(String scopeNumber : applicationScope.getScopeNumberList()) {
            scopeContextList.add(applicationMapper.selectScopeContext(scopeNumber));

            LOGGER.debug("========== scopeNumber : {} ", scopeNumber);
            applicationScope.setScopeNumber(Long.valueOf(scopeNumber));
            applicationMapper.insertApplicationScope(applicationScope);
        }

        //========== OAuth 키 정보 조회(전체)
        DcApplication application = new DcApplication();
        List<DcApplicationAuthKey> applicationAuthList = applicationMapper.selectApplicationAuthList(applicationScope.getApplicationNumber());
        if(applicationAuthList != null && applicationAuthList.size() > 0) {
            application.setApplicationAuthList(applicationAuthList);
        }
        application.setScopeNumberList(applicationScope.getScopeNumberList());

        applicationScope.setClientId(application.getApplicationAuthList().get(0).getClientId());

        applicationScope.setScopeContextList(scopeContextList);
        //========== OAuth 연동
        if("Y".equals(env.getProperty("system.interfaceYn"))) {
            applicationCacheService.modifyApplicationScopeData(applicationScope);
        }
        return application;
    }

    @Override
    @Transactional
    public DcApplication reissueMessageEncryptionKey(DcApplicationKey applicationKey) {
        LOGGER.debug("reissueMessageEncryptionKey.reissueMsgEncryptionKey : {} ", applicationKey);

        /* 키 재발급 */
        UUIDGenerator uuidGenerator = new UUIDGenerator();
        applicationKey.setMsgEncryptionKey(uuidGenerator.createUUID()); //재발급 키

        //DC_APP_KEY (Update)
        authenticationInjector.setAuthentication(applicationKey);
        applicationMapper.reissueApplicationMsgAuthKey(applicationKey);

        //DC_HIST_MGMT (Insert)
        DcHistoryManagement historyManagement = new DcHistoryManagement();
        historyManagement.setHistoryManagementCode(DC_HIST_MGMT_CD_MSG_AUTH_KEY);
        historyManagement.setHistoryManagementMemo("reissueMessageEncryptionKey");
        authenticationInjector.setAuthentication(historyManagement);
        applicationMapper.insertHistoryManagement(historyManagement);
        LOGGER.debug("historyManagementNumber : {} ", historyManagement.getHistoryManagementNumber());
        applicationKey.setHistoryManagementNumber(historyManagement.getHistoryManagementNumber());

        //DC_APP_KEY_HIST (Insert)
        //1.기존 이력 갱신
        applicationMapper.updateApplicationKeyHistory(applicationKey);
        //2.신규 이력 추가
        applicationMapper.insertApplicationKeyHistory(applicationKey);

        //========== Gateway 연동
        if("Y".equals(env.getProperty("system.interfaceYn"))) {
            LOGGER.debug("■■■■■■■■■■ Gateway 연동 [modifyApplicationKey] ■■■■■■■■■■");
            applicationCacheService.modifyApplicationKeyCacheData(applicationKey);
        }

        //========== 키 정보 조회(전체)
        DcApplication application = new DcApplication();
        List<DcApplicationKey> applicationKeyList = applicationMapper.selectApplicationKeyList(applicationKey.getApplicationNumber());
        application.setApplicationKeyList(applicationKeyList);

        return application;
    }

}
