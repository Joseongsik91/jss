package com.oneplat.oap.mgmt.setting.admin.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oneplat.oap.mgmt.common.model.AuthenticationInjector;
import com.oneplat.oap.mgmt.setting.admin.mapper.MenuGroupMapper;
import com.oneplat.oap.mgmt.setting.admin.model.GroupMenu;
import com.oneplat.oap.mgmt.setting.admin.model.GroupMenu.MenuTree;
import com.oneplat.oap.mgmt.setting.admin.model.GroupMenu.RoleMenu;
import com.oneplat.oap.mgmt.setting.admin.model.GroupMenu.SaveGroupMenu;
import com.oneplat.oap.mgmt.setting.admin.service.MenuGroupService;

@Service
public class MenuGroupServiceImpl implements MenuGroupService {

	@Autowired
	MenuGroupMapper menuGroupMapper;

	@Autowired
	AuthenticationInjector authenticationInjector;

	@Override
	public Object searchGroupMenuTree(GroupMenu groupMenu) {
		// TODO Auto-generated method stub

		HashMap<String, Object> map = new HashMap<String, Object>();
		// 전체 메뉴 조회
		MenuTree tree = menuGroupMapper.selectRootMenu(groupMenu);
		map.put("tree", tree);

		// 그룹메뉴 권한 조회(본인것만)
		groupMenu.setHasFlag(true);
		map.put("myTree", menuGroupMapper.selectRootMenu(groupMenu));

		return map;
	}

	@Override
	public List<RoleMenu> searchGroupMenus(GroupMenu groupMenu) {
		// TODO Auto-generated method stub
		return menuGroupMapper.selectRoleMenus(groupMenu);
	}

	@Override
	@Transactional
	public int createGroupMenu(SaveGroupMenu createGroupMenu) {
		// TODO Auto-generated method stub
		authenticationInjector.setAuthentication(createGroupMenu);
		return menuGroupMapper.insertRoleMenuList(createGroupMenu);
	}

	@Override
	@Transactional
	public int modifyGroupMenuAuth(GroupMenu groupMenu) {
		// TODO Auto-generated method stub
		// update MC_role_menu.end_datetime
		authenticationInjector.setAuthentication(groupMenu);
		int resultCount = menuGroupMapper.updateRoleMenu(groupMenu);
		resultCount += menuGroupMapper.insertRoleMenu(groupMenu);
		return resultCount;
	}

	@Override
	@Transactional
	public int removeGroupMenu(SaveGroupMenu removeGroupMenu) {
		// TODO Auto-generated method stub
		authenticationInjector.setAuthentication(removeGroupMenu);
		return menuGroupMapper.updateRoleMenuList(removeGroupMenu);
	}

}
