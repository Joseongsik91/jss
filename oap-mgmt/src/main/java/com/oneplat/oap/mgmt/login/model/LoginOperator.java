package com.oneplat.oap.mgmt.login.model;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.oneplat.oap.core.model.AbstractObject;
import com.oneplat.oap.core.model.ValidationCreate;
import com.oneplat.oap.core.model.ValidationUpdate;
import com.oneplat.oap.mgmt.setting.admin.model.Role;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel(description="운영자(담당자) 모델")
@EqualsAndHashCode(callSuper=false)
@Data
public class LoginOperator extends AbstractObject {
    
    @ApiModelProperty(value="운영자번호", required=false, example="", allowableValues="")
    @NotNull(message = "", groups = {ValidationUpdate.class})
    private Long operatorNumber;

    @ApiModelProperty(value="로그인 아이디", required=false, example="", allowableValues="")
    @NotNull(message = "", groups = {ValidationCreate.class})
    private String loginId;
    
    @ApiModelProperty(value="로그인 비밀번호", required=false, example="", allowableValues="")
    @NotNull(message = "", groups = {ValidationCreate.class})
    private String loginPassword;
    
    @ApiModelProperty(value="운영자 이름", required=false, example="", allowableValues="")
    @NotNull(message = "", groups = {ValidationCreate.class})
    private String operatorName;
    
    @ApiModelProperty(value="운영자 상태 코드", required=false, example="", allowableValues="")
    private String operatorStateCode;
    
    @ApiModelProperty(value="로그인실패횟수", required=false, example="", allowableValues="")
    private int loginFailCount;
    
    @ApiModelProperty(value="계정잠김여부", required=false, example="", allowableValues="")
    private String acountLockYn;

    @ApiModelProperty(value="휴대폰", required=false, example="", allowableValues="")
    @NotNull(message = "", groups = {ValidationCreate.class, ValidationUpdate.class})
    private String cellPhoneNum;
    
    private List<Role> roleList;
}
