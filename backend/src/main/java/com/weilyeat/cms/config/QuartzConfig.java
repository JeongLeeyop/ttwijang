package com.weilyeat.cms.config;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.weilyeat.cms.service.MissionAlarmJob;

@Configuration
public class QuartzConfig {

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setJobFactory(new AutoWiringSpringBeanJobFactory());
        return factory;
    }

    @Bean
    public Scheduler scheduler(SchedulerFactoryBean schedulerFactoryBean) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        scheduler.start();
        return scheduler;
    }

    @Bean
    public JobDetail missionAlarmJobDetail() {
        return JobBuilder.newJob(MissionAlarmJob.class)
                .withIdentity("missionAlarmJob")
                .withDescription("Mission Alarm Job")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger missionAlarmTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(missionAlarmJobDetail())
                .withIdentity("missionAlarmTrigger")
                .withDescription("Mission Alarm Trigger")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInMinutes(1)
                        .repeatForever())
                .build();
    }
}
