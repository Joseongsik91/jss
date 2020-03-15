package com.oneplat.oap.mgmt.api.support;

import com.oneplat.oap.core.exception.ServiceException;
import com.oneplat.oap.core.util.StringHelper;
import com.oneplat.oap.mgmt.api.model.*;
import com.oneplat.oap.mgmt.common.cache.CacheSendData;
import com.oneplat.oap.mgmt.common.cache.CommonCodeCache;
import com.oneplat.oap.mgmt.common.cache.RestSendData;
import com.oneplat.oap.mgmt.common.config.ApiConstants;
import com.oneplat.oap.mgmt.common.config.InterfaceConstants;
import com.oneplat.oap.mgmt.oapservice.mapper.ApiGroupMapper;
import com.oneplat.oap.mgmt.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * TODO API cache Interface data 가공
 *
 * @author mike 
 * @date 2015. 7. 21
 */
public class ApiCacheSendData implements CacheSendData {
    private ApiInfo data;
    private Environment env;
    private CommonCodeCache commonCodeCache;
    private ApiGroupMapper apiGroupMapper;


    public ApiCacheSendData(ApiInfo data, Environment env, CommonCodeCache commonCodeCache, ApiGroupMapper apiGroupMapper) {
        super();
        this.data = data;
        this.env = env;
        this.commonCodeCache = commonCodeCache;
        this.apiGroupMapper = apiGroupMapper;
    }

    @Override
    public RestSendData makeModifyData() throws Exception {
        /*
         * OSDF Interfacing
         */
        /** Set Base Send Parameter */
        RestSendData restSendData = new RestSendData();
        Map<String, String> urlParameterMap = new HashMap<String, String>();
        
        String url = env.getProperty("interface.server.domain") + InterfaceConstants.API_CACHE_DATA_URL+ "/" + data.getApiGeneralInfo().getApiNumber();
        /* restSendData set*/
        restSendData.setUrl(url);
        restSendData.setHttpMethod(HttpMethod.valueOf(InterfaceConstants.HTTP_METHOD_TYPE_PUT));
        restSendData.setMultipartYn("N");
        restSendData.setPayloadObject(convertData());//serviceName
        //restSendData.setUrlVariables(urlParameterMap);//serviceId
        
        return restSendData;
    }

    @Override
    public RestSendData makeGetData() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RestSendData makeDeleteData() throws Exception {
        // TODO Auto-generated method stub
     // TODO Auto-generated method stub
        RestSendData restSendData = new RestSendData();
        String url = env.getProperty("interface.server.domain") + InterfaceConstants.API_CACHE_DATA_URL+ "/" + data.getApiGeneralInfo().getApiNumber();
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
        //Map<String, String> urlParameterMap = new HashMap<String, String>();
 
        /* serviceName set*/
        String url = env.getProperty("interface.server.domain") + InterfaceConstants.API_CACHE_DATA_URL;
        /* restSendData set*/
        restSendData.setUrl(url);
        restSendData.setHttpMethod(HttpMethod.valueOf(InterfaceConstants.HTTP_METHOD_TYPE_POST));
        restSendData.setMultipartYn("N");
        restSendData.setPayloadObject(convertData());//serviceName
        //restSendData.setUrlVariables(urlParameterMap);//serviceId
        
        return restSendData;
    }
    
    @Override
    public RestSendData makeModifyStatus() throws Exception {
        // TODO Auto-generated method stub
        /*
         * OSDF Interfacing
         */
        if(data.getApiGeneralInfo() == null){
            throw new ServiceException(ApiConstants.DATA_INTERFACE_FAIL, "Api config is null");
        }
        ApiInfo apiForm = data;
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put("apiId", apiForm.getApiGeneralInfo().getApiNumber());
        parameterMap.put("apiStatus", commonCodeCache.getCodeValue(apiForm.getApiGeneralInfo().getApiStateCode()).toUpperCase());

        /** Set Base Send Parameter */
        RestSendData restSendData = new RestSendData();
        //Map<String, String> urlParameterMap = new HashMap<String, String>();

        /* serviceName set*/
        String url = env.getProperty("interface.server.domain") + InterfaceConstants.API_CACHE_DATA_URL+ "/" + data.getApiGeneralInfo().getApiNumber() + InterfaceConstants.API_STATUS_CACHE_DATA_URL;
        /* restSendData set*/
        restSendData.setUrl(url);
        restSendData.setHttpMethod(HttpMethod.valueOf(InterfaceConstants.HTTP_METHOD_TYPE_PUT));
        restSendData.setMultipartYn("N");
        restSendData.setPayloadObject(parameterMap);
        return restSendData;
    }
    
