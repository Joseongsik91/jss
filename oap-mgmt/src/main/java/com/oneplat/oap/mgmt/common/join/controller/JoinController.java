package com.oneplat.oap.mgmt.common.join.controller;

import com.oneplat.oap.core.model.SearchRequest;
import com.oneplat.oap.core.model.ValidationCreate;
import com.oneplat.oap.mgmt.common.join.service.JoinService;
import com.oneplat.oap.mgmt.setting.admin.service.GroupManagementService;
import com.oneplat.oap.mgmt.setting.operator.model.Operator.*;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags="/common/join",  description="공통 > 로그인 > 담당자 가입 ", produces = "application/json")
@RestController
@RequestMapping(value = "/common/join")
public class JoinController {
    
    @Autowired
    JoinService joinService;
    
    @Autowired
    GroupManagementService groupService;

    private static final Logger LOGGER = LoggerFactory.getLogger(JoinController.class);

    @ApiOperation(value = "공통 > 로그인 > 담당자 가입 > 아이디 존재여부 조회", notes = "공통 > 로그인 > 담당자 가입 > 아이디 존재여부 조회")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", required = true, dataType = "String", paramType = "query", value = "아이디")
    })
    @RequestMapping(value = "/id", method = RequestMethod.GET)
    public @ResponseBody Object getIdCount(@ApiIgnore String id) {
        LOGGER.debug("searchIdCount : ", id);
        return joinService.searchIdCount(id);
    }
    
    // 업무 목록 (미사용 제외)
    @ApiOperation(value = "공통 > 로그인 > 담당자 가입 > 담당업무 조회", notes = "공통 > 로그인 > 담당자 가입 > 담당업무 조회")
    @RequestMapping(value = "/role", method = RequestMethod.GET)
    public @ResponseBody Object searchRoleList() {
        return groupService.searchUseRoleList(new SearchRequest());
    }
    
    // 등록 - post
    @ApiOperation(value = "공통 > 로그인 > 담당자 가입", notes = "공통 > 로그인 > 담당자 가입")
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody Object join(@ApiParam(value="가입 신청 데이타") @RequestBody @Validated(ValidationCreate.class) CreateOperator createOpr){

        LOGGER.debug("join {}", createOpr);
        createOpr.setCreateId(createOpr.getLoginId());
        return joinService.createOperator(createOpr);
    }   
    
}
