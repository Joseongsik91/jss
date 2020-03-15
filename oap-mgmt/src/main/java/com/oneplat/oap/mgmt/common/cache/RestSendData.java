package com.oneplat.oap.mgmt.common.cache;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
/**
 * Gateway Rest 데이타 전달 모델
 *
 * @author mike 
 * @date 2015. 4. 9
 */
public class RestSendData {
	private String url;
	private HttpMethod httpMethod;
	private String multipartYn;
	private HttpHeaders reqHeader;
	private Map<String, Object> payloadObject;
	private List<String> payloadStringList;
	private Map<String, Object> multipartFileInfo;
	private Map<String, String> urlVariables;
	private String junitStatusCode;

	public List<String> getPayloadStringList() {
		return payloadStringList;
	}

	public void setPayloadStringList(List<String> payloadStringList) {
		this.payloadStringList = payloadStringList;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the httpMethod
	 */
	public HttpMethod getHttpMethod() {
		return httpMethod;
	}
	/**
	 * @param httpMethod the httpMethod to set
	 */
	public void setHttpMethod(HttpMethod httpMethod) {
		this.httpMethod = httpMethod;
	}
	/**
	 * @return the multipartYn
	 */
	public String getMultipartYn() {
		return multipartYn;
	}
	/**
	 * @param multipartYn the multipartYn to set
	 */
	public void setMultipartYn(String multipartYn) {
		this.multipartYn = multipartYn;
	}
	/**
	 * @return the reqHeader
	 */
	public HttpHeaders getReqHeader() {
		return reqHeader;
	}
	/**
	 * @param reqHeader the reqHeader to set
	 */
	public void setReqHeader(HttpHeaders reqHeader) {
		this.reqHeader = reqHeader;
	}
	/**
	 * @return the payloadObject
	 */
	public Map<String, Object> getPayloadObject() {
		return payloadObject;
	}
	/**
	 * @param payloadObject the payloadObject to set
	 */
	public void setPayloadObject(Map<String, Object> payloadObject) {
		this.payloadObject = payloadObject;
	}
	/**
	 * @return the multipartFileInfo
	 */
	public Map<String, Object> getMultipartFileInfo() {
		return multipartFileInfo;
	}
	/**
	 * @param multipartFileInfo the multipartFileInfo to set
	 */
	public void setMultipartFileInfo(Map<String, Object> multipartFileInfo) {
		this.multipartFileInfo = multipartFileInfo;
	}
	/**
	 * @return the urlVariables
	 */
	public Map<String, String> getUrlVariables() {
		return urlVariables;
	}
	/**
	 * @param urlVariables the urlVariables to set
	 */
	public void setUrlVariables(Map<String, String> urlVariables) {
		this.urlVariables = urlVariables;
	}
	/**
	 * @return the junitStatusCode
	 */
	public String getJunitStatusCode() {
		return junitStatusCode;
	}
	/**
	 * @param junitStatusCode the junitStatusCode to set
	 */
	public void setJunitStatusCode(String junitStatusCode) {
		this.junitStatusCode = junitStatusCode;
	}

}
