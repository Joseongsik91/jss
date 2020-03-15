package com.oneplat.oap.mgmt.oapservice.model;

import com.oneplat.oap.core.model.AbstractObject;
import com.oneplat.oap.core.model.ValidationCreate;
import com.oneplat.oap.core.model.ValidationUpdate;
import com.sun.org.apache.xpath.internal.operations.Bool;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author lee
 * @date 2016-12-01
 */
@ApiModel(description="서비스 데이타 모델")
@EqualsAndHashCode(callSuper=false)
@Data
public class OapService extends AbstractObject implements Cloneable{
    /** 서비스 번호*/
    @ApiModelProperty(value = "서비스 번호", required = true, example="123", allowableValues="123")
    private Long serviceNumber;
    /** 사이트 코드*/
    @ApiModelProperty(value = "사이트 코드", required = true, example="TEST", allowableValues="TEST")
    private String siteCode;
    /** 서비스 이름*/
    @ApiModelProperty(value = "서비스 이름", required = true, example="TEST", allowableValues="TEST")
    @NotNull(message = "", groups = {ValidationCreate.class,ValidationUpdate.class})
    private String serviceName;
    /** 서비스 컨텍스트*/
    @ApiModelProperty(value = "서비스 컨텍스트", required = true, example="TEST", allowableValues="TEST")
    @NotNull(message = "", groups = {ValidationCreate.class,ValidationUpdate.class})
    private String serviceContext;
    /** 서비스 구분 코드*/
    @ApiModelProperty(value = "서비스 구분 코드", required = true, example="TEST", allowableValues="TEST")
    private String serviceSectionCode;
    /** 샌드박스 사용 여부 */
    @ApiModelProperty(value="샌드박스 사용 여부", required=true)
    private Boolean sandboxUseYn;
    /** 서비스 승인 여부 */
    @ApiModelProperty(value="서비스 승인 여부", required=true)
    private Boolean serviceApprovalYn;
    /** SLA 사용 여부 */
    @ApiModelProperty(value="SLA 사용 여부", required=true)
    private Boolean slaUseYn;
    /** CAPACITY 사용 여부 */
    @ApiModelProperty(value="CAPACITY 사용 여부", required=true)
    private Boolean capacityUseYn;
    /** 서비스 설명*/
    @ApiModelProperty(value = "서비스 설명", required = true, example="TEST", allowableValues="TEST")
    private String serviceDescription;
    /** 아이콘 파일 경로*/
    @ApiModelProperty(value = "아이콘 파일 경로", required = true, example="TEST", allowableValues="TEST")
    private String iconFileChannel;
    /** 내부 정렬 번호*/
    @ApiModelProperty(value = "내부 정렬 번호", required = true, example="123", allowableValues="123")
    private Integer insideSortNumber;
    /** 외부 정렬 번호*/
    @ApiModelProperty(value = "외부 정렬 번호", required = true, example="123", allowableValues="123")
    private Integer outsideSortNumber;
    /** 서비스 등록 일자*/
    @ApiModelProperty(value = "서비스 등록 일자", required = true, example="", allowableValues="")
    private Date serviceRegisterDate;
    /** 서비스 사용 여부 */
    @ApiModelProperty(value="서비스 사용 여부", required=true)
    private Boolean serviceUseYn;
    /** 서비스 삭제 여부 */
    @ApiModelProperty(value="서비스 삭제 여부", required=true)
    private Boolean serviceDeleteYn;
    /** 서비스 구성 사용 여부 */
    @ApiModelProperty(value="서비스 구성 사용 여부", required=true)
    private Boolean serviceComposeUseYn;
    /** API 구분 코드*/
    @ApiModelProperty(value = "API 구분 코드", required = true, example="TEST", allowableValues="TEST")
    private String apiSectionCode;
    /** NB BASE URL*/
    @ApiModelProperty(value = "NB BASE URL", required = true, example="TEST", allowableValues="TEST")
    private String northboundBaseUrl;
    /** SB BASE URL*/
    @ApiModelProperty(value = "SB BASE URL", required = true, example="TEST", allowableValues="TEST")
    private String southboundBaseUrl;
    /** SB BASE TEST URL*/
    @ApiModelProperty(value = "SB BASE TEST URL", required = true, example="TEST", allowableValues="TEST")
    private String southboundBaseTestUrl;
    /** HIST_BEGIN_DATETIME*/
    private Date historyBeginDateTime;
    /** 서비스 구성 리스트 */
    @ApiModelProperty(value = "서비스 구성", required = false)
    private List<OapServiceCompose> serviceComposes;

    /** new 아이콘 노출여부 */
    @ApiModelProperty(value = "new 아이콘 노출여부")
    private Boolean newIconUseYn;

    /** API 카운트*/
    @ApiModelProperty(value = "API 카운트", required = true, example="123", allowableValues="123")
    private Integer apiCount;

    /** Scope 사용여부 */
    @ApiModelProperty(value="Scope 사용 여부", required=true)
    private Boolean scopeUseYn;

    public Object clone(){
        Object obj = null;
        try{
            obj = super.clone();
        }catch(Exception e){}
        return obj;
    }
}
