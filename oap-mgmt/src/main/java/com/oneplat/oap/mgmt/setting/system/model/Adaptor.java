package com.oneplat.oap.mgmt.setting.system.model;

import com.oneplat.oap.core.model.AbstractObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel(description = "어댑터 모델")
@EqualsAndHashCode(callSuper = false)
@Data
public class Adaptor extends AbstractObject {

	@ApiModelProperty(value = "어댑터 번호", required = true, example = "1", allowableValues = "1")
	private Long adaptorNumber;

	@ApiModelProperty(value = "어댑터 빈 아이디", required = true, example = "TEST", allowableValues = "TEST")
	private String adaptorBeanId;

	@ApiModelProperty(value = "어댑터 설명", required = false, example = "TEST", allowableValues = "TEST")
	private String adaptorDescribe;

	@ApiModelProperty(value = "SAA URI", required = true, example = "TEST", allowableValues = "TEST")
	private String saaUri;

	@ApiModelProperty(value = "어댑터 프로토콜 코드", required = true, example = "TEST", allowableValues = "TEST")
	private String adaptorProtocolCode;

	@ApiModelProperty(value = "어댑터 사용 여부", required = true, example = "Y", allowableValues = "Y")
	private Boolean adaptorUseYn;

	@ApiModelProperty(value = "어댑터 삭제 여부", required = true, example = "N", allowableValues = "N")
	private String adaptorDeleteYn;

}
