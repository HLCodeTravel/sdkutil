package com.transsion.json;

import java.lang.reflect.Type;

/**
 * ObjectFactory allows you to instantiate specific types of objects on path or class types.  This interface allows
 * you to override the default rules.
 */
public interface ObjectFactory {
    Object instantiate(ObjectBinder context, Object value, Type targetType, Class targetClass);
}
