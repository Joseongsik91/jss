package com.oneplat.oap.mgmt.policies.support;

import com.oneplat.oap.core.exception.ServiceException;
import com.oneplat.oap.mgmt.common.cache.CacheSendData;
import com.oneplat.oap.mgmt.common.cache.CacheSender;
import com.oneplat.oap.mgmt.common.config.ApiConstants;
import com.oneplat.oap.mgmt.oapservice.model.OapService;
import com.oneplat.oap.mgmt.oapservice.support.ServiceCacheSendData;
import com.oneplat.oap.mgmt.policies.model.Scope;
import com.oneplat.oap.mgmt.policies.model.ServicePolicy;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author lee
 * @date 2017-01-06
 */
@Service
public class PolicyCacheService {
    @Autowired
    private Environment env;
    @Autowired
    private RestTemplate restTemplate;

    public void policyCapacityCacheModify(ServicePolicy servicePolicy){
        CacheSendData apiCacheData = new PolicyCapacityCacheSendData(servicePolicy, env);
        CacheSender cacheSender = new CacheSender(apiCacheData, restTemplate);
        try {
            int statusCode = cacheSender.sendModify();
            if(statusCode != HttpStatus.SC_OK)
                throw new ServiceException(ApiConstants.DATA_INTERFACE_FAIL, ""+statusCode);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new ServiceException(ApiConstants.DATA_INTERFACE_FAIL, "9999");
        }
    }

    public void policySlaCacheModify(ServicePolicy servicePolicy){
        CacheSendData apiCacheData = new PolicySlaCacheSendData(servicePolicy, env);
        CacheSender cacheSender = new CacheSender(apiCacheData, restTemplate);
        try {
            int statusCode = cacheSender.sendModify();
            if(statusCode != HttpStatus.SC_OK)
                throw new ServiceException(ApiConstants.DATA_INTERFACE_FAIL, ""+statusCode);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new ServiceException(ApiConstants.DATA_INTERFACE_FAIL, "9999");
        }
    }


    public void policyScopeCacheCreate(Scope scope){
        CacheSendData scopeCacheData = new ScopeCacheSendData(scope, env);
        CacheSender cacheSender = new CacheSender(scopeCacheData, restTemplate);
        try {
            int statusCode = cacheSender.sendCreate();
            if(statusCode != HttpStatus.SC_OK){
                throw new ServiceException(ApiConstants.DATA_INTERFACE_FAIL, ""+statusCode);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new ServiceException(ApiConstants.DATA_INTERFACE_FAIL, "9999");
        }
    }

    public void policyScopeCacheModify(Scope scope){
        CacheSendData scopeCacheData = new ScopeCacheSendData(scope, env);
        CacheSender cacheSender = new CacheSender(scopeCacheData, restTemplate);
        try {
            int statusCode = cacheSender.sendModify();
            if(statusCode != HttpStatus.SC_OK){
                throw new ServiceException(ApiConstants.DATA_INTERFACE_FAIL, ""+statusCode);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new ServiceException(ApiConstants.DATA_INTERFACE_FAIL, "9999");
        }
    }
    
    public void policyScopeCacheDelete(Scope scope){
        CacheSendData scopeCacheData = new ScopeCacheSendData(scope, env);
        CacheSender cacheSender = new CacheSender(scopeCacheData, restTemplate);
        try {
            int statusCode = cacheSender.sendDelete();
            if(statusCode != HttpStatus.SC_OK){
                throw new ServiceException(ApiConstants.DATA_INTERFACE_FAIL, ""+statusCode);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new ServiceException(ApiConstants.DATA_INTERFACE_FAIL, "9999");
        }
    }
    
    public void policyScopeApiCacheModify(Map<String, Object> scopeApi){
        CacheSendData scopeApiCacheData = new ScopeApiCacheSendData(scopeApi, env);
        CacheSender cacheSender = new CacheSender(scopeApiCacheData, restTemplate);
        try {
            int statusCode = cacheSender.sendModify();
            if(statusCode != HttpStatus.SC_OK){
                throw new ServiceException(ApiConstants.DATA_INTERFACE_FAIL, ""+statusCode);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new ServiceException(ApiConstants.DATA_INTERFACE_FAIL, "9999");
        }
    }

}
