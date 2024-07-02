package com.ecommerce.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Component;

import com.google.common.io.Resources;

@Component
public class TemplateResolveUtils {

    public String getResolvedTemplate(String templateName){
        try {
            return Resources.toString(Resources.getResource(templateName), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
