package com.oneplat.oap.mgmt.setting.admin.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import com.oneplat.oap.core.exception.NotFoundException;
import com.oneplat.oap.core.exception.ValidationException;
import com.oneplat.oap.core.model.SearchRequest;
import com.oneplat.oap.mgmt.common.model.AuthenticationInjector;
import com.oneplat.oap.mgmt.setting.admin.mapper.GroupManagementMapper;
import com.oneplat.oap.mgmt.setting.admin.model.AdminMenu;
import com.oneplat.oap.mgmt.setting.admin.model.OperatorRole;
import com.oneplat.oap.mgmt.setting.admin.model.OperatorRole.SaveOperatorRole;
import com.oneplat.oap.mgmt.setting.admin.model.Role;
import com.oneplat.oap.mgmt.setting.admin.model.AdminMenu.AdminMenuSort;
import com.oneplat.oap.mgmt.setting.admin.model.Role.CreateRole;
import com.oneplat.oap.mgmt.setting.admin.model.Role.ModifyRole;
import com.oneplat.oap.mgmt.setting.admin.model.Role.RoleOpr;
import com.oneplat.oap.mgmt.setting.admin.model.Role.RolePersonInfo;
import com.oneplat.oap.mgmt.setting.admin.model.Role.RoleSearch;
import com.oneplat.oap.mgmt.setting.admin.service.GroupManagementService;

@Service
public class GroupManagementServiceImpl implements GroupManagementService {

	@Autowired
	GroupManagementMapper groupManagementMapper;

	@Autowired
	AuthenticationInjector authenticationInjector;

	@Override
	public List<Role> searchRoleList(SearchRequest searchRequest) {
		searchRequest.setData();
		return groupManagementMapper.selectRoleList(searchRequest);
	}

	@Override
	public Role searchRole(Long roleNumber) {
		return groupManagementMapper.selectRole(roleNumber);
	}
	
	//업무 목록중에서 미사용 제외
	@Override
	public List<Role> searchUseRoleList(SearchRequest searchRequest) {
		searchRequest.setData();
		return groupManagementMapper.selectUseRoleList(searchRequest);
	}
	

	@Override
	public int checkDuplication(SearchRequest searchRequest) {
		// TODO Auto-generated method stub
		searchRequest.setData();
		return groupManagementMapper.checkDuplication(searchRequest);
	}

