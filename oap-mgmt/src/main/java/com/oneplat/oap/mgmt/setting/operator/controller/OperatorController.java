package com.oneplat.oap.mgmt.setting.operator.controller;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.oneplat.oap.core.model.SearchRequest;
import com.oneplat.oap.core.model.ValidationCreate;
import com.oneplat.oap.core.model.ValidationUpdate;
import com.oneplat.oap.mgmt.setting.operator.model.Operator.CreateOperator;
import com.oneplat.oap.mgmt.setting.operator.model.Operator.ModifyOperator;
import com.oneplat.oap.mgmt.setting.operator.model.Operator.OperatorApprove;
import com.oneplat.oap.mgmt.setting.operator.model.Operator.ResetOperatorPassword;
import com.oneplat.oap.mgmt.setting.operator.model.OperatorCode;
import com.oneplat.oap.mgmt.setting.operator.model.ResponseOperator;
import com.oneplat.oap.mgmt.setting.operator.service.OperatorService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags="/setting/operators",  description="설정 > 담당자관리 > 전체담당자", produces = "application/json")
@RestController
@RequestMapping(value = "/setting/operators") 
public class OperatorController {
    
    @Autowired
    OperatorService oprService;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(OperatorController.class);

    private static final String STR_SEARCH_REQUEST =
            "{" +
            " \"searchWordTypeCode\": \"NAME,ID\"," +
            " \"searchWord\": \"검색어(like검색)\"," +
            " \"roleNumber\": \"roleNumber\"," +
            " \"oprStateCode\": \"MC_OPR_STATE_04,MC_OPR_STATE_05(정상,탈퇴)\"," +
            " \"acountLockYn\": \"Y/N\"," +
            " \"startDate\": \"20150402(승인일시)\"," +
            " \"endDate\": \"20150430(승인일시)\"" +
            "}";
    // 목록 조회
    @ApiOperation(value = "설정 > 담당자관리 > 전체담당자 > 조회", notes = "설정 > 담당자관리 > 전체담당자 > 조회", response=ResponseOperator.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "q", required = true, dataType = "String", paramType = "query", value = "검색조건", defaultValue = STR_SEARCH_REQUEST)
        , @ApiImplicitParam(name = "page", required = true, dataType = "int", paramType = "query", value = "현재 페이지", defaultValue = "1")
        , @ApiImplicitParam(name = "size", required = true, dataType = "int", paramType = "query", value = "페이지 사이즈", defaultValue = "20")
    })
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody Object searchOprList(@ApiIgnore SearchRequest searchRequest) {
        LOGGER.debug("searchOprList");
        searchRequest.setData();
        return new ResponseOperator(oprService.searchOperatorList(searchRequest), searchRequest.getPageInfo());
    }
    
    private static final String STR_SEARCH_APPROVAL_REQUEST =
            "{" +
            " \"searchType\": \"NAME,ID\"," +
            " \"searchWord\": \"검색어(like 검색)\"," +
            " \"searchRole\": \"roleNumber\"," +
            " \"approvalStateCode\": \"MC_OPR_STATE_01,MC_OPR_STATE_05(승인요청,반려)\"," +
            " \"startDate\": \"20150402(요청일시)\"," +
            " \"endDate\": \"20150430(요청일시)\"" +
            "}";

    // 목록 조회
    @ApiOperation(value = "설정 > 담당자관리 > 승인관리 > 조회", notes = "설정 > 담당자관리 > 승인관리 > 조회", response=ResponseOperator.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "q", required = true, dataType = "String", paramType = "query", value = "검색조건", defaultValue = STR_SEARCH_APPROVAL_REQUEST)
        , @ApiImplicitParam(name = "page", required = true, dataType = "int", paramType = "query", value = "현재 페이지", defaultValue = "1")
        , @ApiImplicitParam(name = "size", required = true, dataType = "int", paramType = "query", value = "페이지 사이즈", defaultValue = "20")
    })
    @RequestMapping(value = "/standby", method = RequestMethod.GET)
    public @ResponseBody Object searchApprovalList(@ApiIgnore SearchRequest searchRequest) {
        LOGGER.debug("searchApprovalList", searchRequest);
        searchRequest.setData();
        return new ResponseOperator(oprService.searchApprovalStandByList(searchRequest), searchRequest.getPageInfo());
    }
    
    @ApiOperation(value = "설정 > 담당자관리 > 전체담당자 > 건수조회", notes = "설정 > 담당자관리 > 전체담당자 > 건수조회")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "stateCode", required = true, dataType = "stateCode", paramType = "query", value = "상태코드")
    })
    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public @ResponseBody Object searchOprCount(@ApiIgnore String stateCode) {
        LOGGER.debug("searchOprCount : ", stateCode);
        return oprService.getCount(stateCode);
    }
    
    // 상세(담당자번호로 조회) - get
    @ApiOperation(value = "설정 > 담당자관리 > 전체담당자 > 담당자번호로 상세 조회", notes = "설정 > 담당자관리 > 전체담당자 > 담당자번호로 상세 조회")
    @RequestMapping(value = "/{operatorNumber}", method = RequestMethod.GET)
    public @ResponseBody Object searchOperator(@ApiParam(value="회원(담당자)번호", defaultValue="") @PathVariable Long operatorNumber) {
        LOGGER.debug("searchOpr id : ", operatorNumber);
        return oprService.searchOperator(operatorNumber);
    }
    
    // 상세(아이디로 조회) - get
    @ApiOperation(value = "설정 > 담당자관리 > 전체담당자 > 아이디로 상세 조회", notes = "설정 > 담당자관리 > 전체담당자 > 아이디로 상세 조회")
    @RequestMapping(value = "/id", method = RequestMethod.GET)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "loginId", required = true, dataType = "String", paramType = "query", value = "아이디")
    })
    public @ResponseBody Object searchOperatorWithId(@ApiIgnore String loginId) {
        LOGGER.debug("searchOperatorWithId id : ", loginId);
        return oprService.searchOperator(loginId);
    }
    
    // 비밀번호 자동생성
    @ApiOperation(value = "설정 > 담당자관리 > 전체담당자 > 등록/수정 > 비밀번호자동생성", notes = "설정 > 담당자관리 > 전체담당자 > 등록/수정 > 비밀번호자동생성")
    @RequestMapping(value = "/automakepassword", method = RequestMethod.GET)
    public @ResponseBody Object searchAutoMakePassword() {
        return oprService.searchAutoMakePassword();
    }

    private static final String STR_SAVE_REQUEST =
            "{" +
            " \"operatorNumber\": ," +
            " \"oprStateCode\": \"\"," +
            " \"oprStateDesc\": \"\"," +
            " \"searchType\": \"NAME,ID\"," +
                " \"acountLockReason\": \"\"," +
                " \"acountLockYn\": \"N\"," +
                " \"cellPhoneNum\": \"01012345678\"," +
                " \"dept\": \"개발팀\"," +
                " \"email\": \"test2@oneplat.co\"," +
                " \"extensionPhoneNumber\": \"0298765432\"," +
                " \"loginFailCount\": 0," +
                " \"loginId\": \"test3\"," +
                " \"loginPassword\": \"test3\"," +
                " \"nickName\": \"test3\"," +
                " \"note\": \"개발테스트계정\"," +
                " \"operatorName\": \"test3\"," +
                " \"position\": \"책임\"," +
                " \"roleList\": [" +
                "  2,3,4" +
                " ]," +
            "}";
    // 등록 - post
    @ApiOperation(value = "설정 > 담당자관리 > 전체담당자 > 등록", notes = "설정 > 담당자관리 > 전체담당자 > 등록")
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody Object createOperator(@ApiParam(value="담당자 등록 데이타", defaultValue = STR_SAVE_REQUEST) @RequestBody @Validated(ValidationCreate.class) CreateOperator createOperator){
        // [jira:UPPP-113] 
        // - 관리자에 의한 수동등록시 회원상태는 정상으로 처리
        createOperator.setOperatorStateCode(OperatorCode.MC_OPR_STATE_NORMAL);
        createOperator.setOperatorStateDesc("관리자 수동 등록");
        
        LOGGER.debug("createOperator {}", createOperator);
        return oprService.createOperator(createOperator);
    }

    // 수정 - put
    @RequestMapping(value="/{operatorNumber}", method = RequestMethod.PUT)
    @ApiOperation(value = "설정 > 담당자관리 > 전체담당자 > 담당자 정보 수정", notes = "설정 > 담당자관리 > 전체담당자 > 담당자 정보 수정")
    public @ResponseBody int modifyOperator(
            @ApiParam(value="회원번호", defaultValue="") @PathVariable Long operatorNumber, 
            @ApiParam(value="담당자 정보 수정 데이타", defaultValue = STR_SAVE_REQUEST) @RequestBody @Validated(ValidationUpdate.class) ModifyOperator modifyOperator
            ){
        LOGGER.debug("modifyOperator {}", modifyOperator);
        return oprService.modifyOperator(modifyOperator);
    }
    
    // 삭제 - delete
    @ApiOperation(value = "설정 > 담당자관리 > 전체담당자 > 담당자 정보 삭제", notes = "설정 > 담당자관리 > 전체담당자 > 담당자 정보 삭제")
    @RequestMapping(value = "/{operatorNumber}", method = RequestMethod.DELETE)
    public @ResponseBody int removeOperator(@ApiParam(value="담당자 번호", defaultValue="") @PathVariable Long operatorNumber){
        LOGGER.debug("removeOperator operatorNumber : ", operatorNumber);
        return oprService.removeOperator(operatorNumber);
    }
    
    // 수정 - put
    @RequestMapping(value="/{operatorNumber}/approve", method = RequestMethod.PUT)
    @ApiOperation(value = "설정 > 담당자관리 > 전체담당자 > 담당자 정보 수정 > 승인", notes = "설정 > 담당자관리 > 전체담당자 > 담당자 정보 수정 > 승인")
    public @ResponseBody int modifyOperatorApprove(
            @ApiParam(value="회원번호", defaultValue="") @PathVariable Long operatorNumber, 
            @ApiParam(value="담당자 정보 수정 데이타", defaultValue = STR_SAVE_REQUEST) @RequestBody OperatorApprove modifyOperator
            ){
        LOGGER.debug("modifyOperatorApprove {}", modifyOperator);
        return oprService.modifyOperatorApprove(modifyOperator);
    }
    
    // 비밀번호 초기화
    @ApiOperation(value = "회원 > 담당자관리 > 전체담당자 > 담당자 상세 > 비밀번호 초기화", notes = "회원 > 담당자관리 > 전체담당자 > 담당자 상세 > 비밀번호 초기화")
    @RequestMapping(value = "/resetPassword", method = RequestMethod.PUT)
    public @ResponseBody Object modifySellerMemberPassword(@RequestBody ResetOperatorPassword passwordModel) {
        boolean result = false;
        if(passwordModel.getOperatorNumber()!=null && !StringUtils.isBlank(passwordModel.getLoginId())){
            String resetPassword = passwordModel.getLoginPassword(); 
            resetPassword = StringUtils.isBlank(resetPassword)?"reset123!":resetPassword;
            passwordModel.setLoginPassword(resetPassword);
            
            oprService.modifyOperatorPassword(passwordModel);
            result = true;
        }
        return result;
    }
    
}
