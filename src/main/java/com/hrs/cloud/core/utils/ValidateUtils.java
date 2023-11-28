package com.hrs.cloud.core.utils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ValidateUtils {

    private static ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

    public static <T> List<String> validate( T t ) {
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> constraintViolations = validator.validate( t );
        List<String> messageList = new ArrayList<>();
        for ( ConstraintViolation<T> constraintViolation : constraintViolations ) {
            messageList.add( constraintViolation.getMessage() );
        }
        return messageList;
    }
    
    
    
    public static <T> List<String> validate( T t ,Class<?>... groups) {
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> constraintViolations = validator.validate( t, groups );
        List<String> messageList = new ArrayList<>();
        for ( ConstraintViolation<T> constraintViolation : constraintViolations ) {
            messageList.add( constraintViolation.getMessage() );
        }
        return messageList;
    }
    
    
}
