package swd.fpt.exegroupingmanagement.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import swd.fpt.exegroupingmanagement.dto.request.RegisterRequest;
import swd.fpt.exegroupingmanagement.exception.ErrorCode;

public class ConfirmPasswordValidator implements ConstraintValidator<ConfirmPasswordConstraint, RegisterRequest> {
    @Override
    public void initialize(ConfirmPasswordConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(RegisterRequest registerRequest, ConstraintValidatorContext context) {
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorCode.INVALID_CONFIRM_PASSWORD.getMessage())
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
