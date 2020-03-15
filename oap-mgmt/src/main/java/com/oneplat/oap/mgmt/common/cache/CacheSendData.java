package com.oneplat.oap.mgmt.common.cache;

import java.util.Map;


/**
 * Cache interfacing 가공 데이타 Interface
 *
 * @author mike 
 * @date 2015. 4. 9
 */
public interface CacheSendData {
    enum InitFild{apiGatewayConfig, authConfig, adaptorList, serviceList, apiList, appList, capacityThrottlingPolicyList, commonSlaPoliciesList, pathAuthorizationPolicyMap, appThrottlingPolicyList, abusingPolicyList, authKeyList, pathauthorizationList};
    public RestSendData makeCreateData() throws Exception;
    public RestSendData makeModifyData() throws Exception;
    public RestSendData makeModifyStatus() throws Exception;
    public RestSendData makeGetData() throws Exception;
    public RestSendData makeDeleteData() throws Exception;
    public Map<String, Object> convertData();
}   
