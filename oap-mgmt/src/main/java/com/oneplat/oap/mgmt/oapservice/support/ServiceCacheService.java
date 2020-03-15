package com.oneplat.oap.mgmt.oapservice.support;

import com.oneplat.oap.core.exception.ServiceException;
import com.oneplat.oap.mgmt.common.cache.CacheSendData;
import com.oneplat.oap.mgmt.common.cache.CacheSender;
import com.oneplat.oap.mgmt.common.config.ApiConstants;
import com.oneplat.oap.mgmt.oapservice.model.OapService;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author lee
 * @date 2017-01-05
 */
@Service
public class ServiceCacheService {
    @Autowired
    private Environment env;
    @Autowired
    private RestTemplate restTemplate;

    public void serviceCacheCreate(OapService oapService){
        CacheSendData serviceCacheData = new ServiceCacheSendData(oapService, env);
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

    public void serviceCacheModify(OapService oapService){
        CacheSendData serviceCacheData = new ServiceCacheSendData(oapService, env);
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

    public void serviceCacheDelete(OapService oapService){
        CacheSendData serviceCacheData = new ServiceCacheSendData(oapService, env);
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
