package com.oneplat.oap.mgmt.application.model;

import com.oneplat.oap.core.model.AbstractObject;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@ApiModel
public class DcApplicationKey extends AbstractObject {

    @ApiModelProperty(value = "어플리케이션 번호")
    private Long applicationNumber;

    @ApiModelProperty(value = "서비스 등급 코드")
    private String serviceGradeCode;

    @ApiModelProperty(value = "키 유형 코드")
    private String keyTypeCode;

    @ApiModelProperty(value = "어플리케이션 키 순번")
    private int applicationKeySequence;

    @ApiModelProperty(value = "어플리케이션 키")
    private String applicationKey;
    
    @ApiModelProperty(value = "재발급 어플리케이션 키")
    private String reissueApplicationKey;

    @ApiModelProperty(value = "키 종료 일시")
    private String keyEndDatetime;

    @ApiModelProperty(value = "키 유형 속성값")
    private String keyTypeAttributeValue;

    @ApiModelProperty(value = "유형 제한 여부")
    private String typeLimitYn;

    @ApiModelProperty(value = "키 삭제 여부")
    private String keyDeleteYn;

    @ApiModelProperty(value = "이력 관리 번호")
    private Long historyManagementNumber;

    @ApiModelProperty(value = "HMAC 인증 유형 코드")
    private String hmacAuthTypeCode;

    @ApiModelProperty(value = "메세지 암호화 유형 코드")
    private String msgEncryptionTypeCode;

    @ApiModelProperty(value = "메세지 암호화 키")
    private String msgEncryptionKey;

//    @ApiModelProperty(value = "메세지 암호화 재발급 키")
//    private String reissueMsgEncryptionKey;

    @ApiModelProperty(value = "생성 일시")
    private String createDatetime;

//    @ApiModelProperty(value = "생성 아이디")
//    private String createId;

    @ApiModelProperty(value = "변경 일시")
    private String modifyDatetime;

//    @ApiModelProperty(value = "변경 아이디")
//    private String modifyId;
    
}
