package com.oneplat.oap.mgmt.policies.service.impl;

import com.oneplat.oap.mgmt.common.model.AuthenticationInjector;
import com.oneplat.oap.mgmt.policies.mapper.CapacityMapper;
import com.oneplat.oap.mgmt.policies.model.ServicePolicy;
import com.oneplat.oap.mgmt.policies.model.enums.OapServicePolicyCode;
import com.oneplat.oap.mgmt.policies.model.enums.ServiceGradeCode;
import com.oneplat.oap.mgmt.policies.service.CapacityService;
import com.oneplat.oap.mgmt.policies.support.PolicyCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LSH on 2016. 11. 30..
 */

@Service
@PropertySource("classpath:system.properties")
public class CapacityServiceImpl implements CapacityService {
    @Autowired
    private CapacityMapper capacityMapper;
    @Autowired
    AuthenticationInjector authenticationInjector;
    @Autowired
    private Environment env;
    @Autowired
    private PolicyCacheService policyCacheService;

    @Override
    public List<ServicePolicy> capacityList() {

        /*서비스 리스트 + 그룹*/
        List<ServicePolicy> capacityPoliciesList = capacityMapper.capacityPoliciesList();

        return capacityPoliciesList;
    }

    @Override
    @Transactional
    public void modifyCapacity(ServicePolicy servicePolicy) {
        int resultCount = 0;
        int testResultCount = 0;
        Map<String, Object> response = new HashMap<>();

        authenticationInjector.setAuthentication(servicePolicy);

        /*SAL Update*/
        resultCount += capacityMapper.updateCapacity(servicePolicy);//modifyId, modifyDateTime수정해야함(history도)

        /*SAL update History*/
        resultCount += capacityMapper.updateHistCapacity(servicePolicy);

        /*SAL insertHistory*/
        resultCount += capacityMapper.insertHistCapacity(servicePolicy);

        /*SAL(Test) Update*/
        testResultCount += capacityMapper.updateCapacityTest(servicePolicy);//modifyId, modifyDateTime수정해야함(history도)

        /*SAL(Test) update History*/
        testResultCount += capacityMapper.updateHistCapacityTest(servicePolicy);

        /*SAL(Test) insertHistory*/
        testResultCount += capacityMapper.insertHistCapacityTest(servicePolicy);

        response.put("resultCommon", resultCount);
        response.put("resultTest", testResultCount);
        
        if(env.getProperty("system.interfaceYn").equals("Y"))
            policyCacheService.policyCapacityCacheModify(servicePolicy);
    }

    /**
     * Create capacity policy service policy.
     *
     * @param serviceNumber  the service number
     * @param apiGroupNumber the api group number
     * @return the service policy
     */
    @Override
    @Transactional
    public ServicePolicy createCapacityPolicy(long serviceNumber, long apiGroupNumber) {
        ServicePolicy policy = new ServicePolicy();
        authenticationInjector.setAuthentication(policy);
        policy.setServiceNumber(serviceNumber);
        policy.setApiGroupNumber(apiGroupNumber);
        policy.setPolicyCode(OapServicePolicyCode.CAPACITY.getCode());
        policy.setGradeCode(ServiceGradeCode.PRODUCT.getCode());
        //CAPACITY 상용 등급 추가
        capacityMapper.insertServiceCapacity(policy);
        policy.setGradeCode(ServiceGradeCode.TEST.getCode());
        //CAPACITY TEST 등급 추가
        capacityMapper.insertServiceCapacity(policy);
        //CAPACITY Hist 등록
        capacityMapper.insertServiceCapacityHistory(policy);
        return policy;
    }

    /**
     * Update capacity policy.
     *
     * @param serviceNumber  the service number
     * @param apiGroupNumber the api group number
     */
    @Override
    @Transactional
    public void updateCapacityPolicy(long serviceNumber, long apiGroupNumber) {
        ServicePolicy policy = new ServicePolicy();
        authenticationInjector.setAuthentication(policy);
        policy.setServiceNumber(serviceNumber);
        policy.setApiGroupNumber(apiGroupNumber);
        policy.setPolicyCode(OapServicePolicyCode.CAPACITY.getCode());
        policy.setGradeCode(ServiceGradeCode.PRODUCT.getCode());
        //CAPACITY 상용 등급 추가 또는 수정
        capacityMapper.updateServiceCapacity(policy);
        policy.setGradeCode(ServiceGradeCode.TEST.getCode());
        //CAPACITY TEST 등급 추가
        capacityMapper.updateServiceCapacity(policy);
        //CAPACITY Hist 종료
        capacityMapper.updateServiceCapacityHistory(policy);
        //CAPACITY Hist 추가
        capacityMapper.insertServiceCapacityHistory(policy);
    }

    /**
     * Update capacity policy delete.
     *
     * @param serviceNumber  the service number
     * @param apiGroupNumber the api group number
     */
    @Override
    @Transactional
    public void updateCapacityPolicyDelete(long serviceNumber, long apiGroupNumber) {
        ServicePolicy policy = new ServicePolicy();
        authenticationInjector.setAuthentication(policy);
        policy.setServiceNumber(serviceNumber);
        policy.setApiGroupNumber(apiGroupNumber);
        //미사용 처리
        capacityMapper.updateServiceCapacityDelete(policy);
        capacityMapper.updateServiceCapacityHistory(policy);
        capacityMapper.insertServiceCapacityHistory(policy);
    }
}
