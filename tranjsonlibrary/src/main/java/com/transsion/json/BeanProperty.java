package com.transsion.json;

import com.transsion.json.annotations.TserializedName;
import com.transsion.json.transformer.Transformer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class BeanProperty {
    private final String name;
    private String jsonName;
    private final BeanAnalyzer bean;
    private Class propertyType;
    protected final Field property;
    protected Method readMethod;
    protected Method writeMethod;
    protected final Map<Class<?>, Method> writeMethods = new HashMap<>();
    protected DeferredInstantiation<? extends Transformer> transformer = null;
    protected DeferredInstantiation<? extends ObjectFactory> objectFactory = null;
    protected Boolean included = null;

    public BeanProperty(String name, BeanAnalyzer bean) {
        this.name = jsonName = name;
        this.bean = bean;
        this.property = bean.getDeclaredField(name);

        if (property != null && property.isAnnotationPresent(TserializedName.class)) {
            processAnnotation(property.getAnnotation(TserializedName.class));
        }
    }

    public BeanProperty(Field property, BeanAnalyzer bean) {
        this.name = jsonName = property.getName();
        this.bean = bean;
        this.property = property;
        this.propertyType = property.getType();

        if (property.isAnnotationPresent(TserializedName.class)) {
            processAnnotation(property.getAnnotation(TserializedName.class));
        }
    }

    private void processAnnotation(TserializedName annotation) {
        jsonName = annotation.name().length() > 0 ? annotation.name() : name;
        transformer = annotation.transformer() == Transformer.class ? null : new DeferredInstantiation<>(annotation.transformer());
        objectFactory = annotation.objectFactory() == ObjectFactory.class ? null : new DeferredInstantiation<>(annotation.objectFactory());
        included = annotation.include();
    }

    public String getName() {
        return name;
    }

    public String getJsonName() {
        return jsonName;
    }

    public Field getProperty() {
        return property;
    }

    public Class getPropertyType() {
        return propertyType;
    }

    public Method getReadMethod() {
        if (readMethod == null && bean.getSuperBean() != null && bean.getSuperBean().hasProperty(name)) {
            return bean.getSuperBean().getProperty(name).getReadMethod();
        } else {
            return readMethod;
        }
    }

    public Method getWriteMethod() {
        if (writeMethod == null) {
            writeMethod = writeMethods.get(propertyType);
            if (writeMethod == null && bean.getSuperBean() != null && bean.getSuperBean().hasProperty(name)) {
                return bean.getSuperBean().getProperty(name).getWriteMethod();
            }
        }
        return writeMethod;
    }

    public Collection<Method> getWriteMethods() {
        return writeMethods.values();
    }

    public void addWriteMethod(Method method) {
        Class clazz = method.getParameterTypes()[0];
        if (propertyType == null) {
            propertyType = clazz;
        }
        writeMethods.put(clazz, method);
        method.setAccessible(true);
    }

    public void setReadMethod(Method method) {
        if (propertyType == null) {
            propertyType = method.getReturnType();
            readMethod = method;
            readMethod.setAccessible(true);
        } else if (method.getReturnType().isAssignableFrom(propertyType)) {
            readMethod = method;
            readMethod.setAccessible(true);
        }

        if (readMethod != null && readMethod.isAnnotationPresent(TserializedName.class)) {
            processAnnotation(readMethod.getAnnotation(TserializedName.class));
        }
    }

    public Boolean isIncluded() {
        return included;
    }

    public Object getValue(Object instance) {
        try {
            Method rm = getReadMethod();
            if (rm != null) {
                return rm.invoke(instance, (Object[]) null);
            } else if (property != null) {
                return property.get(instance);
            } else {
                return null;
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new JSONException("Error while reading property " + propertyType.getName() + "." + name, e);
        }
    }

    public Boolean isReadable() {
        Method rm = getReadMethod();
        return rm != null && !Modifier.isStatic(rm.getModifiers()) || property != null && !Modifier.isStatic(property.getModifiers()) && !Modifier.isTransient(property.getModifiers());
    }

    public Boolean isWritable() {
        Method wm = getWriteMethod();
        return wm != null || property != null && Modifier.isPublic(property.getModifiers()) && !Modifier.isTransient(property.getModifiers());
    }

    public Boolean isTransient() {
        return property != null && Modifier.isTransient(property.getModifiers());
    }

    protected boolean isNonProperty() {
        return getReadMethod() == null && getWriteMethod() == null && !Modifier.isPublic(property.getModifiers());
    }

    public Transformer getTransformer() throws InstantiationException, IllegalAccessException {
        return transformer != null ? transformer.get() : null;
    }

    public ObjectFactory getObjectFactory() throws InstantiationException, IllegalAccessException {
        return objectFactory != null ? objectFactory.get() : null;
    }
}
