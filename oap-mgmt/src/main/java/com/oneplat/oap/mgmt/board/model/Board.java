package com.oneplat.oap.mgmt.board.model;

import com.oneplat.oap.core.model.AbstractObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * Created by LSH on 2016. 12. 18..
 */
@ApiModel
@Data
public class Board {

    /** 공지사항 번호*/
    @ApiModelProperty(value = "공지사항 번호", required = true, example = "", allowableValues = "")
    private Long noticeNumber;
    /** 이메일 발송 여부*/
    @ApiModelProperty(value = "이메일 발송 여부", required = true, example = "", allowableValues = "")
    private Boolean emailSendYn;
    /** 사이트 코드*/
    @ApiModelProperty(value = "사이트 코드", required = true, example = "", allowableValues = "")
    private String siteCode;
    /** 제목*/
    @ApiModelProperty(value = "제목", required = true, example = "", allowableValues = "")
    private String title;
    /** 내용*/
    @ApiModelProperty(value = "내용", required = true, example = "", allowableValues = "")
    private String contents;
    /** 노출 여부*/
    @ApiModelProperty(value = "노출여부", required = true, example = "", allowableValues = "")
    private Boolean exposureYn;
    /** 노출 시작 일시*/
    @ApiModelProperty(value = "노출 시작 일시", required = true, example = "", allowableValues = "")
    private Date exposureBeginDateTime;
    /** 노출 종료 일시*/
    @ApiModelProperty(value = "노출 종료 일시", required = true, example = "", allowableValues = "")
    private Date exposureEndDateTime;
    /** 조회 횟수*/
    @ApiModelProperty(value = "조회 횟수", required = true, example = "", allowableValues = "")
    private int inquiryCount;
    /** 상단 고정 여부*/
    @ApiModelProperty(value = "상단 고정 여부", required = true, example = "", allowableValues = "")
    private Boolean topFixYn;
    /** 삭제 여부*/
    @ApiModelProperty(value = "삭제 여부", required = true, example = "", allowableValues = "")
    private Boolean noticeDeleteYn;
}

