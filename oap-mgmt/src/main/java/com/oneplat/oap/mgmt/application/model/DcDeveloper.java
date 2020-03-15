package com.oneplat.oap.mgmt.application.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class DcDeveloper {

    @ApiModelProperty(value = "개발자 번호")
    private Long developerNumber;

    @ApiModelProperty(value = "로그인 아이디")
    private String loginId;

    @ApiModelProperty(value = "로그인 비밀번호")
    private String loginPassword;

    @ApiModelProperty(value = "개발자 이름")
    private String developerName;

    @ApiModelProperty(value = "내선 전화 번호")
    private String extensionPhoneNumber;

    @ApiModelProperty(value = "휴대 전화 번호")
    private String cellPhoneNumber;

    @ApiModelProperty(value = "이메일")
    private String email;

    @ApiModelProperty(value = "닉네임")
    private String nickname;

    @ApiModelProperty(value = "개발자 상태 코드")
    private String developerStateCode;

    @ApiModelProperty(value = "개발자 상태 사유")
    private String developerStateReason;

    @ApiModelProperty(value = "로그인 실패 횟수")
    private int loginFailCount;

    @ApiModelProperty(value = "계정 잠김 여부")
    private String accountLockYn;

    @ApiModelProperty(value = "계정 잠김 사유")
    private String accountLockReason;


    @ApiModelProperty(value = "생성 일시")
    private String createDatetime;

    @ApiModelProperty(value = "생성 아이디")
    private String createId;

    @ApiModelProperty(value = "변경 일시")
    private String modifyDatetime;

    @ApiModelProperty(value = "변경 아이디")
    private String modifyId;
    
}
