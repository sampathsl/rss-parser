/**
 * @author  Sampath Thennakoon
 * @version 1.0
 * @since   2021-10-20
 */

package com.gifted.rss.util;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class DataInValidator implements ConstraintValidator<DataIn, String> {
    private String[] inputData;

    /**
     * Initiate the acceptable request parameter data
     * @param data the {@link DataIn} object with acceptable values
     */
    @Override
    public void initialize(DataIn data) {
        this.inputData = data.anyOf();
    }

    /**
     * Validate the incoming request parameter data
     * @param value the incoming request parameter data
     * @param context the {@link ConstraintValidatorContext} object
     * @return the boolean value whether the incoming request parameter data valid or not
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Arrays.asList(inputData).contains(value);
    }
}