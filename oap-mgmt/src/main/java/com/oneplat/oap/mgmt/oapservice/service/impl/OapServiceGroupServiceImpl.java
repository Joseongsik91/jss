package com.oneplat.oap.mgmt.oapservice.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oneplat.oap.mgmt.common.model.AuthenticationInjector;
import com.oneplat.oap.mgmt.oapservice.mapper.ApiGroupMapper;
import com.oneplat.oap.mgmt.oapservice.model.ApiGroup;
import com.oneplat.oap.mgmt.oapservice.model.OapService;
import com.oneplat.oap.mgmt.oapservice.service.OapServiceGroupService;
import com.oneplat.oap.mgmt.oapservice.service.OapServiceService;
import com.oneplat.oap.mgmt.oapservice.support.ServiceCacheService;
import com.oneplat.oap.mgmt.policies.mapper.CapacityMapper;
import com.oneplat.oap.mgmt.policies.mapper.SlaMapper;
import com.oneplat.oap.mgmt.policies.model.ServicePolicy;
import com.oneplat.oap.mgmt.policies.service.CapacityService;
import com.oneplat.oap.mgmt.policies.service.SlaService;

/**
 * @author lee
 * @date 2016-12-20
 */
@Service
public class OapServiceGroupServiceImpl implements OapServiceGroupService {

    @Autowired
    ApiGroupMapper apiGroupMapper;
    @Autowired
    SlaMapper slaMapper;
    @Autowired
    CapacityMapper capacityMapper;
    @Autowired
    SlaService slaService;
    @Autowired
    CapacityService capacityService;
    @Autowired
    AuthenticationInjector authenticationInjector;
    @Autowired
    OapServiceService oapServiceService;
    @Autowired
    ServiceCacheService serviceCacheService;
    @Autowired
    private Environment env;

    private static final Logger LOGGER = LoggerFactory.getLogger(OapServiceGroupServiceImpl.class);
    /**
     * Select All Group list.
     * 초기화 데이타를 위한 리스트 조회 추가 
     * @return the list
     */
    @Override
    public List<ApiGroup.ApiGroupRelation> selectServiceGroupListExceptRoot(long serviceNumber) {
        return apiGroupMapper.selectServiceGroupListExceptRoot(serviceNumber);
    }
    /**
     * Select service list list.
     *
     * @return the list
     */
    @Override
    public List<ApiGroup.ApiGroupRelation> selectServiceGroupList(long serviceNumber) {
        return apiGroupMapper.selectServiceGroupList(serviceNumber);
    }

    /**
     * Create api group.
     *
     * @param serviceNumber the service number
     * @param apiGroup      the api group
     */
    @Override
    @Transactional
    public void createApiGroup(long serviceNumber, ApiGroup.ApiGroupRelation apiGroup) {
        authenticationInjector.setAuthentication(apiGroup);
        apiGroup.setServiceNumber(serviceNumber);
        OapService oapService = oapServiceService.selectService(serviceNumber);
        oapService.setServiceName(apiGroup.getApiGroupName());
        oapService.setServiceUseYn(apiGroup.getApiGroupUseYn());
        oapService.setSlaUseYn(false);
        oapService.setCapacityUseYn(false);

        //2depth 그룹 등록
        if(2 == apiGroup.getOpponentApiGroupLevel()){
            apiGroupMapper.insertSecondApiGroup(apiGroup);
            apiGroupMapper.insertApiGroupHistory(apiGroup.getApiGroupNumber());
            apiGroup.setOpponentApiGroupNumber(apiGroup.getApiGroupNumber());
            apiGroupMapper.insertApiGroupRelation(apiGroup);
            oapService.setServiceNumber(apiGroup.getApiGroupNumber());
        }else{
            //1depth 그룹 등록
            apiGroupMapper.insertApiGroup(apiGroup);
            apiGroupMapper.insertApiGroupHistory(apiGroup.getApiGroupNumber());
            apiGroup.setOpponentApiGroupNumber(apiGroup.getApiGroupNumber());
            apiGroupMapper.insertApiGroupRelation(apiGroup);

            ServicePolicy slaPolicy = new ServicePolicy();
            //SLA 사용인경우 서비스 그룹 SLA 추가
            if(apiGroup.getSlaUseYn()){
                slaPolicy = slaService.createSlaPolicy(serviceNumber, apiGroup.getApiGroupNumber());
            }

            //CAPACITY 사용인경우 서비스 그룹 CAPACITY 추가
            ServicePolicy capacityPolicy = new ServicePolicy();
            if(apiGroup.getCapacityUseYn()){
                capacityPolicy = capacityService.createCapacityPolicy(serviceNumber, apiGroup.getApiGroupNumber());
            }
            oapService.setServiceNumber(apiGroup.getApiGroupNumber());
            oapService.setSlaUseYn(apiGroup.getSlaUseYn());
            oapService.setCapacityUseYn(apiGroup.getCapacityUseYn());
        }
        /** OSDF Interfacing*/
        if(env.getProperty("system.interfaceYn").equals("Y"))
            serviceCacheService.serviceCacheCreate(oapService);
    }

