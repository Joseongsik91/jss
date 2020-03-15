package com.oneplat.oap.mgmt.oapservice.controller;

import com.oneplat.oap.core.model.SearchRequest;
import com.oneplat.oap.core.model.ValidationCreate;
import com.oneplat.oap.core.model.ValidationUpdate;
import com.oneplat.oap.mgmt.common.model.AuthenticationInjector;
import com.oneplat.oap.mgmt.oapservice.model.DashboardService;
import com.oneplat.oap.mgmt.oapservice.model.OapService;
import com.oneplat.oap.mgmt.oapservice.model.ResponseOapServiceList;
import com.oneplat.oap.mgmt.oapservice.service.OapServiceService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lee
 * @date 2016-12-01
 */
@Api(description="서비스", produces = "application/json")
@RestController
@RequestMapping(value = "/services")
public class OapServiceRestController {

    @Autowired
    OapServiceService oapServiceService;

    @ApiOperation(value = "서비스 리스트 조회", notes = "서비스 리스트 조회", response = ResponseOapServiceList.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "q", required = true, dataType = "String", paramType = "query", value = "리스트 query",
                    defaultValue = "{\"searchWordTypeCode\":\"\",\"searchWord\":\"\",\"startDateTime\":\"\",\"endDateTime\":\"\"}"),
            @ApiImplicitParam(name = "page", required = true, dataType = "int", paramType = "query", value = "현재 페이지", defaultValue = "1"),
            @ApiImplicitParam(name = "size", required = true, dataType = "int", paramType = "query", value = "페이지 사이즈", defaultValue = "20")
    })
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Object getServiceList(@ApiIgnore SearchRequest searchRequest) {
        return new ResponseOapServiceList(oapServiceService.selectServiceList(searchRequest), searchRequest.getPageInfo());
    }

    @ApiOperation(value = "서비스 등록", notes = "서비스 등록", response = OapService.class)
    @RequestMapping(method = RequestMethod.POST)
    public OapService createOapService(@ApiParam(value = "서비스") @RequestBody @Validated(ValidationCreate.class) OapService oapService){
        oapServiceService.createService(oapService);
        return oapService;
//        return oapServiceService.selectService(oapService.getServiceNumber());
    }

    @ApiOperation(value = "서비스 수정", notes = "서비스 수정", response = OapService.class)
    @RequestMapping(value = "/{serviceNumber}", method = RequestMethod.PUT)
    public OapService updateOapService(@ApiParam(value="서비스 번호", defaultValue="123") @PathVariable long serviceNumber,
                                       @ApiParam(value = "서비스") @RequestBody @Validated(ValidationUpdate.class) OapService oapService){
        oapServiceService.updateService(serviceNumber, oapService);
        return oapServiceService.selectService(serviceNumber);
    }

    @ApiOperation(value = "서비스 삭제", notes = "서비스 삭제", response = Long.class)
    @RequestMapping(value = "/{serviceNumber}", method = RequestMethod.DELETE)
    public Object deleteOapService(@ApiParam(value="서비스 번호", defaultValue="123") @PathVariable long serviceNumber){
        oapServiceService.deleteService(serviceNumber);
        return serviceNumber;
    }

    @ApiOperation(value = "서비스 상세 조회", notes = "서비스 상세 조회", response = OapService.class)
    @RequestMapping(value = "/{serviceNumber}", method = RequestMethod.GET)
    public Object getServiceDetail(@ApiParam(value="서비스 번호", defaultValue="123") @PathVariable long serviceNumber) {
        return oapServiceService.selectService(serviceNumber);
    }

    @ApiOperation(value = "서비스 상세 히스토리 조회", notes = "서비스 상세 히스토리 조회", response = List.class)
    @RequestMapping(value = "/{serviceNumber}/history", method = RequestMethod.GET)
    public Object getServiceDetailHistory(@ApiParam(value="서비스 번호", defaultValue="123") @PathVariable long serviceNumber) {
        return oapServiceService.selectServiceHistoryList(serviceNumber);
    }

    @ApiOperation(value = "서비스 이름 조회", notes = "서비스 이름 조회", response = Integer.class)
    @ApiImplicitParam(name = "serviceContext", required = true, dataType = "string", paramType = "query", value = "서비스 이름", defaultValue = "test")
    @RequestMapping(value = "/nameinquiry", method = RequestMethod.GET)
    public Object getServiceNameInquiry(@ApiParam(value="서비스 이름", defaultValue="test") @RequestParam String serviceContext) {
        return oapServiceService.selectServiceNameCount(serviceContext);
    }

    @ApiOperation(value = "서비스 삭제 조회", notes = "서비스 삭제 조회", response = List.class)
    @RequestMapping(value = "/{serviceNumber}/deleteinquiry", method = RequestMethod.GET)
    public Object getServiceDeleteInquiry(@ApiParam(value="서비스 번호", defaultValue="123") @PathVariable long serviceNumber) {
        return oapServiceService.selectServiceDeleteInquiry(serviceNumber);
    }

    @ApiOperation(value = "대시보드 서비스 리스트 조회", notes = "대시보드 서비스 리스트 조회", response = DashboardService.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "q", required = true, dataType = "String", paramType = "query", value = "리스트 query",
                    defaultValue = "{\"siteCode\":\"\"}")
    })
    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public Object getServiceListForDashBoard(@ApiIgnore SearchRequest searchRequest) {
        return oapServiceService.selectServiceListForDashBoard(searchRequest);
    }
}
