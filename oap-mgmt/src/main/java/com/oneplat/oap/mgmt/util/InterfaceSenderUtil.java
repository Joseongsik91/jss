package com.oneplat.oap.mgmt.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oneplat.oap.mgmt.common.cache.RestSendData;
import com.oneplat.oap.mgmt.common.config.ApiConstants;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Gateway Rest 데이타 전달 모델
 *
 * @author mike 
 * @date 2015. 4. 9
 */

public class InterfaceSenderUtil {
	Logger log = LoggerFactory.getLogger(this.getClass());
	final private String BOUNDARY_DATA = "123123123123";
	
	public int sendData(RestSendData sendData, RestTemplate restTemplate) throws Exception{
	    // Create Request Headers
        
        restTemplate.setErrorHandler(new PassResponseErrorHandler());
        //ObjectMapper mapper = new ObjectMapper();
        //String requestJson = mapper.writeValueAsString(sendData.getPayloadObject());
        //log.debug("sendBody:{}", requestJson);
        // Create Body
        HttpEntity<Map<String,Object>> httpEntity = new HttpEntity<Map<String,Object>>( sendData.getPayloadObject(), getBaseHeader());
        //HttpEntity<Map<String,Object>> httpEntity = new HttpEntity<Map<String,Object>>( sendData.getPayloadObject());
        // Create a new RestTemplate instance

        // Add the Jackson and String message converters
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        //restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

     

        // Send Data
        log.debug("Request body{}", httpEntity.getBody());
        ResponseEntity<String> responseEntity = null;
        if(sendData.getUrlVariables() != null){
            responseEntity = restTemplate.exchange(sendData.getUrl(), sendData.getHttpMethod(), httpEntity, String.class, sendData.getUrlVariables());
        }else{
            responseEntity = restTemplate.exchange(sendData.getUrl(), sendData.getHttpMethod(), httpEntity, String.class);
        }
        
        log.debug("Response HttpStatus:{} body{}", responseEntity.getStatusCode(), responseEntity.getBody());
        if(responseEntity != null&&(responseEntity.getStatusCode().value() ==201||responseEntity.getStatusCode().value() ==200)){
            
        }
        else{
           
        }
        
        return responseEntity.getStatusCode().value();
	}

	
        public ResponseEntity<String> sendData2(RestSendData sendData, RestTemplate restTemplate) throws Exception {
            // Create Request Headers
                    
            restTemplate.setErrorHandler(new PassResponseErrorHandler());
            HttpEntity<Map<String,Object>> httpEntity = new HttpEntity<Map<String,Object>>( sendData.getPayloadObject(), getBaseHeader());

            // Add the Jackson and String message converters
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            // Send Data
            log.debug("Request body{}", httpEntity.getBody());
            
            ResponseEntity<String> responseEntity = null;
            if(sendData.getUrlVariables() != null) {
                    responseEntity = restTemplate.exchange(sendData.getUrl(), sendData.getHttpMethod(), httpEntity, String.class, sendData.getUrlVariables());
            } else {
                    responseEntity = restTemplate.exchange(sendData.getUrl(), sendData.getHttpMethod(), httpEntity, String.class);
            }
            
            log.debug("Response HttpStatus:{} body{}", responseEntity.getStatusCode(), responseEntity.getBody());
            
            return responseEntity;
        }


    public ResponseEntity<String> sendDataArray(RestSendData sendData, RestTemplate restTemplate) throws Exception {
        // Create Request Headers

        restTemplate.setErrorHandler(new PassResponseErrorHandler());
        HttpEntity<List<String>> httpEntity = new HttpEntity<List<String>>( sendData.getPayloadStringList(), getBaseHeader());

        // Add the Jackson and String message converters
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        // Send Data
        log.debug("Request body{}", httpEntity.getBody());

        ResponseEntity<String> responseEntity = null;
        if(sendData.getUrlVariables() != null) {
            responseEntity = restTemplate.exchange(sendData.getUrl(), sendData.getHttpMethod(), httpEntity, String.class, sendData.getUrlVariables());
        } else {
            responseEntity = restTemplate.exchange(sendData.getUrl(), sendData.getHttpMethod(), httpEntity, String.class);
        }

        log.debug("Response HttpStatus:{} body{}", responseEntity.getStatusCode(), responseEntity.getBody());

        return responseEntity;
    }
	
	public ResponseEntity<String> getData(RestSendData restSendData, RestTemplate restTemplate) throws Exception {
        // Create Request Headers
        
        restTemplate.setErrorHandler(new PassResponseErrorHandler());

        // Create Body
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(null, getBaseHeader());
        //HttpEntity<Object> httpEntity = new HttpEntity<Object>(null);
        // Send Data
        ResponseEntity<String> responseEntity = null;
        if(restSendData.getUrlVariables() != null){
            responseEntity = restTemplate.exchange(restSendData.getUrl(), restSendData.getHttpMethod(), httpEntity, String.class, restSendData.getUrlVariables());
        }else{
            responseEntity = restTemplate.exchange(restSendData.getUrl(), restSendData.getHttpMethod(), httpEntity, String.class);
        }
        
        return responseEntity;
    }


