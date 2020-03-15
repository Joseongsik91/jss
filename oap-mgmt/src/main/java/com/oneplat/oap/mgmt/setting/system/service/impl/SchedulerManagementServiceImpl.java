package com.oneplat.oap.mgmt.setting.system.service.impl;

import java.util.List;

import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oneplat.oap.core.exception.JobErrorException;
import com.oneplat.oap.core.exception.UnknownServerErrorException;
import com.oneplat.oap.core.model.SearchRequest;
import com.oneplat.oap.mgmt.common.model.AuthenticationInjector;
import com.oneplat.oap.mgmt.setting.system.mapper.SchedulerMapper;
import com.oneplat.oap.mgmt.setting.system.model.Schedule;
import com.oneplat.oap.mgmt.setting.system.model.Schedule.CreateSchedule;
import com.oneplat.oap.mgmt.setting.system.model.Schedule.DeleteSchedule;
import com.oneplat.oap.mgmt.setting.system.model.Schedule.ModifySchedule;
import com.oneplat.oap.mgmt.setting.system.model.ScheduleLog;
import com.oneplat.oap.mgmt.setting.system.service.SchedulerManagementService;

@Service
public class SchedulerManagementServiceImpl implements SchedulerManagementService {

	@Autowired
	SchedulerMapper schedulerMapper;
	@Autowired
	AuthenticationInjector authenticationInjector;
	@Autowired
	private SchedulerFactoryBean quartzScheduler;

	private static final Logger log = LoggerFactory.getLogger(SchedulerManagementServiceImpl.class);

	@Override
	public List<Schedule> scheduleList(SearchRequest searchRequest) {
		// TODO Auto-generated method stub
		if (searchRequest.getPage() == null || searchRequest.getSize() == null) {
			searchRequest.setPage(1);
			searchRequest.setSize(20);
		}
		searchRequest.setData();

		List<Schedule> res = schedulerMapper.scheduleList(searchRequest);

		if (searchRequest.getPageInfo() != null) {
			searchRequest.getPageInfo().setResultCount(res.size());
			searchRequest.getPageInfo().setTotalCount(schedulerMapper.scheduleListTotalConunt(searchRequest));
		}

		return res;
	}

	@Override
	@Transactional
	public Object createSchedule(CreateSchedule createSchedule) {
		// TODO Auto-generated method stub
		Scheduler sch = quartzScheduler.getScheduler();
		JobDetail job;
		Trigger trigger;

		Object rtnVal = "";

		try {
			Object check = (Class<? extends Job>) Class
					.forName("com.oneplat.oap.mgmt.setting.system.scheduler.job." + createSchedule.getClassName());

			trigger = TriggerBuilder.newTrigger()
					.withIdentity(String.valueOf(createSchedule.getScheduleNumber()), "TRIGGER_GROUP")
					.withSchedule(CronScheduleBuilder.cronSchedule(createSchedule.getExecutionCycle())).build();

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			log.error("Create Job error! : " + createSchedule.getScheduleName() + "|| " + e.getMessage());
			rtnVal = "classNotFound";
		} catch (RuntimeException e) {
			log.error("Create Job error! : " + createSchedule.getScheduleName() + "|| " + e.getMessage());
			rtnVal = "invalidExecutionCycle";
			// throw new RuntimeException();
		}

		if (!rtnVal.equals("classNotFound") && !rtnVal.equals("invalidExecutionCycle")) {
			authenticationInjector.setAuthentication(createSchedule);
			schedulerMapper.createSchedule(createSchedule);
			rtnVal = createSchedule.getScheduleNumber();

			try {
				job = JobBuilder
						.newJob((Class<? extends Job>) Class.forName(
								"com.oneplat.oap.mgmt.setting.system.scheduler.job." + createSchedule.getClassName()))
						.withIdentity(String.valueOf(createSchedule.getScheduleNumber()), "JOB_GROUP").storeDurably()
						.build();

				trigger = TriggerBuilder.newTrigger()
						.withIdentity(String.valueOf(createSchedule.getScheduleNumber()), "TRIGGER_GROUP")
						.withSchedule(CronScheduleBuilder.cronSchedule(createSchedule.getExecutionCycle())).build();

				if (createSchedule.getUseYn()) {
					// sch.addJob(job, true);
					sch.scheduleJob(job, trigger);
				}
			} catch (ClassNotFoundException e) {
				log.error("Create Job error! : " + createSchedule.getScheduleName() + "|| " + e.getMessage());
				// throw new UnknownServerErrorException();
				throw new JobErrorException();
			} catch (SchedulerException e) {
				log.error("Create Job error! : " + createSchedule.getScheduleName() + "|| " + e.getMessage());
				throw new UnknownServerErrorException();
			}
		}

		return rtnVal;
	}

