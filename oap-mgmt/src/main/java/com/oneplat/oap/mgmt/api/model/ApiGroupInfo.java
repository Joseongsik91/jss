package com.oneplat.oap.mgmt.api.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * ApiGroupInfo
 * <p>
 * Created by chungyeol.kim on 2016-12-07.
 */
@ApiModel
@Data
public class ApiGroupInfo {
    private List<Api> apiList;
    private Long apiGroupNumber;
    private String apiGroupName;
    private int apiGroupLevel;
}
