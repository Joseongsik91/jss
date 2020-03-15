package com.oneplat.oap.mgmt.oapservice.service.impl;

import com.oneplat.oap.core.model.PageInfo;
import com.oneplat.oap.core.model.SearchRequest;
import com.oneplat.oap.mgmt.common.model.AuthenticationInjector;
import com.oneplat.oap.mgmt.oapservice.mapper.ApiGroupMapper;
import com.oneplat.oap.mgmt.oapservice.mapper.OapServiceMapper;
import com.oneplat.oap.mgmt.oapservice.model.ApiGroup;
import com.oneplat.oap.mgmt.oapservice.model.DashboardService;
import com.oneplat.oap.mgmt.oapservice.model.OapService;
import com.oneplat.oap.mgmt.oapservice.model.OapServiceCompose;
import com.oneplat.oap.mgmt.oapservice.model.enums.ServiceComposeCode;
import com.oneplat.oap.mgmt.oapservice.service.OapServiceService;
import com.oneplat.oap.mgmt.oapservice.support.ServiceCacheService;
import com.oneplat.oap.mgmt.policies.mapper.CapacityMapper;
import com.oneplat.oap.mgmt.policies.mapper.SlaMapper;
import com.oneplat.oap.mgmt.policies.model.Scope;
import com.oneplat.oap.mgmt.policies.model.ServicePolicy;
import com.oneplat.oap.mgmt.policies.service.CapacityService;
import com.oneplat.oap.mgmt.policies.service.ScopeService;
import com.oneplat.oap.mgmt.policies.service.SlaService;
import com.oneplat.oap.mgmt.util.UrlPrefixUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

/**
 * @author lee
 * @date 2016-12-01
 */
@Service
@PropertySource("classpath:system.properties")
public class OapServiceServiceImpl implements OapServiceService {

    @Autowired
    OapServiceMapper oapServiceMapper;
    @Autowired
    ApiGroupMapper apiGroupMapper;
    @Autowired
    CapacityMapper capacityMapper;
    @Autowired
    SlaMapper slaMapper;
    @Autowired
    SlaService slaService;
    @Autowired
    CapacityService capacityService;
    @Autowired
    AuthenticationInjector authenticationInjector;
    @Autowired
    ServiceCacheService serviceCacheService;
    @Autowired
    ScopeService scopeService;
    @Autowired
    Environment env;
    
    /**
     * Select total order list list.
     *
     * @param searchRequest the search request
     * @return the list
     */
    @Override
    public List<OapService> selectServiceList(SearchRequest searchRequest) {
        searchRequest.setData();
        List<OapService> oapServiceList = oapServiceMapper.selectServiceList(searchRequest);

        for(OapService oapService : oapServiceList){
            oapService.setIconFileChannel(UrlPrefixUtil.addPrefix(oapService.getIconFileChannel())); 
        }
        if(searchRequest.getPageInfo()!=null){
            searchRequest.getPageInfo().setTotalCount(oapServiceMapper.selectServiceListTotal(searchRequest));
            searchRequest.getPageInfo().setResultCount(oapServiceList.size());
        }
        return oapServiceList;
    }

