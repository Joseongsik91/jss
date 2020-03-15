package com.oneplat.oap.mgmt.setting.admin.model;

import java.util.Date;

import com.oneplat.oap.core.model.AbstractObject;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * AdminMenu 모델
 *
 * @author 
 * @date 
 * @version 
 */
@ApiModel(description="backoffice 메뉴 관계 모델")
@EqualsAndHashCode(callSuper=false)
@Data
public class AdminMenuRelation extends AbstractObject {
    
    @ApiModelProperty(value="기준 메뉴 번호", required=true, example="123", allowableValues="", dataType="long")
    private Long criteriaMenuNum;

    @ApiModelProperty(value="상대 메뉴 번호", required=true, example="124", allowableValues="", dataType="long")
    private Long opponentMenuNum;
    
    @ApiModelProperty(value="상대 메뉴명", required=true, example="메뉴메뉴", allowableValues="메뉴명", dataType="String")
    private String opponentMenuNm;

    @ApiModelProperty(value="종료일시", required=true, example="", allowableValues="", dataType="Date")
    private Date endDateTime;
    
    @ApiModelProperty(value="기준 메뉴 레벨", required=false, example="1", allowableValues="", dataType="int")
    private int criteriaMenuLevel;
    
    @ApiModelProperty(value="상대 메뉴 레벨", required=false, example="11", allowableValues="", dataType="int")
    private int opponentMenuLevel;
    
    @ApiModelProperty(value="기준 정렬 번호", required=false, example="1", allowableValues="", dataType="int")
    private int criteriaSortNum;
    
    @ApiModelProperty(value="상대 정렬 번호", required=false, example="1", allowableValues="", dataType="long")
    private int opponentSortNum;

    @ApiModelProperty(hidden=true, value="삭제여부", required=false, example="", allowableValues="", dataType="boolean")
    private boolean deleteFlag;
    
    @ApiModelProperty(value="신규 정렬 번호", required=false, example="1", allowableValues="")
    private int newSortNum;
    
}
