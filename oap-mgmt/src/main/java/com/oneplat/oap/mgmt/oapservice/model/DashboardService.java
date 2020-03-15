package com.oneplat.oap.mgmt.oapservice.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * DashboardService
 * <p>
 * Created by chungyeol.kim on 2017-02-01.
 */
@ApiModel(description="대시보드 서비스 데이타 모델")
@EqualsAndHashCode(callSuper=false)
@Data
public class DashboardService {

    @ApiModelProperty(value = "서비스 리스트")
    private List<OapService> services;

    @ApiModelProperty(value = "서비스 개수")
    private int serviceCount;

    @ApiModelProperty(value = "API 개수")
    private int apiCount;
}
