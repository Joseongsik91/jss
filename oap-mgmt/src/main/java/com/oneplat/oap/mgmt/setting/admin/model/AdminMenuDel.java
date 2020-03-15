package com.oneplat.oap.mgmt.setting.admin.model;

import com.oneplat.oap.core.model.AbstractObject;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * AdminMenu 모델
 *
 * @author 
 * @date 
 * @version 
 */
@ApiModel(description="backoffice 메뉴 관리")
@EqualsAndHashCode(callSuper=false)
@Data
public class AdminMenuDel extends AbstractObject {
    @ApiModelProperty(value="자식 번호", required=true, example="", allowableValues="")
    private Long subOpponentMenuNum;
    
    @ApiModelProperty(value="자식자식 번호", required=true, example="", allowableValues="")
    private Long subSubOpponentMenuNum;
    
    @ApiModelProperty(value="자식자싲자식 번호", required=true, example="", allowableValues="")
    private Long subSubSubOpponentMenuNum;
    
    @ApiModelProperty(value="자식자식자식 번호", required=true, example="", allowableValues="")
    private Long subSubSubSubOpponentMenuNum;

}

