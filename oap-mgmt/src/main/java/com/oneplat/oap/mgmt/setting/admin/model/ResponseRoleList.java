package com.oneplat.oap.mgmt.setting.admin.model;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.NoArgsConstructor;

@ApiModel
@NoArgsConstructor
public class ResponseRoleList extends Role{
    @ApiModelProperty(value = "그룹 목록", required = true)
    private List<Role> data;
    
    @ApiModel
    @NoArgsConstructor
    public class ResponseOprRoleList extends OperatorRole{
        @ApiModelProperty(value = "그룹담당자 목록", required = true)
        private List<OperatorRole> data;
    }    
}
