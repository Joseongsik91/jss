package com.oneplat.oap.mgmt.common.upload.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;

@ApiModel(description="파일정보")
@EqualsAndHashCode(callSuper=false)
@Data
public class CkeditorFileInfo {
    private int uploaded;
    private String fileName;
    private String url;
    private HashMap<String, String> error;
}
