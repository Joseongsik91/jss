package com.oneplat.oap.mgmt.setting.system.service;

import java.util.List;

import com.oneplat.oap.core.model.SearchRequest;
import com.oneplat.oap.mgmt.setting.system.model.Schedule;
import com.oneplat.oap.mgmt.setting.system.model.Schedule.CreateSchedule;
import com.oneplat.oap.mgmt.setting.system.model.Schedule.ModifySchedule;
import com.oneplat.oap.mgmt.setting.system.model.ScheduleLog;

public interface SchedulerManagementService {

	public List<Schedule> scheduleList(SearchRequest searchRequest);

	public Schedule getScheduleInfo(Long scheduleNumber);

	public Object createSchedule(CreateSchedule createSchedule);

	public String modifySchedule(ModifySchedule modifySchedule);

	public void deleteSchedule(Long scheduleNumber);

	public boolean scheduleNameCheck(String scheduleName);

	public void insertSchedulerHst(ScheduleLog scheduleLog);

	public List<ScheduleLog> scheduleLogList(SearchRequest searchRequest);

	public ScheduleLog scheduleLogDetail(Long scheduleLogNumber);
}