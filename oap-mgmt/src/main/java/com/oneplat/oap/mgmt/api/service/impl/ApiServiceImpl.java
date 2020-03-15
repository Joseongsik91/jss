package com.oneplat.oap.mgmt.api.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oneplat.oap.core.util.StringHelper;
import com.oneplat.oap.mgmt.api.mapper.ApiMapper;
import com.oneplat.oap.mgmt.api.model.*;
import com.oneplat.oap.mgmt.api.service.ApiService;
import com.oneplat.oap.mgmt.api.support.ApiCacheService;
import com.oneplat.oap.mgmt.common.model.AuthenticationInjector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * ApiServiceImpl
 * <p>
 * Created by chungyeol.kim on 2016-11-28.
 */
@Service
public class ApiServiceImpl implements ApiService {

    @Autowired
    private ApiMapper apiMapper;

    @Autowired
    AuthenticationInjector authenticationInjector;

    @Autowired
    ApiCacheService apiCacheService;

    @Autowired
    Environment env;

    @Override
    public List<ApiGroupInfo> getApiList(ApiSearchRequest searchRequest) {

        searchRequest.setData();
        // http Method Code 리스트 형태로 변형
        searchRequest.setHttpMethodCodeList(this.getHttpMethodCodeList(searchRequest.getHttpMethodCode()));

        List<Api> apiList = apiMapper.selectApiList(searchRequest);

        List<ApiGroupInfo> apiGroupInfoList = new ArrayList<>();

        // 1depth 그룹 추출
        long apiGroupNumber = 0;
        for(Api api : apiList) {
            if(api.getApiGroupLevel() == 1) {
                if(apiGroupNumber != api.getApiGroupNumber()) {
                    apiGroupInfoList.add(this.convertToApiGroupInfo(api));
                }
                apiGroupNumber = api.getApiGroupNumber();
            }
        }

        // 2depth 그룹과 API 정보 설정
        for(ApiGroupInfo apiGroupInfo : apiGroupInfoList) {
            List<Api> apiListInGroup = new ArrayList<>();
            apiGroupNumber = 0;
            for(Api api : apiList) {
                if(api.getApiNumber() != null) {
                    if((api.getApiGroupLevel() == 1 && apiGroupInfo.getApiGroupNumber().equals(api.getApiGroupNumber()))
                            || (api.getApiGroupLevel() == 2 && apiGroupInfo.getApiGroupNumber().equals(api.getCriteriaApiGroupNumber()))) {
                        if(api.getApiGroupLevel() == 2) {
                            if(apiGroupNumber != api.getApiGroupNumber()) {
                                apiListInGroup.add(this.convertToEmptyApi(api));
                            }
                            apiGroupNumber = api.getApiGroupNumber();
                        }
                        apiListInGroup.add(api);
                    }
                } else {
                    if((api.getApiGroupLevel() == 2 && apiGroupInfo.getApiGroupNumber().equals(api.getCriteriaApiGroupNumber()))) {
                        apiListInGroup.add(api);
                    }
                }
            }
            this.hasApi(apiListInGroup);
            apiGroupInfo.setApiList(apiListInGroup);
        }

        // 초기 화면과 검색버튼 클릭시의 동작 구분
        // 검색버튼 클릭 한 경우 검색조건에 맞지않는 그룹정보는 노출되지 않도록 설정
        if("Y".equals(searchRequest.getSearchYn())) {
            for(Iterator<ApiGroupInfo> it = apiGroupInfoList.iterator(); it.hasNext();) {
                ApiGroupInfo apiGroupInfo = it.next();
                if(apiGroupInfo.getApiList() == null || apiGroupInfo.getApiList().size() < 1) {
                    it.remove();
                } else {
                    for(Iterator<Api> apiIterator = apiGroupInfo.getApiList().iterator(); apiIterator.hasNext();) {
                        Api api = apiIterator.next();
                        if(api.getApiGroupLevel() == 2 && api.getApiNumber() == null && !api.isHasApi()) {
                            apiIterator.remove();
                        }
                    }
                    if(apiGroupInfo.getApiList() == null || apiGroupInfo.getApiList().size() < 1) {
                        it.remove();
                    }
                }
            }
        }

        return apiGroupInfoList;
    }

