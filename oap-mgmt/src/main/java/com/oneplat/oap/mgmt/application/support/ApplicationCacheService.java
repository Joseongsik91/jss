package com.oneplat.oap.mgmt.application.support;

import com.oneplat.oap.core.exception.ServiceException;
import com.oneplat.oap.mgmt.application.model.DcApplication;
import com.oneplat.oap.mgmt.application.model.DcApplicationKey;
import com.oneplat.oap.mgmt.application.model.DcApplicationScope;
import com.oneplat.oap.mgmt.application.model.DcApplicationSla;
import com.oneplat.oap.mgmt.common.cache.CacheSendData;
import com.oneplat.oap.mgmt.common.cache.CacheSender;
import com.oneplat.oap.mgmt.common.config.ApiConstants;
import com.oneplat.oap.mgmt.util.InterfaceSenderUtil;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApplicationCacheService {

    @Autowired
    private Environment env;
    
    @Autowired
    private RestTemplate restTemplate;
    
    /**
     * 애플리케이션 등록
     * 
     * @param application
     */
    public void createApplicationCacheData(DcApplication application) {
        CacheSendData applicationCacheData = new ApplicationCacheSendData(application, env);
        CacheSender cacheSender = new CacheSender(applicationCacheData, restTemplate);
        try {
            int statusCode = cacheSender.sendCreate();
            if(statusCode != HttpStatus.SC_OK)
                throw new ServiceException(ApiConstants.DATA_INTERFACE_FAIL, ""+statusCode);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new ServiceException(ApiConstants.DATA_INTERFACE_FAIL, "9999");
        }
    }
    
    /**
     * 애플리케이션 수정
     * 
     * @param application
     */
    public void modifyApplicationCacheData(DcApplication application) {
        CacheSendData applicationCacheData = new ApplicationCacheSendData(application, env);
        CacheSender cacheSender = new CacheSender(applicationCacheData, restTemplate);
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
    
    /**
     * 애플리케이션 삭제
     * 
     * @param application
     */
    public void removeApplicationCacheData(DcApplication application) {
        CacheSendData applicationCacheData = new ApplicationCacheSendData(application, env);
        CacheSender cacheSender = new CacheSender(applicationCacheData, restTemplate);
        try {
            int statusCode = cacheSender.sendDelete();
            if(statusCode != HttpStatus.SC_OK)
                throw new ServiceException(ApiConstants.DATA_INTERFACE_FAIL, ""+statusCode);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new ServiceException(ApiConstants.DATA_INTERFACE_FAIL, "9999");
        }
    }

    /**
     * 애플리케이션 키 등록
     * 
     * @param applicationKey
     */
    public void createApplicationKeyCacheData(DcApplicationKey applicationKey) {
        // TODO Auto-generated method stub
        CacheSendData applicationKeyCacheData = new ApplicationAuthKeyCacheSendData(applicationKey, env);
        CacheSender cacheSender = new CacheSender(applicationKeyCacheData, restTemplate);
        try {
            int statusCode = cacheSender.sendCreate();
            if(statusCode != HttpStatus.SC_OK)
                throw new ServiceException(ApiConstants.DATA_INTERFACE_FAIL, ""+statusCode);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new ServiceException(ApiConstants.DATA_INTERFACE_FAIL, "9999");
        }
    }

    /**
     * 애플리케이션 키 수정
     * 
     * @param applicationKey
     */
    public void modifyApplicationKeyCacheData(DcApplicationKey applicationKey) {
        // TODO Auto-generated method stub
        CacheSendData applicationKeyCacheData = new ApplicationAuthKeyCacheSendData(applicationKey, env);
        CacheSender cacheSender = new CacheSender(applicationKeyCacheData, restTemplate);
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
    
    /**
     * 애플리케이션 키 재발급
     * 
     * @param applicationKey
     */
    public void reissueApplicationKeyCacheData(DcApplicationKey applicationKey) {
        
        applicationKey.setApplicationKey(applicationKey.getReissueApplicationKey());
        
        // TODO Auto-generated method stub
        CacheSendData applicationKeyCacheData = new ApplicationAuthKeyCacheSendData(applicationKey, env);
        CacheSender cacheSender = new CacheSender(applicationKeyCacheData, restTemplate);
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

    /**
     * 애플리케이션 키 삭제
     * 
     * @param applicationKey
     */
    public void removeApplicationKeyCacheData(DcApplicationKey applicationKey) {
        // TODO Auto-generated method stub
        CacheSendData applicationKeyCacheData = new ApplicationAuthKeyCacheSendData(applicationKey, env);
        CacheSender cacheSender = new CacheSender(applicationKeyCacheData, restTemplate);
        try {
            int statusCode = cacheSender.sendDelete();
            if(statusCode != HttpStatus.SC_OK)
                throw new ServiceException(ApiConstants.DATA_INTERFACE_FAIL, ""+statusCode);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new ServiceException(ApiConstants.DATA_INTERFACE_FAIL, "9999");
        }
    }

    /**
     * 애플리케이션 SLA 변경
     * 
     * @param applicationSla
     */
    public void modifyApplicationSlaCacheData(DcApplicationSla applicationSla) {
        // TODO Auto-generated method stub
        CacheSendData applicationSlaCacheData = new ApplicationThrottleCacheSendData(applicationSla, env);
        CacheSender cacheSender = new CacheSender(applicationSlaCacheData, restTemplate);
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

    /**
     * 애플리케이션 스코프 추가
     * 
     * @param applicationScope
     */
    public String createApplicationScopeCacheData(DcApplicationScope applicationScope) {
        // TODO Auto-generated method stub
        ApplicationScopeCacheSendData applicationScopeCacheData = new ApplicationScopeCacheSendData(applicationScope, env);
        
        try {
            InterfaceSenderUtil interfaceSender = new InterfaceSenderUtil();
            ResponseEntity<String> response = interfaceSender.sendData2(applicationScopeCacheData.makeCreateData(), restTemplate);
            
            int statusCode = response.getStatusCode().value();
            if(statusCode == HttpStatus.SC_OK) {
                return response.getBody();
            }else {
                throw new ServiceException(ApiConstants.DATA_INTERFACE_FAIL, ""+statusCode);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new ServiceException(ApiConstants.DATA_INTERFACE_FAIL, "9999");
        }
    }

    /**
     * 애플리케이션 시크릿 키 재발급
     *
     * @param applicationScope
     */
    public String modifyApplicationSecretKeyCacheData(DcApplicationScope applicationScope) {
        // TODO Auto-generated method stub
        ApplicationScopeCacheSendData applicationScopeCacheData = new ApplicationScopeCacheSendData(applicationScope, env);
        try {
            InterfaceSenderUtil interfaceSender = new InterfaceSenderUtil();
            ResponseEntity<String> response = interfaceSender.sendData2(applicationScopeCacheData.makeModifyData(), restTemplate);

            int statusCode = response.getStatusCode().value();
            if(statusCode == HttpStatus.SC_OK) {
                return response.getBody();
            }else {
                throw new ServiceException(ApiConstants.DATA_INTERFACE_FAIL, ""+statusCode);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new ServiceException(ApiConstants.DATA_INTERFACE_FAIL, "9999");
        }
    }


    /**
     * 애플리케이션 Scope 수정
     *
     * @param applicationScope
     */
    public String modifyApplicationScopeData(DcApplicationScope applicationScope) {
        // TODO Auto-generated method stub
        ApplicationScopeCacheSendData applicationScopeCacheData = new ApplicationScopeCacheSendData(applicationScope, env);
        try {
            InterfaceSenderUtil interfaceSender = new InterfaceSenderUtil();
            ResponseEntity<String> response = interfaceSender.sendDataArray(applicationScopeCacheData.makeModifyScopeData(), restTemplate);

            int statusCode = response.getStatusCode().value();
            if(statusCode == HttpStatus.SC_OK) {
                return response.getBody();
            }else {
                throw new ServiceException(ApiConstants.DATA_INTERFACE_FAIL, ""+statusCode);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new ServiceException(ApiConstants.DATA_INTERFACE_FAIL, "9999");
        }
    }


}
