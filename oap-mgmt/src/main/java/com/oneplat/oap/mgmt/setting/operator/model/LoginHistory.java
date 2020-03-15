package com.oneplat.oap.mgmt.setting.operator.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat; 
import com.oneplat.oap.core.model.AbstractObject;
import com.oneplat.oap.core.mybatis.enums.YesNoType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel(description="로그인 이력 모델")
@EqualsAndHashCode(callSuper=false)
@Data
public class LoginHistory extends AbstractObject {
    
    @ApiModelProperty(value="운영자번호", required=false, example="", allowableValues="")
    private Long operatorNumber;

    @ApiModelProperty(value="로그인 아이디", required=false, example="", allowableValues="")
    private String loginId;
    
    @ApiModelProperty(value="운영자 이름", required=false, example="", allowableValues="")
    private String operatorName;
    
    @ApiModelProperty(value="로그인일시", required=false, example="", allowableValues="", dataType="date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date loginDateTime;
    
    @ApiModelProperty(value="접속ip", required=false, example="", allowableValues="")
    private String accessIp4Addr;

    @ApiModelProperty(value="접속유형(내부(Y)/외부(N))", required=false, example="", allowableValues="")
    private YesNoType insideYn;
}
