package com.transsion.json.factories;

import com.transsion.json.ObjectBinder;
import com.transsion.json.ObjectFactory;

import java.util.Collection;
import java.util.ArrayList;
import java.lang.reflect.Type;

public class ListObjectFactory implements ObjectFactory {
    public Object instantiate(ObjectBinder context, Object value, Type targetType, Class targetClass) {
        if( value instanceof Collection) {
            return context.bindIntoCollection((Collection)value, new ArrayList(), targetType);
        } else {
            ArrayList<Object> set = new ArrayList<>();
            set.add( context.bind( value ) );
            return set;
        }
    }
}
