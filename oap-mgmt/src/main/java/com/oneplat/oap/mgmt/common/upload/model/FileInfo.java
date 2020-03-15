package com.oneplat.oap.mgmt.common.upload.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel(description="파일정보")
@EqualsAndHashCode(callSuper=false)
@Data
public class FileInfo {
    @ApiModelProperty(value="원본파일명", required=false, example="", allowableValues="")
    private String originalFileName;

    @ApiModelProperty(value="서버저장명", required=false, example="", allowableValues="")
    private String serverFileName;

    @ApiModelProperty(value="파일크기", required=false, example="", allowableValues="")
    private Long fileSize;

    @ApiModelProperty(value="확장자", required=false, example="", allowableValues="")
    private String fileExtensionName;

    @ApiModelProperty(value="파일경로", required=false, example="", allowableValues="")
    private String filePath;
    
    @ApiModelProperty(value="파일Url", required=false, example="", allowableValues="")
    private String fileUrl;
}
