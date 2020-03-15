package com.oneplat.oap.mgmt.application.model;

import java.util.List;

import com.oneplat.oap.core.model.AbstractObject;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@ApiModel
public class DcApplication extends AbstractObject {

    @ApiModelProperty(value = "어플리케이션 번호")
    private Long applicationNumber;
    
    @ApiModelProperty(value = "개발자 번호")
    private Long developerNumber;

    @ApiModelProperty(value = "사이트 코드")
    private String siteCode;
    
    @ApiModelProperty(value = "어플리케이션 이름")
    private String applicationName;

    @ApiModelProperty(value = "어플리케이션 설명")
    private String applicationDescription;

    @ApiModelProperty(value = "어플리케이션 사용 여부")
    private String applicationUseYn;

    @ApiModelProperty(value = "어플리케이션 삭제 여부")
    private String applicationDeleteYn;
    
    @ApiModelProperty(value = "서비스 수량")
    private int serviceCount;
    
    @ApiModelProperty(value = "서비스 정보")
    private String serviceInfo;
    
    @ApiModelProperty(value = "어플리케이션 서비스 목록")
    private List<DcApplicationService> applicationServiceList;
    

    @ApiModelProperty(value = "생성 일시")
    private String createDatetime;

    @ApiModelProperty(value = "생성 아이디")
    private String createId;

    @ApiModelProperty(value = "변경 일시")
    private String modifyDatetime;

    @ApiModelProperty(value = "변경 아이디")
    private String modifyId;
    
    /* 서비스 번호 목록 */
    private List<String> serviceNumberList;
    
    /* 키정보 목록 */
    private List<DcApplicationKey> applicationKeyList;
    
    /* SLA정보 목록 */
    private List<DcApplicationSla> applicationSlaList;
    
    /* 키(oauth)정보 목록 */
    private List<DcApplicationAuthKey> applicationAuthList;

    /* 서비스 번호 목록 */
    private List<String> scopeNumberList;

}
