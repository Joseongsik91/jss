package com.oneplat.oap.mgmt.common.cache;

import org.springframework.web.client.RestTemplate;

import com.oneplat.oap.mgmt.util.InterfaceSenderUtil;

public class MultipartCacheSender extends CacheSender{
    
    public MultipartCacheSender(CacheSendData cacheSendData,
            RestTemplate restTemplate) {
        super(cacheSendData, restTemplate);
        // TODO Auto-generated constructor stub
    }
    @Override
    public int sendCreate()throws Exception{
        int statusCode = 0;
        /** Send Cache Data To Interface Server */
        InterfaceSenderUtil interfaceSender = new InterfaceSenderUtil();
        statusCode = interfaceSender.sendDataMultipart(cacheSendData.makeCreateData(), restTemplate);

        return statusCode;
    }
    
    @Override
    public int sendModify()throws Exception{
        int statusCode = 0;
        /** Send Cache Data To Interface Server */
        InterfaceSenderUtil interfaceSender = new InterfaceSenderUtil();
        statusCode = interfaceSender.sendDataMultipart(cacheSendData.makeModifyData(), restTemplate);

        return statusCode;
    }

    
}
