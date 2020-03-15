package com.oneplat.oap.mgmt.setting.admin.controller;

import java.security.Principal;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.oneplat.oap.mgmt.setting.admin.model.AdminMenu;
import com.oneplat.oap.mgmt.setting.admin.model.AdminMenu.AdminMenuRel;
import com.oneplat.oap.mgmt.setting.admin.model.AdminMenu.AdminMenuSort;
import com.oneplat.oap.mgmt.setting.admin.model.AdminMenu.CreateAdminMenu;
import com.oneplat.oap.mgmt.setting.admin.model.AdminMenu.DeleteAdminMenu;
import com.oneplat.oap.mgmt.setting.admin.model.AdminMenuRelation;
import com.oneplat.oap.mgmt.setting.admin.model.ResponseAdminMenuList;
import com.oneplat.oap.mgmt.setting.admin.service.AdminMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "/setting/backoffice/menu", description = "설정 > admin메뉴/그룹관리 > 메뉴관리 ", produces = "application/json")
@RestController
@RequestMapping(value = "/setting/admin/menu")
public class AdminMenuRestController {

	@Autowired
	AdminMenuService service;

	private static final String defaultTxt = "TEST_ADMIN";
	private static final Logger LOGGER = LoggerFactory.getLogger(AdminMenuRestController.class);

	// tree용 목록 - get
	@ApiOperation(value = "설정 > admin메뉴/그룹관리 > 메뉴관리 > Admin 메뉴 전체 조회(tree형)", notes = "설정 > admin메뉴/그룹관리 > 메뉴관리 > Admin 메뉴 전체 조회(tree형)", response = ResponseAdminMenuList.class)
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody Object searchAdminMenuList() {
		LOGGER.debug("searchAdminMenuTree");
		return service.searchAdminMenuTree();
	}

	// list 목록 - get
	@ApiOperation(value = "설정 > admin메뉴/그룹관리 > 메뉴관리 > Admin 메뉴 전체 조회(list형)", notes = "설정 > admin메뉴/그룹관리 > 메뉴관리 > Admin 메뉴 전체 조회(list형)", response = ResponseAdminMenuList.class)
	@RequestMapping(value = "/list/{type}", method = RequestMethod.GET)
	public @ResponseBody Object searchAdminMenuAllList(@PathVariable String type) {
		LOGGER.debug("searchAdminMenuAllList");
		return service.searchAdminMenuList(type);
	}

	// 상세 - get
	@ApiOperation(value = "설정 > admin메뉴/그룹관리 > 메뉴관리 > Admin 메뉴 상세 조회", notes = "설정 > admin메뉴/그룹관리 > 메뉴관리 > Admin 메뉴 상세 조회")
	@RequestMapping(value = "/{menuNum}", method = RequestMethod.GET)
	public @ResponseBody Object searchAdminMenu(
			@ApiParam(value = "메뉴번호", defaultValue = "") @PathVariable Long menuNum) {
		LOGGER.debug("searchAdminMenu id : ", menuNum);
		return service.searchAdminMenu(menuNum);
	}

	// 상위메뉴 list
	@ApiOperation(value = "설정 > admin메뉴/그룹관리 > 메뉴관리 > 기준메뉴 조회", notes = "Admin 메뉴 > 등록/수정 > 상위메뉴(기준메뉴) 조회")
	@RequestMapping(value = "/criteriaMenu/{menuNum}", method = RequestMethod.GET)
	public @ResponseBody List<AdminMenuRelation> searchAdminCriteriaMenuList(
			@ApiParam(value = "상대메뉴번호", defaultValue = "") @PathVariable Long menuNum) {
		LOGGER.debug("searchAdminCriteriaMenuList id : ", menuNum);
		return service.searchAdminCriteriaMenuList(menuNum);
	}

