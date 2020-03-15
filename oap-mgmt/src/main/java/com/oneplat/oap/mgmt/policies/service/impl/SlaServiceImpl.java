package com.oneplat.oap.mgmt.policies.service.impl;

import com.oneplat.oap.mgmt.common.model.AuthenticationInjector;
import com.oneplat.oap.mgmt.policies.mapper.SlaMapper;
import com.oneplat.oap.mgmt.policies.model.ServicePolicy;
import com.oneplat.oap.mgmt.policies.model.enums.OapServicePolicyCode;
import com.oneplat.oap.mgmt.policies.model.enums.ServiceGradeCode;
import com.oneplat.oap.mgmt.policies.service.SlaService;
import com.oneplat.oap.mgmt.policies.support.PolicyCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class SlaServiceImpl implements SlaService {

    @Autowired
    private SlaMapper slaMapper;
    @Autowired
    AuthenticationInjector authenticationInjector;
    @Autowired
    private Environment env;
    @Autowired
    private PolicyCacheService policyCacheService;

    private static final Logger LOGGER = LoggerFactory.getLogger(SlaServiceImpl.class);

    @Override
    public List<ServicePolicy> slaList() {

        /*SLA Service List + Service Group List*/
        List<ServicePolicy> slaPolicyList = slaMapper.slaPolicyList();

        LOGGER.debug("slaPolicyList : {}" , slaPolicyList);

        return slaPolicyList;
    }

    @Override
    @Transactional
    public void modifySla(ServicePolicy servicePolicy) {
        int resultCount = 0;
        int testResultCount = 0;
        Map<String, Object> response = new HashMap<>();

        authenticationInjector.setAuthentication(servicePolicy);
        LOGGER.debug("++++++++++++{}", servicePolicy.getCreateId());

        /*SAL Update*/
        resultCount += slaMapper.updateSla(servicePolicy);//modifyId, modifyDateTime수정해야함(history도)

        /*SAL update History*/
        resultCount += slaMapper.updateHistSla(servicePolicy);

        /*SAL insertHistory*/
        resultCount += slaMapper.insertHistSla(servicePolicy);

        /*SAL(Test) Update*/
        testResultCount += slaMapper.updateSlaTest(servicePolicy);//modifyId, modifyDateTime수정해야함(history도)

        /*SAL(Test) update History*/
        testResultCount += slaMapper.updateHistSlaTest(servicePolicy);

        /*SAL(Test) insertHistory*/
        testResultCount += slaMapper.insertHistSlaTest(servicePolicy);

        if(env.getProperty("system.interfaceYn").equals("Y"))
            policyCacheService.policySlaCacheModify(servicePolicy);
    }

    /**
     * Create sla policy service policy.
     *
     * @param serviceNumber  the service number
     * @param apiGroupNumber the api group number
     * @return the service policy
     */
    @Override
    @Transactional
    public ServicePolicy createSlaPolicy(long serviceNumber, long apiGroupNumber) {
        ServicePolicy policy = new ServicePolicy();
        authenticationInjector.setAuthentication(policy);
        policy.setServiceNumber(serviceNumber);
        policy.setApiGroupNumber(apiGroupNumber);
        policy.setPolicyCode(OapServicePolicyCode.SLA.getCode());
        policy.setGradeCode(ServiceGradeCode.PRODUCT.getCode());
        //SLA 상용 등급 추가
        slaMapper.insertServiceSla(policy);
        policy.setGradeCode(ServiceGradeCode.TEST.getCode());
        //SLA TEST 등급 추가
        slaMapper.insertServiceSla(policy);
        //SLA Hist 등록
        slaMapper.insertServiceSlaHistory(policy);
        return policy;
    }

    /**
     * Update sla policy.
     *
     * @param serviceNumber  the service number
     * @param apiGroupNumber the api group number
     */
    @Override
    @Transactional
    public void updateSlaPolicy(long serviceNumber, long apiGroupNumber) {
        ServicePolicy policy = new ServicePolicy();
        authenticationInjector.setAuthentication(policy);
        policy.setServiceNumber(serviceNumber);
        policy.setApiGroupNumber(apiGroupNumber);
        policy.setPolicyCode(OapServicePolicyCode.SLA.getCode());
        policy.setGradeCode(ServiceGradeCode.PRODUCT.getCode());
        //SLA 상용 등급 추가 또는 수정
        slaMapper.updateServiceSla(policy);
        policy.setGradeCode(ServiceGradeCode.TEST.getCode());
        //SLA TEST 등급 추가
        slaMapper.updateServiceSla(policy);
        //SLA Hist 종료
        slaMapper.updateServiceSlaHistory(policy);
        //SLA Hist 추가
        slaMapper.insertServiceSlaHistory(policy);
    }

    /**
     * Update sla policy delete.
     *
     * @param serviceNumber  the service number
     * @param apiGroupNumber the api group number
     */
    @Override
    @Transactional
    public void updateSlaPolicyDelete(long serviceNumber, long apiGroupNumber) {
        ServicePolicy policy = new ServicePolicy();
        authenticationInjector.setAuthentication(policy);
        policy.setServiceNumber(serviceNumber);
        policy.setApiGroupNumber(apiGroupNumber);
        //미사용 처리
        slaMapper.updateServiceSlaDelete(policy);
        slaMapper.updateServiceSlaHistory(policy);
        slaMapper.insertServiceSlaHistory(policy);
    }
}
