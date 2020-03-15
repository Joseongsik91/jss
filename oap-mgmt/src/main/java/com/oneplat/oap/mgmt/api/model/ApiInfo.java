package com.oneplat.oap.mgmt.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.oneplat.oap.core.mybatis.enums.YesNoType;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

/**
 * ApiInfo
 * <p>
 * Created by chungyeol.kim on 2016-11-28.
 */
@ApiModel
@Data
public class ApiInfo {
    private ApiGeneralInfo apiGeneralInfo;
    private ApiRequestInfo apiRequestInfo;
    private ApiResponseInfo apiResponseInfo;
}
