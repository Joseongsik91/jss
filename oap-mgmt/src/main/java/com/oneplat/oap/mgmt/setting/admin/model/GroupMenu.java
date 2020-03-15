package com.oneplat.oap.mgmt.setting.admin.model;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.oneplat.oap.core.model.AbstractObject;
import com.oneplat.oap.core.mybatis.enums.YesNoType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 메뉴/그룹 설정 모델
 *
 * @author
 * @date
 * @version
 */
@ApiModel(description = "메뉴/그룹 설정")
@EqualsAndHashCode(callSuper = false)
@Data
public class GroupMenu extends AbstractObject {

	@ApiModelProperty(value = "역할번호", required = true, example = "", allowableValues = "")
	private Long roleNumber;

	@ApiModelProperty(value = "메뉴 번호", required = true, example = "", allowableValues = "")
	private Long menuNumber;

	@ApiModelProperty(value = "메뉴권한코드", required = true, example = "MC_MENU_AUTH_01", allowableValues = "MC_MENU_AUTH_01, MC_MENU_AUTH_02")
	private String menuAuthCode;

	@ApiModelProperty(value = "메뉴 유형 코드", required = true)
	private String menuTypeCode;

	@ApiModelProperty(hidden = true, value = "검색범위")
	private boolean hasFlag;

	@ApiModel(description = "메뉴/그룹 트리 데이타 모델")
	@EqualsAndHashCode(callSuper = false)
	@NoArgsConstructor
	@Data
	public static class MenuTree extends GroupMenu {
		@ApiModelProperty(value = "메뉴 번호", required = true, example = "", allowableValues = "")
		private Long menuNumber;

		@ApiModelProperty(value = "메뉴명", required = true, example = "", allowableValues = "")
		private String menuName;

		@ApiModelProperty(value = "기준(상위) 메뉴 번호", required = false, example = "", allowableValues = "")
		private Long criteriaMenuNumber;

		@ApiModelProperty(value = "권한설정여부(Y:쓰기가능)", required = false, example = "", allowableValues = "")
		private YesNoType authSetupYn;

		@ApiModelProperty(value = "하위메뉴목록", required = false, example = "", allowableValues = "")
		private List<MenuTree> subMenuList;

	}

	@ApiModel(description = "메뉴/그룹 데이타 모델")
	@EqualsAndHashCode(callSuper = false)
	@NoArgsConstructor
	@Data
	public static class RoleMenu extends AbstractObject {

		@ApiModelProperty(value = "상위 메뉴 번호", required = false, example = "", allowableValues = "")
		private Long criteriaMenuNumber;

		@ApiModelProperty(value = "메뉴 번호", required = false, example = "", allowableValues = "")
		private Long menuNumber;

		@ApiModelProperty(value = "메뉴명", required = false, example = "", allowableValues = "")
		private String menuName;

		@ApiModelProperty(value = "메뉴 레벨", required = false, example = "", allowableValues = "")
		private int menuLevel;

		@ApiModelProperty(value = "권한설정여부(true:쓰기가능)", required = false, example = "", allowableValues = "")
		private Boolean authSetupYn;

		@ApiModelProperty(value = "노출 여부", required = false, example = "", allowableValues = "")
		private Boolean exposureYn;

		@ApiModelProperty(value = "역할번호", required = false, example = "", allowableValues = "")
		private Long roleNumber;

		@ApiModelProperty(value = "메뉴권한코드", required = false, example = "MC_MENU_AUTH_01", allowableValues = "MC_MENU_AUTH_01, MC_MENU_AUTH_02")
		private String menuAuthCode;

	}

	@ApiModel(description = "그룹메뉴권한 등록 모델")
	@EqualsAndHashCode(callSuper = false)
	@NoArgsConstructor
	@Data
	public static class SaveGroupMenu extends AbstractObject {
		@ApiModelProperty(value = "역할번호", required = true, example = "", allowableValues = "")
		@NotNull
		private Long roleNumber;

		@ApiModelProperty(value = "그룹메뉴권한 등록목록", required = true, example = "", allowableValues = "")
		@NotNull
		private List<GroupMenuSimple> groupMenuList;

		@ApiModelProperty(hidden = true)
		private String menuAuthCode;

	}

	@ApiModel(description = "그룹메뉴권한 모델")
	@EqualsAndHashCode(callSuper = false)
	@NoArgsConstructor
	@Data
	public static class GroupMenuSimple {
		@ApiModelProperty(value = "메뉴 번호", required = false, example = "", allowableValues = "")
		private Long menuNumber;

		@ApiModelProperty(value = "메뉴권한코드", required = false, example = "MC_MENU_AUTH_01", allowableValues = "MC_MENU_AUTH_01, MC_MENU_AUTH_02")
		private String menuAuthCode;

	}

}
