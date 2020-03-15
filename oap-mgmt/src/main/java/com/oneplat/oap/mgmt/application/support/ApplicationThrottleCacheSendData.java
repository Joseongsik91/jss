package com.oneplat.oap.mgmt.application.support;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;

import com.oneplat.oap.mgmt.application.model.DcApplicationSla;
import com.oneplat.oap.mgmt.common.cache.CacheSendData;
import com.oneplat.oap.mgmt.common.cache.RestSendData;
import com.oneplat.oap.mgmt.common.config.InterfaceConstants;

/**
 * TODO 정책 cache Interface data 가공
 *
 * @author mike 
 * @date 2015. 7. 21
 */
public class ApplicationThrottleCacheSendData implements CacheSendData {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationThrottleCacheSendData.class);
    
    private DcApplicationSla data;
    
    private Environment env;
    
    
    public ApplicationThrottleCacheSendData(DcApplicationSla data, Environment env) {
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
        url = env.getProperty("interface.server.domain") + InterfaceConstants.APPLICATION_THROTTLE2_DATA_URL + data.getApplicationNumber() + "/keys/" + data.getApplicationNumber() + "||" + (data.getApiGroupNumber() > 0 ? data.getApiGroupNumber() : data.getServiceNumber());
        LOGGER.debug("[makeDeleteData] url : {} ", url);
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
        restSendData.setPayloadObject(convertData());//SLA 정보
        String url = "";
        /** Send Cache Data To Interface Server */
        url = env.getProperty("interface.server.domain") + InterfaceConstants.APPLICATION_THROTTLE2_DATA_URL + data.getApplicationNumber() +"/keys/" + data.getApplicationNumber() + "||" + (data.getApiGroupNumber() > 0 ? data.getApiGroupNumber() : data.getServiceNumber());
        LOGGER.debug("[makeCreateData] url : {} ", url);
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
        restSendData.setPayloadObject(convertData());//SLA 정보
        String url = "";
        /** Send Cache Data To Interface Server */
        url = env.getProperty("interface.server.domain") + InterfaceConstants.APPLICATION_THROTTLE2_DATA_URL + data.getApplicationNumber() +"/keys/" + data.getApplicationNumber() + "||" + (data.getApiGroupNumber() > 0 ? data.getApiGroupNumber() : data.getServiceNumber());
        LOGGER.debug("[makeModifyData] url : {} ", url);
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
    
    public DcApplicationSla getData() {
        return data;
    }

    public void setData(DcApplicationSla data) {
        this.data = data;
    }
    
    public Map<String,Object> convertData(){
        
        /** Set Base Send Parameter */
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        /*        
        Map<String, Object> throttleAppDataMap = new HashMap<String, Object>();
        throttleAppDataMap.put("key", data.getApplicationNumber()+"||"+(data.getApiGroupNumber() > 0 ? data.getApiGroupNumber() : data.getServiceNumber()));
        throttleAppDataMap.put("period", this.setPeriod(data.getCommerceServiceCriteriaCode()));
        throttleAppDataMap.put("maxCount", data.getCommerceServiceLimitQuantity());
        throttleAppDataMap.put("testPeriod", this.setPeriod(data.getTestServiceCriteriaCode()));
        throttleAppDataMap.put("testMaxCount", data.getTestServiceLimitQuantity());
        parameterMap.put("throttlingPolicyList", throttleAppDataMap);
        */

        parameterMap.put("key", data.getApplicationNumber()+"||"+(data.getApiGroupNumber() > 0 ? data.getApiGroupNumber() : data.getServiceNumber()));
        parameterMap.put("period", this.setPeriod(data.getCommerceServiceCriteriaCode()));
        parameterMap.put("maxCount", data.getCommerceServiceLimitQuantity());
        parameterMap.put("testPeriod", this.setPeriod(data.getTestServiceCriteriaCode()));
        parameterMap.put("testMaxCount", data.getTestServiceLimitQuantity());
        
        return parameterMap;
    }
    
    private String setPeriod(String criteriaCode) {
        String result = "";
        if(criteriaCode.lastIndexOf("_SVC_CRITERIA_01") != -1) {
            result = "SEC";
        }
        if(criteriaCode.lastIndexOf("_SVC_CRITERIA_02") != -1) {
            result = "MIN";
        }
        if(criteriaCode.lastIndexOf("_SVC_CRITERIA_03") != -1) {
            result = "HOUR";
        }
        if(criteriaCode.lastIndexOf("_SVC_CRITERIA_04") != -1) {
            result = "DAY";
        }
        if(criteriaCode.lastIndexOf("_SVC_CRITERIA_05") != -1) {
            result = "MONTH";
        }
        return result;
    }
    
}
