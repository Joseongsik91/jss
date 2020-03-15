package com.oneplat.oap.mgmt.setting.admin.model;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.oneplat.oap.core.model.AbstractObject;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel(description="운영자(담당자) 역할 모델")
@EqualsAndHashCode(callSuper=false)
@Data
public class OperatorRole extends AbstractObject {
    
    @ApiModelProperty(value="운영자번호", required=true, example="", allowableValues="")
    @NotNull
    private Long oprNumber;

    @ApiModelProperty(value="운영자이름", required=false, example="", allowableValues="")
    private String oprName;
    
    @ApiModelProperty(value="로그인 아이디", required=false, example="", allowableValues="")
    private String loginId;
    
    @ApiModelProperty(value="부서", required=false, example="", allowableValues="")
    private String dept;
    
    @ApiModelProperty(value="직책", required=false, example="", allowableValues="")
    private String position;
    
    @ApiModelProperty(value="휴대폰", required=false, example="", allowableValues="")
    private String cellPhoneNumber;
    
    @ApiModelProperty(value="이메일", required=false, example="", allowableValues="")
    private String email;

    @ApiModelProperty(value="역할번호", required=false, example="", allowableValues="")
    @NotNull
    private Long roleNumber;

    @ApiModelProperty(value="역할명", required=false, example="", allowableValues="")
    private String roleName;

    @ApiModelProperty(value="역할상태코드", required=false, example="", allowableValues="")
    private String roleStateCode;

    @ApiModelProperty(value="역할상태사유", required=false, example="", allowableValues="")
    private String roleStateReason;
    
    @ApiModel(description="은영자(담당자) 역할 저장 모델")
    @EqualsAndHashCode(callSuper=false)
    @Data
    public static class SaveOperatorRole extends AbstractObject {
        
        @ApiModelProperty(value="역할번호", required=true, example="", allowableValues="")
        @NotNull
        private Long roleNumber;

        @ApiModelProperty(value="운영자번호목록", required=false, example="", allowableValues="")
        private List<Long> oprNumberList;
        
    }    
  
}
