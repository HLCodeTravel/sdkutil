package com.transsion.json.factories;

import com.transsion.json.ObjectBinder;
import com.transsion.json.ObjectFactory;

import java.lang.reflect.Type;

public class ByteObjectFactory implements ObjectFactory {

    public Object instantiate(ObjectBinder context, Object value, Type targetType, Class targetClass) {
        if( value instanceof Number ) {
            return ((Number)value).byteValue();
        } else {
            throw context.cannotConvertValueToTargetType( value, Byte.class );
        }
    }
}