	@Override
	@Transactional
	public Long createRole(CreateRole role) {
		authenticationInjector.setAuthentication(role);

		// 중복확인은 @UniqueName 이용
		RoleSearch validationCheckModel = null;
		if (!StringUtils.isEmpty(role.getRoleCode())) {
			validationCheckModel = new RoleSearch();
			validationCheckModel.setRoleCode(role.getRoleCode());
			if (groupManagementMapper.getRoleCount(validationCheckModel) > 0)
				throw new ValidationException();
		}
		if (!StringUtils.isEmpty(role.getRoleName())) {
			validationCheckModel = new RoleSearch();
			validationCheckModel.setRoleName(role.getRoleName());
			if (groupManagementMapper.getRoleCount(validationCheckModel) > 0)
				throw new ValidationException();
		}

		groupManagementMapper.insertRole(role);
		Long roleNumber = role.getRoleNumber();
		String createId = role.getCreateId();

		// 이력은 원본과 fair로 관리
		groupManagementMapper.insertRoleHist(role);

		/*
		 * // 취급여부 Boolean isCustomerInfoTreat = role.getCustomerInfoTreatYn();
		 * Boolean isSellerInfoTreat = role.getSellerInfoTreatYn();
		 * 
		 * List<RolePersonInfo> rolePersonInfoList = new
		 * ArrayList<RolePersonInfo>(); List<RolePersonInfo> checkedList1 =
		 * role.getCustomPersonInfoCheckedList(); List<RolePersonInfo>
		 * checkedList2 = role.getSellerPersonInfoCheckedList();
		 * List<RolePersonInfo> checkedList3 =
		 * role.getManagerPersonInfoCheckedList(); List<String>
		 * personInfoCheckedList = role.getPersonInfoCheckedList();
		 * 
		 * if (personInfoCheckedList != null) { if (checkedList1 == null)
		 * checkedList1 = new ArrayList<RolePersonInfo>(); if (checkedList2 ==
		 * null) checkedList2 = new ArrayList<RolePersonInfo>(); if
		 * (checkedList3 == null) checkedList3 = new
		 * ArrayList<RolePersonInfo>(); for (String roleCode :
		 * personInfoCheckedList) { RolePersonInfo info = new RolePersonInfo();
		 * info.setPersonInfoTreatCode(roleCode); if
		 * (roleCode.indexOf("MC_PERSON_INFO_SECTION_01") > -1) {
		 * info.setPersonInfoSectionCode("MC_PERSON_INFO_SECTION_01");
		 * checkedList1.add(info); } if
		 * (roleCode.indexOf("MC_PERSON_INFO_SECTION_02") > -1) {
		 * info.setPersonInfoSectionCode("MC_PERSON_INFO_SECTION_02");
		 * checkedList2.add(info); } if
		 * (roleCode.indexOf("MC_PERSON_INFO_SECTION_03") > -1) {
		 * info.setPersonInfoSectionCode("MC_PERSON_INFO_SECTION_03");
		 * checkedList3.add(info); } } }
		 * 
		 * if (isCustomerInfoTreat && checkedList1 != null) { for
		 * (RolePersonInfo info : checkedList1) { if
		 * (!StringUtils.isEmpty(info.getPersonInfoSectionCode()) &&
		 * !StringUtils.isEmpty(info.getPersonInfoTreatCode())) {
		 * info.setRoleNumber(roleNumber); info.setCreateId(createId);
		 * rolePersonInfoList.add(info); } } }
		 * 
		 * if (isSellerInfoTreat) { if (checkedList2 != null) { for
		 * (RolePersonInfo info : checkedList2) { if
		 * (!StringUtils.isEmpty(info.getPersonInfoSectionCode()) &&
		 * !StringUtils.isEmpty(info.getPersonInfoTreatCode())) {
		 * info.setRoleNumber(roleNumber); info.setCreateId(createId);
		 * rolePersonInfoList.add(info); } } } if (checkedList3 != null) { for
		 * (RolePersonInfo info : checkedList3) { if
		 * (!StringUtils.isEmpty(info.getPersonInfoSectionCode()) &&
		 * !StringUtils.isEmpty(info.getPersonInfoTreatCode())) {
		 * info.setRoleNumber(roleNumber); info.setCreateId(createId);
		 * rolePersonInfoList.add(info); } } } } // 개인정보취급 목록 저장 if
		 * (rolePersonInfoList != null && rolePersonInfoList.size() > 0) {
		 * groupManagementMapper.insertRolePersonInfoList(rolePersonInfoList); }
		 */

		return roleNumber;
	}

