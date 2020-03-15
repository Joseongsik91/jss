package com.oneplat.oap.mgmt.setting.system.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.oneplat.oap.core.model.SearchRequest;
import com.oneplat.oap.mgmt.setting.system.model.ResponseScheduleList;
import com.oneplat.oap.mgmt.setting.system.model.ResponseScheduleLogList;
import com.oneplat.oap.mgmt.setting.system.model.Schedule;
import com.oneplat.oap.mgmt.setting.system.model.Schedule.CreateSchedule;
import com.oneplat.oap.mgmt.setting.system.model.Schedule.ModifySchedule;
import com.oneplat.oap.mgmt.setting.system.service.SchedulerManagementService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = "/schedules", description = "설정 > 시스템관리 > 스케줄러", produces = "application/json")
@RestController
@RequestMapping(value = "/schedules")
public class SchedulerManagementRestController {

	@Autowired
	SchedulerManagementService schedulerManagementService;

	private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerManagementRestController.class);

	@ApiOperation(value = "설정 > 시스템관리 > 스케줄러 > 스케줄 목록 조회", notes = "스케줄 목록 조회", response = ResponseScheduleList.class)
	@RequestMapping(method = RequestMethod.GET)
	public Object scheduleList(SearchRequest searchRequest) {
		return new ResponseScheduleList(schedulerManagementService.scheduleList(searchRequest),
				searchRequest.getPageInfo());
	}

	@ApiOperation(value = "설정 > 시스템관리 > 스케줄러 > 스케줄 등록", notes = "스케줄 등록")
	@RequestMapping(method = RequestMethod.POST)
	public Object createSchedule(@ApiParam(value = "스케줄 정보(입력)") @RequestBody CreateSchedule createSchedule) {
		Object result = schedulerManagementService.createSchedule(createSchedule);
		LOGGER.debug("result : " + result);
		return result;
	}

	@ApiOperation(value = "설정 > 시스템관리 > 스케줄러 > 선택 스케줄 정보 조회", notes = "선택 스케줄 정보 조회")
	@RequestMapping(value = "/{scheduleNumber}", method = RequestMethod.GET)
	public Object getScheduleInfo(@ApiParam(value = "스케줄 넘버") @PathVariable("scheduleNumber") Long scheduleNumber) {
		return schedulerManagementService.getScheduleInfo(scheduleNumber);
	}

	@ApiOperation(value = "설정 > 시스템관리 > 스케줄러 > 스케줄 수정", notes = "스케줄 수정")
	@RequestMapping(value = "/{scheduleNumber}", method = RequestMethod.PUT)
	public Object modifySchedule(@ApiParam(value = "스케줄 정보(수정)") @RequestBody ModifySchedule modifySchedule) {
		Schedule rtn = new Schedule();
		String err = schedulerManagementService.modifySchedule(modifySchedule);
		if (!err.equals("classNotFound") && !err.equals("invalidExecutionCycle")) {
			rtn = schedulerManagementService.getScheduleInfo(modifySchedule.getScheduleNumber());
		}
		rtn.setErr(err);
		return rtn;
	}

	@ApiOperation(value = "설정 > 시스템관리 > 스케줄러 > 스케줄 삭제", notes = "스케줄 삭제")
	@RequestMapping(value = "/{scheduleNumber}", method = RequestMethod.DELETE)
	public void deleteSchedule(@ApiParam(value = "스케줄 넘버") @PathVariable("scheduleNumber") Long scheduleNumber) {
		schedulerManagementService.deleteSchedule(scheduleNumber);
	}

	@ApiOperation(value = "설정 > 시스템관리 > 스케줄러 > 스케줄명 중복 확인", notes = "스케줄명 중복 확인")
	@RequestMapping(value = "/scheduleNameCheck", method = RequestMethod.GET)
	public Object scheduleNameCheck(@ApiParam(value = "스케줄 이름") String scheduleName) {
		return schedulerManagementService.scheduleNameCheck(scheduleName);
	}

	@ApiOperation(value = "설정 > 시스템관리 > 스케줄 처리내역", notes = "스케줄 처리내역", response = ResponseScheduleLogList.class)
	@RequestMapping(value = "/scheduleLogs", method = RequestMethod.GET)
	public Object scheduleLogList(SearchRequest searchRequest) {
		return new ResponseScheduleLogList(schedulerManagementService.scheduleLogList(searchRequest),
				searchRequest.getPageInfo());
	}

	@ApiOperation(value = "설정 > 시스템관리 > 스케줄 처리내역 > 선택 스케줄 처리내역 정보 조회", notes = "선택 스케줄 처리내역 정보 조회")
	@RequestMapping(value = "/scheduleLogs/{scheduleLogNumber}", method = RequestMethod.GET)
	public Object scheduleLogDetail(
			@ApiParam(value = "스케줄 로그 번호") @PathVariable("scheduleLogNumber") Long scheduleLogNumber) {
		return schedulerManagementService.scheduleLogDetail(scheduleLogNumber);
	}
}