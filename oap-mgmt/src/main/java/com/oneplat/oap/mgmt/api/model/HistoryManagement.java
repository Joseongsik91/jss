package com.oneplat.oap.mgmt.api.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * HistoryManagement
 * <p>
 * Created by chungyeol.kim on 2016-12-13.
 */
@ApiModel
@Data
public class HistoryManagement {
    private long historyNumber;
    private String createId;
}
