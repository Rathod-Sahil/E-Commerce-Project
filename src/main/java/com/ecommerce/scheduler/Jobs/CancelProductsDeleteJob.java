package com.ecommerce.scheduler.Jobs;

import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.ecommerce.decorators.ProductCancellationDto;
import com.ecommerce.models.AdminConfig;
import com.ecommerce.repositories.AdminConfigRepository;
import com.ecommerce.utils.AdminConfigUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class CancelProductsDeleteJob extends QuartzJobBean {

    private final AdminConfigRepository adminConfigRepository;
    private final AdminConfigUtils adminConfigUtils;

    @Override
    protected void executeInternal(@NotNull JobExecutionContext context){
        LocalDateTime thresholdTime = LocalDateTime.now().minusMinutes(30);
        AdminConfig adminConfig = adminConfigUtils.getAdminConfig();
        List<ProductCancellationDto> cancelledProducts = adminConfig.getCancelledProducts();

        log.info("Task2 executed");

        if (cancelledProducts != null) {
            cancelledProducts.forEach((cancellation) -> {
                if (cancellation.getCancelledTime().isBefore(thresholdTime)) {
                    cancelledProducts.remove(cancellation);
                    adminConfigRepository.save(adminConfig);
                }
            });
        }
    }
}
