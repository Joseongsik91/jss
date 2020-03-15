package com.oneplat.oap.mgmt.api.model;

import lombok.Data;

import java.util.List;

/**
 * Api
 * <p>
 * Created by Hong Gi Seok on 2016-12-15.
 */
@Data
public class ApiConsoleRequest {
    private ApiGeneralInfo apiGeneralInfo;
    ApiParamInfo acceptType;
    ApiParamInfo contentType;
    private List<ApiParamInfo> headers;
    private List<ApiParamInfo> pathParameters;
    private List<ApiParamInfo> queryStringParameters;
    private ApiSampleCode sampleCode;
    private String multipartYn;
    private String payloadYn;
    private String payloadTypeCode;
    private ApplicationConsole applicationConsole;
}
