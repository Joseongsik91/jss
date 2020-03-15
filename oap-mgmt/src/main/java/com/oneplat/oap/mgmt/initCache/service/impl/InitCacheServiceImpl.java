package com.oneplat.oap.mgmt.initCache.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.oneplat.oap.core.exception.ServiceException;
import com.oneplat.oap.core.model.SearchRequest;
import com.oneplat.oap.mgmt.api.model.ApiInfo;
import com.oneplat.oap.mgmt.api.model.ApiSearchRequest;
import com.oneplat.oap.mgmt.api.service.ApiService;
import com.oneplat.oap.mgmt.api.support.ApiCacheSendData;
import com.oneplat.oap.mgmt.application.model.DcApplication;
import com.oneplat.oap.mgmt.application.model.DcApplicationKey;
import com.oneplat.oap.mgmt.application.model.DcApplicationSla;
import com.oneplat.oap.mgmt.application.service.ApplicationService;
import com.oneplat.oap.mgmt.application.support.ApplicationAuthKeyCacheSendData;
import com.oneplat.oap.mgmt.application.support.ApplicationCacheSendData;
import com.oneplat.oap.mgmt.application.support.ApplicationThrottleCacheSendData;
import com.oneplat.oap.mgmt.common.cache.CacheSendData;
import com.oneplat.oap.mgmt.common.cache.CommonCodeCache;
import com.oneplat.oap.mgmt.common.cache.MultipartCacheSender;
import com.oneplat.oap.mgmt.common.config.ApiConstants;
import com.oneplat.oap.mgmt.initCache.service.InitCacheService;
import com.oneplat.oap.mgmt.initCache.support.InitCacheSendData;
import com.oneplat.oap.mgmt.oapservice.mapper.ApiGroupMapper;
import com.oneplat.oap.mgmt.oapservice.model.ApiGroup.ApiGroupRelation;
import com.oneplat.oap.mgmt.oapservice.model.OapService;
import com.oneplat.oap.mgmt.oapservice.service.OapServiceGroupService;
import com.oneplat.oap.mgmt.oapservice.service.OapServiceService;
import com.oneplat.oap.mgmt.oapservice.support.ServiceCacheSendData;
import com.oneplat.oap.mgmt.policies.model.ServicePolicy;
import com.oneplat.oap.mgmt.policies.service.CapacityService;
import com.oneplat.oap.mgmt.policies.service.SlaService;
import com.oneplat.oap.mgmt.policies.support.PolicyCapacityCacheSendData;
import com.oneplat.oap.mgmt.policies.support.PolicySlaCacheSendData;
import com.oneplat.oap.mgmt.setting.system.model.Adaptor;
import com.oneplat.oap.mgmt.setting.system.service.AdaptorService;
import com.oneplat.oap.mgmt.setting.system.support.AdaptorCacheSendData;


/**
 * TODO 초기 데이타 Cache interface 서비스 클래스
 *
 * @author mike 
 * @date 2015. 7. 21
 */
@Service
public class InitCacheServiceImpl implements InitCacheService{
    @Autowired
    private Environment env;
    
    @Autowired
    private CommonCodeCache commonCodeCache;
    
    @Autowired
    RestTemplate restTemplate;
    
    @Autowired
    ApiService apiService;
    
    @Autowired
    ApiGroupMapper apiGroupMapper;
    
    @Autowired
    OapServiceService oapServiceService;
    
    @Autowired
    OapServiceGroupService oapServiceGroupService;
    
    @Autowired 
    AdaptorService adaptorService;
    
    @Autowired
    SlaService slaService;
    
    @Autowired
    CapacityService capacityService;
    
