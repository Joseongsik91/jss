package com.oneplat.oap.mgmt.setting.operator.model;

import com.oneplat.oap.core.model.AbstractPagableResponse;
import com.oneplat.oap.core.model.PageInfo;

import io.swagger.annotations.ApiModel;
import lombok.NoArgsConstructor;

@ApiModel
@NoArgsConstructor
public class ResponseLoginHistory extends AbstractPagableResponse<LoginHistory> {

    /**
    * @param data
    * @param pageInfo
    */
    public ResponseLoginHistory(ResponseLoginHistory responseOpr, PageInfo pageInfo) {
        
        super(responseOpr.getData(), pageInfo);
    }

}
