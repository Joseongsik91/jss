package com.oneplat.oap.mgmt.application.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class DashboardApplications {

    @ApiModelProperty(value = "Total")
    private long totalCount;
    
    @ApiModelProperty(value = "Active")
    private long activeCount;

}
