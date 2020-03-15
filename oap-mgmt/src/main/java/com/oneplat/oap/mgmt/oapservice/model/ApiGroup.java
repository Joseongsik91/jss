package com.oneplat.oap.mgmt.oapservice.model;

import com.oneplat.oap.core.model.AbstractObject;
import com.oneplat.oap.core.model.ValidationCreate;
import com.oneplat.oap.core.model.ValidationUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author lee
 * @date 2016-12-02
 */
@ApiModel(description="API 그룹 모델")
@EqualsAndHashCode(callSuper=false)
@Data
public class ApiGroup extends AbstractObject {
    /** API 그룹 번호*/
    @ApiModelProperty(value = "API 그룹 번호", required = true, example="123", allowableValues="123")
    private Long apiGroupNumber;
    /** 서비스 번호*/
    @ApiModelProperty(value = "서비스 번호", required = true, example="123", allowableValues="123")
    private Long serviceNumber;
    /** API 그룹 이름*/
    @ApiModelProperty(value = "API 그룹 이름", required = true, example="TEST", allowableValues="TEST")
    @NotNull(message = "", groups = {ValidationCreate.class,ValidationUpdate.class})
    private String apiGroupName;
    /** API 그룹 컨텍스트*/
    @ApiModelProperty(value = "API 그룹 컨텍스트", required = true, example="TEST", allowableValues="TEST")
    private String apiGroupContext;
    /** SLA 정책 사용 여부 */
    @ApiModelProperty(value="SLA 정책 사용 여부", required=true)
    private Boolean slaUseYn;
    /** Capacity 정책 사용 여부 */
    @ApiModelProperty(value="Capacity 정책 사용 여부", required=true)
    private Boolean capacityUseYn;
    /** API 그룹 설명*/
    @ApiModelProperty(value = "API 그룹 설명", required = true, example="TEST", allowableValues="TEST")
    private String apiGroupDescription;
    /** API 그룹 사용 여부 */
    @ApiModelProperty(value="API 그룹 사용 여부", required=true)
    private Boolean apiGroupUseYn;
    /** API 그룹 삭제 여부 */
    @ApiModelProperty(value="API 그룹 삭제 여부", required=true)
    private Boolean apiGroupDeleteYn;

    @ApiModel(description="API 그룹 관계 모델")
    @EqualsAndHashCode(callSuper=false)
    @NoArgsConstructor
    @Data
    public static class ApiGroupRelation extends ApiGroup{
        /** 기준 API 그룹 번호*/
        @ApiModelProperty(value = "기준 API 그룹 번호", required = true, example="123", allowableValues="123")
        private Long criteriaApiGroupNumber;
        /** 상대 API 그룹 번호*/
        @ApiModelProperty(value = "상대 API 그룹 번호", required = true, example="123", allowableValues="123")
        private Long opponentApiGroupNumber;
        /** 종료 일시*/
        @ApiModelProperty(value = "종료 일시", required = true, example="", allowableValues="")
        private Date endDateTime;
        /** 기준 API 그룹 레벨*/
        @ApiModelProperty(value = "기준 API 그룹 레벨", required = true, example="123", allowableValues="123")
        private Integer criteriaApiGroupLevel;
        /** 상대 API 그룹 레벨*/
        @ApiModelProperty(value = "상대 API 그룹 레벨", required = true, example="123", allowableValues="123")
        private Integer opponentApiGroupLevel;
        /** 기준 정렬 번호*/
        @ApiModelProperty(value = "기준 정렬 번호", required = true, example="123", allowableValues="123")
        private Integer criteriaSortNumber;
        /** 상대 정렬 번호*/
        @ApiModelProperty(value = "상대 정렬 번호", required = true, example="123", allowableValues="123")
        private Integer opponentSortNumber;
    }
}
