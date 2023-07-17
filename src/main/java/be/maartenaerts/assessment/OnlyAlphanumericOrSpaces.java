package be.maartenaerts.assessment;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.*;

@Pattern(regexp = "[a-zA-Z ]+", message = "Only alphanumeric or spaces allowed")
@Documented
@Constraint(validatedBy = {})
@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface OnlyAlphanumericOrSpaces {
    String message() default "Invalid phone number";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
