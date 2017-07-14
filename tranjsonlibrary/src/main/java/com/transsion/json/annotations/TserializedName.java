package com.transsion.json.annotations;

import com.transsion.json.ObjectFactory;
import com.transsion.json.transformer.Transformer;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(value = RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE, ElementType.METHOD})
public @interface TserializedName {
    String name() default "";
    boolean include() default true;
    Class<? extends Transformer> transformer() default Transformer.class;
    Class<? extends ObjectFactory> objectFactory() default ObjectFactory.class;

}
