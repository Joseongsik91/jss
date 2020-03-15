package com.oneplat.oap.mgmt.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import com.oneplat.oap.mgmt.setting.system.model.Schedule;
import com.oneplat.oap.mgmt.setting.system.service.SchedulerManagementServiceForSearch;
import com.oneplat.oap.core.model.SearchRequest;

/**
 * TODO Quartz 설정 설정 처리 
 *
 * @author mike Ryu, BD Apis
 * @date 2015. 3. 09
 * @version 1.0
 */
 
//@Configuration
@PropertySource("classpath:system.properties")
public class QuartzConfig {
 
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
 
    @Autowired
    private DataSource dataSource;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private SchedulerManagementServiceForSearch schedulerManagementServiceForSearch;
    @Autowired  
    private PlatformTransactionManager transactionManager;
    @Autowired
    private Environment env;
    
 
    @PostConstruct
    public void init() {
        log.debug("QuartzConfig initialized.");
    }
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean quartzScheduler = new SchedulerFactoryBean();
 
        quartzScheduler.setDataSource(dataSource);
        quartzScheduler.setTransactionManager(transactionManager);
        quartzScheduler.setOverwriteExistingJobs(false);
        quartzScheduler.setSchedulerName("jelies-quartz-scheduler");
        // custom job factory of spring with DI support for @Autowired!
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        quartzScheduler.setJobFactory(jobFactory);
        if(!env.getProperty("quartz.startYn").equals("Y")) quartzScheduler.setAutoStartup(false);
        quartzScheduler.setQuartzProperties(quartzProperties());
        
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.addQueryData("yesNoCode", "Y");
        List<Schedule> scheduleList = schedulerManagementServiceForSearch.scheduleList(searchRequest);
        List<JobDetail> jobDetailList = new ArrayList<JobDetail>();
        List<CronTrigger> triggerList = new ArrayList<CronTrigger>();
        for (Schedule schedule : scheduleList) {
            JobDetail job;
            CronTrigger trigger;
            try{
                job = JobBuilder.newJob((Class<? extends Job>) Class.forName("com.oneplat.oap.mgmt.setting.system.scheduler.job."+schedule.getClassName()))
                            .withIdentity(String.valueOf(schedule.getScheduleNumber()),"JOB_GROUP")
                            .storeDurably()
                            .build();

                trigger = TriggerBuilder.newTrigger()
                        .withIdentity(String.valueOf(schedule.getScheduleNumber()),"TRIGGER_GROUP")
                        .withSchedule(CronScheduleBuilder.cronSchedule(schedule.getExecutionCycle()))
                        .forJob(job)
                        .build();
                jobDetailList.add(job);
                triggerList.add(trigger);
            } catch (ClassNotFoundException e) {
                log.error("Create Job error!: "+schedule.getScheduleName());
                continue;
            }
        }
        
        quartzScheduler.setTriggers(triggerList.toArray(new CronTrigger[triggerList.size()]));
        quartzScheduler.setJobDetails(jobDetailList.toArray(new JobDetail[jobDetailList.size()]));
        
        return quartzScheduler;
    }
 
    @Bean
    public Properties quartzProperties() {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("quartz.properties"));
        Properties properties = null;
        try {
            propertiesFactoryBean.afterPropertiesSet();
            properties = propertiesFactoryBean.getObject();
 
        } catch (IOException e) {
            log.warn("Cannot load quartz.properties.");
        }
 
        return properties;
    }
}