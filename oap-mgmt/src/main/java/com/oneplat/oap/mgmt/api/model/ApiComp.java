package com.oneplat.oap.mgmt.api.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * ApiComp
 * <p>
 * Created by chungyeol.kim on 2016-12-13.
 */
@ApiModel
@Data
public class ApiComp {
    private String apiCompCode;
    private String apiCompData;
    private String deleteYn;

    private long apiNumber;
    private long historyNumber;
    private String createId;
    private String modifyId;
}
