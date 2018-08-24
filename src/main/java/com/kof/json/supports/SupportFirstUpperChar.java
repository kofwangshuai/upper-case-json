package com.kof.json.supports;

import com.fasterxml.jackson.annotation.JacksonAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.FIELD,
        ElementType.PARAMETER, // since 2.5
        ElementType.TYPE // since 2.9, to indicate "default view" for properties
})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotation
public @interface SupportFirstUpperChar  {
    public Class<?>[] value() default { };

}
