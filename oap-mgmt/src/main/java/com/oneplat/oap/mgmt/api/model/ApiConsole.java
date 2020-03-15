package com.oneplat.oap.mgmt.api.model;

import lombok.Data;

/**
 * Api
 * <p>
 * Created by Hong Gi Seok on 2016-12-15.
 */
@Data
public class ApiConsole {
    private ApiGeneralInfo apiGeneralInfo;
    private ApiRequestInfo apiRequestInfo;
    private ApiResponseInfo apiResponseInfo;
    private ApiSampleCode sampleCode;
//    @JsonDeserialize(using = FileDeserializer.class)
//    private ApiFile file;
}
