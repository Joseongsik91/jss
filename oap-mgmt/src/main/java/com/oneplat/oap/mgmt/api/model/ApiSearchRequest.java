package com.oneplat.oap.mgmt.api.model;

import com.oneplat.oap.core.model.SearchRequest;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * ApiSearcheRequest
 * <p>
 * Created by chungyeol.kim on 2016-11-30.
 */
@ApiModel
@Data
public class ApiSearchRequest extends SearchRequest {
    private Long serviceNumber;
    private String searchWordTypeCode;
    private String searchWord;
    private String useYn;
    private String siteCode;
    private String apiSectionCode;
    private String httpMethodCode;
    private Long adaptorNumber;
    private String apiStateCode;
    private String periodTypeCode;
    private String startDateTime;
    private String endDateTime;
    private List<String> httpMethodCodeList;
    private String searchYn;
    private Long apiGroupNumber;
    private int apiGroupLevel;
    private String apiVersion;
}
