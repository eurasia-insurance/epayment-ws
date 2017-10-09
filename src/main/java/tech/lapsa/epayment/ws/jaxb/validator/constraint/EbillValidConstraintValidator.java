package tech.lapsa.epayment.ws.jaxb.validator.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.lapsa.kkb.dao.KKBEntityNotFound;
import com.lapsa.kkb.dao.KKBOrderDAO;
import com.lapsa.kkb.dao.KKBPeristenceOperationFailed;
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
	    KKBOrderDAO orderDAO = BeanUtils.getBean(KKBOrderDAO.class);
	    orderDAO.findByIdByPassCache(value.getId());
	} catch (KKBPeristenceOperationFailed e) {
	    // Проверка должна быть пройдена, т.к. до сути проверки
	    // не дошло
	} catch (KKBEntityNotFound e) {
	    return false;
	}

	return true;
    }

}
