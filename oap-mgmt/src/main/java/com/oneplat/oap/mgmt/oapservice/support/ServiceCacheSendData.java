package com.oneplat.oap.mgmt.oapservice.support;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;

import com.oneplat.oap.mgmt.common.cache.CacheSendData;
import com.oneplat.oap.mgmt.common.cache.CommonCodeCache;
import com.oneplat.oap.mgmt.common.cache.RestSendData;
import com.oneplat.oap.mgmt.common.config.InterfaceConstants;
import com.oneplat.oap.mgmt.oapservice.model.OapService;

/**
 * TODO 서비스 cache Interface data 가공
 *
 * @author mike 
 * @date 2015. 7. 21
 */
public class ServiceCacheSendData implements CacheSendData {
    private OapService data;
    
    private Environment env;
    
    public ServiceCacheSendData(OapService data, Environment env) {
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
        String url = env.getProperty("interface.server.domain") + InterfaceConstants.SERVICE_CACHE_DATA_URL + data.getServiceNumber();
        /* restSendData set*/
        restSendData.setUrl(url);
        restSendData.setHttpMethod(HttpMethod.valueOf(InterfaceConstants.HTTP_METHOD_TYPE_DELETE));
        //restSendData.setPayloadObject(new HashMap<String, Object>());
        restSendData.setMultipartYn("N");
        return restSendData;
    }

    @Override
    public RestSendData makeCreateData() throws Exception{
        /*
         * OSDF Interfacing
         */
        /** Set Base Send Parameter */
        RestSendData restSendData = new RestSendData();
        
        String url = env.getProperty("interface.server.domain") + InterfaceConstants.SERVICE_CACHE_DATA_URL;
        /* restSendData set*/
        restSendData.setUrl(url);
        restSendData.setHttpMethod(HttpMethod.valueOf(InterfaceConstants.HTTP_METHOD_TYPE_POST));
        restSendData.setMultipartYn("N");
        restSendData.setPayloadObject(convertData());//serviceName
        /** Send Cache Data To Interface Server */
        
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
        String url = env.getProperty("interface.server.domain") + InterfaceConstants.SERVICE_CACHE_DATA_URL + data.getServiceNumber();
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
        /*
         * OSDF Interfacing
         */
        /** Set Base Send Parameter */
        RestSendData restSendData = new RestSendData();
        Map<String, String> urlParameterMap = new HashMap<String, String>();
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put("enabled", data.getServiceUseYn());                                // 서비스 동작여부 Y
        String url = env.getProperty("interface.server.domain") + InterfaceConstants.SERVICE_CACHE_DATA_URL + data.getServiceNumber();
        /* restSendData set*/
        restSendData.setUrl(url);
        restSendData.setHttpMethod(HttpMethod.valueOf(InterfaceConstants.HTTP_METHOD_TYPE_PUT));
        restSendData.setMultipartYn("N");
        restSendData.setPayloadObject(parameterMap);//serviceName
        restSendData.setUrlVariables(urlParameterMap);//serviceId
        return null;
    }

    public OapService getData() {
        return data;
    }

    public void setData(OapService data) {
        this.data = data;
    }
    
    public Map<String,Object> convertData(){
        
        /** Set Base Send Parameter */
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put("serviceId", String.valueOf(data.getServiceNumber()));
        parameterMap.put("serviceName", data.getServiceName());
        /* 사이트 구분*/
        parameterMap.put("site", data.getSiteCode());
        /* 서비스 구분*/
        parameterMap.put("serviceType", data.getServiceSectionCode());
        /* 사용 여부*/
        parameterMap.put("enabled", data.getServiceUseYn());
        /* SLA 사용 여부*/
        parameterMap.put("slaEnabled", data.getSlaUseYn());
        /* Capacity 사용 여부*/
        parameterMap.put("capacityEnabled", data.getCapacityUseYn());
        /*
        if (config.getDataClassifyCode().equals(ApiConstants.CONTENT_TYPE_CODE)) //default contentType 등록
            parameterMap.put("defaultContentType", "application/json");
        */
        return parameterMap;
    }
    
    
}
