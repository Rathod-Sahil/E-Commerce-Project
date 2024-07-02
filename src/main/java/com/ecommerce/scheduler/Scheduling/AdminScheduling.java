package com.ecommerce.scheduler.Scheduling;

import com.ecommerce.scheduler.Jobs.AdminScheduledJob;
import com.ecommerce.scheduler.Jobs.CancelProductsDeleteJob;
import com.ecommerce.services.SchedulingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminScheduling {

    private final SchedulingService schedulingService;

    @EventListener()
    public void start(ContextRefreshedEvent contextRefreshedEvent) {
        log.info("QuartzScheduler started. ");

        try {
            schedulingService.cronSchedule("adminScheduled", AdminScheduledJob.class, "0 27 16 1/1 * ? *");
            // schedulingService.dateSchedule("dateGroup", CancelProductsDeleteJob.class,new Date(),new SimpleDateFormat("yyyy-MM-dd").parse("2024-12-18"));
            schedulingService.cronSchedule("cancelProduct", CancelProductsDeleteJob.class, "0 0/1 * 1/1 * ? *");
        } catch (Exception e) {
            log.error("Exception in quartz AdminScheduling", e);
        }
    }

}
