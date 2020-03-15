package com.oneplat.oap.mgmt.setting.operator.model;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.oneplat.oap.core.model.AbstractObject;
import com.oneplat.oap.core.model.ValidationUpdate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel(description="운영자 인증 모델")
@EqualsAndHashCode(callSuper=false)
@Data
public class OperatorAuth extends AbstractObject{

    @ApiModelProperty(value="운영자번호", required=false, example="", allowableValues="")
    @NotNull(message = "", groups = {ValidationUpdate.class})
    private Long operatorNumber;
    
    @ApiModelProperty(value="인증 구분 코드", required=false, example="", allowableValues="")
    @NotNull(message = "", groups = {ValidationUpdate.class})
    private String authSectionCode;
    
    @ApiModelProperty(value="생성일시", required=false, example="", allowableValues="")
    @NotNull(message = "", groups = {ValidationUpdate.class})
    private Date issueDatetime;
    
    @ApiModelProperty(value="유효일시", required=false, example="", allowableValues="")
    @NotNull(message = "", groups = {ValidationUpdate.class})
    private Date authValidDatetime;
    
    @ApiModelProperty(value="인증 번호", required=false, example="", allowableValues="")
    @NotNull(message = "", groups = {ValidationUpdate.class})
    private String authNumber;
}
