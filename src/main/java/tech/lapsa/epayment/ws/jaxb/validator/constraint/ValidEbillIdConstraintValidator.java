package tech.lapsa.epayment.ws.jaxb.validator.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidationException;

import tech.lapsa.epayment.facade.EpaymentFacade;
import tech.lapsa.epayment.facade.InvoiceNotFound;
import tech.lapsa.epayment.ws.jaxb.validator.ValidEbillId;
import tech.lapsa.javax.cdi.utility.BeanUtils;

//TODO REFACTOR : Need to rename to ValidInvoiceNumberConstraintValidator
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
		    .orElseThrow(() -> new ValidationException("Cannot find an instance of " + EpaymentFacade.class)) //
		    .forNumber(value);
	    return true;
	} catch (IllegalArgumentException e) {
	    return false;
	} catch (InvoiceNotFound e) {
	    return false;
	}
    }
}
