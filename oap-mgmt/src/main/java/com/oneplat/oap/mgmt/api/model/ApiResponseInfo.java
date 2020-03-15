package com.oneplat.oap.mgmt.api.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * ApiResponseInfo
 * <p>
 * Created by chungyeol.kim on 2016-11-29.
 */
@ApiModel
@Data
public class ApiResponseInfo {
    private List<String> contentTypeList;
    private List<ApiParamInfo> headers;
    private List<ApiSampleCode> sampleCodes;
    private List<ApiErrorCode> errorCodes;
}