    /**
     * 2depth 그릅에 API가 포함되어있는지 여부를 설정
     * @param apiList
     */
    private void hasApi(List<Api> apiList) {
        for(Api api : apiList) {
            if(api.getApiGroupLevel() == 2 && api.getApiNumber() == null) {
                for(Api secondApi : apiList) {
                    if(api.getApiGroupNumber().equals(secondApi.getApiGroupNumber()) && secondApi.getApiNumber() != null) {
                        api.setHasApi(true);
                    }
                }
            }
        }
    }

    /**
     * http Method Code 를 리스트 형태로 반환
     * @param httpMethodCode
     * @return
     */
    private List<String> getHttpMethodCodeList(String httpMethodCode) {
        if(StringHelper.isEmpty(httpMethodCode)) return null;
        return new ArrayList<>(Arrays.asList(httpMethodCode.split(",")));
    }

    /**
     * 2depth 그룹 정보를 설정
     * @param api
     * @return
     */
    private Api convertToEmptyApi(Api api) {
        Api emptyApi = new Api();
        emptyApi.setApiGroupNumber(api.getApiGroupNumber());
        emptyApi.setApiGroupName(api.getApiGroupName());
        emptyApi.setApiGroupLevel(api.getApiGroupLevel());
        return emptyApi;
    }

    /**
     * 1depth 그룹 정보를 설정
     * @param api
     * @return
     */
    private ApiGroupInfo convertToApiGroupInfo(Api api) {
        ApiGroupInfo apiGroupInfo = new ApiGroupInfo();
        apiGroupInfo.setApiGroupLevel(api.getApiGroupLevel());
        apiGroupInfo.setApiGroupName(api.getApiGroupName());
        apiGroupInfo.setApiGroupNumber(api.getApiGroupNumber());
        return apiGroupInfo;
    }

    @Override
    @Transactional
    public void createApi(ApiInfo apiInfo) {

        // API 정보 등록
        Api api = this.convertToApi(apiInfo);
        api.setApiStateCode("MC_API_STATE_01");
        api.setApiDeleteYn("N");
        apiMapper.insertApi(api);

        // API Number 가 있다면 버전업데이트
        // origin api number 업데이트
        if(apiInfo.getApiGeneralInfo().getApiNumber() != null && apiInfo.getApiGeneralInfo().getApiNumber() > 0) {
            api.setOriginApiNumber(apiInfo.getApiGeneralInfo().getApiNumber());
        } else {
            api.setOriginApiNumber(api.getApiNumber());
        }
        apiMapper.updateApi(api);

        // API 이력 관리 테이블 등록
        HistoryManagement historyManagement = new HistoryManagement();
        historyManagement.setCreateId(authenticationInjector.getAuthentication().getName());
        apiMapper.insertApiHistManagement(historyManagement);

        // API 이력 등록
        api.setHistoryNumber(historyManagement.getHistoryNumber());
        apiMapper.insertApiHist(api);

        // API COMP (General,Request,Response) 정보 등록
        // API COMP (General,Request,Response) 이력 등록
        this.createApiCompData(apiInfo, api.getApiNumber(), historyManagement.getHistoryNumber());

        /** OSDF Interfacing*/
        if(env.getProperty("system.interfaceYn").equals("Y")){
            ApiInfo cacheApiInfo = getApi(api.getApiNumber());
            apiCacheService.apiCacheCreate(cacheApiInfo);
        }
    }

