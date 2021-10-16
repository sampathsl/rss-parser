package com.gifted.rss.util;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class DataInValidator implements ConstraintValidator<DataIn, String> {
    private String[] inputData;

    @Override
    public void initialize(DataIn annotation) {
        this.inputData = annotation.anyOf();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return Arrays.asList(inputData).contains(value);
    }
}