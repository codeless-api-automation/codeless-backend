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
@Constraint(validatedBy = ValidPassword.Validator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {

    String message() default "Invalid password";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<ValidPassword, String> {

        @Override
        public void initialize(ValidPassword constraintAnnotation) {
            // do nothing club
        }

        @Override
        public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
            if (Objects.isNull(password)) {
                throw new ApiException("Parameter 'password' cannot be null.", HttpStatus.BAD_REQUEST.value());
            }
            if (password.isEmpty()) {
                throw new ApiException("Parameter 'password' parameter cannot be empty.", HttpStatus.BAD_REQUEST.value());
            }
            if (password.length() < 5) {
                throw new ApiException("Parameter 'password' should have size > 5.", HttpStatus.BAD_REQUEST.value());
            }
            return true;
        }
    }
}
