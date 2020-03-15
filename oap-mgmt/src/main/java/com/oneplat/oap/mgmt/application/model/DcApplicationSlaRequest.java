package com.oneplat.oap.mgmt.application.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class DcApplicationSlaRequest {

    @ApiModelProperty(value = "SLA 변경 요청 번호")
    private Long slaModifyRequestNumber;

    @ApiModelProperty(value = "어플리케이션 번호")
    private Long applicationNumber;

    @ApiModelProperty(value = "서비스 번호")
    private Long serviceNumber;

    @ApiModelProperty(value = "API 그룹 번호")
    private Long apiGroupNumber;

    @ApiModelProperty(value = "서비스 등급 코드")
    private String serviceGradeCode;

    @ApiModelProperty(value = "이전 서비스 제한 수량")
    private long previousServiceLimitQuantity;

    @ApiModelProperty(value = "변경 서비스 제한 수량")
    private long modifyServiceLimitQuantity;

    @ApiModelProperty(value = "이전 서비스 기준 코드")
    private String previousServiceCriteriaCode;

    @ApiModelProperty(value = "변경 서비스 기준 코드")
    private String modifyServiceCriteriaCode;

    @ApiModelProperty(value = "SLA 변경 상태 코드")
    private String slaModifyStateCode;

    @ApiModelProperty(value = "SLA 변경 상태 사유")
    private String slaModifyStateReason;


    @ApiModelProperty(value = "생성 일시")
    private String createDatetime;

    @ApiModelProperty(value = "생성 아이디")
    private String createId;

    @ApiModelProperty(value = "변경 일시")
    private String modifyDatetime;

    @ApiModelProperty(value = "변경 아이디")
    private String modifyId;
    
}
