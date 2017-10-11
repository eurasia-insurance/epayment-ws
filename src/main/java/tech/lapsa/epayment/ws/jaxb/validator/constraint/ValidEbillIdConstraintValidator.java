package tech.lapsa.epayment.ws.jaxb.validator.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidationException;

import com.lapsa.utils.BeanUtils;

import tech.lapsa.epayment.facade.EpaymentFacade;
import tech.lapsa.epayment.ws.jaxb.validator.ValidEbillId;

public class ValidEbillIdConstraintValidator implements ConstraintValidator<ValidEbillId, String> {

    @Override
    public void initialize(ValidEbillId constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
	if (value == null)
	    return true;
	try {
	    BeanUtils.lookup(EpaymentFacade.class) //
		    .orElseThrow(() -> new ValidationException("Cannot find an instance of EpaymentFacade")) //
		    .newEbillFetcherBuilder() //
		    .usingId(value); // it should throws
				     // IllegalArgumentException on
				     // invalid id or nonexistent
				     // entity
	    return true;
	} catch (IllegalArgumentException e) {
	    return false;
	}
    }
}
