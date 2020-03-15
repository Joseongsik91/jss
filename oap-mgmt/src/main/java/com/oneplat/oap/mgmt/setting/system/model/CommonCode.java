package com.oneplat.oap.mgmt.setting.system.model;

import javax.validation.constraints.NotNull;

import com.oneplat.oap.core.model.AbstractObject;
import com.oneplat.oap.core.validation.annotation.UniqueName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel(description="공통코드 모델")
@EqualsAndHashCode(callSuper=false)
@Data
public class CommonCode extends AbstractObject {
    
    @ApiModelProperty(value="그룹코드", required=true, example="TEST", allowableValues="TEST", dataType="String")
    @NotNull
    private String groupCode;
    
    @ApiModelProperty(value="코드", required=true, example="", allowableValues="", dataType="String")
    @NotNull
    @UniqueName(columnName="", tableName="")
    private String code;

    @ApiModelProperty(value="코드명", required=true, example="", allowableValues="", dataType="String")
    @NotNull
    private String codeName;
    
    @ApiModelProperty(value="코드설명", required=true, example="", allowableValues="", dataType="String")
    private String codeDesc;
    
    @ApiModelProperty(value="코드문자값1", required=true, example="", allowableValues="", dataType="String")
    private String codeCharVal1;
    
    @ApiModelProperty(value="코드문자값2", required=true, example="", allowableValues="", dataType="String")
    private String codeCharVal2;
    
    @ApiModelProperty(value="코드문자값3", required=true, example="", allowableValues="", dataType="String")
    private String codeCharVal3;
    
    @ApiModelProperty(value="코드숫자값1", required=true, example="", allowableValues="", dataType="int")
    private int codeNumberVal1;
    
    @ApiModelProperty(value="코드숫자값2", required=true, example="", allowableValues="", dataType="int")
    private int codeNumberVal2;
    
    @ApiModelProperty(value="코드숫자값3", required=true, example="", allowableValues="", dataType="int")
    private int codeNumberVal3;
    
    @ApiModelProperty(value="정렬번호", required=true, example="1", allowableValues="", dataType="int")
    private int sortNumber;
    
    @ApiModelProperty(value="등록자", required=false, example="", allowableValues="")
    private String createId;
    
    @ApiModel(description="공통코드 수정 모델")
    @EqualsAndHashCode(callSuper=false)
    @Data
    public static class ModifyCommonCode extends CommonCode {
        @ApiModelProperty(value="변경코드", required=false, example="TEST", allowableValues="TEST", dataType="String")
        private String oldCode;
        
        @ApiModelProperty(value="변경코드명", required=false, example="TEST", allowableValues="TEST", dataType="String")
        private String oldCodeName;
    }
    
    @ApiModel(description="공통코드 정렬번호 수정 모델")
    @EqualsAndHashCode(callSuper=false)
    @Data
    public static class ModifyCommonCodeSort extends AbstractObject {
        @ApiModelProperty(value="그룹코드유형", required=false, example="MC_PERSON_INFO_SECTION_01", allowableValues="", dataType="String")
        private String groupCode;

        @ApiModelProperty(value="그룹코드유형", required=false, example="MC_PERSON_INFO_SECTION_01_02", allowableValues="", dataType="String")
        private String code;

        @ApiModelProperty(value="코드유형", required=false, example="CD", allowableValues="GRP_CD,CD", dataType="String")
        private String codeType;

        @ApiModelProperty(value="변경전 정렬번호", required=false, example="1", allowableValues="1", dataType="int")
        private int beforeSortNum;

        @ApiModelProperty(value="변경후 정렬번호", required=false, example="3", allowableValues="3", dataType="int")
        private int afterSortNum;

        @ApiModelProperty(hidden=true, value="범위시작번호", required=false, example="1", allowableValues="1", dataType="int")
        private int startNum;

        @ApiModelProperty(hidden=true, value="범위끝번호", required=false, example="1", allowableValues="1", dataType="int")
        private int endNum;
}
}
