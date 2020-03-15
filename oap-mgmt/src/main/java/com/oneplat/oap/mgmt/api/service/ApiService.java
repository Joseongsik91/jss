package com.oneplat.oap.mgmt.api.service;

import com.oneplat.oap.mgmt.api.model.*;

import java.util.List;

/**
 * ApiService
 * <p>
 * Created by chungyeol.kim on 2016-11-28.
 */
public interface ApiService {
    List<ApiGroupInfo> getApiList(ApiSearchRequest searchRequest);
    void createApi(ApiInfo apiInfo);
    ApiInfo modifyApi(ApiInfo apiInfo, long apiNumber, String modifyType);
    ApiInfo getCreateApiView(long serviceNumber);
    ApiInfo getApi(long apiNumber);
    List<Api> getApiGroupList(ApiSearchRequest apiSearchRequest);
    List<String> getApiVersionListForService(long serviceNumber);
    Api getApiNbUrlExist(String nbBaseUrl, String httpMethodCode, String apiVersion);
    List<Api> getApiHistoryList(long apiNumber);
    ApiInfo getApiHistory(long apiNumber, long historyNumber);
    List<ApiInfo> getAllApiList();
    List<ApiDashBoard> getApiListForDashBoard();
}
