package com.oneplat.oap.mgmt.application.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;

import com.oneplat.oap.mgmt.application.model.DcApplicationKey;
import com.oneplat.oap.mgmt.common.cache.CacheSendData;
import com.oneplat.oap.mgmt.common.cache.RestSendData;
import com.oneplat.oap.mgmt.common.config.InterfaceConstants;
import com.oneplat.oap.mgmt.util.StringUtil;

/**
 * TODO 정책 cache Interface data 가공
 *
 * @author mike 
 * @date 2015. 7. 21
 */
public class ApplicationAuthKeyCacheSendData implements CacheSendData {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationAuthKeyCacheSendData.class);
    
    private DcApplicationKey data;
    
    private Environment env;
    
    public ApplicationAuthKeyCacheSendData(DcApplicationKey data, Environment env) {
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
        url = env.getProperty("interface.server.domain") + InterfaceConstants.APPLICATION_AUTHKEY_DATA_URL + data.getApplicationNumber() + "/appKeys/" + data.getKeyTypeCode() + "_" + data.getApplicationKeySequence();
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
        restSendData.setPayloadObject(convertData());//applicationKey 정보
        String url = "";
        /** Send Cache Data To Interface Server */
        url = env.getProperty("interface.server.domain") + InterfaceConstants.APPLICATION_AUTHKEY_DATA_URL + data.getApplicationNumber() + "/appKeys/" + data.getKeyTypeCode() + "_" + data.getApplicationKeySequence();
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
        restSendData.setPayloadObject(convertData());//applicationKey 정보
        /** Send Cache Data To Interface Server */
        String url = "";
        url = env.getProperty("interface.server.domain") + InterfaceConstants.APPLICATION_AUTHKEY_DATA_URL + data.getApplicationNumber() + "/appKeys/" + data.getKeyTypeCode() + "_" + data.getApplicationKeySequence();
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
    
    public DcApplicationKey getData() {
        return data;
    }

    public void setData(DcApplicationKey data) {
        this.data = data;
    }
    
    public Map<String,Object> convertData() {
        
        /** Set Base Send Parameter */
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        
        parameterMap.put("appId", data.getApplicationNumber());
        parameterMap.put("appKey", data.getApplicationKey());
        parameterMap.put("appKeyId", data.getKeyTypeCode() + "_" + data.getApplicationKeySequence());
        parameterMap.put("appKeyType", data.getKeyTypeCode());
        
        parameterMap.put("expirationDate", data.getKeyEndDatetime());
        parameterMap.put("serviceGrade", data.getServiceGradeCode());
        
        Map<String, Object> keyTypeAttributeMap = new HashMap<String, Object>();

        if("DC_KEY_TYPE_01".equals(data.getKeyTypeCode())) { //Server Key
            /*
             * IP허용에 대해서 정책을 사용하지 않는 경우 *로 연동 할수 있도록 LimitYn을 체크 하지 않도록 변경
             */
            //if("Y".equals(data.getTypeLimitYn())) {
                List<String> ipList = new ArrayList<String>();
                String[] valueList = StringUtil.split(data.getKeyTypeAttributeValue(), "\r\n");
                for(String value : valueList) {
                    ipList.add(value);
                }
                keyTypeAttributeMap.put("authIpRuleList", ipList);
            //}
        }else if("DC_KEY_TYPE_02".equals(data.getKeyTypeCode())) { //Browser Key
            if("Y".equals(data.getTypeLimitYn())) {
                List<String> referrerList = new ArrayList<String>();
                String[] valueList = StringUtil.split(data.getKeyTypeAttributeValue(), "\r\n");
                for(String value : valueList) {
                    referrerList.add(value);
                }
                keyTypeAttributeMap.put("authReferrerRuleList", referrerList);
            }
        }else if("DC_KEY_TYPE_03".equals(data.getKeyTypeCode())) { //Android Key
            keyTypeAttributeMap.put("packageName", data.getKeyTypeAttributeValue());
        }else if("DC_KEY_TYPE_04".equals(data.getKeyTypeCode())) { //iOS Key
            keyTypeAttributeMap.put("bundleId", data.getKeyTypeAttributeValue());
        }

        keyTypeAttributeMap.put("hmacAuthTypeCode", data.getHmacAuthTypeCode());
        keyTypeAttributeMap.put("msgEncryptionKey", data.getMsgEncryptionKey());
        keyTypeAttributeMap.put("msgEncryptionTypeCode", data.getMsgEncryptionTypeCode());

        parameterMap.put("authKeyRules", keyTypeAttributeMap);
        return parameterMap;
    }
    
    
}
