package com.oneplat.oap.mgmt.api.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * ApiSampleCode
 * <p>
 * Created by chungyeol.kim on 2016-12-01.
 */
@ApiModel
@Data
public class ApiSampleCode {
    private String contentTypeCode;
    private String sampleCode;
}
