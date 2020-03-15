package com.oneplat.oap.mgmt.application.model;

import java.util.List;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel
public class ResponseApplication {
    
    private DcApplication applicationInfo;
    
    private List<DcApplicationKey> applicationKeyList;
    
    private List<DcApplicationSla> applicationSlaList;
    
}
