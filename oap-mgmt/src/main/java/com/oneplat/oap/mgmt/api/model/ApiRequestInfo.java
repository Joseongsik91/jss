package com.oneplat.oap.mgmt.api.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * ApiRequestInfo
 * <p>
 * Created by chungyeol.kim on 2016-11-29.
 */
@ApiModel
@Data
public class ApiRequestInfo {
    private List<String> contentTypeList;
    private List<ApiParamInfo> headers;
    private List<ApiParamInfo> pathParameters;
    private List<ApiParamInfo> queryStringParameters;
    private List<ApiSampleCode> sampleCodes;
    private List<ApiParamInfo> payloads;
    /** Request 시에 사용 **/
    private String contentType;
}
