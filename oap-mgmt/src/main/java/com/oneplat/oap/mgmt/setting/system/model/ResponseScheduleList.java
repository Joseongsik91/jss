package com.oneplat.oap.mgmt.setting.system.model;

import java.util.List;

import com.oneplat.oap.core.model.AbstractPagableResponse;
import com.oneplat.oap.core.model.PageInfo;

import io.swagger.annotations.ApiModel;
import lombok.NoArgsConstructor;

@ApiModel
@NoArgsConstructor
public class ResponseScheduleList extends AbstractPagableResponse<Schedule> {

    public ResponseScheduleList(List<Schedule> data, PageInfo pageInfo) {
        super(data, pageInfo);
    }
}