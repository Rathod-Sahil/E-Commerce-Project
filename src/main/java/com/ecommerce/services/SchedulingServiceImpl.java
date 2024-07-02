package com.ecommerce.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Service
public class SchedulingServiceImpl implements SchedulingService{

    private final Scheduler scheduler;

    private JobDetail jobDetail(String groupName, Class<? extends QuartzJobBean> jobClass){
        return JobBuilder.newJob(jobClass).withIdentity(groupName).build();
    }

    @Override
    public void cronSchedule(String groupName, Class<? extends QuartzJobBean> jobClass,
                             String cronExpression) {
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity(groupName)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .build();
        try {
            scheduler.scheduleJob(jobDetail(groupName, jobClass), trigger);
        } catch (SchedulerException e) {
            log.error("Cron scheduler exception", e);
        }

    }

    @Override
    public void dateSchedule(String groupName, Class<? extends QuartzJobBean> jobClass, Date startDate) {
        Trigger trigger = TriggerBuilder
                .newTrigger().withIdentity(groupName)
                .startAt(Date.from(startDate.toInstant()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build();
        try {
            scheduler.scheduleJob(jobDetail(groupName, jobClass), trigger);
        } catch (SchedulerException e) {
            log.error("Date scheduler exception", e);
        }
    }
}
