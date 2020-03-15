package com.oneplat.oap.mgmt.policies.model;

import com.oneplat.oap.core.model.AbstractObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Hong Gi Seok on 2017-02-07.
 */
@ApiModel(description="ScopeApi 모델")
@Data
public class ScopeApi extends AbstractObject {
    @ApiModelProperty(value = "Scope 번호")
    long scopeNumber;
    @ApiModelProperty(value = "Api 번호")
    long apiNumber;
    @ApiModelProperty(value = "Api 이름")
    String apiName;
    @ApiModelProperty(value = "Http Method Code")
    String httpMethodCode;
    @ApiModelProperty(value = "Http Method Code Name")
    String httpMethodCodeName;
}
