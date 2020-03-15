package com.oneplat.oap.mgmt.setting.system.model;

import com.oneplat.oap.core.model.AbstractPagableResponse;
import com.oneplat.oap.core.model.PageInfo;

import io.swagger.annotations.ApiModel;
import lombok.NoArgsConstructor;

@ApiModel
@NoArgsConstructor
public class ResponseCommonCode extends AbstractPagableResponse<CommonCode> {

    /**
    * @param data
    * @param pageInfo
    */
    public ResponseCommonCode(ResponseCommonCode response, PageInfo pageInfo) {
        
        super(response.getData(), pageInfo);
    }

}
