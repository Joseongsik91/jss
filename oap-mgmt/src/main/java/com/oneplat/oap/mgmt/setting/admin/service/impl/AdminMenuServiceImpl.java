package com.oneplat.oap.mgmt.setting.admin.service.impl;

import java.util.ArrayList;
import java.util.List;
import javax.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.oneplat.oap.mgmt.common.model.AuthenticationInjector;
import com.oneplat.oap.mgmt.setting.admin.mapper.AdminMenuMapper;
import com.oneplat.oap.mgmt.setting.admin.model.AdminMenu;
import com.oneplat.oap.mgmt.setting.admin.model.AdminMenu.AdminMenuRel;
import com.oneplat.oap.mgmt.setting.admin.model.AdminMenu.AdminMenuTree;
import com.oneplat.oap.mgmt.setting.admin.model.AdminMenu.CreateAdminMenu;
import com.oneplat.oap.mgmt.setting.admin.model.AdminMenu.DeleteAdminMenu;
import com.oneplat.oap.mgmt.setting.admin.model.AdminMenuDel;
import com.oneplat.oap.mgmt.setting.admin.model.AdminMenuRelation;
import com.oneplat.oap.mgmt.setting.admin.service.AdminMenuService;
import com.oneplat.oap.mgmt.setting.system.model.enums.StCommonCode;
import com.mysql.jdbc.StringUtils;

