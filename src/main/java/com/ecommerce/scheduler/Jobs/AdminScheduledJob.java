package com.ecommerce.scheduler.Jobs;

import java.util.List;

import com.ecommerce.constants.TemplateConstants;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.ecommerce.decorators.EmailDetails;
import com.ecommerce.decorators.EmailDto;
import com.ecommerce.decorators.ProductCountDto;
import com.ecommerce.models.AdminConfig;
import com.ecommerce.utils.AdminConfigUtils;
import com.ecommerce.rabbitMQ.publisher.EmailPublisher;
import com.ecommerce.helper.MustacheHelper;
import com.ecommerce.services.ProductService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class AdminScheduledJob extends QuartzJobBean{

    private final ProductService productService;
    private final MustacheHelper mustacheHelper;
    private final EmailPublisher emailPublisher;
    private final AdminConfigUtils adminConfigUtils;

    @Override
    protected void executeInternal(@NotNull JobExecutionContext context){
       ProductCountDto countProductDetails = productService.productDetails();
        EmailDto emailDto = new EmailDto();
        emailDto.setAvailableProducts(countProductDetails.getAvailableProductCount());
        emailDto.setExpiredProducts(countProductDetails.getExpiredProductCount());
        emailDto.setComingSoonProducts(countProductDetails.getCominSoonProductCount());
        emailDto.setSelledProducts(countProductDetails.getDailySellProduct());
        emailDto.setCanceledProducts(countProductDetails.getDailyCancelProduct());

        String content = mustacheHelper.setMessageContent(TemplateConstants.PRODUCT_DETAILS, emailDto);
        AdminConfig adminConfig = adminConfigUtils.getAdminConfig();

        notification(adminConfig.getAdminEmails(), content);
        notification(adminConfig.getSuperAdminEmails(), content);
        log.info("Notification send successfully.");
    }

    private void notification(List<String> adminEmails, String content) {
        adminEmails.forEach((admin) -> emailPublisher.sendEmailNotification(new EmailDetails(admin, "Daily product details", content)));
    }

}
