package com.oneplat.oap.mgmt.policies.model;

import com.oneplat.oap.core.model.AbstractObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel(description = "Scope 관리 모델")
@EqualsAndHashCode(callSuper = false)
@Data
public class Scope extends AbstractObject {

	@ApiModelProperty(value = "Scope 번호", example = "123", allowableValues = "123")
	private Long scopeNumber;

	@ApiModelProperty(value = "Service 번호", example = "123", allowableValues = "123")
	private Long serviceNumber;

	@ApiModelProperty(value = "Service 이름", example = "Service", allowableValues = "Service")
	private String serviceName;

	@ApiModelProperty(value = "Scope명", example = "Scope", allowableValues = "Scope", dataType = "String")
	private String scopeName;

	@ApiModelProperty(value = "", example = "", allowableValues = "", dataType = "String")
	private String scopeContext;

	@ApiModelProperty(value = "", example = "", allowableValues = "", dataType = "String")
	private String parentContext;

	@ApiModelProperty(value = "icon", example = "icon", allowableValues = "icon", dataType = "String")
	private String iconFileChannel;

	@ApiModelProperty(value = "Scope 설명", example = "Scope", allowableValues = "Scope", dataType = "String")
	private String scopeDescription;

	@ApiModelProperty(value = "Scope 사용 여부")
	private Boolean scopeUseYn;

	@ApiModelProperty(value = "기준 Scope 번호", example = "1", allowableValues = "", dataType = "long")
	private Long criteriaScopeNumber;

	@ApiModelProperty(value = "상대 Scope 번호", example = "1", allowableValues = "", dataType = "long")
	private Long opponentScopeNumber;

	@ApiModelProperty(value = "기준 Scope 레벨", example = "1", allowableValues = "", dataType = "int")
	private int criteriaScopeLevel;

	@ApiModelProperty(value = "상대 Scope 레벨", example = "1", allowableValues = "", dataType = "int")
	private int opponentScopeLevel;

	@ApiModelProperty(value = "기준 정렬 번호", example = "1", allowableValues = "", dataType = "int")
	private int criteriaSortNumber;

	@ApiModelProperty(value = "상대 정렬 번호", example = "1", allowableValues = "", dataType = "long")
	private int opponentSortNumber;

}
