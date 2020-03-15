package com.oneplat.oap.mgmt.setting.system.model;

import javax.validation.constraints.NotNull;

import com.oneplat.oap.core.model.AbstractObject;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel(description="그룹공통코드 모델")
@EqualsAndHashCode(callSuper=false)
@Data
public class CommonGroupCode extends AbstractObject {

    @ApiModelProperty(value="그룹코드", required=true, example="TEST", allowableValues="TEST", dataType="String")
    @NotNull
    private String groupCode;
    
    @ApiModelProperty(value="그룹코드명", required=true, example="TEST", allowableValues="TEST", dataType="String")
    @NotNull
    private String groupCodeName;
    
    @ApiModelProperty(value="그룹코드설명", required=true, example="TEST", allowableValues="TEST", dataType="String")
    private String groupCodeDesc;
    
    @ApiModelProperty(value="정렬번호", required=true, example="1", allowableValues="", dataType="int")
    private int sortNumber;   
    
    @ApiModelProperty(value="등록자", required=false, example="", allowableValues="")
    private String createId;
    
    @ApiModel(description="그룹공통코드 수정 모델")
    @EqualsAndHashCode(callSuper=false)
    @Data
    public static class ModifyCommonGroupCode extends CommonGroupCode {
        @ApiModelProperty(value="변경전 그룹코드", required=false, example="TEST", allowableValues="TEST", dataType="String")
        private String oldGroupCode;
        
        @ApiModelProperty(value="변경전 그룹코드명", required=false, example="TEST", allowableValues="TEST", dataType="String")
        private String oldGroupCodeName;
    }
    
}
