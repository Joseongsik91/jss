package com.oneplat.oap.mgmt.api.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * ApiParamInfo
 * <p>
 * Created by chungyeol.kim on 2016-12-01.
 */
@ApiModel
@Data
public class ApiParamInfo {
    private String type;
    private String name;
    private String dataTypeCode;
    private String mandatoryYn;
    private boolean checked;
    private String description;
    private String example;
    /* request 요청시에만 사용 */
    private String value;
}
