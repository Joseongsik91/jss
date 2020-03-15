package com.oneplat.oap.mgmt.api.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * ApiErrorCode
 * <p>
 * Created by chungyeol.kim on 2016-12-01.
 */
@ApiModel
@Data
public class ApiErrorCode {
    private String id;
    private String code;
    private String message;
    private String httpStatusCode;
}
