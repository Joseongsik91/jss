package com.oneplat.oap.mgmt.setting.admin.service;

import java.util.List;

import com.oneplat.oap.mgmt.setting.admin.model.GroupMenu;
import com.oneplat.oap.mgmt.setting.admin.model.GroupMenu.RoleMenu;
import com.oneplat.oap.mgmt.setting.admin.model.GroupMenu.SaveGroupMenu;

public interface MenuGroupService {
	Object searchGroupMenuTree(GroupMenu groupMenu);

	List<RoleMenu> searchGroupMenus(GroupMenu groupMenu);

	int createGroupMenu(SaveGroupMenu createGroupMenu);

	int modifyGroupMenuAuth(GroupMenu groupMenu);

	int removeGroupMenu(SaveGroupMenu removeGroupMenu);
}
