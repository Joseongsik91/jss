package com.oneplat.oap.mgmt.setting.system.model;

import java.util.List;

import com.oneplat.oap.core.model.AbstractPagableResponse;
import com.oneplat.oap.core.model.PageInfo;

public class ResponseScheduleLogList extends AbstractPagableResponse<ScheduleLog> {
    public ResponseScheduleLogList(List<ScheduleLog> data, PageInfo pageInfo) {
        super(data, pageInfo);
    }
}