    public ApiInfo getData() {
        return data;
    }

    public void setData(ApiInfo data) {
        this.data = data;
    }
    
    private List<HashMap<String,Object>> makeQueryParamData(List<ApiParamInfo> dataList){
        List<HashMap<String,Object>> resultData = new ArrayList<HashMap<String,Object>>();
        if(dataList != null&&dataList.size()>0){
            for(ApiParamInfo parameter: dataList){
                HashMap<String,Object> map = new HashMap<String, Object>();
                map.put("name", parameter.getName());
                map.put("regExp", ".+");
                map.put("mandatory", StringHelper.convertToBooleanString(parameter.getMandatoryYn()));
                resultData.add(map);
            }
        }
        
        return resultData;
    }
    private List<HashMap<String,Object>> makePathParamData(List<ApiParamInfo> dataList){
        List<HashMap<String,Object>> resultData = new ArrayList<HashMap<String,Object>>();
        if(dataList != null&&dataList.size()>0){
            int idx = 1;
            for(ApiParamInfo parameter: dataList){
                HashMap<String,Object> map = new HashMap<String, Object>();
                map.put("index", idx);
                map.put("name", parameter.getName());
                map.put("isMandatory", StringHelper.convertToBooleanString(parameter.getMandatoryYn()));
                map.put("type", commonCodeCache.getCodeValue(parameter.getDataTypeCode()));
                //map.put("regExp", ".+");
                resultData.add(map);
                idx++;
            }
        }
        return resultData;
    }
    private List<HashMap<String,Object>> makeParameterData(List<ApiParamInfo> dataList){
        List<HashMap<String,Object>> resultData = new ArrayList<HashMap<String,Object>>();
        if(dataList != null&&dataList.size()>0){
            for(ApiParamInfo parameter: dataList){
                HashMap<String,Object> map = new HashMap<String, Object>();
                map.put("name", parameter.getName());
                map.put("isMandatory", StringHelper.convertToBooleanString(parameter.getMandatoryYn()));
                map.put("type", commonCodeCache.getCodeValue(parameter.getDataTypeCode()));
                resultData.add(map);
            }
        }
        return resultData;
    }
    private List<HashMap<String,Object>> makeSampleCodeData(List<ApiSampleCode> dataList){
        List<HashMap<String,Object>> resultData = new ArrayList<HashMap<String,Object>>();
        if(dataList != null&&dataList.size()>0){
            for(ApiSampleCode parameter: dataList){
                HashMap<String,Object> map = new HashMap<String, Object>();
                map.put("contentType", commonCodeCache.getCodeValue(parameter.getContentTypeCode()));
                map.put("responseContent", this.encodedSampleCode(parameter.getSampleCode()));
                //map.put("responseContent", StringUtil.encodeString(parameter.getSampleCode()));
                resultData.add(map);
            }
        }
        return resultData;
    }
    private String encodedSampleCode(String sampleCode) {
        String encodedSampleCode = "";
        if(!StringHelper.isEmpty(sampleCode)) {
            try {
                byte[] targetBytes = sampleCode.getBytes("UTF-8");
                Base64.Encoder encoder = Base64.getEncoder();
                encodedSampleCode = encoder.encodeToString(targetBytes);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return encodedSampleCode;
    }
    private List<String> makeContentTypeData(List<String> dataList){
        
        List<String> resultData = new ArrayList<String>();
        if(!StringHelper.isEmpty(dataList)){
            for(String contentType: dataList)
                resultData.add(commonCodeCache.getCodeValue(contentType));
        }else{
            resultData.add("application/json");
        }
        
        return resultData;
    }

    private String makeServiceId(ApiGeneralInfo apiGeneralInfo){
        /* API 속한 그룹(100151),API 속한 그룹의 상위 그룹(121023),서비스(10075) */
        String resultData = "";
        String separator  = ",";
        long serviceId = apiGroupMapper.selectGroupServiceId(apiGeneralInfo.getFirstApiGroupNumber());
        try{
            //2depth Group
            resultData = StringUtil.long2string(apiGeneralInfo.getSecondApiGroupNumber()) + separator + StringUtil.long2string(apiGeneralInfo.getFirstApiGroupNumber()) + separator + StringUtil.long2string(serviceId);
        }catch (NullPointerException e){
            //1depth Group
            resultData = StringUtil.long2string(apiGeneralInfo.getFirstApiGroupNumber()) + separator + StringUtil.long2string(serviceId);
        }
        return resultData;
    }

    public Map<String,Object> convertData(){
        Map<String, Object> parameterMap = new HashMap<String, Object>();

        ApiInfo apiForm = data;
        if(data.getApiGeneralInfo() == null)
            throw new ServiceException(ApiConstants.DATA_INTERFACE_FAIL, "Api config is null");
        else if(data.getApiRequestInfo() == null)
            throw new ServiceException(ApiConstants.DATA_INTERFACE_FAIL, "Api composition Error");

        ApiGeneralInfo apiGeneralInfo = data.getApiGeneralInfo();

        parameterMap.put("adaptorId", ""+apiGeneralInfo.getAdaptorNumber());
        parameterMap.put("apiHttpMethod", commonCodeCache.getCodeValue(apiGeneralInfo.getNbMethodCode()).toUpperCase());
        parameterMap.put("apiId", apiGeneralInfo.getApiNumber());
        parameterMap.put("apiStatus", commonCodeCache.getCodeValue(apiGeneralInfo.getApiStateCode()).toUpperCase());
        parameterMap.put("apiType", commonCodeCache.getCodeValue(apiGeneralInfo.getApiSectionCode()).toUpperCase());
        parameterMap.put("apiUri", apiGeneralInfo.getNbBaseUrl());
        parameterMap.put("assetHttpMethod", commonCodeCache.getCodeValue(apiGeneralInfo.getNbMethodCode()).toUpperCase());
        parameterMap.put("assetTestUri", apiGeneralInfo.getSbApiTestUrl());
        parameterMap.put("assetUri", apiGeneralInfo.getSbBaseUrl());
        parameterMap.put("enabled", StringHelper.convertToBooleanString(apiGeneralInfo.getApiUseYn()));
        parameterMap.put("serviceId", makeServiceId(apiGeneralInfo));
        parameterMap.put("site", apiGeneralInfo.getSiteCode());
        if(apiForm.getApiRequestInfo().getContentTypeList()!=null)parameterMap.put("requestContentTypeList",makeContentTypeData(apiForm.getApiRequestInfo().getContentTypeList()));
        if(apiForm.getApiResponseInfo().getContentTypeList()!=null)parameterMap.put("responseContentTypeList",makeContentTypeData(apiForm.getApiResponseInfo().getContentTypeList()));
        if(apiForm.getApiRequestInfo().getHeaders()!=null)parameterMap.put("apiHeaderList",makeParameterData(apiForm.getApiRequestInfo().getHeaders()));
        if(apiForm.getApiRequestInfo().getPathParameters()!=null)parameterMap.put("apiPathParamList",makePathParamData(apiForm.getApiRequestInfo().getPathParameters()));
        if(apiForm.getApiRequestInfo().getQueryStringParameters()!=null)parameterMap.put("apiQueryStringList",makeParameterData(apiForm.getApiRequestInfo().getQueryStringParameters()));
        if(apiForm.getApiResponseInfo().getSampleCodes()!=null)parameterMap.put("sampleResponseList",makeSampleCodeData(apiForm.getApiResponseInfo().getSampleCodes()));
//        parameterMap.put("multipart", false);
        return parameterMap;
    }
}