    private Api convertToApi(ApiInfo apiInfo) {
        ApiGeneralInfo apiGeneralInfo = apiInfo.getApiGeneralInfo();
        Api api = new Api();
        if(apiGeneralInfo != null) {
            if(apiGeneralInfo.getApiGroupNumber() != null) {
                api.setApiGroupNumber(apiGeneralInfo.getApiGroupNumber());
            }
            if(apiGeneralInfo.getAdaptorNumber() != null) {
                api.setAdaptorNumber(apiGeneralInfo.getAdaptorNumber());
            }
            api.setSiteCode(apiGeneralInfo.getSiteCode());
            api.setApiName(apiGeneralInfo.getApiName());
            api.setApiDesc("");
            api.setApiSectionCode(apiGeneralInfo.getApiSectionCode());
            api.setApiVersion(apiGeneralInfo.getApiVersion());
            api.setHttpMethodCode(apiGeneralInfo.getNbMethodCode());
            api.setNbBaseUrl(apiGeneralInfo.getNbBaseUrl());
            api.setApiUseYn(apiGeneralInfo.getApiUseYn());
            api.setApiDeleteYn(apiGeneralInfo.getApiDeleteYn());
            
            api.setMultipartYn(apiGeneralInfo.getMultipartYn());
        }
        api.setCreateId(authenticationInjector.getAuthentication().getName());
        api.setModifyId(authenticationInjector.getAuthentication().getName());
        return api;
    }

    private void createApiCompData(ApiInfo apiInfo, long apiNumber, long historyNumber) {

        ApiComp apiComp = new ApiComp();
        apiComp.setCreateId(authenticationInjector.getAuthentication().getName());
        apiComp.setModifyId(authenticationInjector.getAuthentication().getName());
        apiComp.setDeleteYn("N");
        apiComp.setApiNumber(apiNumber);
        apiComp.setHistoryNumber(historyNumber);

        apiComp.setApiCompCode("MC_API_COMP_01");
        apiComp.setApiCompData(this.objectToJson(apiInfo.getApiGeneralInfo()));
        apiMapper.insertApiComp(apiComp);
        apiMapper.insertApiCompHist(apiComp);

        apiComp.setApiCompCode("MC_API_COMP_02");
        apiComp.setApiCompData(this.objectToJson(apiInfo.getApiRequestInfo()));
        apiMapper.insertApiComp(apiComp);
        apiMapper.insertApiCompHist(apiComp);

        apiComp.setApiCompCode("MC_API_COMP_03");
        apiComp.setApiCompData(this.objectToJson(apiInfo.getApiResponseInfo()));
        apiMapper.insertApiComp(apiComp);
        apiMapper.insertApiCompHist(apiComp);

    }

    @Override
    @Transactional
    public ApiInfo modifyApi(ApiInfo apiInfo, long apiNumber, String modifyType) {
        // API 정보 수정
        Api api = this.convertToApi(apiInfo);
        api.setApiNumber(apiNumber);
        if(!StringHelper.isEmpty(apiInfo.getApiGeneralInfo().getApiStateCode())) {
            api.setApiStateCode(apiInfo.getApiGeneralInfo().getApiStateCode());
            if("MC_API_STATE_04".equals(apiInfo.getApiGeneralInfo().getApiStateCode())) {
                api.setApiUseYn("N");
            }
        }
        apiMapper.updateApi(api);

        // 이력 관리 테이블 등록
        HistoryManagement historyManagement = new HistoryManagement();
        historyManagement.setCreateId(authenticationInjector.getAuthentication().getName());
        apiMapper.insertApiHistManagement(historyManagement);

        // API 이력 등록
        api = apiMapper.selectApi(apiNumber);
        api.setHistoryNumber(historyManagement.getHistoryNumber());
        apiMapper.updateApiHist(api);
        apiMapper.insertApiHist(api);

        // General,Request,Response 정보 수정
        // API COMP 이력 등록
        this.modifyApiCompData(apiInfo, apiNumber, historyManagement.getHistoryNumber(), modifyType);
        // cache 연동과 controller return ApiInfo get
        ApiInfo returnApiInfo = getApi(apiNumber);
        /** OSDF Interfacing*/
        if(env.getProperty("system.interfaceYn").equals("Y")){
            if("state".equals(modifyType)) {
                apiCacheService.apiCacheModifyStatus(returnApiInfo);
            }else if("update".equals(modifyType)) {
                apiCacheService.apiCacheModify(returnApiInfo);
            }else if("delete".equals(modifyType)) {
                apiCacheService.apiCacheDelete(returnApiInfo);
            }
        }
        return returnApiInfo;
    }

