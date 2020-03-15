package com.oneplat.oap.mgmt.setting.operator.controller;

import com.oneplat.oap.core.model.SearchRequest;
import com.oneplat.oap.mgmt.setting.operator.model.ResponseLoginHistory;
import com.oneplat.oap.mgmt.setting.operator.service.OperatorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags="/setting/operators",  description="설정 > 담당자관리 > 접속관리", produces = "application/json")
@RestController
@RequestMapping(value = "/setting/operators/loginHistories")
public class LoginHistoryController {
    
    @Autowired
    OperatorService oprService;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginHistoryController.class);

    private static final String STR_SEARCH_REQUEST =
            "{" +
            " \"searchType\": \"NAME,ID\"," +
            " \"searchName\": \"검색어(equal검색)\"," +
            " \"insideYn\": \"\"," +
            " \"loginDateType\": \"last,date\"," +
            " \"startDate\": \"20150402(로그인일시)\"," +
            " \"endDate\": \"20150430(로그인일시)\"" +
            "}";

    // 목록 조회
    @ApiOperation(value = "설정 > 담당자관리 > 접속관리 > 조회", notes = "설정 > 담당자관리 > 접속관리 > 조회", response=ResponseLoginHistory.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "q", required = true, dataType = "String", paramType = "query", value = "검색조건", defaultValue = STR_SEARCH_REQUEST)
        , @ApiImplicitParam(name = "page", required = true, dataType = "int", paramType = "query", value = "현재 페이지", defaultValue = "1")
        , @ApiImplicitParam(name = "size", required = true, dataType = "int", paramType = "query", value = "페이지 사이즈", defaultValue = "20")
    })
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody Object searchLoginHistoryList(@ApiIgnore SearchRequest searchRequest) {
        LOGGER.debug("searchLoginHistoryList");
        searchRequest.setData();
        return new ResponseLoginHistory(oprService.searchLoginHistoryList(searchRequest), searchRequest.getPageInfo());
    }
    
}
