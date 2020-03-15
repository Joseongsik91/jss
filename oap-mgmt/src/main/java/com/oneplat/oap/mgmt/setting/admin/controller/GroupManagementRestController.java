package com.oneplat.oap.mgmt.setting.admin.controller;

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
import com.oneplat.oap.mgmt.setting.admin.model.ResponseRoleList;
import com.oneplat.oap.mgmt.setting.admin.model.Role;
import com.oneplat.oap.mgmt.setting.admin.model.Role.CreateRole;
import com.oneplat.oap.mgmt.setting.admin.model.Role.ModifyRole;
import com.oneplat.oap.mgmt.setting.admin.service.GroupManagementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "/setting/backoffice/group", description = "설정 > admin메뉴/그룹관리 > 그룹관리", produces = "application/json")
@RestController
@RequestMapping(value = "/setting/admin/group")
public class GroupManagementRestController {

	@Autowired
	GroupManagementService groupManagementService;

	private static final String defaultTxt = "TEST_ADMIN";
	private static final Logger LOGGER = LoggerFactory.getLogger(GroupManagementRestController.class);

	@ApiOperation(value = "설정 > admin메뉴/그룹관리 > 그룹관리 > 그룹조회", notes = "설정 > admin메뉴/그룹관리 > 그룹관리 > 그룹조회", response = ResponseRoleList.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "q", required = true, dataType = "String", paramType = "query", value = "검색조건", defaultValue = "{\"searchName\":\"\"}") })
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody Object searchRoleList(@ApiIgnore SearchRequest searchRequest) {
		return groupManagementService.searchRoleList(searchRequest);
	}

	@ApiOperation(value = "설정 > admin메뉴/그룹관리 > 그룹관리 > 그룹 상세조회", notes = "설정 > admin메뉴/그룹관리 > 그룹관리 > 그룹 상세조회")
	@RequestMapping(value = "/{roleNumber}", method = RequestMethod.GET)
	public @ResponseBody Object searchRole(
			@ApiParam(value = "권한번호", defaultValue = "1") @PathVariable Long roleNumber) {
		LOGGER.debug("searchRole id : ", roleNumber);
		return groupManagementService.searchRole(roleNumber);
	}

	@ApiOperation(value = "설정 > admin메뉴/그룹관리 > 그룹관리 > 중복건수 조회", notes = "설정 > admin메뉴/그룹관리 > 그룹관리 > 중복건수 조회")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "q", required = true, dataType = "String", paramType = "query", value = "검색조건", defaultValue = "{\"searchType\":\"CD,NAME\",\"searchName\":\"\"}") })
	@RequestMapping(value = "/checkDup", method = RequestMethod.GET)
	public @ResponseBody Object searchCount(@ApiIgnore SearchRequest searchRequest) {
		return groupManagementService.checkDuplication(searchRequest);
	}

	// 등록 - post
	@ApiOperation(value = "설정 > admin메뉴/그룹관리 > 그룹관리 > 그룹 등록", notes = "설정 > admin메뉴/그룹관리 > 그룹관리 > 그룹 등록")
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody Object createRoleInfo(@ApiParam(value = "그룹등록 데이타") @RequestBody CreateRole role) {
		LOGGER.debug("createRole {}", role);
		return groupManagementService.createRole(role);
	}

	// 수정 - put
	@ApiOperation(value = "설정 > admin메뉴/그룹관리 > 그룹관리 > 그룹 수정", notes = "설정 > admin메뉴/그룹관리 > 그룹관리 > 그룹 수정")
	@RequestMapping(value = "/{roleNumber}", method = RequestMethod.PUT)
	public @ResponseBody Object modifyRoleInfo(@PathVariable Long roleNumber,
			@ApiParam(value = "그룹 수정 데이타") @RequestBody ModifyRole modifyRole) {
		LOGGER.debug("modifyRole {}", modifyRole);
		modifyRole.setRoleNumber(roleNumber);
		return groupManagementService.modifyRole(modifyRole);
	}

	// 삭제 - delete
	@ApiOperation(value = "설정 > admin메뉴/그룹관리 > 그룹관리 > 그룹 삭제", notes = "설정 > admin메뉴/그룹관리 > 그룹관리 > 그룹 삭제")
	@RequestMapping(value = "/{roleNumber}", method = RequestMethod.DELETE)
	public @ResponseBody void removeRoleInfo(
			@ApiParam(value = "권한번호", defaultValue = "1") @PathVariable Long roleNumber) {
		LOGGER.debug("removeRole roleNumber : ", roleNumber);
		groupManagementService.deleteRole(roleNumber);
	}

	// 순서변경 - put
	@ApiOperation(value = "설정 > admin메뉴/그룹관리 > 그룹관리 > 그룹  순서 변경", notes = "설정 > admin메뉴/그룹관리 > 그룹관리 > 그룹  순서 변경")
	@RequestMapping(value = "/sort", method = RequestMethod.PUT)
	public @ResponseBody void modifyRoleSort(@ApiParam(value = "순위 정렬 데이타") @RequestBody Role role) {
		LOGGER.debug("modifyRoleSort {}", role);
		groupManagementService.modifyRoleSort(role);
	}

	@ApiOperation(value = "설정 > admin메뉴/그룹관리 > 그룹관리 > 그룹 상세조회 > 담당자수 조회", notes = "설정 > admin메뉴/그룹관리 > 그룹관리 > 그룹 상세조회 > 담당자수 조회")
	@RequestMapping(value = "/{roleNumber}/membercount", method = RequestMethod.GET)
	public @ResponseBody Object searchRoleUserCount(
			@ApiParam(value = "권한번호", defaultValue = "1") @PathVariable Long roleNumber) {
		LOGGER.debug("searchRoleUserCount id : ", roleNumber);
		return groupManagementService.selectRoleOprCount(roleNumber);
	}

}