    private void modifyApiCompData(ApiInfo apiInfo, long apiNumber, long historyNumber, String modifyType) {

        ApiComp apiComp = new ApiComp();
        apiComp.setCreateId(authenticationInjector.getAuthentication().getName());
        apiComp.setModifyId(authenticationInjector.getAuthentication().getName());
        apiComp.setDeleteYn("N");
        apiComp.setApiNumber(apiNumber);
        apiComp.setHistoryNumber(historyNumber);

        apiComp.setApiCompCode("MC_API_COMP_01");
        if("update".equals(modifyType)) {
            apiComp.setApiCompData(this.objectToJson(apiInfo.getApiGeneralInfo()));
        }
        apiMapper.updateApiComp(apiComp);

        apiComp.setApiCompCode("MC_API_COMP_02");
        if("update".equals(modifyType)) {
            apiComp.setApiCompData(this.objectToJson(apiInfo.getApiRequestInfo()));
        }
        apiMapper.updateApiComp(apiComp);

        apiComp.setApiCompCode("MC_API_COMP_03");
        if("update".equals(modifyType)) {
            apiComp.setApiCompData(this.objectToJson(apiInfo.getApiResponseInfo()));
        }
        apiMapper.updateApiComp(apiComp);

        List<ApiComp> apiCompList = apiMapper.selectApiCompList(apiNumber);
        apiMapper.updateApiCompHist(apiComp);
        for(ApiComp apiCompHist : apiCompList) {
            apiCompHist.setApiNumber(apiNumber);
            apiCompHist.setHistoryNumber(historyNumber);
            apiCompHist.setCreateId(authenticationInjector.getAuthentication().getName());
            apiCompHist.setModifyId(authenticationInjector.getAuthentication().getName());
            apiMapper.insertApiCompHist(apiCompHist);
        }

    }

