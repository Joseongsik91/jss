package com.oneplat.oap.mgmt.setting.system.model;

import java.util.Date;

import com.oneplat.oap.core.model.AbstractObject;
import com.oneplat.oap.core.validation.annotation.UniqueName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel(description = "스케줄 모델")
@EqualsAndHashCode(callSuper = false)
@Data
public class Schedule extends AbstractObject {

	@ApiModelProperty(value = "스케줄번호", required = true, example = "1", allowableValues = "1", dataType = "int")
	private Long scheduleNumber;

	@ApiModelProperty(value = "정렬번호", required = true, example = "1", allowableValues = "", dataType = "int")
	private int sortNumber;

	@ApiModelProperty(value = "실행주기 입력 구분", required = true, example = "TEST", allowableValues = "TEST", dataType = "String")
	private String inputSectionCode;

	@ApiModelProperty(value = "스케줄명", required = true, example = "TEST", allowableValues = "TEST", dataType = "String")
	@UniqueName(tableName = "ST_SCHED", columnName = "SCHED_NM")
	private String scheduleName;

	@ApiModelProperty(value = "설명", required = true, example = "설명", allowableValues = "설명", dataType = "String")
	private String scheduleDescribe;

	@ApiModelProperty(value = "실행주기", required = true, example = "실행주기", allowableValues = "실행주기", dataType = "String")
	private String executionCycle;

	@ApiModelProperty(value = "클래스명", required = true, example = "TEST", allowableValues = "TEST", dataType = "String")
	private String className;

	@ApiModel(description = "스케줄 등록 모델")
	@EqualsAndHashCode(callSuper = false)
	@Data
	public static class CreateSchedule extends Schedule {
	}

	@ApiModel(description = "스케줄 수정 모델")
	@EqualsAndHashCode(callSuper = false)
	@Data
	public static class ModifySchedule extends Schedule {
	}

	@ApiModel(description = "스케줄 삭제 모델")
	@EqualsAndHashCode(callSuper = false)
	@Data
	public static class DeleteSchedule extends Schedule {
	}

	@ApiModelProperty(value = "에러 체크", required = false, example = "에러체크", allowableValues = "에러체크", dataType = "String")
	private String err;
}