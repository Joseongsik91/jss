package com.oneplat.oap.mgmt.setting.admin.controller;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.oneplat.oap.core.model.SearchRequest;
import com.oneplat.oap.mgmt.setting.admin.model.OperatorRole.SaveOperatorRole;
import com.oneplat.oap.mgmt.setting.admin.service.GroupManagementService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "/setting/backoffice/group", description = "설정 > admin메뉴/그룹관리 > 그룹/담당자설정", produces = "application/json")
@RestController
@RequestMapping(value = "/setting/admin/group/role")
public class GroupRoleManagementRestController {

	@Autowired
	GroupManagementService groupManagementService;

	private static final Logger LOGGER = LoggerFactory.getLogger(GroupRoleManagementRestController.class);

	// 그룹/담당자 설정 상세조회
	@ApiOperation(value = "설정 > admin메뉴/그룹관리 > 그룹/담당자설정 > 그룹별 담당자 상세조회", notes = "설정 > admin메뉴/그룹관리 > 그룹/담당자설정 > 그룹별 담당자 상세조회")
	@RequestMapping(value = "/{roleNumber}", method = RequestMethod.GET)
	public @ResponseBody Object selectRoleOpr(
			@ApiParam(value = "권한번호", defaultValue = "1") @PathVariable Long roleNumber) {
		LOGGER.debug("selectRoleOpr id : ", roleNumber);
		return groupManagementService.selectRoleOpr(roleNumber);
	}

	// 담당자 목록 조회
	@ApiOperation(value = "설정 > admin메뉴/그룹관리 > 그룹/담당자설정 > 담당자조회", notes = "설정 > admin메뉴/그룹관리 > 그룹/담당자설정 > 담당자조회")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "q", required = true, dataType = "String", paramType = "query", value = "검색조건", defaultValue = "{\"roleNumber\":\"그룹번호\", \"searchName\":\"담당자명\"}") })
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody Object selectOprRoleSearchList(@ApiIgnore SearchRequest searchRequest) {
		return groupManagementService.selectOprRoleSearchList(searchRequest);
	}

	// 등록 - post
	@ApiOperation(value = "설정 > admin메뉴/그룹관리 > 그룹/담당자설정 > 담당자 등록", notes = "설정 > admin메뉴/그룹관리 > 그룹/담당자설정 > 담당자 등록")
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody Object createRoleInfo(
			@ApiParam(value = "그룹등록 데이타") @RequestBody SaveOperatorRole createOprRole, @ApiIgnore Principal principal) {
		LOGGER.debug("createRoleOpr {}", createOprRole);
		return groupManagementService.createRoleOpr(createOprRole);
	}

	// 삭제 - delete
	@ApiOperation(value = "설정 > admin메뉴/그룹관리 > 그룹/담당자설정 > 담당자 삭제", notes = "설정 > admin메뉴/그룹관리 > 그룹/담당자설정 > 담당자 삭제")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "oprNumberList", required = true, dataType = "List", paramType = "query", value = "삭제 담당자 목록", defaultValue = ",로 구분해서 넣으세요") })
	@RequestMapping(value = "/{roleNumber}", method = RequestMethod.DELETE)
	public @ResponseBody int removeOprRole(@ApiParam(value = "권한번호", defaultValue = "1") @PathVariable Long roleNumber,
			@ApiIgnore SaveOperatorRole deleteOprRole) {
		LOGGER.debug("removeOprRole {}", deleteOprRole);
		deleteOprRole.setRoleNumber(roleNumber);
		return groupManagementService.removeOprRole(deleteOprRole);
	}

}
