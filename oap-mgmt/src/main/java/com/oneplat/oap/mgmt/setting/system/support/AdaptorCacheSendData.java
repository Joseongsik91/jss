package com.oneplat.oap.mgmt.setting.system.support;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;

import com.oneplat.oap.mgmt.common.cache.CacheSendData;
import com.oneplat.oap.mgmt.common.cache.CommonCodeCache;
import com.oneplat.oap.mgmt.common.cache.RestSendData;
import com.oneplat.oap.mgmt.common.config.InterfaceConstants;
import com.oneplat.oap.mgmt.setting.system.model.Adaptor;

/**
 * TODO Adaptor cache Interface data 가공
 *
 * @author mike 
 * @date 2015. 7. 21
 */
public class AdaptorCacheSendData implements CacheSendData {
    private Adaptor data;
    
    private Environment env;
    
    
    public AdaptorCacheSendData(Adaptor data, Environment env) {
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
        String url = env.getProperty("interface.server.domain") + InterfaceConstants.ADAPTOR_DATA_URL + "/" + data.getAdaptorNumber();
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
        String url = env.getProperty("interface.server.domain") + InterfaceConstants.ADAPTOR_DATA_URL;
        /* restSendData set*/
        restSendData.setUrl(url);
        restSendData.setHttpMethod(HttpMethod.valueOf(InterfaceConstants.HTTP_METHOD_TYPE_POST));
        restSendData.setMultipartYn("N");
        restSendData.setPayloadObject(convertData());
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
                             
        String url = env.getProperty("interface.server.domain") + InterfaceConstants.ADAPTOR_DATA_URL + "/" + data.getAdaptorNumber();
        /* restSendData set*/
        restSendData.setUrl(url);
        restSendData.setHttpMethod(HttpMethod.valueOf(InterfaceConstants.HTTP_METHOD_TYPE_PUT));
        restSendData.setMultipartYn("N");
        restSendData.setPayloadObject(convertData());
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


    public Adaptor getData() {
        return data;
    }

    public void setData(Adaptor data) {
        this.data = data;
    }
    
    public Map<String,Object> convertData(){
        
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        /* serviceId set*/
        parameterMap.put("adaptorId", String.valueOf(data.getAdaptorNumber()));
        /* serviceName set*/
        parameterMap.put("adaptorBeanId", data.getAdaptorBeanId());
        
        return parameterMap;
    } 
    
}