	// 등록 - post
	@ApiOperation(value = "설정 > admin메뉴/그룹관리 > 메뉴관리 > Admin 메뉴 등록", notes = "설정 > admin메뉴/그룹관리 > 메뉴관리 > Admin 등록")
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody Object createAdminMenu(
			@ApiParam(value = "Admin 메뉴 등록 데이타") @RequestBody CreateAdminMenu adminMenu) {
		LOGGER.debug("createAdminMenu {}", adminMenu);
		return service.createAdminMenu(adminMenu);
	}

	// 수정 - put
	@ApiOperation(value = "설정 > admin메뉴/그룹관리 > 메뉴관리 > Admin 메뉴 수정", notes = "설정 > admin메뉴/그룹관리 > 메뉴관리 > Admin 메뉴 수정")
	@RequestMapping(value = "/{menuNum}", method = RequestMethod.PUT)
	public @ResponseBody Object modifyAdminMenu(@ApiParam(value = "메뉴번호", defaultValue = "") @PathVariable Long menuNum,
			@ApiParam(value = "Admin 메뉴 수정 데이타") @RequestBody AdminMenu adminMenu) {
		LOGGER.debug("modifyAdminMenu {}", adminMenu);
		adminMenu.setMenuNum(menuNum);
		service.modifyAdminMenu(adminMenu);
		return service.searchAdminMenu(menuNum);
	}

	// 삭제 - delete
	@ApiOperation(value = "설정 > admin메뉴/그룹관리 > 메뉴관리 > Admin 메뉴 삭제", notes = "설정 > admin메뉴/그룹관리 > 메뉴관리 > Admin 메뉴 삭제")
	@RequestMapping(value = "/{criteriaMenuNum}/{opponentMenuNum}", method = RequestMethod.DELETE)
	public @ResponseBody void removeAdminMenu(
			@ApiParam(value = "기준메뉴번호", defaultValue = "") @PathVariable Long criteriaMenuNum,
			@ApiParam(value = "상대메뉴번호(삭제대상건)", defaultValue = "") @PathVariable Long opponentMenuNum) {
		LOGGER.debug("removeAdminMenu criteriaMenuNum : ", criteriaMenuNum);
		LOGGER.debug("removeAdminMenu opponentMenuNum : ", opponentMenuNum);

		DeleteAdminMenu adminMenu = new DeleteAdminMenu();
		adminMenu.setCriteriaMenuNum(criteriaMenuNum);
		adminMenu.setMenuNum(opponentMenuNum);
		service.removeAdminMenu(adminMenu);
	}

	// 순서변경 - put
	@ApiOperation(value = "설정 > admin메뉴/그룹관리 > 메뉴관리 > Admin 메뉴 순서 변경", notes = "설정 > admin메뉴/그룹관리 > 메뉴관리 > Admin 메뉴 순서 변경")
	@RequestMapping(value = "/sort", method = RequestMethod.PUT)
	public @ResponseBody void modifyAdminMenuSort(
			@ApiParam(value = "Admin 메뉴 순위 정렬 데이타") @RequestBody AdminMenuRelation adminMenuRelation) {
		LOGGER.debug("modifyAdminMenuSort {}", adminMenuRelation);
		service.modifyAdminMenuSort(adminMenuRelation);
	}

	// 메뉴권한수정- put
	@ApiOperation(value = "설정 > admin메뉴/그룹관리 > 메뉴관리 > Admin 메뉴권한코드 변경", notes = "설정 > admin메뉴/그룹관리 > 메뉴관리 > Admin 메뉴권한코드 변경")
	@RequestMapping(value = "/{menuNum}/auth/{menuAuthCode}", method = RequestMethod.PUT)
	public @ResponseBody void modifyAdminMenuAuth(
			@ApiParam(value = "메뉴번호", defaultValue = "") @PathVariable Long menuNum,
			@ApiParam(value = "메뉴권한코드", defaultValue = "") @PathVariable String menuAuthCode) {
		LOGGER.debug("modifyAdminMenuAuth {}", menuNum, menuAuthCode);
		AdminMenu adminMenu = new AdminMenu();
		adminMenu.setMenuNum(menuNum);
		adminMenu.setMenuAuthCode(menuAuthCode);
		service.modifyAdminMenu(adminMenu);
	}

}