    private String objectToJson(Object object) {

        if(object == null) return "";

        ObjectMapper objectMapper = new ObjectMapper();
        String data = "";
        try {
            data = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    public ApiInfo getCreateApiView(long serviceNumber) {
        ApiInfo apiInfo = new ApiInfo();

        // MC_SVC 테이블에 등록된 API 기본정보 셀렉트
        ApiGeneralInfo apiGeneralInfo = apiMapper.selectServiceBaseInfo(serviceNumber);
        if(apiGeneralInfo != null) {

            // API 공통정보 미사용인 경우 빈 오브젝트 반환
            if("N".equals(apiGeneralInfo.getServiceCompUseYn())) {
                apiInfo.setApiGeneralInfo(apiGeneralInfo);
                return apiInfo;
            }

            String nbBaseUrl = apiGeneralInfo.getNbBaseUrl();
            // protocol 정보 설정
            apiGeneralInfo.setProtocolCode(this.getProtocolCode(nbBaseUrl.substring(0, 7)));
            // 기본도메인 정보 설정
            String nonProtocol = nbBaseUrl.substring(nbBaseUrl.indexOf("://")+3);
            String[] urlArray = nonProtocol.split("/");
            String baseDomain = urlArray[0]+"/"+urlArray[1];
            apiGeneralInfo.setNbHost(baseDomain);
            // 리소스 uri version 설정
            apiGeneralInfo.setNbApiVersion("{version}");
            // 리소스 uri 설정
            String resourceUrl = "";
            for(int cnt = 3; cnt < urlArray.length; cnt++) {
                if("".equals(resourceUrl)) {
                    resourceUrl += urlArray[cnt];
                } else {
                    resourceUrl += "/" + urlArray[cnt];
                }
            }
            apiGeneralInfo.setNbApiResourceUrl(resourceUrl);
        }

        // 서비스 기본 설정 정보 취득
        List<ApiComp> serviceBaseInfoList = apiMapper.selectServiceBaseCompList(serviceNumber);

        List<ApiParamInfo> headerList = new ArrayList<>();
        List<String> contentTypeList = new ArrayList<>();
        List<String> acceptList = new ArrayList<>();
        if(serviceBaseInfoList != null && serviceBaseInfoList.size() > 0) {
            for(ApiComp apiComp : serviceBaseInfoList) {
                ObjectMapper objectMapper = new ObjectMapper();
                if("MC_SVC_COMP_01".equals(apiComp.getApiCompCode())) {
                    try {
                        headerList = objectMapper.readValue(apiComp.getApiCompData(), new ArrayList<ApiParamInfo>().getClass());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if("MC_SVC_COMP_02".equals(apiComp.getApiCompCode())) {
                    try {
                        contentTypeList = objectMapper.readValue(apiComp.getApiCompData(), new ArrayList<String>().getClass());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        acceptList = objectMapper.readValue(apiComp.getApiCompData(), new ArrayList<String>().getClass());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        ApiRequestInfo apiRequestInfo = new ApiRequestInfo();
        ApiResponseInfo apiResponseInfo = new ApiResponseInfo();

        apiRequestInfo.setHeaders(headerList);
        apiRequestInfo.setContentTypeList(contentTypeList);
        apiResponseInfo.setContentTypeList(acceptList);

        apiInfo.setApiGeneralInfo(apiGeneralInfo);
        apiInfo.setApiRequestInfo(apiRequestInfo);
        apiInfo.setApiResponseInfo(apiResponseInfo);

        return apiInfo;
    }

    // 프로토콜 코드 설정
    private String getProtocolCode(String protocol) {
        return protocol.equals("http://") ? "MC_API_PROTOCOL_01" : "MC_API_PROTOCOL_02";
    }

    @Override
    public ApiInfo getApi(long apiNumber) {

        // API COMP 셀렉트 후 General / Request / Response 로 각각 설정
        List<ApiComp> apiCompList = apiMapper.selectApiCompList(apiNumber);
        ApiInfo apiInfo = this.getApiInfo(apiCompList);

        // API 정보 셀렉트
        Api api = apiMapper.selectApi(apiNumber);
        apiInfo.getApiGeneralInfo().setApiStateCode(api.getApiStateCode());
        apiInfo.getApiGeneralInfo().setAdaptorBeanId(api.getAdaptorBeanId());
        apiInfo.getApiGeneralInfo().setApiNumber(api.getApiNumber());
        apiInfo.getApiGeneralInfo().setCreateDateTime(api.getCreateDateTime());
        apiInfo.getApiGeneralInfo().setCreateId(api.getCreateId());
        apiInfo.getApiGeneralInfo().setModifyDateTime(api.getModifyDateTime());
        apiInfo.getApiGeneralInfo().setModifyId(api.getModifyId());
        if(api.getApiGroupLevel() > 1) {
            Api firstGroupInfo = apiMapper.selectFirstGroupInfo(api.getApiGroupNumber());
            if(firstGroupInfo != null) {
                apiInfo.getApiGeneralInfo().setFirstApiGroupNumber(firstGroupInfo.getApiGroupNumber());
                apiInfo.getApiGeneralInfo().setFirstApiGroupName(firstGroupInfo.getApiGroupName());
                apiInfo.getApiGeneralInfo().setSecondApiGroupNumber(api.getApiGroupNumber());
                apiInfo.getApiGeneralInfo().setSecondApiGroupName(api.getApiGroupName());
            }
        } else {
            apiInfo.getApiGeneralInfo().setFirstApiGroupNumber(api.getApiGroupNumber());
            apiInfo.getApiGeneralInfo().setFirstApiGroupName(api.getApiGroupName());
        }

        // API 버전 리스트 설정
        List<Api> apiVersionList = apiMapper.selectApiVersionList(api.getOriginApiNumber());
        apiInfo.getApiGeneralInfo().setApiVersionList(apiVersionList);

        return apiInfo;
    }

    private ApiGeneralInfo convertToApiGeneralInfo(Api api) {
        ApiGeneralInfo apiGeneralInfo = new ApiGeneralInfo();

        String nbBaseUrl = api.getNbBaseUrl();
        // protocol 정보 설정
        apiGeneralInfo.setProtocolCode(this.getProtocolCode(nbBaseUrl.substring(0, 7)));
        // 기본도메인 정보 설정
        String nonProtocol = nbBaseUrl.substring(nbBaseUrl.indexOf("://")+3);
        String[] urlArray = nonProtocol.split("/");
        String baseDomain = urlArray[0]+"/"+urlArray[1];
        apiGeneralInfo.setNbHost(baseDomain);
        // 리소스 uri version 설정
        apiGeneralInfo.setNbApiVersion("{version}");
        // 리소스 uri 설정
        String resourceUrl = "";
        for(int cnt = 3; cnt < urlArray.length; cnt++) {
            if("".equals(resourceUrl)) {
                resourceUrl += urlArray[cnt];
            } else {
                resourceUrl += "/" + urlArray[cnt];
            }
        }
        apiGeneralInfo.setNbApiResourceUrl(resourceUrl);

        apiGeneralInfo.setApiStateCode(api.getApiStateCode());
        apiGeneralInfo.setApiUseYn(api.getApiUseYn());
        apiGeneralInfo.setAdaptorNumber(api.getAdaptorNumber());
        apiGeneralInfo.setAdaptorBeanId(api.getAdaptorBeanId());
        apiGeneralInfo.setApiGroupNumber(api.getApiGroupNumber());
        apiGeneralInfo.setApiName(api.getApiName());
        apiGeneralInfo.setApiSectionCode(api.getApiSectionCode());
        apiGeneralInfo.setSbBaseUrl(api.getSbBaseUrl());
        apiGeneralInfo.setSbApiTestUrl(api.getSbApiTestUrl());
        apiGeneralInfo.setSiteCode(api.getSiteCode());
        apiGeneralInfo.setResponseInfoUseYn("");

        return apiGeneralInfo;
    }

    @Override
    public List<Api> getApiGroupList(ApiSearchRequest apiSearchRequest) {
        return apiMapper.selectGroupList(apiSearchRequest);
    }

    @Override
    public List<String> getApiVersionListForService(long serviceNumber) {
        return apiMapper.selectApiVersionListForService(serviceNumber);
    }

    @Override
    public Api getApiNbUrlExist(String nbBaseUrl, String httpMethodCode, String apiVersion) {
        Api api = new Api();
        List<Api> apiList = apiMapper.selectApiNumber(nbBaseUrl, httpMethodCode, apiVersion);
        if(apiList != null && apiList.size() > 0) {
            api.setApiNumber(apiList.get(0).getApiNumber());
        }
        return api;
    }

    @Override
    public List<Api> getApiHistoryList(long apiNumber) {
        return apiMapper.selectApiHistoryList(apiNumber);
    }

    @Override
    public ApiInfo getApiHistory(long apiNumber, long historyNumber) {

        // API COMP 셀렉트 후 General / Request / Response 로 각각 설정
        List<ApiComp> apiCompList = apiMapper.selectApiCompHistoryList(apiNumber, historyNumber);
        ApiInfo apiInfo = this.getApiInfo(apiCompList);

        // API 정보 셀렉트
        Api api = apiMapper.selectApiHistory(apiNumber, historyNumber);
        apiInfo.getApiGeneralInfo().setApiStateCode(api.getApiStateCode());
        apiInfo.getApiGeneralInfo().setAdaptorBeanId(api.getAdaptorBeanId());
        if(api.getApiGroupLevel() > 1) {
            Api firstGroupInfo = apiMapper.selectFirstGroupInfo(api.getApiGroupNumber());
            if(firstGroupInfo != null) {
                apiInfo.getApiGeneralInfo().setFirstApiGroupNumber(firstGroupInfo.getApiGroupNumber());
                apiInfo.getApiGeneralInfo().setFirstApiGroupName(firstGroupInfo.getApiGroupName());
                apiInfo.getApiGeneralInfo().setSecondApiGroupNumber(api.getApiGroupNumber());
                apiInfo.getApiGeneralInfo().setSecondApiGroupName(api.getApiGroupName());
            }
        } else {
            apiInfo.getApiGeneralInfo().setFirstApiGroupNumber(api.getApiGroupNumber());
            apiInfo.getApiGeneralInfo().setFirstApiGroupName(api.getApiGroupName());
        }

        return apiInfo;
    }

    private ApiInfo getApiInfo(List<ApiComp> apiCompList) {
        ApiInfo apiInfo = new ApiInfo();
        ApiGeneralInfo apiGeneralInfo = new ApiGeneralInfo();
        ApiRequestInfo apiRequestInfo = new ApiRequestInfo();
        ApiResponseInfo apiResponseInfo = new ApiResponseInfo();

        for(ApiComp apiComp : apiCompList) {
            ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            if("MC_API_COMP_01".equals(apiComp.getApiCompCode())) {
                try {
                    apiGeneralInfo = objectMapper.readValue(apiComp.getApiCompData(), ApiGeneralInfo.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if("MC_API_COMP_02".equals(apiComp.getApiCompCode())) {
                try {
                    apiRequestInfo = objectMapper.readValue(apiComp.getApiCompData(), ApiRequestInfo.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if("MC_API_COMP_03".equals(apiComp.getApiCompCode())) {
                try {
                    apiResponseInfo = objectMapper.readValue(apiComp.getApiCompData(), ApiResponseInfo.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        apiInfo.setApiGeneralInfo(apiGeneralInfo);
        apiInfo.setApiRequestInfo(apiRequestInfo);
        apiInfo.setApiResponseInfo(apiResponseInfo);

        return apiInfo;
    }

    /**
     * 전체 API Cache 적용을 위한 데이터 셀렉트
     * @return
     */
    @Override
    public List<ApiInfo> getAllApiList() {
        List<ApiInfo> apiInfoList = new ArrayList<>();

        List<Api> allApiList = apiMapper.selectAllApiList();

        for(Api api : allApiList) {
            List<ApiComp> apiCompList = this.getApiCompListForCache(api);
            ApiInfo apiInfo = this.getApiInfo(apiCompList);
            apiInfo.getApiGeneralInfo().setApiStateCode(api.getApiStateCode());
            apiInfo.getApiGeneralInfo().setAdaptorBeanId(api.getAdaptorBeanId());
            apiInfo.getApiGeneralInfo().setApiNumber(api.getApiNumber());
            apiInfo.getApiGeneralInfo().setCreateDateTime(api.getCreateDateTime());
            apiInfo.getApiGeneralInfo().setCreateId(api.getCreateId());
            apiInfo.getApiGeneralInfo().setModifyDateTime(api.getModifyDateTime());
            apiInfo.getApiGeneralInfo().setModifyId(api.getModifyId());

            if(api.getApiGroupLevel() > 1) {
                apiInfo.getApiGeneralInfo().setFirstApiGroupNumber(api.getFirstApiGroupNumber());
                apiInfo.getApiGeneralInfo().setSecondApiGroupNumber(api.getApiGroupNumber());
            } else {
                apiInfo.getApiGeneralInfo().setFirstApiGroupNumber(api.getApiGroupNumber());
            }

            apiInfoList.add(apiInfo);
        }

        return apiInfoList;
    }

    private List<ApiComp> getApiCompListForCache(Api api) {
        List<ApiComp> apiCompList = new ArrayList<>();

        ApiComp apiComp = new ApiComp();
        apiComp.setApiCompCode("MC_API_COMP_01");
        apiComp.setApiCompData(api.getGeneralData());
        apiCompList.add(apiComp);

        apiComp = new ApiComp();
        apiComp.setApiCompCode("MC_API_COMP_02");
        apiComp.setApiCompData(api.getRequestData());
        apiCompList.add(apiComp);

        apiComp = new ApiComp();
        apiComp.setApiCompCode("MC_API_COMP_03");
        apiComp.setApiCompData(api.getResponseData());
        apiCompList.add(apiComp);

        return apiCompList;
    }

    @Override
    public List<ApiDashBoard> getApiListForDashBoard() {
        return apiMapper.selectApiListForDashBoard();
    }
}