@Service
public class AdminMenuServiceImpl implements AdminMenuService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AdminMenuServiceImpl.class);

	@Autowired
	AdminMenuMapper adminMenuMapper;

	@Autowired
	AuthenticationInjector authenticationInjector;

	@Override
	public AdminMenuTree searchAdminMenuTree() {
		// TODO Auto-generated method stub
		return adminMenuMapper.selectRootAdminMenu();
	}

	@Override
	public AdminMenuRel searchAdminMenu(Long menuNum) {
		// TODO Auto-generated method stub
		return adminMenuMapper.selectAdminMenuDetail(menuNum);
	}

	@Override
	public List<AdminMenuRelation> searchAdminCriteriaMenuList(Long menuNum) {
		// TODO Auto-generated method stub
		return adminMenuMapper.selectAdminCriteriaMenuList(menuNum);
	}

	@Override
	@Transactional
	public AdminMenu createAdminMenu(CreateAdminMenu adminMenu) {
		// TODO validation
		// 기준level 존재여부
		authenticationInjector.setAuthentication(adminMenu);

		// 1. MC_menu INSERT
		Boolean authSetupYn = adminMenu.getAuthSetupYn();
		String menuAuthCode = adminMenu.getMenuAuthCode();
		if (authSetupYn == null) {
			authSetupYn = false;
			if (!StringUtils.isNullOrEmpty(menuAuthCode)) {
				authSetupYn = menuAuthCode.equals(StCommonCode.MC_MENU_AUTH_WRITE.getCode()) ? true : false;
			}
		}
		if (StringUtils.isNullOrEmpty(menuAuthCode)) {
			adminMenu.setMenuAuthCode(StCommonCode.MC_MENU_AUTH_READ.getCode());
			if (authSetupYn != null) {
				adminMenu.setMenuAuthCode(authSetupYn ? StCommonCode.MC_MENU_AUTH_WRITE.getCode()
						: StCommonCode.MC_MENU_AUTH_READ.getCode());
			}
		}
		adminMenuMapper.insertAdminMenu(adminMenu);
		Long menuNum = adminMenu.getMenuNum();

		// 이력테이블은 원본과 fair
		adminMenuMapper.insertAdmnMenuHist(menuNum);

		// 2. MC_menu_relation 최하위 sort INSERT
		AdminMenuRelation adminMenuRelation = new AdminMenuRelation();
		adminMenuRelation.setCriteriaMenuNum(adminMenu.getCriteriaMenuNum());
		adminMenuRelation.setCriteriaMenuLevel(adminMenu.getCriteriaMenuLevel());
		adminMenuRelation.setOpponentMenuNum(menuNum);
		adminMenuRelation.setOpponentMenuLevel(adminMenu.getCriteriaMenuLevel() + 1);
		adminMenuRelation.setCreateId(adminMenu.getCreateId());
		adminMenuRelation.setModifyId(adminMenu.getModifyId());
		adminMenuMapper.insertAdmnMenuRelation(adminMenuRelation);

		// 상위메뉴의 leafNode를 변경
		Long criteriaMenuNum = adminMenu.getCriteriaMenuNum();
		AdminMenu criteriaMenu = new AdminMenu();
		authenticationInjector.setAuthentication(criteriaMenu);
		criteriaMenu.setLeafNodeYn(false);
		criteriaMenu.setMenuNum(criteriaMenuNum);
		adminMenuMapper.updateAdminMenuLeafNode(criteriaMenu);

		// 이력테이블은 원본과 fair
		adminMenuMapper.updateAdmnMenuHist(criteriaMenu);
		adminMenuMapper.insertAdmnMenuHist(criteriaMenuNum);

		// 등록건 재조회
		AdminMenu newAdminMenu = adminMenuMapper.selectAdminMenuDetail(adminMenu.getMenuNum());
		return newAdminMenu;
	}

	@Override
	@Transactional
	public void modifyAdminMenu(AdminMenu adminMenu) {
		// TODO validation
		authenticationInjector.setAuthentication(adminMenu);

		Long menuNum = adminMenu.getMenuNum();

		// 삭제여부 확인
		AdminMenu adminMenuInfo = adminMenuMapper.selectAdminMenuDetail(menuNum);

		if (adminMenuInfo != null && !adminMenuInfo.getDeleteYn()) {

			Boolean authSetupYn = adminMenu.getAuthSetupYn();
			String menuAuthCode = adminMenu.getMenuAuthCode();
			if (authSetupYn == null) {
				adminMenu.setAuthSetupYn(adminMenuInfo.getAuthSetupYn());
				if (!StringUtils.isNullOrEmpty(menuAuthCode)) {
					adminMenu.setAuthSetupYn(
							menuAuthCode.equals(StCommonCode.MC_MENU_AUTH_WRITE.getCode()) ? true : false);
				}
			}
			if (StringUtils.isNullOrEmpty(menuAuthCode)) {
				adminMenu.setMenuAuthCode(adminMenuInfo.getMenuAuthCode());
				if (authSetupYn != null) {
					adminMenu.setMenuAuthCode(authSetupYn ? StCommonCode.MC_MENU_AUTH_WRITE.getCode()
							: StCommonCode.MC_MENU_AUTH_READ.getCode());
				}
			}

			// 3. MC_menu UPDATE(DELETE_YN에 따라 분기시킬까)
			if (adminMenu.getDeleteYn() != null && adminMenu.getDeleteYn()) {
				adminMenuMapper.deleteAdminMenu(adminMenu);
			} else {
				adminMenuMapper.updateAdminMenu(adminMenu);
			}

			// 이력테이블은 원본과 fair로 관리
			// 1. MC_menu_hist UPDATE
			adminMenuMapper.updateAdmnMenuHist(adminMenu);

			// 2. MC_menu_hist INSERT
			adminMenuMapper.insertAdmnMenuHist(menuNum);
		}
		/*
		 * else if (adminMenuInfo == null || adminMenuInfo.getDeleteYn()) { //
		 * error 처리 throw new NotFoundException(); }
		 */
	}

	@Override
	@Transactional
	public void removeAdminMenu(DeleteAdminMenu deleteModel) {
		// TODO validation
		authenticationInjector.setAuthentication(deleteModel);

		// validation check
		// [BP_SET_02001] 7-2 사용중인 하위메뉴가 존재하면 error처리함
		if (adminMenuMapper.getIsAbleSubMenuCount(deleteModel.getMenuNum()) > 0) {
			// TODO 오류메세지 설정
			throw new ValidationException();
		}

		// 1. TODO 삭제대상건의 rel 정보 조회하여 일괄 삭제를 할지 하위가 존재하면 삭제하지 못하게 할지
		// => 화면에서 script로 tree를 구현할때 node의 useYn, deleteYn으로 트리를 생성하므로 하위 node의
		// 상태를 바꾸지 않아도 된다
		List<AdminMenuDel> childrensList = adminMenuMapper.selectChildrensMenu(deleteModel);

		for (int i = 0; childrensList.size() > i; i++) {

			if (childrensList.get(i).getSubSubSubSubOpponentMenuNum() > 0) {
				AdminMenu adminSubSubSubSubMenu = new AdminMenu();
				adminSubSubSubSubMenu.setMenuNum(childrensList.get(i).getSubSubSubSubOpponentMenuNum());
				adminSubSubSubSubMenu.setDeleteYn(true);
				adminSubSubSubSubMenu.setCreateId(deleteModel.getCreateId());
				adminSubSubSubSubMenu.setModifyId(deleteModel.getModifyId());
				modifyAdminMenu(adminSubSubSubSubMenu);
			}

			if (childrensList.get(i).getSubSubSubOpponentMenuNum() > 0) {
				AdminMenu adminSubSubSubMenu = new AdminMenu();
				adminSubSubSubMenu.setMenuNum(childrensList.get(i).getSubSubSubOpponentMenuNum());
				adminSubSubSubMenu.setDeleteYn(true);
				adminSubSubSubMenu.setCreateId(deleteModel.getCreateId());
				adminSubSubSubMenu.setModifyId(deleteModel.getModifyId());
				modifyAdminMenu(adminSubSubSubMenu);
			}

			if (childrensList.get(i).getSubSubOpponentMenuNum() > 0) {
				AdminMenu adminSubSubMenu = new AdminMenu();
				adminSubSubMenu.setMenuNum(childrensList.get(i).getSubSubOpponentMenuNum());
				adminSubSubMenu.setDeleteYn(true);
				adminSubSubMenu.setCreateId(deleteModel.getCreateId());
				adminSubSubMenu.setModifyId(deleteModel.getModifyId());
				modifyAdminMenu(adminSubSubMenu);
			}

			if (childrensList.get(i).getSubOpponentMenuNum() > 0) {
				AdminMenu adminSubMenu = new AdminMenu();
				adminSubMenu.setMenuNum(childrensList.get(i).getSubOpponentMenuNum());
				adminSubMenu.setDeleteYn(true);
				adminSubMenu.setCreateId(deleteModel.getCreateId());
				adminSubMenu.setModifyId(deleteModel.getModifyId());
				modifyAdminMenu(adminSubMenu);
			}

		}

		// 2. call modifyAdminMenu()
		AdminMenu adminMenu = new AdminMenu();
		adminMenu.setMenuNum(deleteModel.getMenuNum());
		adminMenu.setDeleteYn(true);
		adminMenu.setCreateId(deleteModel.getCreateId());
		adminMenu.setModifyId(deleteModel.getModifyId());
		modifyAdminMenu(adminMenu);

		// 3. re sort relation
		Long criteriaMenuNum = deleteModel.getCriteriaMenuNum();

		AdminMenuRelation adminMenuRelation = new AdminMenuRelation();
		adminMenuRelation.setCriteriaMenuNum(criteriaMenuNum);
		adminMenuRelation.setOpponentMenuNum(deleteModel.getMenuNum());
		adminMenuRelation.setDeleteFlag(true);
		modifyAdminMenuSort(adminMenuRelation);

		// 상위메뉴의 leafNode를 변경
		if (adminMenuMapper.getIsAbleSubMenuCount(criteriaMenuNum) == 0) {
			AdminMenu criteriaMenu = new AdminMenu();
			authenticationInjector.setAuthentication(criteriaMenu);
			criteriaMenu.setLeafNodeYn(true);
			criteriaMenu.setMenuNum(criteriaMenuNum);
			adminMenuMapper.updateAdminMenuLeafNode(criteriaMenu);

			// 이력테이블은 원본과 fair로 관리
			// 1. MC_menu_hist UPDATE
			adminMenuMapper.updateAdmnMenuHist(criteriaMenu);

			// 2. MC_menu_hist INSERT
			adminMenuMapper.insertAdmnMenuHist(criteriaMenuNum);
		}
	}

	/**
	 * 메뉴 재정렬 service 메뉴는 동일 level에서만 재정렬이 가능함
	 **/
	@Override
	@Transactional
	public void modifyAdminMenuSort(AdminMenuRelation adminMenuRelation) {
		// TODO validation

		int addSort = 0;

		List<AdminMenuRelation> list = adminMenuMapper.getAdminMenuSortList(adminMenuRelation);
		List<AdminMenuRelation> menuList = new ArrayList<AdminMenuRelation>();
		Long sortNumber = null;

		if (!adminMenuRelation.isDeleteFlag()) {
			AdminMenuRelation target = adminMenuMapper.getTargetSortMenu(adminMenuRelation);
			target.setOpponentSortNum(adminMenuRelation.getOpponentSortNum());
			menuList.add(target);
			sortNumber = Long.parseLong(adminMenuRelation.getOpponentSortNum() + "");
		}

		for (int i = 0; i < list.size(); i++) {
			AdminMenuRelation relation = list.get(i);

			if (!adminMenuRelation.isDeleteFlag() && sortNumber.compareTo(Long.parseLong((i + 1) + "")) == 0) {
				addSort = 1;
			}
			relation.setOpponentSortNum(i + 1 + addSort);
			menuList.add(relation);
		}

		AdminMenu adminMenu;
		for (AdminMenuRelation buf : menuList) {
			authenticationInjector.setAuthentication(buf);
			// MC_MENU_RELATION 수정 및 등록
			adminMenuMapper.updateAdminMenuSortRelation(buf);
			adminMenuMapper.insertAdmnMenuRelation(buf);
			// MC_MENU 정렬번호 수정
			adminMenuMapper.updateAdminMenuSort(buf);

			adminMenu = new AdminMenu();
			adminMenu.setMenuNum(buf.getOpponentMenuNum());
			adminMenu.setModifyId(buf.getModifyId());
			adminMenuMapper.updateAdmnMenuHist(adminMenu);
			adminMenuMapper.insertAdmnMenuHist(adminMenu.getMenuNum());
		}
	}

	@Override
	public List<AdminMenuRel> searchAdminMenuList(String type) {
		// TODO Auto-generated method stub
		return adminMenuMapper.selectAdminMenuList(type);
	}

}
