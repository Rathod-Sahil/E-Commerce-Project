package com.ecommerce.services;

import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

public interface SchedulingService {

    void cronSchedule(String groupName, Class<? extends QuartzJobBean> jobClass, String cronExpression);
    void dateSchedule(String groupName, Class<? extends QuartzJobBean> jobClass, Date startDate);
}
