package com.oneplat.oap.mgmt.oapservice.model;

import com.oneplat.oap.core.model.AbstractObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lee
 * @date 2016-12-09
 */
@ApiModel(description="서비스 구성 데이타 모델")
@EqualsAndHashCode(callSuper=false)
@Data
public class OapServiceCompose extends AbstractObject {
    /** 서비스 번호*/
    @ApiModelProperty(value = "서비스 번호", required = true, example="123", allowableValues="123")
    private Long serviceNumber;
    /** 서비스 구성 코드*/
    @ApiModelProperty(value = "서비스 구성 코드", required = true, example="TEST", allowableValues="TEST")
    private String serviceComposeCode;
    /** 서비스 구성 데이터*/
    @ApiModelProperty(value = "서비스 구성 데이터", required = true, example="TEST", allowableValues="TEST")
    private String serviceComposeData;
    /** 서비스 구성 삭제 여부 */
    @ApiModelProperty(value="서비스 구성 삭제 여부", required=true)
    private Boolean serviceComposeDeleteYn;
}
