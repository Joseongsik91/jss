package com.oneplat.oap.mgmt.policies.model;

import com.oneplat.oap.core.model.ValidationUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by LSH on 2016. 12. 1..
 */

@ApiModel(description = "서비스 정책(policy) 그룹(하위) 모델")
@EqualsAndHashCode(callSuper = false)
@Data
public class ServiceGroupPolicy {
    @ApiModelProperty(value="서비스번호", required = true, example = "", allowableValues="")
    @NotNull(message = "", groups = {ValidationUpdate.class})
    private Long serviceNumber;

    @ApiModelProperty(value="API 그룹 번호", required = true, example = "", allowableValues="")
    @NotNull(message = "", groups = {ValidationUpdate.class})
    private Long apiGroupNumber;

    @ApiModelProperty(value="서비스 이름", required = true, example = "", allowableValues="")
    @NotNull(message = "", groups = {ValidationUpdate.class})
    private String serviceName;

    @ApiModelProperty(value="서비스 정책 코드", required = true, example = "", allowableValues="")
    @NotNull(message = "", groups = {ValidationUpdate.class})
    private String policyCode;      //Capacity, SLA

    @ApiModelProperty(value="서비스 등급 코드(상용)", required = true, example = "", allowableValues="")
    @NotNull(message = "", groups = {ValidationUpdate.class})
    private String gradeCode;         //상용

    @ApiModelProperty(value="서비스 등급 코드(테스트)", required = false, example = "", allowableValues="")
    @NotNull(message = "", groups = {ValidationUpdate.class})
    private String testGradeCode;     //테스트

    @ApiModelProperty(value="서비스 제한 수량(상용)", required = false, example = "", allowableValues="")
    @NotNull(message = "", groups = {ValidationUpdate.class})
    private Long limitQuantity;

    @ApiModelProperty(value="서비스 제한 수량(테스트)", required = false, example = "", allowableValues="")
    @NotNull(message = "", groups = {ValidationUpdate.class})
    private Long testLimitQuantity;

    @ApiModelProperty(value="서비스 기준 코드(상용)", required = false, example = "", allowableValues="")
    @NotNull(message = "", groups = {ValidationUpdate.class})
    private String criteriaCode;         //초, 분, 시, 일, 월,

    @ApiModelProperty(value="서비스 기준 코드(테스트)", required = false, example = "", allowableValues="")
    @NotNull(message = "", groups = {ValidationUpdate.class})
    private String testCriteriaCode;     //초, 분, 시, 일, 월,

    @ApiModelProperty(value="서비스 정책 사용 여부", required = false, example = "", allowableValues="")
    @NotNull(message = "", groups = {ValidationUpdate.class})
    private String policyDeleteYn;       //Y, N




    @ApiModelProperty(value="서비스 정책 그룹 List", required = false, example = "", allowableValues="")
    @NotNull(message = "", groups = {ValidationUpdate.class})
    private List<ServicePolicy> groupList;



    /*History*/
    @ApiModelProperty(value="이력 시작 일시", required = false, example = "", allowableValues="")
    @NotNull(message = "", groups = {ValidationUpdate.class})
    private String historyBeginDateTime;

    @ApiModelProperty(value="이력 종료 일시", required = false, example = "", allowableValues="")
    @NotNull(message = "", groups = {ValidationUpdate.class})
    private String historyEndDateTime;



    @ApiModelProperty(value="생성 일시", required = false, example = "", allowableValues="")
    @NotNull(message = "", groups = {ValidationUpdate.class})
    private String createDateTime;

    @ApiModelProperty(value="생성 아이디", required = false, example = "", allowableValues="")
    @NotNull(message = "", groups = {ValidationUpdate.class})
    private String createId;

    @ApiModelProperty(value="변경 일시", required = false, example = "", allowableValues="")
    @NotNull(message = "", groups = {ValidationUpdate.class})
    private String modifyDateTime;

    @ApiModelProperty(value="변경 아이디", required = false, example = "", allowableValues="")
    @NotNull(message = "", groups = {ValidationUpdate.class})
    private String modifyId;



}
