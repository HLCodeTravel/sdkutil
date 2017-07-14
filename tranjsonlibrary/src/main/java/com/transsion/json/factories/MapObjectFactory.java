package com.transsion.json.factories;

import com.transsion.json.ObjectFactory;
import com.transsion.json.ObjectBinder;

import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;
import java.util.Map;
import java.util.HashMap;

public class MapObjectFactory implements ObjectFactory {
    public Object instantiate(ObjectBinder context, Object value, Type targetType, Class targetClass) {
        if( targetType != null ) {
            if( targetType instanceof ParameterizedType ) {
                ParameterizedType ptype = (ParameterizedType) targetType;
                Type keyType = ptype.getActualTypeArguments()[0];
                Type valueType = ptype.getActualTypeArguments()[1];
                return context.bindIntoMap( (Map)value, createMapImpl(),
                        keyType == Object.class ? null : keyType,
                        valueType == Object.class ? null : valueType );
            }
        }
        return context.bindIntoMap( (Map)value, createMapImpl(), null, null );
    }

    protected HashMap<Object, Object> createMapImpl() {
        return new HashMap<>();
    }
}
