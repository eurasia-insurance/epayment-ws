package tech.lapsa.epayment.ws.jaxb.validator.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

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
	    EpaymentFacade facade = BeanUtils.getBean(EpaymentFacade.class);
	    facade.newEbillBuilder().withFetched(value.getId()).build();
	    return true;
	} catch (IllegalArgumentException e) {
	    return false;
	} catch (NullPointerException e) {
	    return false;
	}
    }

}