	@Override
	public Schedule getScheduleInfo(Long scheduleNumber) {
		// TODO Auto-generated method stub
		return schedulerMapper.getScheduleInfo(scheduleNumber);
	}

	@Override
	@Transactional
	public String modifySchedule(ModifySchedule modifySchedule) {
		// TODO Auto-generated method stub
		Scheduler sch = quartzScheduler.getScheduler();
		JobDetail job;
		Trigger trigger;

		String rtnVal = "";

		try {
			job = JobBuilder
					.newJob((Class<? extends Job>) Class.forName(
							"com.oneplat.oap.mgmt.setting.system.scheduler.job." + modifySchedule.getClassName()))
					.withIdentity(String.valueOf(modifySchedule.getScheduleNumber()), "JOB_GROUP").storeDurably()
					.build();

			trigger = TriggerBuilder.newTrigger()
					.withIdentity(String.valueOf(modifySchedule.getScheduleNumber()), "TRIGGER_GROUP")
					.withSchedule(CronScheduleBuilder.cronSchedule(modifySchedule.getExecutionCycle())).build();

			if (sch.checkExists(job.getKey())) {
				sch.deleteJob(job.getKey());
			}
			if (sch.checkExists(trigger.getKey())) {
				sch.unscheduleJob(trigger.getKey());
			}
			if (modifySchedule.getUseYn()) {
				sch.scheduleJob(job, trigger);
			}
		} catch (ClassNotFoundException e) {
			log.error("Create Job error! : " + modifySchedule.getScheduleName() + "|| " + e.getMessage());
			rtnVal = "classNotFound";
			// throw new UnknownServerErrorException();
			// throw new JobErrorException();
		} catch (SchedulerException e) {
			log.error("Create Job error! : " + modifySchedule.getScheduleName() + "|| " + e.getMessage());
			throw new UnknownServerErrorException();
		} catch (RuntimeException e) {
			log.error("Create Job error! : " + modifySchedule.getScheduleName() + "|| " + e.getMessage());
			rtnVal = "invalidExecutionCycle";
			// throw new RuntimeException();
		}

		if (!rtnVal.equals("classNotFound") && !rtnVal.equals("invalidExecutionCycle")) {
			authenticationInjector.setAuthentication(modifySchedule);
			schedulerMapper.modfifySchedule(modifySchedule);
		}

		return rtnVal;
	}

