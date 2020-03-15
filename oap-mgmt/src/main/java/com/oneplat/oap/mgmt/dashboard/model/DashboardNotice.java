package com.oneplat.oap.mgmt.dashboard.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * DashboardNotice
 * <p>
 * Created by chungyeol.kim on 2017-02-06.
 */
@ApiModel(description="대시보드 API 데이타 모델")
@Data
public class DashboardNotice {
    @ApiModelProperty(value = "스케줄러 최근1주일 실패 건수")
    private int schedulerFailCount;
    @ApiModelProperty(value = "운영자 승인 요청 건수")
    private int operatorApprovalCount;
}
