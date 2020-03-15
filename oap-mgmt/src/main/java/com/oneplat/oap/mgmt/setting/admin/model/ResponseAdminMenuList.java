package com.oneplat.oap.mgmt.setting.admin.model;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.NoArgsConstructor;

@ApiModel
@NoArgsConstructor
public class ResponseAdminMenuList extends AdminMenuRelation {
    
    @ApiModelProperty(value = "메뉴 목록", required = true)
    private List<AdminMenuRelation> data;

}
