package com.ecommerce.configurations;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.stereotype.Component;

@Component
public class NullAwareBeans extends BeanUtilsBean {

    @Override
    public void copyProperty(Object dest, String name, Object value)
            throws IllegalAccessException, InvocationTargetException {
        if(value==null)return;
        super.copyProperty(dest, name, value);
    }
}
