package com.oneplat.oap.mgmt.common.navigation;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oneplat.oap.mgmt.login.model.LoginUser;

import lombok.Data;

@Controller
@RequestMapping("/navigation")
public class NavigationController {

	@Autowired
	NavigationMapper navigationMapper;

	@RequestMapping("/menu/management")
	public @ResponseBody List<NavigationMenu> getManagementMenuList(@AuthenticationPrincipal LoginUser loginUser) {
		return navigationMapper.selectMenuList(loginUser.getLoginUserNumber(), "MC_MENU_TYPE_01");
	}

	@RequestMapping("/menu/portal")
	public @ResponseBody List<NavigationMenu> getPortalMenuList(@AuthenticationPrincipal LoginUser loginUser) {
		return navigationMapper.selectMenuList(loginUser.getLoginUserNumber(), "MC_MENU_TYPE_02");
	}

	@Mapper
	public static interface NavigationMapper {

		@Results(id = "navigationMenu", value = { @Result(property = "id", column = "MENU_NUM"),
				@Result(property = "name", column = "MENU_NM"), @Result(property = "description", column = "MENU_DESC"),
				@Result(property = "state", column = "PAGE_URL"),
				@Result(property = "menuAuthCode", column = "MENU_AUTH_CD"),
				@Result(property = "level", column = "MENU_LEVEL"),
				@Result(property = "sortIndex", column = "SORT_NUM"),
				@Result(property = "authSetupYn", column = "AUTH_SETUP_YN"),
				@Result(property = "exposureYn", column = "EXPOSURE_YN"),
				@Result(property = "useYn", column = "USE_YN"),
				@Result(property = "parentId", column = "PARENT_MENU_NUM"), })
		@Select("SELECT MENU_NUM, MENU_NM, MENU_DESC, '' AS PAGE_URL, 0 AS MENU_LEVEL, 1 AS SORT_NUM, 'N' AS EXPOSURE_YN, 'Y' AS USE_YN, 0 AS PARENT_MENU_NUM, 'MC_MENU_AUTH_01' AS MENU_AUTH_CD "
				+ "FROM MC_MENU " + "WHERE MENU_LEVEL = 0 " + "AND MENU_TYPE_CD = #{menuTypeCode} " + "UNION "
				+ "SELECT MM.MENU_NUM, MM.MENU_NM, MM.MENU_DESC, MM.PAGE_URL, MMR.OPPONENT_MENU_LEVEL AS MENU_LEVEL, MMR.OPPONENT_SORT_NUM AS SORT_NUM, MM.EXPOSURE_YN, MM.USE_YN, MMR.CRITERIA_MENU_NUM AS PARENT_MENU_NUM, MAX(MRM.MENU_AUTH_CD) AS MENU_AUTH_CD "
				+ "FROM MC_MENU AS MM " + "INNER JOIN MC_MENU_RELATION AS MMR ON MM.MENU_NUM = MMR.OPPONENT_MENU_NUM "
				+ "INNER JOIN MC_ROLE_MENU AS MRM ON MM.MENU_NUM = MRM.MENU_NUM "
				+ "INNER JOIN MC_OPR_ROLE AS MOR ON MRM.ROLE_NUM = MOR.ROLE_NUM "
				+ "WHERE MOR.OPR_NUM = #{loginUserNumber} AND DELETE_YN = 'N' AND MMR.END_DATETIME > NOW(6) "
				+ "AND MRM.END_DATETIME > NOW(6) AND MOR.END_DATETIME > NOW(6) "
				+ "AND MM.MENU_TYPE_CD = #{menuTypeCode} " + "GROUP BY MM.MENU_NUM "
				+ "ORDER BY MENU_LEVEL ASC, SORT_NUM ASC")
		public List<NavigationMenu> selectMenuList(@Param("loginUserNumber") Long loginUserNumber,
				@Param("menuTypeCode") String menuTypeCode);

	}

	@Data
	public static class NavigationMenu {

		private Long id;
		private String name;
		private String description;
		private String state;
		private String menuAuthCode;
		private Integer level;
		private Integer sortIndex;
		private Boolean exposureYn;
		private Boolean useYn;
		private Long parentId;

	}

}