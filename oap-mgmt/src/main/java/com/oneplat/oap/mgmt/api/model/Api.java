package com.oneplat.oap.mgmt.api.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Api
 * <p>
 * Created by chungyeol.kim on 2016-12-01.
 */
@ApiModel
@Data
public class Api {
    private Long apiNumber;
    private Long apiGroupNumber;
    private String apiGroupName;
    private int apiGroupLevel;
    private Long adaptorNumber;
    private String adaptorBeanId;
    private String siteCode;
    private String siteCodeName;
    private String apiName;
    private String apiDesc;
    private String apiSectionCode;
    private String apiSectionCodeName;
    private String apiVersion;
    private String httpMethodCode;
    private String httpMethodCodeName;
    private String nbBaseUrl;
    private String apiStateCode;
    private String apiStateCodeName;
    private String serviceBeginDateTime;
    private String serviceEndDateTime;
    private String apiUseYn;
    private String apiDeleteYn;
    private String createDateTime;
    private String createId;
    private String modifyDateTime;
    private String modifyId;
    private List<Api> secondApiList;
    private Long criteriaApiGroupNumber;
    private boolean hasApi;
    private long historyNumber;
    private String sbBaseUrl;
    private String sbApiTestUrl;
    private Long originApiNumber;
    private String apiGroupContext;
    private Long firstApiGroupNumber;
    private String generalData;
    private String requestData;
    private String responseData;
    
    private String multipartYn;
}
