package com.oneplat.oap.mgmt.application.model;

import com.oneplat.oap.core.model.AbstractObject;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@ApiModel
public class DcHistoryManagement extends AbstractObject {

    @ApiModelProperty(value = "이력 관리 번호")
    private Long historyManagementNumber;
    
    @ApiModelProperty(value = "이력 관리 코드")
    private String historyManagementCode;
    
    @ApiModelProperty(value = "이력 관리 메모")
    private String historyManagementMemo;

    
    @ApiModelProperty(value = "생성 일시")
    private String createDatetime;

//    @ApiModelProperty(value = "생성 아이디")
//    private String createId;

    @ApiModelProperty(value = "변경 일시")
    private String modifyDatetime;

//    @ApiModelProperty(value = "변경 아이디")
//    private String modifyId;
    
}
