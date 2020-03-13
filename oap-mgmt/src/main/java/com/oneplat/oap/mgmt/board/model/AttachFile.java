package com.oneplat.oap.mgmt.board.model;

import com.oneplat.oap.core.model.AbstractObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by LSH on 2016. 12. 19..
 */
@ApiModel
@EqualsAndHashCode(callSuper=false)
@Data
public class AttachFile extends AbstractObject {

    /** 첨부 파일 번호*/
    @ApiModelProperty(value = "첨부 파일 번호", required = true, example = "", allowableValues = "", dataType = "Long")
    private Long attachFileNumber;
    /** 공지사항 번호*/
    @ApiModelProperty(value = "공지사항 번호", required = true, example = "", allowableValues = "", dataType = "Long")
    private Long noticeNumber;
    /** 첨부 파일 URL*/
    @ApiModelProperty(value = "첨부 파일 URL", required = true, example = "", allowableValues = "", dataType = "String")
    private String fileUrl;
    /** 첨부 파일 크기*/
    @ApiModelProperty(value = "첨부 파일 사이즈", required = true, example = "", allowableValues = "", dataType = "int")
    private long fileSize;
    /** 첨부 파일 이름*/
    @ApiModelProperty(value = "첨부 파일 이름", required = true, example = "", allowableValues = "")
    private String fileName;

}