    @Autowired
    ApplicationService applicationService;
    
    
    public Map<String, Object> CreateInitData(){
        /*
         * OSDF Interfacing
         */
        InitCacheSendData initCacheData = new InitCacheSendData(env);
        Map<String, Object> totalConfigData = new HashMap<String, Object>();
        Map<String, Object> apiGatewayConfig = new HashMap<String, Object>();
        Map<String, Object> authConfig = new HashMap<String, Object>();
        //어뎁터 등록
        {            
            List<Object> parameterList = new ArrayList<Object>();
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.addQueryData("adaptorUseYnCode", "Y");
            
            List<Adaptor> adaptorList = adaptorService.adaptorList(searchRequest);
            for(Adaptor adaptor : adaptorList){
                CacheSendData adaptorCacheData = new AdaptorCacheSendData(adaptor, env);
                if(adaptorCacheData.convertData().size()>0)parameterList.add(adaptorCacheData.convertData());
            }
            apiGatewayConfig.put(CacheSendData.InitFild.adaptorList.name(), parameterList);
        }
        //서비스 등록
        {
            List<Object> parameterList = new ArrayList<Object>();
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.addQueryData("useYn", "Y");
            
            List<OapService> serviceList = oapServiceService.selectServiceList(searchRequest);
            for(OapService serviceData : serviceList){
                CacheSendData serviceCacheData = new ServiceCacheSendData(serviceData, env);
                if(serviceCacheData.convertData().size()>0)parameterList.add(serviceCacheData.convertData());
                //API그룹도 서비스와 동일하게 등록 처리 
                Long serviceNumber = serviceData.getServiceNumber();
                List<ApiGroupRelation> apiGroupList = oapServiceGroupService.selectServiceGroupListExceptRoot(serviceNumber);
                for(ApiGroupRelation apiGroup: apiGroupList){
                    //API 그룹 등록 (서비스와 동일하게 등록)
//                    OapService oapService = oapServiceService.selectService(serviceNumber);
                    OapService oapService = new OapService();
                    oapService.setSiteCode(serviceData.getSiteCode());
                    oapService.setServiceSectionCode(serviceData.getServiceSectionCode());
                    oapService.setServiceNumber(apiGroup.getApiGroupNumber());
                    oapService.setServiceName(apiGroup.getApiGroupName());
                    oapService.setServiceUseYn(apiGroup.getApiGroupUseYn());
                    oapService.setSlaUseYn(apiGroup.getSlaUseYn());
                    oapService.setCapacityUseYn(apiGroup.getCapacityUseYn());
                    CacheSendData apiGroupCacheData = new ServiceCacheSendData(oapService, env);
                    if(apiGroupCacheData.convertData().size()>0)parameterList.add(apiGroupCacheData.convertData());
                }
                apiGatewayConfig.put(CacheSendData.InitFild.serviceList.name(), parameterList);
            }
        }
        //API등록
        {
            ApiSearchRequest apiRequest = new ApiSearchRequest();
            apiRequest.setUseYn("Y");
            List<Object> parameterList = new ArrayList<Object>();
            List<ApiInfo> apiGroupList = apiService.getAllApiList();
            for(ApiInfo apiData: apiGroupList){
                if(apiData.getApiGeneralInfo()!=null){
                    CacheSendData apiCacheData = new ApiCacheSendData(apiData, env, commonCodeCache, apiGroupMapper);
                    if(apiCacheData.convertData().size()>0)parameterList.add(apiCacheData.convertData());
                }
            }
            apiGatewayConfig.put(CacheSendData.InitFild.apiList.name(), parameterList);
        }
        //application 관련
        {
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.addQueryData("useYnCode", "Y");
            List<DcApplication> applicationList = applicationService.getApplicationList(searchRequest);
            //app 리스트 등록
            {
                List<Object> parameterList = new ArrayList<Object>();
                for(DcApplication application: applicationList){
                    application = applicationService.getApplicationDetail(application.getApplicationNumber());
                    CacheSendData applicationCacheData = new ApplicationCacheSendData(application, env);
                    if(applicationCacheData.convertData().size()>0)parameterList.add(applicationCacheData.convertData());
                }
                
                apiGatewayConfig.put(CacheSendData.InitFild.appList.name(), parameterList);
            }   
            //application Sla 등록(2차 Throttle)
            {
                List<Object> parameterList = new ArrayList<Object>();
                for(DcApplication application: applicationList){
                    application = applicationService.getApplicationDetail(application.getApplicationNumber());
                    if(application.getApplicationSlaList()!=null){
                        for(DcApplicationSla applicationSLA:application.getApplicationSlaList()){
                            if("-".equals(applicationSLA.getModifyDatetime())) {
                               continue; 
                            }
                            applicationSLA.setApplicationNumber(application.getApplicationNumber());
                            CacheSendData applicationThrottleCacheData = new ApplicationThrottleCacheSendData(applicationSLA, env);
                            if(applicationThrottleCacheData.convertData().size()>0)parameterList.add(applicationThrottleCacheData.convertData());
                        }
                    }
                    
                }
                
                apiGatewayConfig.put(CacheSendData.InitFild.appThrottlingPolicyList.name(), parameterList);
            }
            //auth key 등록
            {
                
                List<Object> parameterList = new ArrayList<Object>();
                for(DcApplication application: applicationList){
                    application = applicationService.getApplicationDetail(application.getApplicationNumber());
                    if(application.getApplicationKeyList()!=null){
                        for(DcApplicationKey applicationKey:application.getApplicationKeyList()){
                            applicationKey.setApplicationNumber(application.getApplicationNumber());
                            CacheSendData applicationAuthKeyCacheData = new ApplicationAuthKeyCacheSendData(applicationKey, env);
                            if(applicationAuthKeyCacheData.convertData().size()>0)parameterList.add(applicationAuthKeyCacheData.convertData());
                        }
                    }
                }
                authConfig.put(CacheSendData.InitFild.authKeyList.name(), parameterList);
            }
        }
        //Throttling Capacity 등록
        {
            List<Object> parameterList = new ArrayList<Object>();
            
            List<ServicePolicy> capacityList = capacityService.capacityList();
            for(ServicePolicy capacity: capacityList){
                CacheSendData capacityCacheData = new PolicyCapacityCacheSendData(capacity, env);
                if(capacityCacheData.convertData().size()>0)parameterList.add(capacityCacheData.convertData());
                /*
                 * 하위 그룹에 대해서도 capacity 정보를 연동 처리 한다.
                 */
                if(capacity.getGroupList()!=null){
                    for(ServicePolicy group: capacity.getGroupList()){
                        CacheSendData capacityCacheData1= new PolicySlaCacheSendData(group, env);
                        if(capacityCacheData1.convertData().size()>0)parameterList.add(capacityCacheData1.convertData());
                    }
                }
            }
            
            apiGatewayConfig.put(CacheSendData.InitFild.capacityThrottlingPolicyList.name(), parameterList);
        }
        //common sla 등록
        {
            List<Object> parameterList = new ArrayList<Object>();
            List<ServicePolicy> policyList = slaService.slaList();
            for(ServicePolicy sla: policyList){
                CacheSendData slaCacheData = new PolicySlaCacheSendData(sla, env);
                if(slaCacheData.convertData().size()>0)parameterList.add(slaCacheData.convertData());
                /*
                 * 하위 그룹에 대해서도 SLA 정보를 연동 처리 한다.
                 */
                if(sla.getGroupList()!=null){
                    for(ServicePolicy group: sla.getGroupList()){
                        CacheSendData slaCacheData1= new PolicySlaCacheSendData(group, env);
                        if(slaCacheData1.convertData().size()>0)parameterList.add(slaCacheData1.convertData());
                    }
                }
                
            }

            apiGatewayConfig.put(CacheSendData.InitFild.commonSlaPoliciesList.name(), parameterList);
        }

        //apiGatewayConfig&authConfig 필드 데이타 추가 
        totalConfigData.put(CacheSendData.InitFild.apiGatewayConfig.name(), apiGatewayConfig);
        totalConfigData.put(CacheSendData.InitFild.authConfig.name(), authConfig);
        
        
        initCacheData.setData(totalConfigData);
        MultipartCacheSender cacheSender = new MultipartCacheSender(initCacheData, restTemplate);
        try {
            int statusCode = cacheSender.sendCreate();
            if(statusCode != HttpStatus.SC_OK)
                throw new ServiceException(ApiConstants.DATA_INTERFACE_FAIL, ""+statusCode); 
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new ServiceException(ApiConstants.DATA_INTERFACE_FAIL, "9999");
        }
        
        return totalConfigData;
    }
    
   /* public Map<String, Object> CreateInitDataForFile(){
        
    }*/
}
