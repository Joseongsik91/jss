package com.oneplat.oap.mgmt.policies.support;

import com.oneplat.oap.mgmt.common.cache.CacheSendData;
import com.oneplat.oap.mgmt.common.cache.RestSendData;
import com.oneplat.oap.mgmt.common.config.InterfaceConstants;
import com.oneplat.oap.mgmt.policies.model.Scope;
import com.oneplat.oap.mgmt.policies.model.ScopeApi;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO 정책 cache Interface data 가공
 *
 * @author hong
 * @date 2017. 2. 9
 */
public class ScopeApiCacheSendData implements CacheSendData {

    private Environment env;
    private Map<String, Object> data;

    public ScopeApiCacheSendData(Map<String, Object> data, Environment env) {
        super();
        this.data = data;
        this.env = env;
    }

    @Override
    public RestSendData makeGetData() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RestSendData makeDeleteData() throws Exception {
     // TODO Auto-generated method stub
        RestSendData restSendData = new RestSendData();
        return restSendData;
    }

    @Override
    public RestSendData makeCreateData() throws Exception{
     // TODO Auto-generated method stub
        RestSendData restSendData = new RestSendData();
        return restSendData;
    }
    
    @Override
    public RestSendData makeModifyData() throws Exception {
        // TODO Auto-generated method stub
        RestSendData restSendData = new RestSendData();
        String url = env.getProperty("interface.server.domain") + InterfaceConstants.API_CACHE_DATA_URL + "/" + data.get("apiNumber");
        /* restSendData set*/
        restSendData.setUrl(url);
        restSendData.setHttpMethod(HttpMethod.valueOf(InterfaceConstants.HTTP_METHOD_TYPE_PUT));
        restSendData.setMultipartYn("N");
        restSendData.setPayloadObject(convertData());
        return restSendData;
    }

    @Override
    public RestSendData makeModifyStatus() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }
    
    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    @Override
    public Map<String, Object> convertData() {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put("scope", (null == data.get("scopeContext") ? "": data.get("scopeContext")));
        return parameterMap;
    }

}