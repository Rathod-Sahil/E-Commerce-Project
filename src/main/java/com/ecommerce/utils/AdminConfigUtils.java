package com.ecommerce.utils;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import com.ecommerce.models.AdminConfig;
import com.ecommerce.repositories.AdminConfigRepository;

@RequiredArgsConstructor
@Component
public class AdminConfigUtils {

    private final AdminConfigRepository adminConfigRepository;
    public AdminConfig getAdminConfig(){
        List<AdminConfig> adminConfigList = adminConfigRepository.findAll();
        return ObjectUtils.isEmpty(adminConfigList) ? new AdminConfig() : adminConfigList.get(0);
    }
}
