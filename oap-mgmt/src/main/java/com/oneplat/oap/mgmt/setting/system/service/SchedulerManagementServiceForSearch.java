package com.oneplat.oap.mgmt.setting.system.service;

import java.util.List;

import com.oneplat.oap.core.model.SearchRequest;
import com.oneplat.oap.mgmt.setting.system.model.Schedule;

public interface SchedulerManagementServiceForSearch {

    public List<Schedule> scheduleList(SearchRequest searchRequest);

    public Schedule getScheduleInfo(Long scheduleNumber);
}