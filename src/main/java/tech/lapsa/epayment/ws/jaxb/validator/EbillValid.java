package tech.lapsa.epayment.ws.jaxb.validator;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import tech.lapsa.epayment.ws.jaxb.validator.constraint.EbillValidConstraintValidator;

@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = EbillValidConstraintValidator.class)
public @interface EbillValid {

    String message() default ValidationMessages.EBILL_VALID;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
