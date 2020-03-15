package com.oneplat.oap.mgmt.application.model;

import java.util.List;

import com.oneplat.oap.core.model.AbstractPagableResponse;
import com.oneplat.oap.core.model.PageInfo;

import io.swagger.annotations.ApiModel;
import lombok.NoArgsConstructor;

@ApiModel
@NoArgsConstructor
public class ResponseApplications extends AbstractPagableResponse<DcApplication> {

    public ResponseApplications(List<DcApplication> data, PageInfo pageInfo) {
        super(data, pageInfo);
    }
    
}
