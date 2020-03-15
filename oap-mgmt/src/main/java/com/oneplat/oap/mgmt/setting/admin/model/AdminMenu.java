package com.oneplat.oap.mgmt.setting.admin.model;

import java.util.Date;
import java.util.List;

import com.oneplat.oap.core.model.AbstractObject;
import com.oneplat.oap.core.mybatis.enums.YesNoType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * AdminMenu 모델
 *
 * @author
 * @date
 * @version
 */
@ApiModel(description = "backoffice 메뉴 관리")
@EqualsAndHashCode(callSuper = false)
@Data
public class AdminMenu extends AbstractObject {

	@ApiModelProperty(value = "메뉴 번호", required = true, example = "123", allowableValues = "123")
	private Long menuNum;

	@ApiModelProperty(value = "메뉴명", required = true, example = "TEST", allowableValues = "TEST")
	private String menuNm;

	@ApiModelProperty(value = "메뉴 설명", required = false, example = "test desc", allowableValues = "test desc")
	private String menuDesc;

	@ApiModelProperty(value = "링크 URL", required = false, example = "", allowableValues = "")
	private String pageUrl;

	@ApiModelProperty(value = "메뉴 유형 코드", required = true)
	private String menuTypeCode;

	@ApiModelProperty(value = "메뉴 레벨", required = false, example = "", allowableValues = "")
	private int menuLevel;

	@ApiModelProperty(value = "정렬 번호", required = false, example = "", allowableValues = "")
	private int sortNum;

	@ApiModelProperty(value = "권한 설정 여부(N:Read,Y:Write)", required = false)
	private Boolean authSetupYn;

	@ApiModelProperty(value = "권한 설정 코드", required = false)
	private String menuAuthCode;

	@ApiModelProperty(value = "노출 여부", required = true)
	private Boolean exposureYn;

	@ApiModelProperty(value = "최하위 여부")
	private Boolean leafNodeYn;

	@ApiModelProperty(value = "삭제 여부")
	private Boolean deleteYn;

	@ApiModelProperty(value = "기준 메뉴 번호", required = true, example = "123", allowableValues = "123")
	private Long criteriaMenuNum;

	@ApiModel(description = "Admin menu rel포함 모델")
	@EqualsAndHashCode(callSuper = false)
	@Data
	public static class AdminMenuRel extends AdminMenu {

		@ApiModelProperty(value = "기준 메뉴 번호", required = true, example = "123", allowableValues = "", dataType = "long")
		private Long criteriaMenuNum;

		@ApiModelProperty(value = "상대 메뉴 번호", required = true, example = "124", allowableValues = "", dataType = "long")
		private Long opponentMenuNum;

		@ApiModelProperty(value = "상대 메뉴명", required = true, example = "메뉴메뉴", allowableValues = "메뉴명", dataType = "String")
		private String opponentMenuNm;

		@ApiModelProperty(value = "종료일시", required = true, example = "", allowableValues = "", dataType = "Date")
		private Date endDateTime;

		@ApiModelProperty(value = "기준 메뉴 레벨", required = false, example = "1", allowableValues = "", dataType = "int")
		private int criteriaMenuLevel;

		@ApiModelProperty(value = "상대 메뉴 레벨", required = false, example = "11", allowableValues = "", dataType = "int")
		private int opponentMenuLevel;

		@ApiModelProperty(value = "기준 정렬 번호", required = false, example = "1", allowableValues = "", dataType = "int")
		private int criteriaSortNum;

		@ApiModelProperty(value = "상대 정렬 번호", required = false, example = "1", allowableValues = "", dataType = "long")
		private int opponentSortNum;

		@ApiModelProperty(value = "등록자", required = false)
		private String createId;
		@ApiModelProperty(value = "수정자", required = false)
		private String modifyId;

	}

	@ApiModel(description = "Admin menu 등록 데이타 모델")
	@EqualsAndHashCode(callSuper = false)
	@Data
	public static class CreateAdminMenu extends AdminMenu {

		@ApiModelProperty(value = "기준 메뉴 번호", required = true, example = "123", allowableValues = "123")
		private Long criteriaMenuNum;
		@ApiModelProperty(value = "기준 메뉴 레벨", required = true, example = "123", allowableValues = "123")
		private int criteriaMenuLevel;
		@ApiModelProperty(hidden = true)
		private Long menuNum;
		@ApiModelProperty(hidden = true)
		private int sortNum;
	}

	@ApiModel(description = "Admin menu 삭제 데이타 모델")
	@EqualsAndHashCode(callSuper = false)
	@Data
	public static class DeleteAdminMenu extends AbstractObject {
		@ApiModelProperty(value = "기준 메뉴 번호", required = true, example = "123", allowableValues = "123")
		private Long criteriaMenuNum;

		@ApiModelProperty(value = "메뉴 번호", required = true, example = "123", allowableValues = "123")
		private Long menuNum;

	}

	@ApiModel(description = "Admin menu 정렬 데이타 모델")
	@EqualsAndHashCode(callSuper = false)
	@NoArgsConstructor
	@Data
	public static class AdminMenuSort extends AbstractObject {
		@ApiModelProperty(value = "기준 메뉴 번호", required = true, example = "123", allowableValues = "")
		private Long criteriaMenuNum;

		@ApiModelProperty(value = "상대 메뉴 번호", required = true, example = "124", allowableValues = "")
		private Long opponentMenuNum;

		@ApiModelProperty(value = "상대 정렬 번호", required = false, example = "1", allowableValues = "")
		private Long opponentSortNum;

		@ApiModelProperty(hidden = true, value = "삭제여부", required = false, example = "", allowableValues = "")
		private boolean deleteFlag;
	}

	@ApiModel(description = "Admin menu 트리 데이타 모델")
	@EqualsAndHashCode(callSuper = false)
	@NoArgsConstructor
	@Data
	public static class AdminMenuTree {
		@ApiModelProperty(value = "메뉴 번호", required = true, example = "", allowableValues = "")
		private Long menuNumber;

		@ApiModelProperty(value = "메뉴명", required = true, example = "", allowableValues = "")
		private String menuName;

		@ApiModelProperty(value = "기준(상위) 메뉴 번호", required = true, example = "", allowableValues = "")
		private Long criteriaMenuNumber;

		@ApiModelProperty(value = "사용여부", required = true, example = "", allowableValues = "")
		private YesNoType useYn;

		@ApiModelProperty(value = "하위메뉴목록", required = true, example = "", allowableValues = "")
		private List<AdminMenuTree> subMenuList;

		@ApiModelProperty(value = "자식 번호", required = false, example = "", allowableValues = "")
		private Long subOpponentMenuNum;

		@ApiModelProperty(value = "자식자식 번호", required = false, example = "", allowableValues = "")
		private Long subSubOpponentMenuNum;

		@ApiModelProperty(value = "자식자싲자식 번호", required = false, example = "", allowableValues = "")
		private Long subSubSubOpponentMenuNum;

		@ApiModelProperty(value = "자식자식자식 번호", required = false, example = "", allowableValues = "")
		private Long subSubSubSubOpponentMenuNum;

	}

}
