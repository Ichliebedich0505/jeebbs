package edu.scut.jeebbs.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Validate bean use JSR303
 */
public class BeanValidators {

    /**
     * If validate is failed throws {@link ConstraintViolationException}
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void validateWithException(Validator validator,
                                             Object object,
                                             Class<?>... groups) throws ConstraintViolationException {
        Set constraintViolations = validator.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }


    /**
     * Help method to extract error message from {@link ConstraintViolationException}
     */
    public static List<String> extractMessage(ConstraintViolationException e) {
        return extractMessage(e.getConstraintViolations());
    }

    /**
     * Help method to extract error messages from {@link ConstraintViolation} set
     */
    @SuppressWarnings("rawtypes")
    public static List<String> extractMessage(Set<? extends ConstraintViolation> constraintViolations) {
        List<String> errorMessages = Lists.newArrayList();
        for (ConstraintViolation violation: constraintViolations) {
            errorMessages.add(violation.getMessage());
        }
        return errorMessages;
    }

    /**
     * Help method to extract error messages and caused field from {@link ConstraintViolationException}
     */
    public static Map<String, String> extractPropertyAndMessage(ConstraintViolationException e) {
        return extractPropertyAndMessage(e.getConstraintViolations());
    }

    /**
     * Help method to extract error messages and caused field from {@link ConstraintViolation} set
     */
    @SuppressWarnings("rawtypes")
    public static Map<String, String> extractPropertyAndMessage(Set<? extends ConstraintViolation> constraintViolations) {
        Map<String, String> errorMessages = Maps.newHashMap();
        for (ConstraintViolation violation : constraintViolations) {
            errorMessages.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        return errorMessages;
    }

    /**
     * Help method to change {@link ConstraintViolationException} to list [propertyPath +" "+ message]
     */
    public static List<String> extractPropertyAndMessageAsList(ConstraintViolationException e) {
        return extractPropertyAndMessageAsList(e.getConstraintViolations(), " ");
    }

    /**
     * Help method to change {@link ConstraintViolationException} to list [propertyPath +separator+ message]
     */
    public static List<String> extractPropertyAndMessageAsList(ConstraintViolationException e, String separator) {
        return extractPropertyAndMessageAsList(e.getConstraintViolations(), separator);
    }

    /**
     * Help method to change {@link ConstraintViolation} set to list [propertyPath +" "+ message]
     */
    @SuppressWarnings("rawtypes")
    public static List<String> extractPropertyAndMessageAsList(Set<? extends ConstraintViolation> constraintViolations) {
        return extractPropertyAndMessageAsList(constraintViolations, " ");
    }

    /**
     * Help method to change {@link ConstraintViolation} set to list [propertyPath +separator+ message]
     */
    @SuppressWarnings("rawtypes")
    public static List<String> extractPropertyAndMessageAsList(Set<? extends ConstraintViolation> constraintViolations,
                                                               String separator) {
        List<String> errorMessages = Lists.newArrayList();
        for (ConstraintViolation violation : constraintViolations) {
            errorMessages.add(violation.getPropertyPath() + separator + violation.getMessage());
        }
        return errorMessages;
    }
}
