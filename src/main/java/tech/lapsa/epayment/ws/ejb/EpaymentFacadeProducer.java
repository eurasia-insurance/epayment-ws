package tech.lapsa.epayment.ws.ejb;

import javax.ejb.EJB;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;

import tech.lapsa.epayment.facade.EpaymentFacade;
import tech.lapsa.epayment.facade.EpaymentFacade.EpaymentFacadeRemote;
import tech.lapsa.javax.cdi.qualifiers.QDelegateToEJB;

@Dependent
public class EpaymentFacadeProducer {

    @EJB
    private EpaymentFacadeRemote ejb;

    @Produces
    @QDelegateToEJB
    public EpaymentFacade getEjb() {
	return ejb;
    }
}
