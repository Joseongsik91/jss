package com.oneplat.oap.mgmt.policies.support;

import com.oneplat.oap.mgmt.common.cache.CacheSendData;
import com.oneplat.oap.mgmt.common.cache.RestSendData;
import com.oneplat.oap.mgmt.common.config.InterfaceConstants;
import com.oneplat.oap.mgmt.policies.model.Scope;
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
public class ScopeCacheSendData implements CacheSendData {

    private Environment env;
    private Scope data;

    public ScopeCacheSendData(Scope data, Environment env) {
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

        String url = env.getProperty("oauth.interface.server.domain") + InterfaceConstants.OAUTH_SCOPE_CACHE_DATA_URL + "/" + data.getScopeContext();
        /* restSendData set*/
        restSendData.setUrl(url);
        restSendData.setHttpMethod(HttpMethod.valueOf(InterfaceConstants.HTTP_METHOD_TYPE_DELETE));
        restSendData.setMultipartYn("N");
        
        return restSendData;
    }

    @Override
    public RestSendData makeCreateData() throws Exception{
     // TODO Auto-generated method stub
        RestSendData restSendData = new RestSendData();

        String url = env.getProperty("oauth.interface.server.domain") + InterfaceConstants.OAUTH_SCOPE_CACHE_DATA_URL;
        /* restSendData set*/
        restSendData.setUrl(url);
        restSendData.setHttpMethod(HttpMethod.valueOf(InterfaceConstants.HTTP_METHOD_TYPE_POST));
        restSendData.setMultipartYn("N");
        restSendData.setPayloadObject(convertData());

        return restSendData;
    }
    
    @Override
    public RestSendData makeModifyData() throws Exception {
        // TODO Auto-generated method stub
        /** Set Base Send Parameter */
    	RestSendData restSendData = new RestSendData();

        String url = env.getProperty("oauth.interface.server.domain") + InterfaceConstants.OAUTH_SCOPE_CACHE_DATA_URL + "/" + data.getScopeContext();
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
    
    public Scope getData() {
        return data;
    }

    public void setData(Scope data) {
        this.data = data;
    }
    
    public Map<String,Object> convertData(){
        /** Set Base Send Parameter */
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        
        if(data.getCriteriaScopeNumber()==0)
        	parameterMap.put("criteriaScope", "ROOT");
        else
        	parameterMap.put("criteriaScope", data.getParentContext());
        
        parameterMap.put("criteriaScopeLevel", data.getCriteriaScopeLevel());
        parameterMap.put("criteriaSortNum", data.getCriteriaSortNumber());
        parameterMap.put("iconUrl", data.getIconFileChannel());
        parameterMap.put("opponentScope", data.getScopeContext());
        parameterMap.put("opponentScopeDesc", data.getScopeDescription());
        parameterMap.put("opponentScopeLevel", data.getOpponentScopeLevel());
        parameterMap.put("opponentScopeName", data.getScopeName());
        parameterMap.put("opponentSortNum", data.getOpponentSortNumber());
        return parameterMap;
    }

}