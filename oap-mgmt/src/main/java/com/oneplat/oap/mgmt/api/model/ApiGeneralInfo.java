package com.oneplat.oap.mgmt.api.model;

import com.oneplat.oap.core.mybatis.enums.YesNoType;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * ApiGeneralInfo
 * <p>
 * Created by chungyeol.kim on 2016-11-29.
 */
@ApiModel
@Data
public class ApiGeneralInfo {
    private String siteCode;
    private Long apiGroupNumber;
    private String apiName;
    private String apiSectionCode;
    private String apiVersion;
    private Long adaptorNumber;
    private String adaptorBeanId;

    private String nbMethodCode;
    private String nbBaseUrl;

    private String protocolCode;
    private String nbHost;
    private String nbApiVersion;
    private String nbApiResourceUrl;

    private String sbBaseUrl;
    private String sbMethodCode;
    private String sbApiTestUrl;

    private String apiStateCode;
    private String apiUseYn;
    private String apiDeleteYn;
    private String responseInfoUseYn;

    private Long firstApiGroupNumber;
    private String firstApiGroupName;
    private Long secondApiGroupNumber;
    private String secondApiGroupName;

    private List<Api> apiVersionList;
    private Long apiNumber;
    private String serviceCompUseYn;
    private String serviceContext;

    private String createDateTime;
    private String createId;
    private String modifyDateTime;
    private String modifyId;
    
    private String multipartYn;
}
