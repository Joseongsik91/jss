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

import com.oneplat.oap.mgmt.setting.admin.model.GroupMenu;
import com.oneplat.oap.mgmt.setting.admin.model.GroupMenu.SaveGroupMenu;
import com.oneplat.oap.mgmt.setting.admin.service.MenuGroupService;
import com.oneplat.oap.mgmt.setting.system.model.enums.StCommonCode;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags="/setting/backoffice/menugroup",  description="설정 > admin메뉴/그룹관리 > 메뉴/그룹설정 ", produces = "application/json")
@RestController
@RequestMapping(value = "/setting/admin/menugroup")
public class MenuGroupRestController {

    @Autowired
    MenuGroupService service;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(MenuGroupRestController.class);
    
/*    @ApiOperation(value = "설정 > admin메뉴/그룹관리 > 메뉴/그룹설정 > 전체메뉴(tree형)", notes = "설정 > admin메뉴/그룹관리 > 메뉴/그룹설정 > 전체메뉴(tree형) 조회")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "roleNumber", required = true, dataType = "Long", paramType = "query", value = "그룹(권한)번호", defaultValue = "2")
    })
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody Object searchGroupMenuTree(@ApiIgnore GroupMenu groupMenu) {
        LOGGER.debug("searchMenuGroupTree {}", groupMenu);
        return service.searchGroupMenuTree(groupMenu);
    }*/
    
    @ApiOperation(value = "설정 > admin메뉴/그룹관리 > 메뉴/그룹설정 > 권한에 설정된 메뉴 목록 조회", notes = "설정 > admin메뉴/그룹관리 > 메뉴/그룹설정 > 권한에 설정된 메뉴 목록 조회")
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody Object searchGroupMenus(GroupMenu groupMenu) {
        LOGGER.debug("searchGroupMenus {}", groupMenu.getRoleNumber());
        return service.searchGroupMenus(groupMenu);
    }

    // 등록 - post
    @ApiOperation(value = "설정 > admin메뉴/그룹관리 > 메뉴/그룹설정 > 그룹 메뉴 등록", notes = "설정 > admin메뉴/그룹관리 > 메뉴/그룹설정 > 그룹 메뉴 등록")
    @RequestMapping(value="/{roleNumber}", method = RequestMethod.POST)
    public @ResponseBody Object createGroupMenu(@ApiParam(value="그룹 메뉴 등록 데이타") @RequestBody SaveGroupMenu createGroupMenu ){

        LOGGER.debug("createGroupMenu {}", createGroupMenu);
        
        // 읽기 기본설정
        createGroupMenu.setMenuAuthCode(StCommonCode.MC_MENU_AUTH_READ.getCode());
        return service.createGroupMenu(createGroupMenu);
    }
    
    // 권한설정
    @ApiOperation(value = "설정 > admin메뉴/그룹관리 > 메뉴/그룹설정 > 그룹 메뉴 권한 수정", notes = "설정 > admin메뉴/그룹관리 > 메뉴/그룹설정 > 그룹 메뉴 권한 수정")
    @RequestMapping(value = "/{roleNumber}", method = RequestMethod.PUT)
    public @ResponseBody Object modifyGroupMenuAuth(@ApiParam(value="그룹번호", defaultValue="1") @PathVariable Long roleNumber
            , @ApiParam(value="그룹 메뉴 등록 데이타") @RequestBody GroupMenu groupMenu ){

        LOGGER.debug("modifyGroupMenuAuth {}", groupMenu);
        return service.modifyGroupMenuAuth(groupMenu);
    }
    

    // 삭제 - delete
    @ApiOperation(value = "설정 > admin메뉴/그룹관리 > 메뉴/그룹설정 > 그룹 메뉴 삭제", notes = "설정 > admin메뉴/그룹관리 > 메뉴/그룹설정 > 그룹 메뉴 삭제")
    @RequestMapping(value = "/{roleNumber}/change", method = RequestMethod.PUT)
    public @ResponseBody int removeGroupMenu(@ApiParam(value="그룹번호", defaultValue="1") @PathVariable Long roleNumber
            , @ApiParam(value="그룹 메뉴 변경 데이타") @RequestBody SaveGroupMenu removeGroupMenu ){

        LOGGER.debug("removeGroupMenu {}", removeGroupMenu);
        return service.removeGroupMenu(removeGroupMenu);
    }    
}
