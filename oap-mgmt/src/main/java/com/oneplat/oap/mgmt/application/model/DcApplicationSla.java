package com.oneplat.oap.mgmt.application.model;

import com.oneplat.oap.core.model.AbstractObject;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@ApiModel
public class DcApplicationSla extends AbstractObject {

    @ApiModelProperty(value = "어플리케이션 번호")
    private Long applicationNumber;

    @ApiModelProperty(value = "서비스 번호")
    private Long serviceNumber;

    @ApiModelProperty(value = "API 그룹 번호")
    private Long apiGroupNumber;

    @ApiModelProperty(value = "서비스 등급 코드")
    private String serviceGradeCode;

    @ApiModelProperty(value = "종료 일시")
    private String endDatetime;
    
    @ApiModelProperty(value = "시작 일시")
    private String beginDatetime;

    @ApiModelProperty(value = "서비스 제한 수량")
    private long serviceLimitQuantity;

    @ApiModelProperty(value = "서비스 기준 코드")
    private String serviceCriteriaCode;
    
    
    @ApiModelProperty(value = "서비스 이름")
    private String serviceName;
    
    @ApiModelProperty(value = "API 그룹 이름")
    private String apiGroupName;
    
    @ApiModelProperty(value = "상용 서비스 제한 수량")
    private long commerceServiceLimitQuantity;

    @ApiModelProperty(value = "상용 서비스 기준 코드")
    private String commerceServiceCriteriaCode;
    
    @ApiModelProperty(value = "테스트 서비스 제한 수량")
    private long testServiceLimitQuantity;

    @ApiModelProperty(value = "테스트 서비스 기준 코드")
    private String testServiceCriteriaCode;
    

    @ApiModelProperty(value = "생성 일시")
    private String createDatetime;

//    @ApiModelProperty(value = "생성 아이디")
//    private String createId;

    @ApiModelProperty(value = "변경 일시")
    private String modifyDatetime;

//    @ApiModelProperty(value = "변경 아이디")
//    private String modifyId;
    
}
