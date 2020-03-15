package com.oneplat.oap.mgmt.setting.system.scheduler.job;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

public class TestJob1 implements Job {
    private static final Log log = (Log) LogFactory.getLog(TestJob1.class);

    public void execute(JobExecutionContext context) {
        log.debug("TestJob1.execute() is Executed... : {}" + new Date());
        log.debug("job executing"); 
        log.debug("now=" + new Date()); 
        log.debug("scheduled time=" + context.getScheduledFireTime()); 
        log.debug("actual fire time=" + context.getFireTime());
        log.debug("next fire time=" + context.getNextFireTime());
    }
}
