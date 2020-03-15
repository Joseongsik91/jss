package com.oneplat.oap.mgmt.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Api Dashboard
 * <p>
 * Created by chungyeol.kim on 2016-12-01.
 */
@ApiModel(description="대시보드 API 데이타 모델")
@Data
public class ApiDashBoard {
    @ApiModelProperty(value = "API 이름")
    private String apiName;

    @ApiModelProperty(value = "서비스 이름")
    private String serviceName;
}
