package tech.lapsa.epayment.ws.jaxb.validator.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidationException;

import tech.lapsa.epayment.facade.EpaymentFacade.EpaymentFacadeRemote;
import tech.lapsa.epayment.ws.jaxb.validator.ValidInvoiceNumber;
import tech.lapsa.java.commons.exceptions.IllegalArgument;
import tech.lapsa.java.commons.naming.MyNaming;

public class ValidInvoiceNumberConstraintValidator implements ConstraintValidator<ValidInvoiceNumber, String> {

    @Override
    public void initialize(final ValidInvoiceNumber constraintAnnotation) {
    }

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) throws ValidationException {
	if (value == null)
	    return true;

	final EpaymentFacadeRemote epayments = MyNaming.lookupEJB(ValidationException::new,
		EpaymentFacadeRemote.APPLICATION_NAME,
		EpaymentFacadeRemote.MODULE_NAME,
		EpaymentFacadeRemote.BEAN_NAME,
		EpaymentFacadeRemote.class);
	try {
	    return epayments.hasInvoiceWithNumber(value);
	} catch (final IllegalArgument e) {
	    return false;
	}
    }
}
