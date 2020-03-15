package com.oneplat.oap.mgmt.setting.admin.model;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.oneplat.oap.core.model.AbstractObject;
import com.oneplat.oap.core.model.ValidationCreate;
import com.oneplat.oap.core.validation.annotation.UniqueName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel(description = "역할 모델")
@EqualsAndHashCode(callSuper = false)
@Data
public class Role extends AbstractObject {

	@ApiModelProperty(value = "역할번호", required = true, example = "", allowableValues = "")
	private Long roleNumber;

	@ApiModelProperty(value = "역할명", required = true, example = "", allowableValues = "")
	private String roleName;

	@ApiModelProperty(value = "역할코드", required = true, example = "", allowableValues = "")
	private String roleCode;

	@ApiModelProperty(value = "역할설명", required = false, example = "", allowableValues = "")
	private String roleDesc;

	@ApiModelProperty(value = "정렬번호", required = false, example = "", allowableValues = "")
	private Long sortNumber;

	@ApiModelProperty(value = "고객정보취급여부", required = false, example = "", allowableValues = "")
	private Boolean customerInfoTreatYn;

	@ApiModelProperty(value = "판매자정보취급여부", required = false, example = "", allowableValues = "")
	private Boolean sellerInfoTreatYn;

	@ApiModelProperty(value = "개인정보취급항목 목록", required = false, example = "", allowableValues = "")
	private List<RolePersonInfo> rolePersonInfoList;

	@ApiModelProperty(value = "등록자", required = false, example = "", allowableValues = "")
	private String createId;

	@ApiModelProperty(value = "수정자", required = false, example = "", allowableValues = "")
	private String modifyId;

	private boolean isDelete;

	@ApiModel(description = "역할 개인정보 모델")
	@EqualsAndHashCode(callSuper = false)
	@Data
	public static class RoleSearch {
		@ApiModelProperty(hidden = true, value = "역할번호", required = false, example = "", allowableValues = "")
		private Long roleNumber;

		@ApiModelProperty(value = "역할명", required = false, example = "", allowableValues = "")
		private String roleName;

		@ApiModelProperty(value = "역할코드", required = false, example = "", allowableValues = "")
		private String roleCode;
	}

	@ApiModel(description = "역할 등록 모델")
	@EqualsAndHashCode(callSuper = false)
	@Data
	public static class CreateRole extends Role {
		@ApiModelProperty(value = "개인정보취급목록", required = false, example = "", allowableValues = "")
		private List<String> personInfoCheckedList;

		@ApiModelProperty(value = "고객개인정보취급목록", required = false, example = "", allowableValues = "")
		private List<RolePersonInfo> customPersonInfoCheckedList;

		@ApiModelProperty(value = "판매자개인정보취급목록", required = false, example = "", allowableValues = "")
		private List<RolePersonInfo> sellerPersonInfoCheckedList;

		@ApiModelProperty(value = "담당자개인정보취급목록", required = false, example = "", allowableValues = "")
		private List<RolePersonInfo> managerPersonInfoCheckedList;
	}

	@ApiModel(description = "역할 수정 모델")
	@EqualsAndHashCode(callSuper = false)
	@Data
	public static class ModifyRole extends Role {
		@ApiModelProperty(value = "역할명", required = true, example = "", allowableValues = "")
		@UniqueName(columnName = "ROLE_NM", tableName = "kg_uppp.MC_ROLE", groups = { ValidationCreate.class })
		private String roleName;

		@ApiModelProperty(value = "역할코드", required = true, example = "", allowableValues = "")
		@UniqueName(columnName = "ROLE_CD", tableName = "kg_uppp.MC_ROLE", groups = { ValidationCreate.class })
		private String roleCode;

		@ApiModelProperty(value = "역할코드", required = false, example = "", allowableValues = "")
		private String oldRoleCode;

		@ApiModelProperty(value = "역할코드명", required = false, example = "", allowableValues = "")
		private String oldRoleName;

		@ApiModelProperty(value = "개인정보취급목록", required = false, example = "", allowableValues = "")
		private List<String> personInfoCheckedList;

		@ApiModelProperty(value = "고객개인정보취급목록", required = false, example = "", allowableValues = "")
		private List<RolePersonInfo> customPersonInfoCheckedList;

		@ApiModelProperty(value = "판매자개인정보취급목록", required = false, example = "", allowableValues = "")
		private List<RolePersonInfo> sellerPersonInfoCheckedList;

		@ApiModelProperty(value = "담당자개인정보취급목록", required = false, example = "", allowableValues = "")
		private List<RolePersonInfo> managerPersonInfoCheckedList;

	}

	@ApiModel(description = "역할 개인정보 모델")
	@EqualsAndHashCode(callSuper = false)
	@Data
	public static class RolePersonInfo extends AbstractObject {
		@ApiModelProperty(value = "역할번호", required = true, example = "", allowableValues = "")
		@NotNull
		private Long roleNumber;

		@ApiModelProperty(value = "개인정보 구분코드", required = true, example = "", allowableValues = "")
		@NotNull
		private String personInfoSectionCode;

		@ApiModelProperty(value = "개인정보 취급코드", required = true, example = "", allowableValues = "")
		@NotNull
		private String personInfoTreatCode;
	}

	@ApiModel(description = "역할 운영자 포함 모델")
	@EqualsAndHashCode(callSuper = false)
	@Data
	public static class RoleOpr extends Role {
		@ApiModelProperty(value = "운영자수", required = false, example = "", allowableValues = "")
		private int oprCount;

		@ApiModelProperty(value = "운영자 목록", required = false, example = "", allowableValues = "")
		private List<OperatorRole> oprList;
	}

}