package com.codeless.api.automation.security.validation;

import com.codeless.api.automation.exception.ApiException;
import org.springframework.http.HttpStatus;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Objects;

@Documented
@Constraint(validatedBy = ValidUsername.Validator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUsername {

    String message() default "Invalid username";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<ValidUsername, String> {

        @Override
        public void initialize(ValidUsername constraintAnnotation) {
            // do nothing club
        }

        @Override
        public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
            if (Objects.isNull(username)) {
                throw new ApiException("Parameter 'username' cannot be null.", HttpStatus.BAD_REQUEST.value());
            }
            if (username.isEmpty()) {
                throw new ApiException("Parameter 'username' parameter cannot be empty.", HttpStatus.BAD_REQUEST.value());
            }
            if (username.length() < 5) {
                throw new ApiException("Parameter 'username' should have size > 5.", HttpStatus.BAD_REQUEST.value());
            }
            return true;
        }
    }
}
