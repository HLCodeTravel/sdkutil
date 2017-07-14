package com.transsion.json.factories;

import com.transsion.json.JsonNumber;
import com.transsion.json.ObjectBinder;
import com.transsion.json.ObjectFactory;

import java.lang.reflect.Type;

public class JsonNumberObjectFactory implements ObjectFactory {
    public Object instantiate(ObjectBinder context, Object value, Type targetType, Class targetClass) {
        JsonNumber number = (JsonNumber) value;
        if( number.isLong() ) {
            return number.longValue();
        } else {
            return number.doubleValue();
        }
    }
}