	@Override
	@Transactional
	public Long modifyRole(ModifyRole role) {
		authenticationInjector.setAuthentication(role);

		Long roleNumber = role.getRoleNumber();
		String createId = role.getCreateId();

		if (groupManagementMapper.selectRole(roleNumber) == null) {
			throw new NotFoundException();
		}

		/*
		 * RoleSearch validationCheckModel = null; if
		 * (!StringUtils.isEmpty(role.getOldRoleCode())) { validationCheckModel
		 * = new RoleSearch();
		 * validationCheckModel.setRoleCode(role.getRoleCode()); if
		 * (groupManagementMapper.getRoleCount(validationCheckModel) > 0) {
		 * throw new ValidationException(); } } if
		 * (!StringUtils.isEmpty(role.getOldRoleName())) { validationCheckModel
		 * = new RoleSearch();
		 * validationCheckModel.setRoleName(role.getRoleName()); if
		 * (groupManagementMapper.getRoleCount(validationCheckModel) > 0) {
		 * throw new ValidationException(); } }
		 */

		groupManagementMapper.updateRole(role);

		// 이력은 원본테이블과 fair로 관리
		groupManagementMapper.updateRoleHist(role);
		groupManagementMapper.insertRoleHist(role);

		/*
		 * // 취급여부 Boolean isCustomerInfoTreat = role.getCustomerInfoTreatYn();
		 * Boolean isSellerInfoTreat = role.getSellerInfoTreatYn();
		 * 
		 * List<RolePersonInfo> rolePersonInfoList = new
		 * ArrayList<RolePersonInfo>(); List<RolePersonInfo> checkedList1 =
		 * role.getCustomPersonInfoCheckedList(); List<RolePersonInfo>
		 * checkedList2 = role.getSellerPersonInfoCheckedList();
		 * List<RolePersonInfo> checkedList3 =
		 * role.getManagerPersonInfoCheckedList(); List<String>
		 * personInfoCheckedList = role.getPersonInfoCheckedList();
		 * 
		 * if (personInfoCheckedList != null) { if (checkedList1 == null)
		 * checkedList1 = new ArrayList<RolePersonInfo>(); if (checkedList2 ==
		 * null) checkedList2 = new ArrayList<RolePersonInfo>(); if
		 * (checkedList3 == null) checkedList3 = new
		 * ArrayList<RolePersonInfo>(); for (String roleCode :
		 * personInfoCheckedList) { RolePersonInfo info = new RolePersonInfo();
		 * info.setPersonInfoTreatCode(roleCode); if
		 * (roleCode.indexOf("MC_PERSON_INFO_SECTION_01") > -1) {
		 * info.setPersonInfoSectionCode("MC_PERSON_INFO_SECTION_01");
		 * checkedList1.add(info); } if
		 * (roleCode.indexOf("MC_PERSON_INFO_SECTION_02") > -1) {
		 * info.setPersonInfoSectionCode("MC_PERSON_INFO_SECTION_02");
		 * checkedList2.add(info); } if
		 * (roleCode.indexOf("MC_PERSON_INFO_SECTION_03") > -1) {
		 * info.setPersonInfoSectionCode("MC_PERSON_INFO_SECTION_03");
		 * checkedList3.add(info); } } }
		 * 
		 * // 취급가능일 경우에만 if (isCustomerInfoTreat && checkedList1 != null) { for
		 * (RolePersonInfo info : checkedList1) { if
		 * (!StringUtils.isEmpty(info.getPersonInfoSectionCode()) &&
		 * !StringUtils.isEmpty(info.getPersonInfoTreatCode())) {
		 * info.setRoleNumber(roleNumber); info.setCreateId(createId);
		 * rolePersonInfoList.add(info); } } }
		 * 
		 * // 취급가능일 경우에만 if (isSellerInfoTreat) { if (checkedList2 != null) {
		 * for (RolePersonInfo info : checkedList2) { if
		 * (!StringUtils.isEmpty(info.getPersonInfoSectionCode()) &&
		 * !StringUtils.isEmpty(info.getPersonInfoTreatCode())) {
		 * info.setRoleNumber(roleNumber); info.setCreateId(createId);
		 * rolePersonInfoList.add(info); } } } if (checkedList3 != null) { for
		 * (RolePersonInfo info : checkedList3) { if
		 * (!StringUtils.isEmpty(info.getPersonInfoSectionCode()) &&
		 * !StringUtils.isEmpty(info.getPersonInfoTreatCode())) {
		 * info.setRoleNumber(roleNumber); info.setCreateId(createId);
		 * rolePersonInfoList.add(info); } } } }
		 * 
		 * // 개인정보취급코드의 변경 이력 업데이트건이 있을 경우 등록 if (rolePersonInfoList != null &&
		 * rolePersonInfoList.size() > 0) { // 조건에 ON DUPLICATE KEY UPDATE 가
		 * 추가되어 데이타가 있는 경우 갱신되지 않는다 count +=
		 * groupManagementMapper.updateRolePersonInfoOldList(role); count +=
		 * groupManagementMapper.insertRolePersonInfoList(rolePersonInfoList); }
		 * else { groupManagementMapper.deleteRolePersonInfo(role); }
		 */
		return roleNumber;
	}

