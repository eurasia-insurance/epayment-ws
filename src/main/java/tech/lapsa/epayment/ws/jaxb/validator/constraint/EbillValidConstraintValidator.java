package tech.lapsa.epayment.ws.jaxb.validator.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidationException;

import com.lapsa.epayment.facade.EpaymentFacade;
import com.lapsa.utils.BeanUtils;

import tech.lapsa.epayment.ws.jaxb.entity.XmlEbillShort;
import tech.lapsa.epayment.ws.jaxb.validator.EbillValid;

public class EbillValidConstraintValidator implements ConstraintValidator<EbillValid, XmlEbillShort> {

    @Override
    public void initialize(EbillValid constraintAnnotation) {
    }

    @Override
    public boolean isValid(XmlEbillShort value, ConstraintValidatorContext context) {
	if (value == null)
	    return true;
	try {
	    BeanUtils.lookup(EpaymentFacade.class) //
		    .orElseThrow(() -> new ValidationException("Cannot find an instance of EpaymentFacade")) //
		    .newEbillFetcherBuilder() //
		    .usingId(value.getId()); // it should throws
						 // IllegalArgumentException on
						 // invalid id or nonexistent
						 // entity
	    return true;
	} catch (IllegalArgumentException e) {
	    return false;
	}
    }
}
