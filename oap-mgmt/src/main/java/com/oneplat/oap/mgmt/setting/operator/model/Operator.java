package com.oneplat.oap.mgmt.setting.operator.model;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat; 
import com.oneplat.oap.core.model.AbstractObject;
import com.oneplat.oap.core.model.ValidationCreate;
import com.oneplat.oap.core.model.ValidationUpdate;
import com.oneplat.oap.core.mybatis.enums.YesNoType;
import com.oneplat.oap.mgmt.setting.admin.model.Role;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel(description="운영자(담당자) 모델")
@EqualsAndHashCode(callSuper=false)
@Data
public class Operator extends AbstractObject {
    
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

    @ApiModelProperty(value="부서", required=false, example="", allowableValues="")
    @NotNull(message = "", groups = {ValidationCreate.class, ValidationUpdate.class})
    private String dept;
    
    @ApiModelProperty(value="직책", required=false, example="", allowableValues="")
    @NotNull(message = "", groups = {ValidationCreate.class, ValidationUpdate.class})
    private String position;
    
    @ApiModelProperty(value="이메일", required=false, example="", allowableValues="")
    @NotNull(message = "", groups = {ValidationCreate.class, ValidationUpdate.class})
    private String email;
    
    @ApiModelProperty(value="내선전화", required=false, example="", allowableValues="")
    @NotNull(message = "", groups = {ValidationCreate.class, ValidationUpdate.class})
    private String extensionPhoneNumber;
    
    @ApiModelProperty(value="휴대폰", required=false, example="", allowableValues="")
    @NotNull(message = "", groups = {ValidationCreate.class, ValidationUpdate.class})
    private String cellPhoneNum;
    
    @ApiModelProperty(value="닉네임", required=false, example="", allowableValues="")
    private String nickName;
    
    @ApiModelProperty(value="비고", required=false, example="", allowableValues="")
    private String note;
    
    @ApiModelProperty(value="운영자 상태 코드", required=false, example="", allowableValues="")
    private OperatorCode operatorStateCode;
    
    @ApiModelProperty(value="운영자 상태 사유", required=false, example="", allowableValues="")
    private String operatorStateDesc;
    
    @ApiModelProperty(value="로그인실패횟수", required=false, example="", allowableValues="")
    private int loginFailCount;
    
    @ApiModelProperty(value="계정잠김여부", required=false, example="", allowableValues="")
    private Boolean acountLockYn;

    @ApiModelProperty(value="계정잠김사유", required=false, example="", allowableValues="")
    private String acountLockReason;
    
    @ApiModelProperty(value="고객정보취급여부", required=false, example="", allowableValues="")
    private Boolean customerInfoTreatYn;

    @ApiModelProperty(value="판매자정보취급여부", required=false, example="", allowableValues="")
    private Boolean sellerInfoTreatYn; 
    
    @ApiModelProperty(value="승인일시", required=false, example="", allowableValues="", dataType="date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date joinApprovalDateTime;
    
    @ApiModelProperty(value="탈퇴일시", required=false, example="", allowableValues="", dataType="date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date leaveDateTime;

    @ApiModelProperty(value="담당업무", required=true, example="", allowableValues="")
    private List<Role> roleList;

    @ApiModelProperty(value="담당업무", required=true, example="", allowableValues="")
    private String roleListString;

    /*SunHaLee
    * Email
     * Password 구분*/
    @ApiModelProperty(value="담당업무", required=true, example="", allowableValues="")
    private String newPasswordCheck;


    @ApiModel(description="운영자(담당자) 등록 데이타 모델")
    @EqualsAndHashCode(callSuper=false)
    @Data
    public static class CreateOperator extends Operator{
        @ApiModelProperty(hidden=true)
        private Long operatorNumber;
        @ApiModelProperty(hidden=true)
        private OperatorCode operatorStateCode;
        @ApiModelProperty(hidden=true)
        private String operatorStateDesc;
        
        @ApiModelProperty(value="회원상태", required=true, example="", allowableValues="")
        private YesNoType operatorStateYn;
        
        @ApiModelProperty(value="담당업무", required=true, example="", allowableValues="")
        @NotNull
        private List<Role> roleSelectList;

        @ApiModelProperty(value="담당자개인정보취급목록", required=false, example="", allowableValues="")
        private List<String> managerPersonInfoCheckedList;
        
    }
    
    @ApiModel(description="운영자(담당자) 수정 데이타 모델")
    @EqualsAndHashCode(callSuper=false)
    @Data
    public static class ModifyOperator extends Operator{
        @ApiModelProperty(hidden=true)
        private String loginId;
        @ApiModelProperty(hidden=true)
        private String operatorName;

        @ApiModelProperty(value="회원상태", required=true, example="", allowableValues="")
        private YesNoType operatorStateYn;
        
        @ApiModelProperty(value="담당업무", required=true, example="", allowableValues="")
        @NotNull
        private List<Role> roleSelectList;

        @ApiModelProperty(value="담당자개인정보취급목록", required=false, example="", allowableValues="")
        private List<String> managerPersonInfoCheckedList;
        
        // 개인정보취급목록 합
        @ApiModelProperty(hidden=true)
        List<OperatorPersonInfo> oprPersonInfoList;
    }

    @ApiModel(description="담당자 개인정보 모델")
    @EqualsAndHashCode(callSuper=false)
    @Data
    public static class OperatorPersonInfo extends AbstractObject {
        @ApiModelProperty(value="담당자번호", required=false, example="", allowableValues="")
        private Long operatorNumber;
        
        @ApiModelProperty(value="개인정보 구분코드", required=false, example="", allowableValues="")
        private String personInfoSectionCode;
        
        @ApiModelProperty(value="개인정보 취급코드", required=false, example="", allowableValues="")
        private String personInfoTreatCode;
    }    
    
    @ApiModel(description="운영자(담당자) 상세 데이타 모델")
    @EqualsAndHashCode(callSuper=false)
    @Data
    public static class OperatorDetail extends Operator{
        
        private String createId;
        private String createName;
        private String modifyId;
        private String modifyName;
        
        @ApiModelProperty(value="담당자개인정보취급목록", required=false, example="", allowableValues="")
        private List<OperatorPersonInfo> personInfoList;
        
    }


    @ApiModel(description="운영자(담당자) 승인 데이타 모델")
    @EqualsAndHashCode(callSuper=false)
    @Data
    public static class OperatorApprove extends AbstractObject{
        
        @ApiModelProperty(value="운영자번호", required=false, example="", allowableValues="")
        private Long operatorNumber;
        
        @ApiModelProperty(value="운영자 상태 코드", required=false, example="", allowableValues="")
        private OperatorCode operatorStateCode;
        
        @ApiModelProperty(value="운영자 상태 사유", required=false, example="", allowableValues="")
        private String operatorStateDesc;
        
    }
    
    @ApiModel(description="운영자(담당자) 비밀번호 수정 모델")
    @EqualsAndHashCode(callSuper=false)
    @Data
    public static class ResetOperatorPassword extends AbstractObject {
        @ApiModelProperty(value="운영자번호", required=false, example="", allowableValues="")
        private Long operatorNumber;
        
        @ApiModelProperty(value="로그인 아이디", required=false, example="", allowableValues="")
        private String loginId;
        
        @ApiModelProperty(value="로그인 비밀번호", required=false, example="", allowableValues="")
        private String loginPassword;
    } 
    
}