	@Override
	@Transactional
	public void deleteSchedule(Long scheduleNumber) {
		// TODO Auto-generated method stub
		DeleteSchedule deleteSchedule = new DeleteSchedule();
		deleteSchedule.setScheduleNumber(scheduleNumber);
		Schedule schedule = getScheduleInfo(deleteSchedule.getScheduleNumber()); // 삭제할
																					// 스케쥴러
																					// 정보
																					// 가져옴

		Scheduler sch = quartzScheduler.getScheduler();
		JobDetail job;
		Trigger trigger;
		try {
			job = JobBuilder
					.newJob((Class<? extends Job>) Class
							.forName("com.oneplat.oap.mgmt.setting.system.scheduler.job." + schedule.getClassName()))
					.withIdentity(String.valueOf(schedule.getScheduleNumber()), "JOB_GROUP").storeDurably().build();

			trigger = TriggerBuilder.newTrigger()
					.withIdentity(String.valueOf(schedule.getScheduleNumber()), "TRIGGER_GROUP")
					.withSchedule(CronScheduleBuilder.cronSchedule(schedule.getExecutionCycle())).build();

			if (sch.checkExists(job.getKey())) {
				sch.deleteJob(job.getKey());
			}
			if (sch.checkExists(trigger.getKey())) {
				sch.unscheduleJob(trigger.getKey());
			}
		} catch (ClassNotFoundException e) {
			log.error("Create Job error! : " + schedule.getScheduleName() + "|| " + e.getMessage());
			// throw new UnknownServerErrorException();
			throw new JobErrorException();
		} catch (SchedulerException e) {
			log.error("Create Job error! : " + schedule.getScheduleName() + "|| " + e.getMessage());
			throw new UnknownServerErrorException();
		}

		authenticationInjector.setAuthentication(deleteSchedule);
		schedulerMapper.deleteSchedule(deleteSchedule);

	}

	@Override
	public boolean scheduleNameCheck(String scheduleName) {
		// TODO Auto-generated method stub
		boolean result = false;
		try {
		} catch (Exception e) {
		}
		String getName = schedulerMapper.scheduleNameCheck(scheduleName);
		if (getName == null)
			result = true;
		return result;
	}

	@Override
	@Transactional
	public void insertSchedulerHst(ScheduleLog scheduleLog) {
		scheduleLog.setCreateId("SCHEDULER");
		scheduleLog.setModifyId("SCHEDULER");
		schedulerMapper.insertSchedulerHst(scheduleLog);
	}

	// 스케줄러 강제 실행
	public void excuteOcScheduler(Schedule scheduler) {
		scheduler = getScheduleInfo(scheduler.getScheduleNumber());
		Scheduler sch = quartzScheduler.getScheduler();
		JobDetail job;
		Trigger trigger;

		try {
			job = JobBuilder
					.newJob((Class<? extends Job>) Class
							.forName("com.oneplat.oap.mgmt.setting.system.scheduler.job." + scheduler.getClassName()))
					.withIdentity(String.valueOf(scheduler.getScheduleNumber()), "JOB_GROUP").storeDurably().build();

			trigger = TriggerBuilder.newTrigger()
					.withIdentity(String.valueOf(scheduler.getScheduleNumber()), "TRIGGER_GROUP")
					.withSchedule(CronScheduleBuilder.cronSchedule(scheduler.getExecutionCycle())).build();
			if (sch.checkExists(job.getKey())) {
				job = sch.getJobDetail(job.getKey());
			}
			sch.scheduleJob(job, trigger);
		} catch (ClassNotFoundException e) {
			log.error("Create Job error! : " + scheduler.getScheduleName() + "|| " + e.getMessage());
			// throw new UnknownServerErrorException();
			throw new JobErrorException();
		} catch (SchedulerException e) {
			log.error("Create Job error! : " + scheduler.getScheduleName() + "|| " + e.getMessage());
			throw new UnknownServerErrorException();
		}
	}

	@Override
	public List<ScheduleLog> scheduleLogList(SearchRequest searchRequest) {
		if (searchRequest.getPage() == null || searchRequest.getSize() == null) {
			searchRequest.setPage(1);
			searchRequest.setSize(20);
		}
		searchRequest.setData();

		List<ScheduleLog> res = schedulerMapper.scheduleLogList(searchRequest);

		if (searchRequest.getPageInfo() != null) {
			searchRequest.getPageInfo().setResultCount(res.size());
			searchRequest.getPageInfo().setTotalCount(schedulerMapper.scheduleLogListTotalCount(searchRequest));
		}

		return res;
	}

	@Override
	public ScheduleLog scheduleLogDetail(Long scheduleLogNumber) {
		// TODO Auto-generated method stub
		return schedulerMapper.scheduleLogDetail(scheduleLogNumber);
	}
}