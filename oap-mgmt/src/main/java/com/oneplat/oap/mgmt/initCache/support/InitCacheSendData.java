package com.oneplat.oap.mgmt.initCache.support;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;

import com.oneplat.oap.mgmt.common.cache.CacheSendData;
import com.oneplat.oap.mgmt.common.cache.RestSendData;
import com.oneplat.oap.mgmt.common.config.InterfaceConstants;

/**
 * TODO 전체 초기 데이타 cache Interface data 가공
 *
 * @author mike 
 * @date 2015. 7. 21
 */
public class InitCacheSendData implements CacheSendData {
    
    private Environment env;
    private Map<String, Object> data;
    
    public InitCacheSendData(Environment env) {
        super();
        this.env = env;
        data = new HashMap<String, Object>();
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
        String url = env.getProperty("interface.server.domain") + InterfaceConstants.INIT_DATA_URL;
        /* restSendData set*/
        restSendData.setUrl(url);
        restSendData.setHttpMethod(HttpMethod.valueOf(InterfaceConstants.HTTP_METHOD_TYPE_DELETE));
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
        String url = env.getProperty("interface.server.domain") + InterfaceConstants.INIT_DATA_URL;
        //String url = "http://localhost:8080/oc/apis/initCache/initialize";
        /* restSendData set*/
        restSendData.setUrl(url);
        restSendData.setHttpMethod(HttpMethod.valueOf(InterfaceConstants.HTTP_METHOD_TYPE_POST));
        restSendData.setMultipartYn("N");
        //restSendData.setPayloadObject(convertData());//serviceName
        restSendData.setMultipartFileInfo(convertData());//serviceName
        /** Send Cache Data To Interface Server */
//        InterfaceSenderUtil interfaceSender = new InterfaceSenderUtil();
//        int statusCode = interfaceSender.sendData(restSendData, restTemplate);
//
//        if(statusCode != HttpStatus.SC_OK)
//            throw new ServiceException(ApiConstants.DATA_INTERFACE_FAIL, ""+statusCode); 
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
        /** Send Cache Data To Interface Server */
//        InterfaceSenderUtil interfaceSender = new InterfaceSenderUtil();
//        int statusCode = interfaceSender.sendData(restSendData, restTemplate);
//
//        if(statusCode != HttpStatus.SC_OK)
//            throw new ServiceException(ApiConstants.DATA_INTERFACE_FAIL, ""+statusCode); 
//        return statusCode;
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

    public void setData(Map<String, Object>data) {
        this.data = data;
    }
    
    public Map<String,Object> convertData(){
        
        /** Set Base Send Parameter */
        Map<String, Object> totalData = new HashMap<String, Object>();
        totalData.put("initializeFile", this.data);
        //totalData.put("test", apiGatewayConfig);
        return totalData;
    }


    
}