    /**
     * Update api group.
     *
     * @param serviceNumber the service number
     * @param apiGroup      the api group
     */
    @Override
    @Transactional
    public void updateApiGroup(long serviceNumber, ApiGroup.ApiGroupRelation apiGroup) {
        authenticationInjector.setAuthentication(apiGroup);
        apiGroup.setServiceNumber(serviceNumber);
        OapService oapService = oapServiceService.selectService(serviceNumber);
        /** 서비스 그룹 생성*/
        oapService.setServiceNumber(apiGroup.getApiGroupNumber());
        oapService.setServiceName(apiGroup.getApiGroupName());
        oapService.setServiceUseYn(apiGroup.getApiGroupUseYn());
        oapService.setSlaUseYn(false);
        oapService.setCapacityUseYn(false);

        //2depth 그룹 수정
        if(2 == apiGroup.getOpponentApiGroupLevel()){
            apiGroupMapper.updateSecondApiGroup(apiGroup);
            apiGroupMapper.updateApiGroupHistory(apiGroup.getApiGroupNumber());
            apiGroupMapper.insertApiGroupHistory(apiGroup.getApiGroupNumber());
        }else{
            //1depth 그룹 수정
            apiGroupMapper.updateApiGroup(apiGroup);
            apiGroupMapper.updateApiGroupHistory(apiGroup.getApiGroupNumber());
            apiGroupMapper.insertApiGroupHistory(apiGroup.getApiGroupNumber());
            //SLA 사용인경우 서비스 그룹 SLA 추가/업데이트
            if(apiGroup.getSlaUseYn()){
                slaService.updateSlaPolicy(serviceNumber, apiGroup.getApiGroupNumber());
            }else{
                slaService.updateSlaPolicyDelete(serviceNumber, apiGroup.getApiGroupNumber());
            }

            //CAPACITY 사용인경우 서비스 그룹 CAPACITY 추가
            if(apiGroup.getCapacityUseYn()){
                capacityService.updateCapacityPolicy(serviceNumber, apiGroup.getApiGroupNumber());
            }else{
                capacityService.updateCapacityPolicyDelete(serviceNumber, apiGroup.getApiGroupNumber());
            }
            oapService.setSlaUseYn(apiGroup.getSlaUseYn());
            oapService.setCapacityUseYn(apiGroup.getCapacityUseYn());
        }
        /** OSDF Interfacing*/
        if(env.getProperty("system.interfaceYn").equals("Y"))
            serviceCacheService.serviceCacheModify(oapService);
    }

    /**
     * Update api group order.
     * @param serviceNumber the service number
     * @param apiGroupRelation      the api group
     */
    @Override
    @Transactional
    public void updateApiGroupOrder(long serviceNumber, ApiGroup.ApiGroupRelation apiGroupRelation) {
        apiGroupRelation.setServiceNumber(serviceNumber);
        List<ApiGroup.ApiGroupRelation> list =  apiGroupMapper.selectGroupRelationOrderList(apiGroupRelation);
        Integer opponentSortNumber = apiGroupRelation.getOpponentSortNumber();
        ApiGroup.ApiGroupRelation groupRelation = apiGroupMapper.selectGroupRelationOrder(apiGroupRelation);
        groupRelation.setOpponentSortNumber(opponentSortNumber);

        List<ApiGroup.ApiGroupRelation> orderList = new ArrayList<>();
        orderList.add(groupRelation);

        // sortNum 추가 값
        int addOrder = 0;
        // 자신의 정렬 순서보다 큰 건수 전체 정렬 순서 수정
        for(int i = 0; list.size() > i; i++) {
            ApiGroup.ApiGroupRelation model = list.get(i);
            if( opponentSortNumber.compareTo(Integer.parseInt( (i + 1) + "")) == 0 ) {
                addOrder = 1;
            }
            model.setOpponentSortNumber( Integer.parseInt( (i + 1 + addOrder) + "") );
            orderList.add(model);
        }

        for(ApiGroup.ApiGroupRelation order : orderList) {
            authenticationInjector.setAuthentication(order);
            apiGroupMapper.updateApiGroupRelationDelete(order);
            apiGroupMapper.insertApiGroupRelationSort(order);
        }
    }

    /**
     * Delete api group.
     *
     * @param serviceNumber the service number
     * @param apiGroupNumber      the api group
     */
    @Override
    @Transactional
    public void deleteApiGroup(long serviceNumber, long opponentApiGroupLevel, long apiGroupNumber){
        ApiGroup.ApiGroupRelation apiGroup = new ApiGroup.ApiGroupRelation();
        authenticationInjector.setAuthentication(apiGroup);
        apiGroup.setServiceNumber(serviceNumber);
        apiGroup.setApiGroupNumber(apiGroupNumber);

        apiGroupMapper.deleteApiGroup(apiGroup);
        apiGroupMapper.updateApiGroupHistory(apiGroupNumber);
        apiGroupMapper.insertApiGroupHistory(apiGroupNumber);

        //1depth 그룹 Policy 삭제
        if(1 == opponentApiGroupLevel){
            //SLA 서비스 그룹 삭제
            slaService.updateSlaPolicyDelete(serviceNumber, apiGroup.getApiGroupNumber());
            //CAPACITY 서비스 그룹 삭제
            capacityService.updateCapacityPolicyDelete(serviceNumber, apiGroupNumber);
        }
        /** OSDF Interfacing*/
        if(env.getProperty("system.interfaceYn").equals("Y")){
            OapService oapService = new OapService();
            /** 서비스 그룹 삭제*/
            oapService.setServiceNumber(apiGroup.getApiGroupNumber());
            serviceCacheService.serviceCacheDelete(oapService);
        }
    }
}
