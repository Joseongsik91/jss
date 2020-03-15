package com.oneplat.oap.mgmt.setting.admin.service;

import java.util.List;
import com.oneplat.oap.mgmt.setting.admin.model.AdminMenu;
import com.oneplat.oap.mgmt.setting.admin.model.AdminMenu.AdminMenuRel;
import com.oneplat.oap.mgmt.setting.admin.model.AdminMenu.AdminMenuTree;
import com.oneplat.oap.mgmt.setting.admin.model.AdminMenu.CreateAdminMenu;
import com.oneplat.oap.mgmt.setting.admin.model.AdminMenu.DeleteAdminMenu;
import com.oneplat.oap.mgmt.setting.admin.model.AdminMenuRelation;

public interface AdminMenuService {
	public AdminMenuTree searchAdminMenuTree();

	public List<AdminMenuRel> searchAdminMenuList(String type);

	public AdminMenuRel searchAdminMenu(Long menuNum);

	public List<AdminMenuRelation> searchAdminCriteriaMenuList(Long menuNum);

	public AdminMenu createAdminMenu(CreateAdminMenu adminMenu);

	public void modifyAdminMenu(AdminMenu adminMenu);

	public void removeAdminMenu(DeleteAdminMenu adminMenu);

	public void modifyAdminMenuSort(AdminMenuRelation adminMenuRelation);
}