	public int sendDataMultipart(RestSendData restSendData, RestTemplate restTemplate) {
        int statusCode = 0;
        ObjectMapper mapper =  new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //RequestConfig.Builder requestBuilder = RequestConfig.custom();
        HttpClientBuilder builder = HttpClientBuilder.create();
        //builder.setDefaultRequestConfig(requestBuilder.build());
        HttpClient client = builder.build();
        
        HttpPost httpost;
        try {
            
            
            httpost = new HttpPost(new URI(restSendData.getUrl()));
            
            httpost.setHeaders(getMultipartHeader(restSendData));
            
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            Map<String, Object> param = restSendData.getPayloadObject();
            if(param != null){
                for( String key : param.keySet() ){
                    String requestJson = mapper.writeValueAsString(param.get(key));
                    entityBuilder.addPart(key, new StringBody(requestJson, ContentType.APPLICATION_JSON));
                    //entityBuilder.addPart(key, new StringBody("test", ContentType.APPLICATION_JSON));
                    entityBuilder.setBoundary(BOUNDARY_DATA);
                }
            }
            
            
            Map<String, Object> multiPartFilesMap = restSendData.getMultipartFileInfo();
            if(multiPartFilesMap != null){
                Iterator<String> multipartFileNames = restSendData.getMultipartFileInfo().keySet().iterator();
               
                while(multipartFileNames.hasNext()){
                    entityBuilder.setBoundary(BOUNDARY_DATA);
                    String multipartFileName = multipartFileNames.next();
                    //ByteArrayBody body = new ByteArrayBody(StringUtils.getBytes((String)multiPartFilesMap.get(multipartFileName)), multipartFileName);
                    ByteArrayBody body = new ByteArrayBody(mapper.writeValueAsBytes(multiPartFilesMap.get(multipartFileName)), multipartFileName);
                    entityBuilder.addPart(multipartFileName, body);
                }
            }
            httpost.setEntity(entityBuilder.build());
            
            HttpResponse response = client.execute(httpost);
            
            log.error("Response HttpStatus:{} body{}", response.getStatusLine().getStatusCode(), IOUtils.toString(response.getEntity().getContent()));
            
            
            statusCode = response.getStatusLine().getStatusCode();   
            
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            httpost.getEntity().writeTo(bos);
            System.out.println(new String(bos.toByteArray()));
            
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
      
        return statusCode;


    }
	
	/**
     * Base Header get
     * @param
     */
	private HttpHeaders getBaseHeader(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ArrayList<MediaType> accepts = new ArrayList<MediaType>();
        accepts.add(MediaType.APPLICATION_JSON);
        headers.setAccept(accepts);
//        HttpHeaders headers = new HttpHeaders();
//        ArrayList<Charset> acceptCharsets = new ArrayList<Charset>();
//        MediaType mediaType = new MediaType("application/json");
//        headers.setContentType(mediaType);
        
        return headers;
    }
    
    /**
     * Base Header get
     * @param restSendData
     */
	private Header[] getMultipartHeader(RestSendData restSendData){
        Header[] headers = {
                new BasicHeader(org.apache.http.HttpHeaders.CONTENT_TYPE, "multipart/mixed;charset=utf-8;boundary="+BOUNDARY_DATA)
            };
        
        return headers;
    }
	
	public static int getIntervalValue(String policyBaseCode){
        int intervalValue = 0;
        switch(policyBaseCode){
            case ApiConstants.MINUTE_TYPE_CODE:
                intervalValue = 60;
                break;
            case ApiConstants.HOUR_TYPE_CODE:
                intervalValue = 60*60;
                break;
            case ApiConstants.DAY_TYPE_CODE:
                intervalValue = 24*60*60;
                break;
            case ApiConstants.MONTH_TYPE_CODE:
                intervalValue = 31*24*60*60;
                break;
            case ApiConstants.SECOND_TYPE_CODE:
                intervalValue = 1;
                break;
        }
        return intervalValue;
        
    }

    public static String getIntervalStringValue(String policyBaseCode){
        String intervalValue = "";
        switch(policyBaseCode){
            case ApiConstants.MINUTE_TYPE_CODE:
                intervalValue = "MIN";
                break;
            case ApiConstants.HOUR_TYPE_CODE:
                intervalValue = "HOUR";
                break;
            case ApiConstants.DAY_TYPE_CODE:
                intervalValue = "DAY";
                break;
            case ApiConstants.MONTH_TYPE_CODE:
                intervalValue = "MON";
                break;
            case ApiConstants.SECOND_TYPE_CODE:
                intervalValue = "SEC";
                break;
        }
        return intervalValue;

    }
}
