package com.transsion.json.factories;

import com.transsion.json.ObjectBinder;
import com.transsion.json.ObjectFactory;

import java.lang.reflect.Type;

public class FloatObjectFactory implements ObjectFactory {
    public Object instantiate(ObjectBinder context, Object value, Type targetType, Class targetClass) {
        if (value instanceof Number) {
            return ((Number) value).floatValue();
        } else {
            try {
                return Float.parseFloat(value.toString());
            } catch (Exception e) {
                throw context.cannotConvertValueToTargetType(value, Float.class);
            }
        }
    }
}