	@Override
	@Transactional
	public void deleteRole(Long roleNumber) {
		Role role = new Role();
		role.setRoleNumber(roleNumber);
		authenticationInjector.setAuthentication(role);

		Role targetRole = groupManagementMapper.selectRole(roleNumber);
		if (targetRole == null) {
			throw new NotFoundException();
		} else {
			int oprCount = groupManagementMapper.selectRoleOprCount(roleNumber);
			if (oprCount > 0) {
				throw new ValidationException();
			}

			groupManagementMapper.deleteRole(role);

			// 이력테이블은 원본과 fair로 관리
			groupManagementMapper.updateRoleHist(role);
			role.setCreateId(role.getModifyId());
			groupManagementMapper.insertRoleHist(role);

			// groupManagementMapper.deleteRolePersonInfo(role);

			role.setDelete(true);
			modifyRoleSort(role);
		}
	}

	@Override
	@Transactional
	public void modifyRoleSort(Role role) {
		authenticationInjector.setAuthentication(role);

		int addSort = 0;

		List<Role> list = groupManagementMapper.getRoleSortList(role);
		List<Role> roleList = new ArrayList<Role>();

		if (!role.isDelete())
			roleList.add(role);

		for (int i = 0; i < list.size(); i++) {
			Role roleInfo = list.get(i);

			if (!role.isDelete() && role.getSortNumber().compareTo(Long.parseLong((i + 1) + "")) == 0) {
				addSort = 1;
			}
			roleInfo.setSortNumber(Long.parseLong((i + 1 + addSort) + ""));
			roleList.add(roleInfo);
		}

		for (Role buf : roleList) {
			authenticationInjector.setAuthentication(buf);
			buf.setCreateId(role.getModifyId());
			groupManagementMapper.updateSortNumber(buf);
			groupManagementMapper.updateRoleHist(buf);
			groupManagementMapper.insertRoleHist(buf);
		}
	}

	@Override
	public RoleOpr selectRoleOpr(Long roleNumber) {
		// TODO Auto-generated method stub
		return groupManagementMapper.selectRoleOpr(roleNumber);
	}

	@Override
	public int selectRoleOprCount(Long roleNumber) {
		// TODO Auto-generated method stub
		return groupManagementMapper.selectRoleOprCount(roleNumber);
	}

	@Override
	public List<OperatorRole> selectOprRoleSearchList(SearchRequest searchRequest) {
		// TODO Auto-generated method stub
		searchRequest.setData();
		return groupManagementMapper.selectOprRolesSearchList(searchRequest);
	}

	@Override
	public int createRoleOpr(SaveOperatorRole saveOprRole) {
		authenticationInjector.setAuthentication(saveOprRole);

		// TODO Auto-generated method stub
		SearchRequest searchRequest = new SearchRequest();
		HashMap<String, Object> query = new HashMap<String, Object>();
		query.put("searchType", saveOprRole.getRoleNumber());
		query.put("searchList", saveOprRole.getOprNumberList());

		searchRequest.setQuery(query);
		List<OperatorRole> list = groupManagementMapper.selectOprRoleSearchList(searchRequest);
		if (list != null && list.size() > 0) {
			StringBuffer sb = new StringBuffer();
			for (OperatorRole role : list) {
				sb.append(sb.length() > 0 ? "," : "(");
				sb.append(role.getOprName());
			}
			sb.append(sb.length() > 0 ? ")" : "");
			throw new DuplicateKeyException("이미 추가된 담당자입니다." + (sb.toString()));
		}

		int resultCount = groupManagementMapper.updateOprRoleList(saveOprRole);
		// FK 설정이 되어있으므로 MC_ROLE에서 SELECT를 한 후 INSERT하게하여 SQL error를 발생하지 않도록함
		resultCount += groupManagementMapper.insertOprRoleForOprList(saveOprRole);
		return resultCount;
	}

	@Override
	public int removeOprRole(SaveOperatorRole saveOprRole) {
		authenticationInjector.setAuthentication(saveOprRole);

		// TODO Auto-generated method stub
		if (saveOprRole.getOprNumberList() == null || saveOprRole.getOprNumberList().size() == 0) {
			throw new NotFoundException();
		}
		return groupManagementMapper.updateOprRoleList(saveOprRole);
	}

}