    /**
     * Create service.
     *
     * @param oapService the oap service
     */
    @Override
    @Transactional
    public void createService(OapService oapService) {
        authenticationInjector.setAuthentication(oapService);
        oapServiceMapper.insertService(oapService);

        long serviceNumber = oapService.getServiceNumber();
        oapServiceMapper.insertServiceHistory(serviceNumber);

        //서비스 추가 구성 사용 인경우 추가 정보 등록 or 수정
        if(oapService.getServiceComposeUseYn()){
            for(OapServiceCompose serviceCompose : oapService.getServiceComposes()){
                authenticationInjector.setAuthentication(serviceCompose);
                serviceCompose.setServiceNumber(serviceNumber);
                oapServiceMapper.insertServiceCompose(serviceCompose);
            }
            oapServiceMapper.insertServiceComposeHistory(serviceNumber);
        }

        //기본 API Group 등록
        ApiGroup apiGroup = new ApiGroup();
        authenticationInjector.setAuthentication(apiGroup);
        apiGroup.setServiceNumber(serviceNumber);
        apiGroupMapper.insertDefaultApiGroup(apiGroup);
        long apiGroupNumber = apiGroup.getApiGroupNumber();
        apiGroupMapper.insertApiGroupHistory(apiGroupNumber);

        //기본 API Group Relation 등록
        ApiGroup.ApiGroupRelation apiGroupRelation = new ApiGroup.ApiGroupRelation();
        authenticationInjector.setAuthentication(apiGroupRelation);
        apiGroupRelation.setOpponentApiGroupNumber(apiGroupNumber);
        apiGroupMapper.insertDefaultApiGroupRelation(apiGroupRelation);

        ServicePolicy slaPolicy = new ServicePolicy();
        //SLA 사용인경우 서비스 SLA 추가
        if(oapService.getSlaUseYn()){
            slaPolicy = slaService.createSlaPolicy(serviceNumber,0L);
        }

        //CAPACITY 사용인경우 서비스 CAPACITY 추가
        ServicePolicy capacityPolicy = new ServicePolicy();
        if(oapService.getCapacityUseYn()){
            capacityPolicy = capacityService.createCapacityPolicy(serviceNumber, 0L);
        }

        //SCOPE 추가
        scopeService.createInitScope(getScopeModel(oapService));

        /** OSDF Interfacing*/
        if(env.getProperty("system.interfaceYn").equals("Y")){
            /** 서비스 생성*/
            serviceCacheService.serviceCacheCreate(oapService);
            /** 서비스 그룹 생성*/
            OapService serviceGroup = (OapService)oapService.clone();
            serviceGroup.setServiceNumber(apiGroupNumber);
            serviceGroup.setServiceName("GROUP");
            serviceGroup.setServiceUseYn(true);
            serviceGroup.setSlaUseYn(false);
            serviceGroup.setCapacityUseYn(false);
            serviceCacheService.serviceCacheCreate(serviceGroup);
        }

    }

    /**
     * Select service oap service.
     *
     * @param serviceNumber the service number
     * @return the oap service
     */
    @Override
    public OapService selectService(long serviceNumber) {
        OapService oapService = oapServiceMapper.selectService(serviceNumber);
        
        oapService.setIconFileChannel(UrlPrefixUtil.addPrefix(oapService.getIconFileChannel())); 
        if(oapService.getServiceComposeUseYn()){
            oapService.setServiceComposes(oapServiceMapper.selectServiceComposeList(serviceNumber));
        }
        return oapService;
    }

