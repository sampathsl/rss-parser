package com.gifted.rss.util;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DataInValidator.class)
@Target({ElementType.TYPE, ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataIn {
    String[] anyOf();

    String message() default "error.wrongInputData";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}