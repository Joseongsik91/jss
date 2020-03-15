package com.oneplat.oap.mgmt.application.model;

import java.util.Date;

import com.oneplat.oap.core.model.AbstractObject;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@ApiModel
public class DcApplicationService extends AbstractObject {

    @ApiModelProperty(value = "어플리케이션 번호")
    private Long applicationNumber;

    @ApiModelProperty(value = "서비스 번호")
    private Long serviceNumber;
    
    @ApiModelProperty(value = "서비스 이름")
    private String serviceName;

    @ApiModelProperty(value = "종료 일시")
    private Date endDatetime;
    
    
    @ApiModelProperty(value = "사이트 코드")
    private String siteCode;
    
    @ApiModelProperty(value = "서비스 구분 코드")
    private String serviceSectionCode;
    
    @ApiModelProperty(value = "서비스 사용 여부")
    private String serviceUseYn;
    
    @ApiModelProperty(value = "SLA 사용 여부")
    private String slaUseYn;
    
    @ApiModelProperty(value = "Capacity 사용 여부")
    private String capacityUseYn;
    

    @ApiModelProperty(value = "생성 일시")
    private String createDatetime;

//    @ApiModelProperty(value = "생성 아이디")
//    private String createId;

    @ApiModelProperty(value = "변경 일시")
    private String modifyDatetime;

//    @ApiModelProperty(value = "변경 아이디")
//    private String modifyId;
    
}
