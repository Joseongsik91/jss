package com.oneplat.oap.mgmt.api.service.impl;

import com.amazonaws.util.DateUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oneplat.oap.mgmt.api.mapper.ConsoleMapper;
import com.oneplat.oap.mgmt.api.model.*;
import com.oneplat.oap.mgmt.api.service.ConsoleService;
import com.oneplat.oap.mgmt.common.model.ConsoleConstant;
import com.oneplat.oap.mgmt.util.*;
import com.oneplat.oap.mgmt.util.auth.AesModule;
import com.oneplat.oap.mgmt.util.auth.DesModule;
import com.oneplat.oap.mgmt.util.auth.HmacShaModule;
import com.oneplat.oap.mgmt.util.auth.RsaModule;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ConsoleServiceImpl
 * <p>
 * Created by Hong Gi Seok 2016-12-08
 */
@Service
public class ConsoleServiceImpl implements ConsoleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleServiceImpl.class);

    @Autowired
    private ConsoleMapper consoleMapper;

    protected static final DateUtils dateUtils = new DateUtils();

    public ApiInfo selectApiInfo(Long apiNumber) {

        //API 구성 리스트
        List<ApiComponent> apiComponentList = consoleMapper.selectApiComponent(apiNumber);

        //API 구성 정보
        ApiInfo apiInfo = new ApiInfo();

        for (ApiComponent apiComponent : apiComponentList) {
            if (apiComponent.getApiComponentCode().equals("MC_API_COMP_01")) { //General
                apiInfo.setApiGeneralInfo(stringConvertApiGeneralInfo(apiComponent.getApiComponentData()));
            } else if (apiComponent.getApiComponentCode().equals("MC_API_COMP_02")) { //Request
                apiInfo.setApiRequestInfo(stringConvertApiRequestInfo(apiComponent.getApiComponentData()));
            } else if (apiComponent.getApiComponentCode().equals("MC_API_COMP_03")) { //Response
                apiInfo.setApiResponseInfo(stringConvertApiResponseInfo(apiComponent.getApiComponentData()));
            }
        }

        return apiInfo;
    }

    @Override
    public Map<String, Object> consoleTest(ApiConsoleRequest apiConsoleRequest, List<MultipartFile> files, RestMultipart restMultipart, HttpServletRequest request) {

        Map<String, Object> result = new HashMap<String, Object>();

        Map<String, HttpMethod> methodCodeValue = new HashMap<String, HttpMethod>();
        methodCodeValue.put("MC_HTTP_METHOD_01", HttpMethod.GET);
        methodCodeValue.put("MC_HTTP_METHOD_02", HttpMethod.PUT);
        methodCodeValue.put("MC_HTTP_METHOD_03", HttpMethod.POST);
        methodCodeValue.put("MC_HTTP_METHOD_04", HttpMethod.DELETE);

        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> queryString = new HashMap<String, String>();
        Map<String, String> pathParam = new HashMap<String, String>();
        Map<String, String> formParam = new HashMap<String, String>();
        Map<String, String> urlVariables = new HashMap<String, String>();
        StringBuffer requestData = new StringBuffer();

        try {
            //timestamp
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            //Header Setting
            headers.put(ConsoleConstant.DATE, dateUtils.formatRfc822Date(new Date()));
            headers.put(ConsoleConstant.TIMESTAMP, String.valueOf(timestamp.getTime()));
            headers.put(ConsoleConstant.ACCEPT, consoleMapper.getCosoleCodeName(apiConsoleRequest.getAcceptType().getValue()));

            String headerString = consoleMapper.getCosoleCodeName(apiConsoleRequest.getContentType().getValue());
            if ("MC_CONTENT_TYPE_05".equals(apiConsoleRequest.getContentType().getValue())) {
                headerString = request.getContentType();
            } else {
                headerString += ";" + ConsoleConstant.CHARSET_UTF8;
            }
            headers.put(ConsoleConstant.CONTENT_TYPE, headerString);

            for (ApiParamInfo apiParamInfo : apiConsoleRequest.getHeaders()) {
                headers.put(apiParamInfo.getName(), apiParamInfo.getValue());
            }

            //PathParam Setting
            for (ApiParamInfo apiParamInfo : apiConsoleRequest.getPathParameters()) {
                pathParam.put(apiParamInfo.getName(), apiParamInfo.getValue());
            }
            //QueryString Setting
            for (ApiParamInfo apiParamInfo : apiConsoleRequest.getQueryStringParameters()) {
                queryString.put(apiParamInfo.getName(), apiParamInfo.getValue());
            }
            //Payload Data
            String payload = apiConsoleRequest.getSampleCode().getSampleCode();

            //메세지 인증
            String msgEncryptionType = apiConsoleRequest.getApplicationConsole().getMsgEncryptionType();
            if (msgEncryptionType != null && !msgEncryptionType.equals("")) {
                //암호화 payload 전달
                if (apiConsoleRequest.getPayloadYn().equals("Y")){
                    payload = msgPayloadAuthorize(apiConsoleRequest.getSampleCode().getSampleCode(), msgEncryptionType);
                }
                //QueryString 암호화
                if(queryString.size() != 0) msgAuthorize(queryString, msgEncryptionType);
            }

            //Request Console Test URI
            String url = String.valueOf(this.getRequestUrlLog(apiConsoleRequest.getApiGeneralInfo().getNbBaseUrl(), pathParam, queryString, "UTF-8"));

            //Request Console Log ================================
            requestData.append(this.getRequestHeaderLog(headers));

            //HMAC 인증
            String hmacAuthTypeCode = apiConsoleRequest.getApplicationConsole().getHmacAuthTypeCode();
            if (hmacAuthTypeCode != null && !hmacAuthTypeCode.equals("")) {
                String hmacAuth = hmacAuthorize(apiConsoleRequest.getApplicationConsole().getApplicationKey(), url, timestamp, hmacAuthTypeCode);
                headers.put("hmacAuth", hmacAuth);
                requestData.append("hmacAuth: "+hmacAuth).append(ConsoleConstant.NEW_LINE);
            }

            //Request Console Log ================================
            requestData.append(ConsoleConstant.NEW_LINE)
                    .append(consoleMapper.getCosoleCodeName(apiConsoleRequest.getApiGeneralInfo().getNbMethodCode()) + " Method " + this.getRequestUrlLog(url, pathParam, queryString, "UTF-8"))
                    .append(ConsoleConstant.NEW_LINE)
                    .append(payload);

            //Request Result 저장
            LOGGER.debug(requestData.toString());

            result.put("requestResult", requestData.toString());

            //HTTP Header 세팅
            HttpHeaders requestHeader = new HttpHeaders();
            for (Map.Entry<String, String> header : headers.entrySet()) {
                requestHeader.set(header.getKey(), header.getValue());
            }

            TrustedRestTemplate rc = new TrustedRestTemplate();
            RestResponseErrorHandler errorHandler = new RestResponseErrorHandler();
            rc.setErrorHandler(errorHandler);

            if ("Y".equals(apiConsoleRequest.getMultipartYn())) {

                HttpResponse response = restMultipart.sendMultipart(
                        ConsoleUtil.getReplacedMultipartUrl(url, queryString, pathParam, HTTP.UTF_8),
                        headers, formParam, files, request);

                StringBuffer fileResponse = new StringBuffer();
                fileResponse.append(this.getMultipartLog(response)).append(ConsoleConstant.NEW_LINE)
                        .append(ConsoleUtil.getEntity(response, "multipart/form-data"));
                LOGGER.debug(fileResponse.toString());
                result.put("responseResult", fileResponse.toString());

            } else {
                if (ConsoleConstant.FORM_PARAM.equals(apiConsoleRequest.getSampleCode().getContentTypeCode())) {
                    //FormParam 일때
                    MultiValueMap<String, Object> multiParam = new LinkedMultiValueMap<String, Object>();
                    for (Map.Entry<String, String> entry : formParam.entrySet()) {
                        multiParam.set(entry.getKey(), entry.getValue());
                    }

                    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(multiParam, requestHeader);
                    ResponseEntity<String> response = rc.exchangeForConsole(new URI(url), methodCodeValue.get(apiConsoleRequest.getApiGeneralInfo().getNbMethodCode()), requestEntity, String.class, urlVariables);
                    //Response Result 저장
                    LOGGER.debug(this.getRestTemplateResponseHeaderLog(response, apiConsoleRequest.getAcceptType().getValue(), errorHandler, request));
                    result.put("responseResult", this.getRestTemplateResponseHeaderLog(response, apiConsoleRequest.getAcceptType().getValue(), errorHandler, request));
                } else {
                    //FormParam 아닐때
                    HttpEntity<String> requestEntity = new HttpEntity<String>(payload, requestHeader);
                    ResponseEntity<String> response = rc.exchangeForConsole(new URI(url), methodCodeValue.get(apiConsoleRequest.getApiGeneralInfo().getNbMethodCode()), requestEntity, String.class, urlVariables);
                    //Response Result 저장
                    LOGGER.debug(this.getRestTemplateResponseHeaderLog(response, apiConsoleRequest.getAcceptType().getValue(), errorHandler, request));
                    result.put("responseResult", this.getRestTemplateResponseHeaderLog(response, apiConsoleRequest.getAcceptType().getValue(), errorHandler, request));
                }

            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ConsoleException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Map<String, Object> selectApiServiceList() {

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", consoleMapper.selectApiServiceList());

        return result;
    }

    @Override
    public Map<String, Object> selectApiServiceApiList(long serviceNumber) {

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", consoleMapper.selectApiList(serviceNumber));

        return result;
    }

    @Override
    public Map<String, Object> selectApplicationConsole() {

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", consoleMapper.selectApplicationConsole());

        return result;
    }

    //console test code =====================================================================================================================
//            HttpHeaders reqHeader = new HttpHeaders();
//            reqHeader.set("TDCProjectKey","de671511-7c84-49c6-8259-031197ca988b");
//            reqHeader.set("Accept","application/json");
//            reqHeader.set(ConsoleConstant.DATE, dateUtils.formatRfc822Date(date));
//
//            String urlTest = "https://apis.sktelecom.com/v1/weather/status?latitude=1&longitude=1";
//            TrustedRestTemplate rc = new TrustedRestTemplate();
//
//            RestResponseErrorHandler errorHandler = new RestResponseErrorHandler();
//            rc.setErrorHandler(errorHandler);
//
//            MultiValueMap<String, Object> multiParam = new LinkedMultiValueMap<String, Object>();
//    //        multiParam.set("latitude", "1");
//    //        multiParam.set("longitude", "2");
//            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(multiParam, reqHeader);
//
//            ResponseEntity<String> response = rc.exchangeForConsole(new URI(urlTest), HttpMethod.GET, requestEntity, null, urlVariables);
//
//            //console test code =====================================================================================================================
//
//            //request 결과
//            apiConsoleResult.setRequestData("");
//            //response 결과
//            apiConsoleResult.setResponseDate(this.getRestTemplateResponseHeaderLog(response, "JSON", errorHandler, request));

    /**
     * get multipart log
     *
     * @param response
     * @return
     */
    private String getMultipartLog(HttpResponse response) {
        StringBuffer resultData = new StringBuffer();
        Header[] resHeaders = response.getAllHeaders();
        for (Header resHeader : resHeaders) {
            resultData.append(resHeader.getName()).append(ConsoleConstant.COLON).append(resHeader.getValue())
                    .append(ConsoleConstant.NEW_LINE);
        }
        resultData.append("Status code: ").append(this.getMultipartResponseStatusCode(response)).append(ConsoleConstant.NEW_LINE);

        return resultData.toString();
    }

    private String getMultipartResponseStatusCode(HttpResponse multipartResponse) {
        String statusCode = "";
        if (multipartResponse != null && multipartResponse.getStatusLine() != null) {
            statusCode = multipartResponse.getStatusLine().getStatusCode() + "";
        }
        return statusCode;
    }


    private String getRestTemplateResponseHeaderLog(ResponseEntity<String> response, String type, RestResponseErrorHandler errorHandler, HttpServletRequest request) throws ConsoleException {
        String acceptType = "";
        if ("MC_ACCEPT_TYPE_01".equals(type)) {
            acceptType = "JSON";
        } else if ("MC_ACCEPT_TYPE_02".equals(type)) {
            acceptType = "XML";
        } else if ("MC_ACCEPT_TYPE_03".equals(type)) {
            acceptType = "YAML";
        } else if ("MC_ACCEPT_TYPE_04".equals(type)) {
            acceptType = "HTML";
        }

        StringBuffer resultData = new StringBuffer();
        HttpHeaders resHeader = response.getHeaders();
        for (Map.Entry<String, List<String>> entry : resHeader.entrySet()) {
            resultData.append(entry.getKey()).append(ConsoleConstant.COLON);
            for (String value : entry.getValue()) {
                resultData.append(value).append(ConsoleConstant.NEW_LINE);
            }
        }
        if (errorHandler.isRespnoseError()) {
            resultData.append(ConsoleUtil.getErrorHandlerLog(errorHandler, request)).append(ConsoleConstant.NEW_LINE);
        } else {
            //body가 null인경우 때문에 status code를 찍어줌.
            LOGGER.debug("Protocol : " + request.getProtocol());
            resultData.append("Status code: ").append(errorHandler.getResponseMessage()).append(ConsoleConstant.NEW_LINE);
        }
        resultData.append(ConsoleConstant.NEW_LINE).append(ConsoleUtil.getFormattedString(StringUtil.null2string(response.getBody(), ""), acceptType));
        return resultData.toString();
    }


    /**
     * get request header log
     *
     * @param headers
     * @return
     */
    private String getRequestHeaderLog(Map<String, String> headers) {
        StringBuffer requestHeader = new StringBuffer();
        // request log append(for header)
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            String value = entry.getValue();
            if (ConsoleConstant.APPKEY.equals(entry.getKey())) {
                value = "**************************"; //appKey 미노출
            }
            requestHeader.append(entry.getKey()).append(ConsoleConstant.COLON).append(value)
                    .append(ConsoleConstant.NEW_LINE);
        }
        return requestHeader.toString();
    }

    private URI getRequestUrlLog(String url, Map<String, String> pathParam, Map<String, String> queryString, String encodingType) throws URISyntaxException {
        StringBuffer requestUrl = new StringBuffer();

        String[] urls = StringUtils.split(url, "?");
        String convertUrl = (urls != null ? urls[0] : "");

        try {
            for (Map.Entry<String, String> entry : pathParam.entrySet()) {
                String value = entry.getValue();
                if (!"".equals(encodingType)) {
                    value = URLEncoder.encode(value, encodingType);
                }
                convertUrl = StringUtils.replace(convertUrl, "{" + entry.getKey() + "}", value);
            }

            StringBuffer query = new StringBuffer();
            for (Map.Entry<String, String> entry : queryString.entrySet()) {
                String value = entry.getValue();
                if (!"".equals(encodingType)) {
                    value = URLEncoder.encode(value, encodingType);
                }
                if (query.length() > 0) {
                    query.append('&');
                }
                query.append(entry.getKey()).append('=').append(value);
            }
            if (query.length() > 0) {
                convertUrl += '?' + query.toString();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        URI returnUrl = new URI(convertUrl);

        return returnUrl;
    }

    //General Object Convert
    public ApiGeneralInfo stringConvertApiGeneralInfo(String data) {
        ApiGeneralInfo apiGeneralInfo = new ApiGeneralInfo();

        ObjectMapper mapper = new ObjectMapper();
        try {
            apiGeneralInfo = mapper.readValue(data, ApiGeneralInfo.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return apiGeneralInfo;
    }

    //Request Object Convert
    public ApiRequestInfo stringConvertApiRequestInfo(String data) {
        ApiRequestInfo apiRequestInfo = new ApiRequestInfo();

        ObjectMapper mapper = new ObjectMapper();
        try {
            apiRequestInfo = mapper.readValue(data, ApiRequestInfo.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return apiRequestInfo;
    }

    //Response Object Convert
    public ApiResponseInfo stringConvertApiResponseInfo(String data) {
        ApiResponseInfo apiResponseInfo = new ApiResponseInfo();

        ObjectMapper mapper = new ObjectMapper();
        try {
            apiResponseInfo = mapper.readValue(data, ApiResponseInfo.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return apiResponseInfo;
    }

    public String getQueryString(Map<String, String> queryString) {
        StringBuffer query = new StringBuffer();
        try {
            for (Map.Entry<String, String> entry : queryString.entrySet()) {
                String value = entry.getValue();
                value = URLEncoder.encode(value, "UTF-8");
                if (query.length() > 0) {
                    query.append('&');
                }
                query.append(entry.getKey()).append('=').append(value);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return query.toString();
    }

    public String hmacAuthorize(String appKey, String url, Timestamp timestamp, String type) throws URISyntaxException {
        //결과저장
        String result = null;
        //Hmac 모듈
        HmacShaModule hmacShaModule = new HmacShaModule();

        URI uri = new URI(url);

        StringBuffer sb = new StringBuffer();
        if (type.equals(ConsoleConstant.ADD_AUTH_SHA1)) {
            result = hmacShaModule.encryption(appKey, uri.getPath(), timestamp.getTime(), "HmacSHA1");
        } else if (type.equals(ConsoleConstant.ADD_AUTH_SHA256)) {
            result = hmacShaModule.encryption(appKey, uri.getPath(), timestamp.getTime(), "HmacSHA256");
        } else if (type.equals(ConsoleConstant.ADD_AUTH_SHA224)) {
            result = hmacShaModule.encryption(appKey, uri.getPath(), timestamp.getTime(), "HmacSHA224");
        } else if (type.equals(ConsoleConstant.ADD_AUTH_SHA384)) {
            result = hmacShaModule.encryption(appKey, uri.getPath(), timestamp.getTime(), "HmacSHA384");
        } else if (type.equals(ConsoleConstant.ADD_AUTH_SHA512)) {
            result = hmacShaModule.encryption(appKey, uri.getPath(), timestamp.getTime(), "HmacSHA512");
        }
        return result;
    }

    public void msgAuthorize(Map<String, String> data, String type) {
        if (type.equals(ConsoleConstant.MSG_AUTH_DES)) {
            DesModule desModule = new DesModule("des-key@#$@#", "DES"); // Des
            for (Map.Entry<String, String> entry : data.entrySet()) {
                entry.setValue(desModule.encryption(entry.getValue()));
            }
        }else if (type.equals(ConsoleConstant.MSG_AUTH_DES_TRIPLE)) {
            DesModule desModule = new DesModule("destriple-key@#$@#*^&$%^","DESede"); // Des Triple
            for (Map.Entry<String, String> entry : data.entrySet()) {
                entry.setValue(desModule.encryption(entry.getValue()));
            }
        }else if(type.equals(ConsoleConstant.MSG_AUTH_AES)){
            AesModule aesModule = new AesModule("aes-key@#$@#$@#$"); // Aes
            for (Map.Entry<String, String> entry : data.entrySet()) {
                entry.setValue(aesModule.encryption(entry.getValue()));
            }
        }else if(type.equals(ConsoleConstant.MSG_AUTH_RSA)){
            RsaModule rsaModule = new RsaModule();
            for (Map.Entry<String, String> entry : data.entrySet()) {
                entry.setValue(rsaModule.encryption(entry.getValue()));
            }
        }
    }

    public String msgPayloadAuthorize(String payload, String type) {
        //결과저장
        String result = null;
        if (type.equals(ConsoleConstant.MSG_AUTH_DES)) {
            DesModule desModule = new DesModule("des-key@#$@#", "DES"); // Des
            result = desModule.encryption(payload);
        }else if (type.equals(ConsoleConstant.MSG_AUTH_DES_TRIPLE)) {
            DesModule desModule = new DesModule("destriple-key@#$@#*^&$%^", "DESede"); // Des Triple
            result = desModule.encryption(payload);
        }else if (type.equals(ConsoleConstant.MSG_AUTH_AES)) {
            AesModule aesModule = new AesModule("aes-key@#$@#$@#$"); // Aes
            result = aesModule.encryption(payload);
        }else if(type.equals(ConsoleConstant.MSG_AUTH_RSA)) {
            RsaModule rsaModule = new RsaModule();
            result = rsaModule.encryption(payload);
        }
        return result;
    }
}
