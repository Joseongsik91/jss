package com.oneplat.oap.mgmt.setting.operator.model;

import com.oneplat.oap.core.model.AbstractPagableResponse;
import com.oneplat.oap.core.model.PageInfo;

import io.swagger.annotations.ApiModel;
import lombok.NoArgsConstructor;

@ApiModel
@NoArgsConstructor
public class ResponseOperator extends AbstractPagableResponse<Operator> {

    /**
    * @param data
    * @param pageInfo
    */
    public ResponseOperator(ResponseOperator responseOpr, PageInfo pageInfo) {
        
        super(responseOpr.getData(), pageInfo);
    }

}
