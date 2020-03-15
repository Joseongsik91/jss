package com.oneplat.oap.mgmt.setting.admin.service;

import java.util.List;
import com.oneplat.oap.core.model.SearchRequest;
import com.oneplat.oap.mgmt.setting.admin.model.OperatorRole;
import com.oneplat.oap.mgmt.setting.admin.model.OperatorRole.SaveOperatorRole;
import com.oneplat.oap.mgmt.setting.admin.model.Role;
import com.oneplat.oap.mgmt.setting.admin.model.Role.CreateRole;
import com.oneplat.oap.mgmt.setting.admin.model.Role.ModifyRole;
import com.oneplat.oap.mgmt.setting.admin.model.Role.RoleOpr;

public interface GroupManagementService {

	List<Role> searchRoleList(SearchRequest searchrequest);
	
	//업무 목록중에서 미사용 제외
	List<Role> searchUseRoleList(SearchRequest searchrequest);

	Role searchRole(Long roleNumber);

	int checkDuplication(SearchRequest searchRequest);

	Long createRole(CreateRole role);

	Long modifyRole(ModifyRole modifyrole);

	void deleteRole(Long roleNumber);

	void modifyRoleSort(Role role);

	RoleOpr selectRoleOpr(Long roleNumber);

	int selectRoleOprCount(Long roleNumber);

	List<OperatorRole> selectOprRoleSearchList(SearchRequest searchRequest);

	int createRoleOpr(SaveOperatorRole createOprRole);

	int removeOprRole(SaveOperatorRole deleteOprRole);

}
