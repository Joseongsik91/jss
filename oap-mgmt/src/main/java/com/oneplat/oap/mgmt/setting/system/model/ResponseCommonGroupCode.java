package com.oneplat.oap.mgmt.setting.system.model;

import com.oneplat.oap.core.model.AbstractPagableResponse;
import com.oneplat.oap.core.model.PageInfo;

import io.swagger.annotations.ApiModel;
import lombok.NoArgsConstructor;

@ApiModel
@NoArgsConstructor
public class ResponseCommonGroupCode extends AbstractPagableResponse<CommonGroupCode> {

    /**
    * @param data
    * @param pageInfo
    */
    public ResponseCommonGroupCode(ResponseCommonGroupCode response, PageInfo pageInfo) {
        
        super(response.getData(), pageInfo);
    }

}
