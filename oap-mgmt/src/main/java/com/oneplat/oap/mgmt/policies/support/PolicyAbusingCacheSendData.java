package com.oneplat.oap.mgmt.policies.support;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;

import com.oneplat.oap.mgmt.common.cache.CacheSendData;
import com.oneplat.oap.mgmt.common.cache.RestSendData;
import com.oneplat.oap.mgmt.common.config.InterfaceConstants;
import com.oneplat.oap.mgmt.policies.model.ServicePolicy;
import com.oneplat.oap.mgmt.util.InterfaceSenderUtil;

/**
 * TODO 정책 cache Interface data 가공
 *
 * @author mike 
 * @date 2015. 7. 21
 */
public class PolicyAbusingCacheSendData implements CacheSendData {
    private ServicePolicy data;
    
    private Environment env;
    
    
    public PolicyAbusingCacheSendData(ServicePolicy data, Environment env) {
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
        String url = "";
        if(data.getApiGroupNumber()==0)
            url = env.getProperty("interface.server.domain") + InterfaceConstants.ABUSING_DATA_URL + "/" + data.getServiceNumber();
        else
            url = env.getProperty("interface.server.domain") + InterfaceConstants.ABUSING_DATA_URL + "/" + data.getApiGroupNumber();
        /* restSendData set*/
        restSendData.setUrl(url);
        restSendData.setHttpMethod(HttpMethod.valueOf(InterfaceConstants.HTTP_METHOD_TYPE_DELETE));
        restSendData.setMultipartYn("N");
        restSendData.setPayloadObject(new HashMap<String, Object>());
        return restSendData;
    }

    @Override
    public RestSendData makeCreateData() throws Exception{
     // TODO Auto-generated method stub
        /*
         * OSDF Interfacing
         */
        /** Set Base Send Parameter */
        RestSendData restSendData = new RestSendData();
        restSendData.setPayloadObject(convertData());//serviceName
        String url = "";
        /** Send Cache Data To Interface Server */
        if(data.getApiGroupNumber()==0)
            url = env.getProperty("interface.server.domain") + InterfaceConstants.ABUSING_DATA_URL + "/" + data.getServiceNumber();
        else
            url = env.getProperty("interface.server.domain") + InterfaceConstants.ABUSING_DATA_URL + "/" + data.getApiGroupNumber();
        /* restSendData set*/
        restSendData.setUrl(url);
        restSendData.setHttpMethod(HttpMethod.valueOf(InterfaceConstants.HTTP_METHOD_TYPE_PUT));
        restSendData.setMultipartYn("N");
        
        return restSendData;
    }
    
    @Override
    public RestSendData makeModifyData() throws Exception {
        // TODO Auto-generated method stub
        /*
         * OSDF Interfacing
         */
        /** Set Base Send Parameter */
        RestSendData restSendData = new RestSendData();
        restSendData.setPayloadObject(convertData());//serviceName
        String url = "";
        /** Send Cache Data To Interface Server */
        if(data.getApiGroupNumber()==0)
            url = env.getProperty("interface.server.domain") + InterfaceConstants.ABUSING_DATA_URL + "/" + data.getServiceNumber();
        else
            url = env.getProperty("interface.server.domain") + InterfaceConstants.ABUSING_DATA_URL + "/" + data.getApiGroupNumber();
        
        /* restSendData set*/
        restSendData.setUrl(url);
        restSendData.setHttpMethod(HttpMethod.valueOf(InterfaceConstants.HTTP_METHOD_TYPE_PUT));
        restSendData.setMultipartYn("N");
        
        return restSendData;
    }

    @Override
    public RestSendData makeModifyStatus() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }
    
    public ServicePolicy getData() {
        return data;
    }

    public void setData(ServicePolicy data) {
        this.data = data;
    }
    
    public Map<String,Object> convertData(){
        
        /** Set Base Send Parameter */
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        if(data.getApiGroupNumber()==0)
            parameterMap.put("key", String.valueOf(data.getServiceNumber()));
        else
            parameterMap.put("key", String.valueOf(data.getApiGroupNumber()));
        
        
        parameterMap.put("type", data.getApiGroupNumber()==0?"":"");
        parameterMap.put("interval", InterfaceSenderUtil.getIntervalValue(data.getCriteriaCode()));    
        parameterMap.put("maxCount", data.getLimitQuantity());
//        parameterMap.put("restrictInterval", ConvertUtil.getIntervalValue(data.getLockTimeBaseCode())); 
//        parameterMap.put("restrictMaxCount", data.getLockTimeQuantity());
        parameterMap.put("enabled", true);//정책은 사용여부 속성이 별도 없음.
        
        return parameterMap;
    }
    
    
}