package com.oneplat.oap.mgmt.setting.system.controller;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.oneplat.oap.core.model.SearchRequest;
import com.oneplat.oap.mgmt.setting.system.model.CommonCode;
import com.oneplat.oap.mgmt.setting.system.model.CommonCode.ModifyCommonCode;
import com.oneplat.oap.mgmt.setting.system.model.CommonCode.ModifyCommonCodeSort;
import com.oneplat.oap.mgmt.setting.system.model.CommonGroupCode;
import com.oneplat.oap.mgmt.setting.system.model.CommonGroupCode.ModifyCommonGroupCode;
import com.oneplat.oap.mgmt.setting.system.model.ResponseCommonCode;
import com.oneplat.oap.mgmt.setting.system.model.ResponseCommonGroupCode;
import com.oneplat.oap.mgmt.setting.system.service.CommonCodeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "/setting", description = "시스템관리", produces = "application/json")
@RestController
@RequestMapping(value = "/setting/system")
public class CommonCodeManagementRestController {

	@Autowired
	CommonCodeService commonCodeService;

	private static final Logger LOGGER = LoggerFactory.getLogger(CommonCodeManagementRestController.class);

	@ApiOperation(value = "설정 > 시스템관리 > 공통코드관리 > 그룹공통코드 목록 조회", notes = "그룹공통코드 목록 조회", response = ResponseCommonGroupCode.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "q", required = true, dataType = "String", paramType = "query", value = "검색어", defaultValue = "{\"searchTypeCode\":\"CD_NM\", \"searchName\":\"이름\", \"useYn\":\"\", \"sortField\":\"\", \"orderBy\":\"\"}"),
			@ApiImplicitParam(name = "page", required = true, dataType = "int", paramType = "query", value = "현재 페이지", defaultValue = "1"),
			@ApiImplicitParam(name = "size", required = true, dataType = "int", paramType = "query", value = "페이지 사이즈", defaultValue = "20") })
	@RequestMapping(value = "/commonGroupCode", method = RequestMethod.GET)
	public @ResponseBody Object searchCommonGroupCodeList(@ApiIgnore SearchRequest searchRequest) {
		searchRequest.setData();
		return new ResponseCommonGroupCode(commonCodeService.searchCommonGroupCodeList(searchRequest),
				searchRequest.getPageInfo());
	}

	@ApiOperation(value = "설정 > 시스템관리 > 공통코드관리 > 공통코드 목록 조회", notes = "공통코드 목록 조회", response = ResponseCommonCode.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "q", required = true, dataType = "String", paramType = "query", value = "검색어", defaultValue = "{\"groupCode\":\"MC_PERSON_INFO_SECTION_01\", \"codeName\":\"like 검색용 코드명\", \"useYn\":\"\", \"sortField\":\"CD_NM\", \"orderBy\":\"\"}"),
			@ApiImplicitParam(name = "page", required = true, dataType = "int", paramType = "query", value = "현재 페이지", defaultValue = "1"),
			@ApiImplicitParam(name = "size", required = true, dataType = "int", paramType = "query", value = "페이지 사이즈", defaultValue = "20") })
	@RequestMapping(value = "/commonCode", method = RequestMethod.GET)
	public @ResponseBody Object searchCommonCodeList(@ApiIgnore SearchRequest searchRequest) {
		searchRequest.setData();
		return new ResponseCommonCode(commonCodeService.searchCommonCodeList(searchRequest),
				searchRequest.getPageInfo());
	}

	@ApiOperation(value = "설정 > 시스템관리 > 공통코드관리 > 코드 중복여부조회", notes = "코드 중복여부조회")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "q", required = true, dataType = "String", paramType = "query", value = "검색어", defaultValue = "{\"codeType\":\"GRP_CD\",\"code\":\"MC_PERSON_INFO_SECTION_01\", \"groupCode\":\"\", \"codeName\":\"\"}") })
	@RequestMapping(value = "/commonCode/count", method = RequestMethod.GET)
	public @ResponseBody Object getDuplicateCount(@ApiIgnore SearchRequest searchRequest) {
		searchRequest.setData();
		return commonCodeService.getDuplicateCount(searchRequest);
	}

	@ApiOperation(value = "설정 > 시스템관리 > 공통코드관리 > 그룹공통코드등록", notes = "그룹공통코드등록")
	@RequestMapping(value = "/commonCode/group", method = RequestMethod.POST)
	public @ResponseBody Object createCommonGroupCode(
			@ApiParam(value = "그룹공통코드 등록 데이타") @RequestBody CommonGroupCode commonGroupCode) {
		return commonCodeService.createCommonGroupCode(commonGroupCode);
	}

	@ApiOperation(value = "설정 > 시스템관리 > 공통코드관리 > 그룹공통코드수정", notes = "그룹공통코드수정")
	@RequestMapping(value = "/commonCode/group", method = RequestMethod.PUT)
	public @ResponseBody Object modifyCommonGroupCode(
			@ApiParam(value = "그룹 공통코드 수정 데이타") @RequestBody ModifyCommonGroupCode modifyCommonGroupCode) {
		return commonCodeService.modifyCommonGroupCode(modifyCommonGroupCode);
	}

	@ApiOperation(value = "설정 > 시스템관리 > 공통코드관리 > 공통코드등록", notes = "공통코드등록")
	@RequestMapping(value = "/commonCode/code", method = RequestMethod.POST)
	public @ResponseBody Object createCommonCode(@ApiParam(value = "공통코드 등록 데이타") @RequestBody CommonCode commonCode) {
		return commonCodeService.createCommonCode(commonCode);
	}

	@ApiOperation(value = "설정 > 시스템관리 > 공통코드관리 > 공통코드수정", notes = "공통코드수정")
	@RequestMapping(value = "/commonCode/code", method = RequestMethod.PUT)
	public @ResponseBody Object modifyCommonCode(
			@ApiParam(value = "공통코드 수정 데이타") @RequestBody ModifyCommonCode modifyCommonCode) {
		return commonCodeService.modifyCommonCode(modifyCommonCode);
	}

	@ApiOperation(value = "설정 > 시스템관리 > 공통코드관리 > 순서 변경", notes = "순서변경")
	@RequestMapping(value = "/commonCode/sort", method = RequestMethod.PUT)
	public @ResponseBody int modifyCommonCodeSort(
			@ApiParam(value = "공통코드 수정 데이타") @RequestBody ModifyCommonCodeSort sortModel,
			@ApiIgnore Principal principal) {
		String createId = (principal != null ? principal.getName() : "");
		sortModel.setCreateId(createId);
		sortModel.setModifyId(createId);

		return commonCodeService.modifyCommonCodeSort(sortModel);
	}

}
