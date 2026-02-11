package com.kkarimi.eventmanagement.metrics;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MeasuredOperation {

    String timer() default "";

    String successCounter() default "";

    String failureCounter() default "";
}
