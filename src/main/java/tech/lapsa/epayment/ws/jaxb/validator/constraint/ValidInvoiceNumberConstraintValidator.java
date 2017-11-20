package tech.lapsa.epayment.ws.jaxb.validator.constraint;

import static tech.lapsa.java.commons.function.MyExceptions.*;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidationException;

import tech.lapsa.epayment.facade.EpaymentFacade;
import tech.lapsa.epayment.facade.InvoiceNotFound;
import tech.lapsa.epayment.ws.jaxb.validator.ValidInvoiceNumber;
import tech.lapsa.javax.cdi.utility.BeanUtils;

public class ValidInvoiceNumberConstraintValidator implements ConstraintValidator<ValidInvoiceNumber, String> {

    @Override
    public void initialize(final ValidInvoiceNumber constraintAnnotation) {
    }

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
	if (value == null)
	    return true;
	try {
	    reThrowAsUnchecked(() -> {
		BeanUtils.lookup(EpaymentFacade.class) //
			.orElseThrow(
				() -> new ValidationException("Cannot find an instance of " + EpaymentFacade.class)) //
			.invoiceByNumber(value);
	    });
	    return true;
	} catch (final IllegalArgumentException e) {
	    return false;
	} catch (final InvoiceNotFound e) {
	    return false;
	}
    }
}
