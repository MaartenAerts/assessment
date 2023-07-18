package be.maartenaerts.assessment;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.*;

@Pattern(regexp = "[a-zA-Z ]+", message = "Only alphabetic or spaces allowed")
@Documented
@Constraint(validatedBy = {})
@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface OnlyAlphabeticOrSpaces {
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
