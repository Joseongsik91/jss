package com.oneplat.oap.mgmt.application.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;

import com.oneplat.oap.mgmt.application.model.DcApplicationScope;
import com.oneplat.oap.mgmt.common.cache.RestSendData;
import com.oneplat.oap.mgmt.common.config.InterfaceConstants;

public class ApplicationScopeCacheSendData {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationScopeCacheSendData.class);
    
    private DcApplicationScope data;
    
    private Environment env;
    
    public ApplicationScopeCacheSendData(DcApplicationScope data, Environment env) {
        super();
        this.data = data;
        this.env = env;
    }
    
    public RestSendData makeCreateData() throws Exception {
        // TODO Auto-generated method stub
        /*
         * OAuth Interfacing
         */
        /** Set Base Send Parameter */
        RestSendData restSendData = new RestSendData();
        restSendData.setPayloadObject(convertData());//applicationScope 정보
        String url = "";
        /** Send Cache Data To Interface Server */
        url = env.getProperty("oauth.interface.server.domain") + "/mnt/clients";
        LOGGER.debug("[makeCreateData] url : {} ", url);
        /* restSendData set*/
        restSendData.setUrl(url);
        restSendData.setHttpMethod(HttpMethod.valueOf(InterfaceConstants.HTTP_METHOD_TYPE_POST));
        restSendData.setMultipartYn("N");
        
        return restSendData;
    }

    public RestSendData makeModifyData() throws Exception {
        // TODO Auto-generated method stub
        /*
         * OAuth Interfacing
         */
        RestSendData restSendData = new RestSendData();
        String url = "";
        /** Send Cache Data To Interface Server */
        url = env.getProperty("oauth.interface.server.domain") + "/mnt/clients/"+ data.getClientId() +"/changeSecret";
        LOGGER.debug("[makeModifyData] url : {} ", url);
        /* restSendData set*/
        restSendData.setUrl(url);
        restSendData.setHttpMethod(HttpMethod.valueOf(InterfaceConstants.HTTP_METHOD_TYPE_PUT));
        restSendData.setMultipartYn("N");

        return restSendData;
    }

    public RestSendData makeModifyScopeData() throws Exception {
        // TODO Auto-generated method stub

        /*
         * OAuth Interfacing
         */
        RestSendData restSendData = new RestSendData();
        restSendData.setPayloadStringList(data.getScopeContextList());
        String url = "";
        /** Send Cache Data To Interface Server */
        url = env.getProperty("oauth.interface.server.domain") + "/mnt/clients/"+ data.getClientId() +"/changeScope";
        LOGGER.debug("[makeModifyScopeData] url : {} ", url);
        /* restSendData set*/
        restSendData.setUrl(url);
        restSendData.setHttpMethod(HttpMethod.valueOf(InterfaceConstants.HTTP_METHOD_TYPE_PUT));
        restSendData.setMultipartYn("N");

        return restSendData;
    }

    public DcApplicationScope getData() {
        return data;
    }

    public void setData(DcApplicationScope data) {
        this.data = data;
    }

    public Map<String, Object> convertData() {
        
        /** Set Base Send Parameter */
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        
        List<String> grantTypeList = new ArrayList<String>();
        grantTypeList.add("client_credentials");

        List<String> scopeList = new ArrayList<String>();
        for(String scope : data.getScopeContextList()) {
            scopeList.add(scope);
        }
        parameterMap.put("accessTokenValiditySeconds", 3600);
        parameterMap.put("applicationNumber", data.getApplicationNumber());
        parameterMap.put("authorizedGrantTypes", grantTypeList);
        parameterMap.put("clientName", data.getApplicationName());
        parameterMap.put("clientDesc", data.getApplicationDescription());
        parameterMap.put("refreshTokenUseYn", false);
        parameterMap.put("refreshTokenValiditySeconds", 0);
        parameterMap.put("scope", scopeList);
        
        return parameterMap;
    }
    
}
