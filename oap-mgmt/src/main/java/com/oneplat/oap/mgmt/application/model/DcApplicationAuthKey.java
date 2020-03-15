package com.oneplat.oap.mgmt.application.model;

import com.oneplat.oap.core.model.AbstractObject;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@ApiModel
public class DcApplicationAuthKey extends AbstractObject {

    @ApiModelProperty(value = "어플리케이션 번호")
    private Long applicationNumber;
    
    @ApiModelProperty(value = "클라이언트 아이디")
    private String clientId;
    
    @ApiModelProperty(value = "클라이언트 시크릿")
    private String clientSecret;
    
    @ApiModelProperty(value = "키 종료 일시")
    private String keyEndDatetime;
    
    
    @ApiModelProperty(value = "생성 일시")
    private String createDatetime;

//    @ApiModelProperty(value = "생성 아이디")
//    private String createId;

    @ApiModelProperty(value = "변경 일시")
    private String modifyDatetime;

//    @ApiModelProperty(value = "변경 아이디")
//    private String modifyId;
    
}
