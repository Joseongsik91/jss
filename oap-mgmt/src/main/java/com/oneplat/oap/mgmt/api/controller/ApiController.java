package com.oneplat.oap.mgmt.api.controller;

import com.oneplat.oap.core.model.SearchRequest;
import com.oneplat.oap.mgmt.api.model.*;
import com.oneplat.oap.mgmt.api.service.ApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * ApiController
 * <p>
 * Created by chungyeol.kim on 2016-11-28.
 */
@Api(description="API 관리", produces = "application/json")
@RestController
@RequestMapping(value = "/apis")
public class ApiController {

    @Autowired
    private ApiService apiService;

    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", dataType = "int", paramType = "query", value = "현재 페이지", defaultValue = "1"),
        @ApiImplicitParam(name = "size", dataType = "int", paramType = "query", value = "페이지 사이즈", defaultValue = "20"),
        @ApiImplicitParam(name = "searchTypeCode", dataType = "String", paramType = "query", value = "검색어 타입", defaultValue = "Name"),
        @ApiImplicitParam(name = "searchName", dataType = "String", paramType = "query", value = "검색어", defaultValue = "api"),
        @ApiImplicitParam(name = "useYn", dataType = "String", paramType = "query", value = "사용여부", defaultValue = "Y"),
        @ApiImplicitParam(name = "siteCode", dataType = "String", paramType = "query", value = "사이트 코드", defaultValue = "MC_SITE_01"),
        @ApiImplicitParam(name = "apiSectionCode", dataType = "String", paramType = "query", value = "API 구분 코드", defaultValue = "MC_API_SECTION_01"),
        @ApiImplicitParam(name = "httpMethod", dataType = "String", paramType = "query", value = "http method", defaultValue = "MC_HTTP_METHOD_01"),
        @ApiImplicitParam(name = "adaptorNumber", dataType = "long", paramType = "query", value = "어댑터 번호", defaultValue = ""),
        @ApiImplicitParam(name = "serviceNumber", dataType = "long", paramType = "query", value = "서비스 번호", defaultValue = ""),
        @ApiImplicitParam(name = "status", dataType = "String", paramType = "query", value = "상태", defaultValue = ""),
        @ApiImplicitParam(name = "apiVersion", dataType = "String", paramType = "query", value = "API 버전", defaultValue = ""),
        @ApiImplicitParam(name = "periodTypeCode", dataType = "String", paramType = "query", value = "기간 타입", defaultValue = ""),
        @ApiImplicitParam(name = "beginDate", dataType = "String", paramType = "query", value = "시작일", defaultValue = "2016/10/01"),
        @ApiImplicitParam(name = "endDate", dataType = "String", paramType = "query", value = "종료일", defaultValue = "2016/12/31")
    })
    @ApiOperation(value = "API 목록", notes = "API 목록", response = ApiGroupInfo.class)
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ApiGroupInfo> getApiList(@ApiIgnore ApiSearchRequest searchRequest) {
        return apiService.getApiList(searchRequest);
    }

    @ApiImplicitParams({
        @ApiImplicitParam(name = "serviceNumber", dataType = "long", paramType = "query", value = "서비스 번호", defaultValue = "")
    })
    @ApiOperation(value = "서비스의 API 공통정보", notes = "서비스의 API 공통정보", response = ApiInfo.class)
    @RequestMapping(value = "/base", method = RequestMethod.GET)
    public ApiInfo getApiAdd(@RequestParam("serviceNumber") long serviceNumber) {
        return apiService.getCreateApiView(serviceNumber);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ApiOperation(value = "API 등록", notes = "API 등록")
    @ResponseStatus(HttpStatus.CREATED)
    public void createApi(@RequestBody ApiInfo apiInfo) {
        apiService.createApi(apiInfo);
    }

    @RequestMapping(value = "/{apiNumber}", method = RequestMethod.PUT)
    @ApiOperation(value = "API 수정", notes = "API 수정")
    public Object modifyApi(@RequestBody ApiInfo apiInfo, @PathVariable long apiNumber) {
        return apiService.modifyApi(apiInfo, apiNumber, "update");
    }

    @RequestMapping(value = "/{apiNumber}/state", method = RequestMethod.PUT)
    @ApiOperation(value = "API State 수정", notes = "API State 수정")
    public Object modifyApiState(@RequestBody ApiInfo apiInfo, @PathVariable long apiNumber) {
        return apiService.modifyApi(apiInfo, apiNumber, "state");
    }

    @RequestMapping(value = "/{apiNumber}", method = RequestMethod.DELETE)
    @ApiOperation(value = "API 삭제", notes = "API 삭제")
    @ResponseStatus(HttpStatus.OK)
    public void deleteApi(@PathVariable long apiNumber) {
        ApiInfo apiInfo = new ApiInfo();
        ApiGeneralInfo apiGeneralInfo = new ApiGeneralInfo();
        apiGeneralInfo.setApiDeleteYn("Y");
        apiInfo.setApiGeneralInfo(apiGeneralInfo);
        apiService.modifyApi(apiInfo, apiNumber, "delete");
    }

    @RequestMapping(value = "/{apiNumber}", method = RequestMethod.GET)
    public Object detailApi(@PathVariable long apiNumber) {
        return apiService.getApi(apiNumber);
    }

    @ApiOperation(value = "Service Group List", notes = "Service Group List")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "serviceNumber", dataType = "long", paramType = "query", value = "서비스 번호", defaultValue = ""),
        @ApiImplicitParam(name = "apiGroupNumber", dataType = "long", paramType = "query", value = "그룹 번호", defaultValue = ""),
        @ApiImplicitParam(name = "apiGroupLevel", dataType = "int", paramType = "query", value = "그룹 레벨", defaultValue = "")
    })
    @RequestMapping(value = "/groups", method = RequestMethod.GET)
    public Object getGroupList(@ApiIgnore ApiSearchRequest searchRequest) {
        return apiService.getApiGroupList(searchRequest);
    }

    @ApiOperation(value = "서비스별 API 버전 목록", notes = "서비스별 API 버전 목록")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "serviceNumber", dataType = "long", paramType = "query", value = "서비스 번호", defaultValue = "")
    })
    @RequestMapping(value = "/versions", method = RequestMethod.GET)
    public Object getApiVersionList(@ApiIgnore ApiSearchRequest searchRequest) {
        return apiService.getApiVersionListForService(searchRequest.getServiceNumber());
    }

    @ApiOperation(value = "northbound URL 중복 체크", notes = "northbound URL 중복 체크")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "nbBaseUrl", dataType = "String", paramType = "query", value = "northbound URL", defaultValue = ""),
        @ApiImplicitParam(name = "httpMethodCode", dataType = "String", paramType = "query", value = "메소드 코드", defaultValue = ""),
        @ApiImplicitParam(name = "apiVersion", dataType = "String", paramType = "query", value = "API 버전", defaultValue = "")
    })
    @RequestMapping(value = "/nbBaseUrlExist", method = RequestMethod.GET)
    public Object getApiNbUrlExist(@RequestParam("nbBaseUrl") String nbBaseUrl,
                                   @RequestParam("httpMethodCode") String httpMethodCode,
                                   @RequestParam("apiVersion") String apiVersion) {
        return apiService.getApiNbUrlExist(nbBaseUrl, httpMethodCode, apiVersion);
    }

    @ApiOperation(value = "API 이력 목록", notes = "API 이력 목록")
    @RequestMapping(value = "/{apiNumber}/histories", method = RequestMethod.GET)
    public Object getApiHistoryList(@PathVariable("apiNumber") long apiNumber) {
        return apiService.getApiHistoryList(apiNumber);
    }

    @ApiOperation(value = "API 이력 상세", notes = "API 이력 상세")
    @RequestMapping(value = "/{apiNumber}/histories/{historyNumber}", method = RequestMethod.GET)
    public Object getApiHistoryDetail(@PathVariable("apiNumber") long apiNumber, @PathVariable("historyNumber") long historyNumber) {
        return apiService.getApiHistory(apiNumber, historyNumber);
    }

    @ApiOperation(value = "대시보드 API 목록", notes = "대시보드 API 목록", response = ApiDashBoard.class)
    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public List<ApiDashBoard> getApiListForDashBoard() {
        return apiService.getApiListForDashBoard();
    }
}
