package com.oneplat.oap.mgmt.api.model;

import lombok.Data;

import java.util.Date;

/**
 * ApiComponent
 * <p>
 * Created by Hong Gi Seok 2016-12-08
 */
@Data
public class ApiComponent {
    private Long apiNumber;
    private String apiComponentCode;
    private String apiComponentData;
    private String apiComponentDeleteYn;
    private String createDateTime;
    private String createId;
    private String modifyDateTime;
    private String modifyId;
}