    /**
     * Update service.
     *
     * @param oapService the oap service
     */
    @Override
    @Transactional
    public void updateService(long serviceNumber, OapService oapService) {
        oapService.setIconFileChannel(UrlPrefixUtil.removePrefix(oapService.getIconFileChannel()));
        authenticationInjector.setAuthentication(oapService);
        oapServiceMapper.updateService(oapService);
        oapServiceMapper.updateServiceHistory(serviceNumber);
        oapServiceMapper.insertServiceHistory(serviceNumber);

        //서비스 추가 구성 사용 인경우 추가 정보 등록 or 수정
        if(oapService.getServiceComposeUseYn()){
            if(null != oapService.getServiceComposes() || 0 < oapService.getServiceComposes().size()){
                // List -> Map
                Map<String, OapServiceCompose> composeMap = oapService.getServiceComposes().stream().collect(
                        Collectors.toMap(OapServiceCompose::getServiceComposeCode, Function.identity()));
                //헤더 정보 사용시 정보 업데이트
                if(composeMap.containsKey(ServiceComposeCode.HEADER.getCode())){
                    OapServiceCompose compose =  composeMap.get(ServiceComposeCode.HEADER.getCode());
                    compose.setServiceNumber(serviceNumber);
                    updateServiceCompose(compose);
                }else{
                    //헤더 정보 미사용시 미사용 처리
                    OapServiceCompose compose =  new OapServiceCompose();
                    compose.setServiceNumber(serviceNumber);
                    compose.setServiceComposeCode(ServiceComposeCode.HEADER.getCode());
                    deleteServiceCompose(compose);
                }

                if(composeMap.containsKey(ServiceComposeCode.CONTENTTYPE.getCode())){
                    OapServiceCompose compose =  composeMap.get(ServiceComposeCode.CONTENTTYPE.getCode());
                    compose.setServiceNumber(serviceNumber);
                    updateServiceCompose(compose);
                }else{
                    OapServiceCompose compose =  new OapServiceCompose();
                    compose.setServiceNumber(serviceNumber);
                    compose.setServiceComposeCode(ServiceComposeCode.CONTENTTYPE.getCode());
                    deleteServiceCompose(compose);
                }

                if(composeMap.containsKey(ServiceComposeCode.ACCEPT.getCode())) {
                    OapServiceCompose compose = composeMap.get(ServiceComposeCode.ACCEPT.getCode());
                    compose.setServiceNumber(serviceNumber);
                    updateServiceCompose(compose);
                }else{
                    OapServiceCompose compose =  new OapServiceCompose();
                    compose.setServiceNumber(serviceNumber);
                    compose.setServiceComposeCode(ServiceComposeCode.ACCEPT.getCode());
                    deleteServiceCompose(compose);
                }

            }else{
                //Compose 정보 모두 삭제
                OapServiceCompose compose =  new OapServiceCompose();
                authenticationInjector.setAuthentication(compose);
                compose.setServiceNumber(serviceNumber);
                oapServiceMapper.deleteServiceCompose(compose);
                oapServiceMapper.updateServiceComposeHistory(compose);
                oapServiceMapper.insertServiceComposeHistory(serviceNumber);
            }
        }

        //SLA 사용인경우 서비스 SLA 추가 or 사용 처리
        if(oapService.getSlaUseYn()) {
            slaService.updateSlaPolicy(serviceNumber, 0L);
        }else {
            slaService.updateSlaPolicyDelete(serviceNumber, 0L);
        }
        if(oapService.getCapacityUseYn()){
            capacityService.updateCapacityPolicy(serviceNumber, 0L);
        }else{
            capacityService.updateCapacityPolicyDelete(serviceNumber, 0L);
        }

        //Scope사용여부 수정
        scopeService.updateScopeUseYn(getScopeModel(oapService));

        /** OSDF Interfacing*/
        if(env.getProperty("system.interfaceYn").equals("Y"))
            serviceCacheService.serviceCacheModify(oapService);
    }

    /**
     * Delete service.
     *
     * @param serviceNumber the service number
     */
    @Override
    @Transactional
    public void deleteService(long serviceNumber) {
        OapService oapService = new OapService();
        oapService.setServiceNumber(serviceNumber);
        authenticationInjector.setAuthentication(oapService);
        /** service delete*/
        oapServiceMapper.deleteService(oapService);
        oapServiceMapper.updateServiceHistory(serviceNumber);
        oapServiceMapper.insertServiceHistory(serviceNumber);
        /** group delete*/
        apiGroupMapper.deleteServiceAllApiGroup(oapService);
        apiGroupMapper.updateServiceAllApiGroupHistory(serviceNumber);
        apiGroupMapper.insertServiceAllApiGroupHistory(serviceNumber);
        /** policy delete*/
        ServicePolicy policy = new ServicePolicy();
        authenticationInjector.setAuthentication(policy);
        policy.setServiceNumber(serviceNumber);
        //전체 삭제여부 처리
        capacityMapper.updateServicePolicyDelete(policy);
        capacityMapper.updateServicePolicyTotalHistory(serviceNumber);
        capacityMapper.insertServicePolicyTotalHistory(serviceNumber);
        /** OSDF Interfacing*/
        if(env.getProperty("system.interfaceYn").equals("Y"))
            serviceCacheService.serviceCacheDelete(oapService);
    }

