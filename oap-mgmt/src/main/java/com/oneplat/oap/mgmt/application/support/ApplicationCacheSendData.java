package com.oneplat.oap.mgmt.application.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;

import com.oneplat.oap.core.util.StringHelper;
import com.oneplat.oap.mgmt.application.model.DcApplication;
import com.oneplat.oap.mgmt.application.model.DcApplicationService;
import com.oneplat.oap.mgmt.common.cache.CacheSendData;
import com.oneplat.oap.mgmt.common.cache.RestSendData;
import com.oneplat.oap.mgmt.common.config.InterfaceConstants;

/**
 * TODO 정책 cache Interface data 가공
 *
 * @author mike 
 * @date 2015. 7. 21
 */
public class ApplicationCacheSendData implements CacheSendData {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationCacheSendData.class);
    private DcApplication data;
    private Environment env;
    
    public ApplicationCacheSendData(DcApplication data, Environment env) {
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
        url = env.getProperty("interface.server.domain") + InterfaceConstants.APPLICATION_DATA_URL + data.getApplicationNumber();
        LOGGER.debug("[makeDeleteData] url : {} ", url);
        /* restSendData set*/
        restSendData.setUrl(url);
        restSendData.setHttpMethod(HttpMethod.valueOf(InterfaceConstants.HTTP_METHOD_TYPE_DELETE));
        restSendData.setMultipartYn("N");
        restSendData.setPayloadObject(new HashMap<String, Object>());
        return restSendData;
    }

    @Override
    public RestSendData makeCreateData() throws Exception {
        // TODO Auto-generated method stub
        /*
         * OSDF Interfacing
         */
        /** Set Base Send Parameter */
        RestSendData restSendData = new RestSendData();
        restSendData.setPayloadObject(convertData()); //Application 정보
        String url = "";
        /** Send Cache Data To Interface Server */
        url = env.getProperty("interface.server.domain") + InterfaceConstants.APPLICATION_DATA_URL;
        LOGGER.debug("[makeCreateData] url : {} ", url);
        /* restSendData set*/
        restSendData.setUrl(url);
        restSendData.setHttpMethod(HttpMethod.valueOf(InterfaceConstants.HTTP_METHOD_TYPE_POST));
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
        restSendData.setPayloadObject(convertData()); //Application 정보
        String url = "";
        /** Send Cache Data To Interface Server */
        url = env.getProperty("interface.server.domain") + InterfaceConstants.APPLICATION_DATA_URL + data.getApplicationNumber();
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
    
    public DcApplication getData() {
        return data;
    }

    public void setData(DcApplication data) {
        this.data = data;
    }

    public Map<String,Object> convertData() {
        
        /** Set Base Send Parameter */
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        
        parameterMap.put("appId", data.getApplicationNumber());
        parameterMap.put("appName", data.getApplicationName());
        parameterMap.put("enabled", StringHelper.convertToBooleanString(data.getApplicationUseYn())); 
        parameterMap.put("site", "DC_SITE_02".equals(data.getSiteCode()) ? "INSIDE" : "OUTSIDE"); 
        List<Map<String, Object>> appServiceMapList = new ArrayList<Map<String,Object>>();
        if(data.getApplicationServiceList() != null && data.getApplicationServiceList().size() > 0) {
            for(DcApplicationService service : data.getApplicationServiceList()){
                Map<String, Object> appServiceMap = new HashMap<String, Object>();
                appServiceMap.put("serviceId",          service.getServiceNumber());
                appServiceMap.put("serviceName",        service.getServiceName());
                appServiceMap.put("site",               "DC_SITE_02".equals(data.getSiteCode()) ? "MC_SITE_03" : "MC_SITE_02");
                appServiceMap.put("serviceType",        service.getServiceSectionCode());
                appServiceMap.put("enabled",            StringHelper.convertToBooleanString(service.getServiceUseYn()));
                appServiceMap.put("slaEnabled",         StringHelper.convertToBooleanString(service.getSlaUseYn()));
                appServiceMap.put("capacityEnabled",    StringHelper.convertToBooleanString(service.getCapacityUseYn()));
                appServiceMapList.add(appServiceMap);
            }
        }
        parameterMap.put("usingServiceList", appServiceMapList);
        return parameterMap;
    }
    
}
