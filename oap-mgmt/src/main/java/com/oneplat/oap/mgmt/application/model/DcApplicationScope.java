package com.oneplat.oap.mgmt.application.model;

import java.util.List;

import com.oneplat.oap.core.model.AbstractObject;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@ApiModel
public class DcApplicationScope extends AbstractObject {

    @ApiModelProperty(value = "어플리케이션 번호")
    private Long applicationNumber;
    
    @ApiModelProperty(value = "어플리케이션 이름")
    private String applicationName;

    @ApiModelProperty(value = "어플리케이션 설명")
    private String applicationDescription;
    
    @ApiModelProperty(value = "스코프 번호")
    private Long scopeNumber;

    @ApiModelProperty(value = "키 종료 일시")
    private String keyEndDatetime;
    
    
    @ApiModelProperty(value = "Scope명", example = "Scope", allowableValues = "Scope", dataType = "String")
    private String scopeName;

    @ApiModelProperty(value = "Scope 컨텍스트", example = "", allowableValues = "", dataType = "String")
    private String scopeContext;
    
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
    
    /* 스코프 번호 목록 */
    private List<String> scopeNumberList;
    
    /* 스코프 번호 목록 */
    private List<String> scopeContextList;

    @ApiModelProperty(value = "클라이언트 아이디")
    private String clientId;
    
    
    @ApiModelProperty(value = "생성 일시")
    private String createDatetime;

//    @ApiModelProperty(value = "생성 아이디")
//    private String createId;

    @ApiModelProperty(value = "변경 일시")
    private String modifyDatetime;

//    @ApiModelProperty(value = "변경 아이디")
//    private String modifyId;
    
}