    /**
     * Select service history list list.
     *
     * @param serviceNumber the service number
     * @return the list
     */
    @Override
    public List<OapService> selectServiceHistoryList(long serviceNumber) {
        return oapServiceMapper.selectServiceHistoryList(serviceNumber);
    }

    /**
     * Select service name count int.
     *
     * @param serviceContext the service context
     * @return the int
     */
    @Override
    public int selectServiceNameCount(String serviceContext) {
        return oapServiceMapper.selectServiceNameCount(serviceContext);
    }

    /**
     * Select service delete inquiry int.
     *
     * @param serviceNumber the service number
     * @return the List
     */
    @Override
    public List<Integer> selectServiceDeleteInquiry(long serviceNumber) {
        List<Integer> countList = new ArrayList<>();
        countList.add(oapServiceMapper.selectServiceAppCount(serviceNumber));
        countList.add(oapServiceMapper.selectServiceApiCount(serviceNumber));
        return countList;
    }

    public void updateServiceCompose(OapServiceCompose oapServiceCompose){
        authenticationInjector.setAuthentication(oapServiceCompose);
        //헤더, content-type, accept 정보 수정
        oapServiceMapper.updateServiceCompose(oapServiceCompose);
        oapServiceMapper.updateServiceComposeCodeHistory(oapServiceCompose);
        oapServiceMapper.insertServiceComposeCodeHistory(oapServiceCompose);
    }

    public void deleteServiceCompose(OapServiceCompose oapServiceCompose){
        authenticationInjector.setAuthentication(oapServiceCompose);
        //헤더, content-type, accept 정보 미사용
        oapServiceMapper.deleteServiceComposeCode(oapServiceCompose);
        oapServiceMapper.updateServiceComposeCodeHistory(oapServiceCompose);
        oapServiceMapper.insertServiceComposeCodeHistory(oapServiceCompose);
    }

    @Override
    public DashboardService selectServiceListForDashBoard(SearchRequest searchRequest) {
        searchRequest.setData();

        List<OapService> serviceList = oapServiceMapper.selectServiceListForDashBoard(searchRequest);
        DashboardService dashboardService = new DashboardService();
        searchRequest.setPageInfo(new PageInfo());
        List<OapService> dashboardServiceList = oapServiceMapper.selectServiceListForDashBoard(searchRequest);

        for(OapService oapService : dashboardServiceList){
            oapService.setIconFileChannel(UrlPrefixUtil.addPrefix(oapService.getIconFileChannel())); 
        }
        dashboardService.setServices(dashboardServiceList);
        
        dashboardService.setServiceCount(serviceList == null ? 0 : serviceList.size());
        dashboardService.setApiCount(this.getApiTotalCount(serviceList));

        return dashboardService;
    }

    private int getApiTotalCount(List<OapService> serviceList) {
        if(serviceList == null) return 0;
        int apiTotalCount = 0;
        for(OapService oapService : serviceList) {
            apiTotalCount += oapService.getApiCount();
        }
        return apiTotalCount;
    }

    //Scope Model Setting
    private Scope getScopeModel(OapService oapService){
        Scope scope = new Scope();
        scope.setServiceNumber(oapService.getServiceNumber());
        scope.setScopeName(oapService.getServiceName());
        scope.setIconFileChannel(oapService.getIconFileChannel());
        scope.setScopeContext(oapService.getServiceContext());
        scope.setScopeDescription(oapService.getServiceDescription());
        scope.setScopeUseYn(oapService.getScopeUseYn());
        scope.setModifyId(oapService.getModifyId());
        scope.setCreateId(oapService.getCreateId());
        return scope;
    }

}