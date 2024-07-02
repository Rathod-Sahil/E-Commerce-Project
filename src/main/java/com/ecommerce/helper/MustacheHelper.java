package com.ecommerce.helper;

import com.ecommerce.utils.TemplateResolveUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import com.samskivert.mustache.Mustache;

@RequiredArgsConstructor
@Component
public class MustacheHelper {
    private final TemplateResolveUtils templateResolveUtils;

    public String setMessageContent(String templateName, Object object) {
        String content = templateResolveUtils.getResolvedTemplate(templateName);
        return Mustache.compiler().defaultValue("").escapeHTML(false).compile(content).execute(object);
    }
}
