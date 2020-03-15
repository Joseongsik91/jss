package com.oneplat.oap.mgmt.common.cache;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.oneplat.oap.mgmt.util.InterfaceSenderUtil;

/**
 * Cache interfacing 전송 처리 
 *
 * @author mike 
 * @date 2015. 4. 9
 */
public class CacheSender {
    
    RestTemplate restTemplate;
    
    CacheSendData cacheSendData;
    
    
    public CacheSender(CacheSendData cacheSendData, RestTemplate restTemplate) {
        super();
        this.cacheSendData = cacheSendData;
        this.restTemplate = restTemplate;
    }
    
    public int sendGet()throws Exception{
        int statusCode = 0;
        /** Send Cache Data To Interface Server */
        InterfaceSenderUtil interfaceSender = new InterfaceSenderUtil();
        statusCode = interfaceSender.sendData(cacheSendData.makeGetData(), restTemplate);

        return statusCode;
    }
    public int sendCreate()throws Exception{
        int statusCode = 0;
        /** Send Cache Data To Interface Server */
        InterfaceSenderUtil interfaceSender = new InterfaceSenderUtil();
        statusCode = interfaceSender.sendData(cacheSendData.makeCreateData(), restTemplate);

        return statusCode;
    }
    public int sendModify()throws Exception{
        int statusCode = 0;
        /** Send Cache Data To Interface Server */
        InterfaceSenderUtil interfaceSender = new InterfaceSenderUtil();
        statusCode = interfaceSender.sendData(cacheSendData.makeModifyData(), restTemplate);

        return statusCode;
    }
    public int sendDelete()throws Exception{
        int statusCode = 0;
        /** Send Cache Data To Interface Server */
        InterfaceSenderUtil interfaceSender = new InterfaceSenderUtil();
        ResponseEntity<String> responseEntity = interfaceSender.getData(cacheSendData.makeDeleteData(), restTemplate);
        statusCode = responseEntity.getStatusCode().value();
        return statusCode;
    }
    public int sendModifyStatus()throws Exception{
        int statusCode = 0;
        /** Send Cache Data To Interface Server */
        InterfaceSenderUtil interfaceSender = new InterfaceSenderUtil();
        statusCode = interfaceSender.sendData(cacheSendData.makeModifyStatus(), restTemplate);

        return statusCode;
    }
    
}
