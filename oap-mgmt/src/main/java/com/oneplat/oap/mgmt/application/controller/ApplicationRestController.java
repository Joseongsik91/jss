package com.oneplat.oap.mgmt.application.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.oneplat.oap.core.model.SearchRequest;
import com.oneplat.oap.core.model.ValidationCreate;
import com.oneplat.oap.core.model.ValidationUpdate;
import com.oneplat.oap.mgmt.application.model.DashboardApplications;
import com.oneplat.oap.mgmt.application.model.DcApplication;
import com.oneplat.oap.mgmt.application.model.DcApplicationKey;
import com.oneplat.oap.mgmt.application.model.DcApplicationScope;
import com.oneplat.oap.mgmt.application.model.DcApplicationSla;
import com.oneplat.oap.mgmt.application.model.ResponseApplications;
import com.oneplat.oap.mgmt.application.service.ApplicationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

@Api(description="애플리케이션", produces = "application/json")
@RestController
@RequestMapping(value = "/applications")
public class ApplicationRestController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationRestController.class);
    
    @Autowired
    private ApplicationService applicationService;
    
    @ApiOperation(value = "애플리케이션 조회", notes = "애플리케이션 조회")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "q", required = true, dataType = "String", paramType = "query", value = "리스트 query",
            defaultValue = "{"
                    + "\"searchWordTypeCode\":\"\""
                    + ",\"searchWord\":\"\""
                    + ",\"siteCode\":\"\""
                    + ",\"serviceNumber\":\"\""
                    + ",\"useYn\":\"\""
                    + ",\"startDate\":\"\""
                    + ",\"endDate\":\"\"}"),
        @ApiImplicitParam(name = "page", required = true, dataType = "int", paramType = "query", value = "현재 페이지", defaultValue = "1"),
        @ApiImplicitParam(name = "size", required = true, dataType = "int", paramType = "query", value = "페이지 사이즈", defaultValue = "20")
    })
    @RequestMapping(method = RequestMethod.GET)
    public Object getApplications(@ApiIgnore SearchRequest searchRequest) {
        LOGGER.debug("■■■■■■■■■■  getApplications..");
        return new ResponseApplications(applicationService.getApplicationList(searchRequest), searchRequest.getPageInfo());
    }

    
    @ApiOperation(value = "애플리케이션 기본정보 조회", notes = "애플리케이션 기본정보 조회")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "applicationNumber", required = true, dataType = "String", paramType = "path", value = "애플리케이션 번호") 
    })
    @RequestMapping(value = "/{applicationNumber}/info", method = RequestMethod.GET)
    public Object getApplicationInfo(@PathVariable Long applicationNumber) {
        LOGGER.debug("■■■■■■■■■■  getApplicationInfo..");
        return applicationService.getApplicationInfo(applicationNumber);
    }

    
    @ApiOperation(value = "검색조건 서비스 조회", notes = "검색조건 서비스 조회")
    @RequestMapping(value = "/searchService", method = RequestMethod.GET)
    public Object getSearchServiceList() {
        LOGGER.debug("■■■■■■■■■■  getSearchServiceList..");
        return applicationService.getSearchServiceList(new SearchRequest());
    }
    
    
    /**
     * 애플리케이션 상세 조회(기본정보 | 키정보 | SLA정보)
     * 
     * @param applicationNumber
     * @return
     */
    @ApiOperation(value = "애플리케이션 상세 조회", notes = "애플리케이션 상세 조회(기본정보 | 키정보 | SLA정보)")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "applicationNumber", required = true, dataType = "String", paramType = "path", value = "애플리케이션 번호") 
    })
    @RequestMapping(value = "/{applicationNumber}", method = RequestMethod.GET)
    public Object getApplicationDetail(@PathVariable Long applicationNumber) {
        LOGGER.debug("■■■■■■■■■■  getApplicationDetail..");
        return applicationService.getApplicationDetail(applicationNumber);
    }
    
    
    /**
     * 애플리케이션 이력 조회
     * 
     * @param applicationNumber
     * @return
     */
    @ApiOperation(value = "애플리케이션 이력 조회", notes = "애플리케이션 이력 조회")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "applicationNumber", required = true, dataType = "String", paramType = "path", value = "애플리케이션 번호") 
    })    
    @RequestMapping(value = "/{applicationNumber}/history", method = RequestMethod.GET)
    public Object getApplicationDetailHistoryList(@PathVariable Long applicationNumber) {
        LOGGER.debug("■■■■■■■■■■  getApplicationDetailHistoryList..");
        return applicationService.getApplicationDetailHistoryList(applicationNumber);
    }
    
    
    /**
     * 애플리케이션 등록
     * 
     * @param application
     *   등록 샘플 :  
        {
            "developerNumber": "1",
            "siteCode": "DC_SITE_01",
            "applicationName": "테스트앱",
            "applicationDescription": "테스트앱",
            "serviceNumberList": [
                "10000",
                "10001"
            ],
            "applicationUseYn": "Y"
        }
     * @return
     */
    @ApiOperation(value = "애플리케이션 등록", notes = "애플리케이션 등록", response = DcApplication.class)
    @RequestMapping(method = RequestMethod.POST)
    public Object createApplication(@ApiParam(value = "애플리케이션") @RequestBody @Validated(ValidationCreate.class) DcApplication application) {
        LOGGER.debug("■■■■■■■■■■  createApplication..");
        return applicationService.createApplication(application);
    }
    
    /**
     * 애플리케이션 수정
     * 
     * @param application
     *   수정 샘플 :  
        {
            "applicationNumber": "10000000",
            "siteCode": "DC_SITE_01",
            "applicationName": "테스트앱",
            "applicationDescription": "테스트앱",
            "serviceNumberList": [
                "10000",
                "10001"
            ],
            "applicationUseYn": "Y"
        }
     * @return
     */
    @ApiOperation(value = "애플리케이션 수정", notes = "애플리케이션 수정", response = DcApplication.class)
    @RequestMapping(method = RequestMethod.PUT)
    public Object modifyApplication(@ApiParam(value = "애플리케이션") @RequestBody @Validated(ValidationUpdate.class) DcApplication application) {
        LOGGER.debug("■■■■■■■■■■  modifyApplication..");
        return applicationService.modifyApplication(application);
    }
    
    
    @ApiOperation(value = "애플리케이션 삭제", notes = "애플리케이션 삭제", response = DcApplication.class)
    @RequestMapping(value = "/{applicationNumber}/remove", method = RequestMethod.PUT)
    public Object removeApplication(@PathVariable Long applicationNumber) {
        LOGGER.debug("■■■■■■■■■■  removeApplication..");
        DcApplication application = new DcApplication();
        application.setApplicationNumber(applicationNumber);
        applicationService.removeApplication(application);
        return applicationNumber;
    }
    
    
    @ApiOperation(value = "애플리케이션 SLA 변경", notes = "애플리케이션 SLA 변경")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "applicationNumber", required = true, dataType = "String", paramType = "path", value = "애플리케이션 번호") 
    })
    @RequestMapping(value = "/{applicationNumber}/sla", method = RequestMethod.POST)
    public Object modifyApplicationSla(@ApiParam(value = "애플리케이션 SLA") @RequestBody @Validated(ValidationCreate.class) DcApplicationSla applicationSla) {
        LOGGER.debug("■■■■■■■■■■  modifyApplicationSla..");
        return applicationService.modifyApplicationSla(applicationSla);
    }

    
    /**
     * 애플리케이션 SLA 이력 조회
     * 
     * @param applicationNumber
     * @return
     */
    @ApiOperation(value = "애플리케이션 SLA 이력 조회", notes = "애플리케이션 SLA 이력 조회")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "applicationNumber", required = true, dataType = "String", paramType = "path", value = "애플리케이션 번호") 
    })    
    @RequestMapping(value = "/{applicationNumber}/{serviceNumber}/{apiGroupNumber}/history", method = RequestMethod.GET)
    public Object getApplicationSlaHistoryList(@PathVariable Long applicationNumber, @PathVariable Long serviceNumber, @PathVariable Long apiGroupNumber) {
        LOGGER.debug("■■■■■■■■■■  getApplicationSlaHistoryList..");
        DcApplicationSla applicationSla = new DcApplicationSla();
        applicationSla.setApplicationNumber(applicationNumber);
        applicationSla.setServiceNumber(serviceNumber);
        applicationSla.setApiGroupNumber(apiGroupNumber);
        return applicationService.getApplicationSlaHistoryList(applicationSla);
    }
    

    @ApiOperation(value = "애플리케이션 키 등록", notes = "애플리케이션 키 등록")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "applicationNumber", required = true, dataType = "String", paramType = "path", value = "애플리케이션 번호") 
    })
    @RequestMapping(value = "/{applicationNumber}/key", method = RequestMethod.POST)
    public Object createApplicationKey(@ApiParam(value = "애플리케이션 키") @RequestBody @Validated(ValidationCreate.class) DcApplicationKey applicationKey) {
        LOGGER.debug("■■■■■■■■■■  createApplicationKey..");
        return applicationService.createApplicationKey(applicationKey);
    }
    
    
    @ApiOperation(value = "애플리케이션 키 편집", notes = "애플리케이션 키 편집")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "applicationNumber", required = true, dataType = "String", paramType = "path", value = "애플리케이션 번호") 
    })
    @RequestMapping(value = "/{applicationNumber}/key", method = RequestMethod.PUT)
    public Object modifyApplicationKey(@ApiParam(value = "애플리케이션 키") @RequestBody @Validated(ValidationUpdate.class) DcApplicationKey applicationKey) {
        LOGGER.debug("■■■■■■■■■■  modifyApplicationKey..");
        return applicationService.modifyApplicationKey(applicationKey);
    }
    
    
    @ApiOperation(value = "애플리케이션 키 재발급", notes = "애플리케이션 키 재발급")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "applicationNumber", required = true, dataType = "String", paramType = "path", value = "애플리케이션 번호") 
    })
    @RequestMapping(value = "/{applicationNumber}/reissueKey", method = RequestMethod.PUT)
    public Object reissueApplicationKey(@ApiParam(value = "애플리케이션 키") @RequestBody @Validated(ValidationUpdate.class) DcApplicationKey applicationKey) {
        LOGGER.debug("■■■■■■■■■■  reissueApplicationKey..");
        return applicationService.reissueApplicationKey(applicationKey);
    }
    
    
    @ApiOperation(value = "애플리케이션 키 삭제", notes = "애플리케이션 키 삭제")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "applicationNumber", required = true, dataType = "String", paramType = "path", value = "애플리케이션 번호") 
    })
    @RequestMapping(value = "/{applicationNumber}/removeKey", method = RequestMethod.PUT)
    public Object removeApplicationKey(@ApiParam(value = "애플리케이션 키") @RequestBody @Validated(ValidationUpdate.class) DcApplicationKey applicationKey) {
        LOGGER.debug("■■■■■■■■■■  removeApplicationKey..");
        return applicationService.removeApplicationKey(applicationKey);
    }
    
    @ApiOperation(value = "Dashboard Applications", notes = "Dashboard Applications", response = DashboardApplications.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "q", required = true, dataType = "String", paramType = "query", value = "리스트 query", defaultValue = "{\"siteCode\":\"\"}")
    })
    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public Object getDashboardApplications(@ApiIgnore SearchRequest searchRequest) {
        LOGGER.debug("■■■■■■■■■■  getDashboardApplications..");
        return applicationService.getDashboardApplications(searchRequest);
    }
    

    
    @ApiOperation(value = "스코프 조회", notes = "스코프 조회")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "applicationNumber", required = true, dataType = "String", paramType = "path", value = "애플리케이션 번호") 
    })
    @RequestMapping(value = "/{applicationNumber}/searchScope", method = RequestMethod.GET)
    public Object getSearchScopeList(@PathVariable Long applicationNumber) {
        LOGGER.debug("■■■■■■■■■■  getSearchScopeList..");
        return applicationService.getSearchScopeList(applicationNumber);
    }
    
    
    @ApiOperation(value = "애플리케이션 스코프 등록", notes = "애플리케이션 스코프 등록")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "applicationNumber", required = true, dataType = "String", paramType = "path", value = "애플리케이션 번호") 
    })
    @RequestMapping(value = "/{applicationNumber}/scope", method = RequestMethod.POST)
    public Object createApplicationScope(@ApiParam(value = "애플리케이션 스코프") @RequestBody @Validated(ValidationCreate.class) DcApplicationScope applicationScope) {
        LOGGER.debug("■■■■■■■■■■  createApplicationScope..");
        return applicationService.createApplicationScope(applicationScope);
    }

    @ApiOperation(value = "애플리케이션 스코프 수정", notes = "애플리케이션 스코프 수정")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applicationNumber", required = true, dataType = "String", paramType = "path", value = "애플리케이션 번호")
    })
    @RequestMapping(value = "/{applicationNumber}/scope", method = RequestMethod.PUT)
    public Object modifyApplicationScope(@ApiParam(value = "애플리케이션 스코프") @RequestBody @Validated(ValidationCreate.class) DcApplicationScope applicationScope) {
        LOGGER.debug("■■■■■■■■■■  modifyApplicationScope..");
        return applicationService.modifyApplicationScope(applicationScope);
    }
    
    
    @ApiOperation(value = "애플리케이션 시크릿키 재발급", notes = "애플리케이션 시크릿키 재발급")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "applicationNumber", required = true, dataType = "String", paramType = "path", value = "애플리케이션 번호") 
    })
    @RequestMapping(value = "/{applicationNumber}/reissueSecretKey", method = RequestMethod.PUT)
    public Object modifyApplicationSecretKeyScope(@ApiParam(value = "애플리케이션 시크릿키") @RequestBody DcApplicationScope applicationScope) {
        LOGGER.debug("■■■■■■■■■■  modifyApplicationSecretKeyScope..");
        return applicationService.reissueApplicationClientSecret(applicationScope);
    }

    @ApiOperation(value = "애플리케이션 메세지 암호화 키 재발급", notes = "애플리케이션 메세지 암호화 키 재발급")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applicationNumber", required = true, dataType = "String", paramType = "path", value = "애플리케이션 번호")
    })
    @RequestMapping(value = "/{applicationNumber}/reissueMsgEncryptionKey", method = RequestMethod.PUT)
    public Object modifyApplicationMessageEncryptionKey(@ApiParam(value = "애플리케이션 키") @RequestBody @Validated(ValidationUpdate.class) DcApplicationKey applicationKey) {
        LOGGER.debug("■■■■■■■■■■  modifyApplicationMessageEncryptionKey..");
        return applicationService.reissueMessageEncryptionKey(applicationKey);
    }
    
}