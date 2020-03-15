package com.oneplat.oap.mgmt.api.support;

import com.oneplat.oap.core.exception.ServiceException;
import com.oneplat.oap.mgmt.api.model.Api;
import com.oneplat.oap.mgmt.api.model.ApiInfo;
import com.oneplat.oap.mgmt.common.cache.CacheSendData;
import com.oneplat.oap.mgmt.common.cache.CacheSender;
import com.oneplat.oap.mgmt.common.cache.CommonCodeCache;
import com.oneplat.oap.mgmt.common.config.ApiConstants;
import com.oneplat.oap.mgmt.oapservice.mapper.ApiGroupMapper;
import com.oneplat.oap.mgmt.oapservice.model.OapService;
import com.oneplat.oap.mgmt.oapservice.support.ServiceCacheSendData;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author lee
 * @date 2017-01-09
 */
@Service
public class ApiCacheService {
    @Autowired
    private Environment env;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private CommonCodeCache commonCodeCache;
    @Autowired
    private ApiGroupMapper apiGroupMapper;

    public void apiCacheCreate(ApiInfo apiInfo){
        CacheSendData serviceCacheData = new ApiCacheSendData(apiInfo, env, commonCodeCache, apiGroupMapper);
        CacheSender cacheSender = new CacheSender(serviceCacheData, restTemplate);
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

    public void apiCacheModifyStatus(ApiInfo apiInfo){
        CacheSendData serviceCacheData = new ApiCacheSendData(apiInfo, env, commonCodeCache, apiGroupMapper);
        CacheSender cacheSender = new CacheSender(serviceCacheData, restTemplate);
        try {
            int statusCode = cacheSender.sendModifyStatus();
            if(statusCode != HttpStatus.SC_OK){
                throw new ServiceException(ApiConstants.DATA_INTERFACE_FAIL, ""+statusCode);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new ServiceException(ApiConstants.DATA_INTERFACE_FAIL, "9999");
        }
    }

    public void apiCacheModify(ApiInfo apiInfo){
        CacheSendData serviceCacheData = new ApiCacheSendData(apiInfo, env, commonCodeCache, apiGroupMapper);
        CacheSender cacheSender = new CacheSender(serviceCacheData, restTemplate);
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

    public void apiCacheDelete(ApiInfo apiInfo){
        CacheSendData serviceCacheData = new ApiCacheSendData(apiInfo, env, commonCodeCache, apiGroupMapper);
        CacheSender cacheSender = new CacheSender(serviceCacheData, restTemplate);
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
}
