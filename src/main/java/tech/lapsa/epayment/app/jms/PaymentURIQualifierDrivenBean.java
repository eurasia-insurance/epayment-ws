package tech.lapsa.epayment.app.jms;

import static tech.lapsa.java.commons.function.MyExceptions.*;

import java.net.URI;
import java.util.Properties;

import javax.ejb.MessageDriven;
import javax.inject.Inject;

import tech.lapsa.epayment.domain.Invoice;
import tech.lapsa.epayment.facade.EpaymentFacade;
import tech.lapsa.epayment.facade.InvoiceNotFound;
import tech.lapsa.epayment.shared.entity.XmlPaymentURIQualifierRequest;
import tech.lapsa.epayment.shared.entity.XmlPaymentURIQualifierResponse;
import tech.lapsa.epayment.shared.jms.EpaymentDestinations;
import tech.lapsa.javax.jms.CallableServiceDrivenBean;

@MessageDriven(mappedName = EpaymentDestinations.PAYMENT_URI_QUALIFIER)
public class PaymentURIQualifierDrivenBean
	extends CallableServiceDrivenBean<XmlPaymentURIQualifierRequest, XmlPaymentURIQualifierResponse> {

    public PaymentURIQualifierDrivenBean() {
	super(XmlPaymentURIQualifierRequest.class);
    }

    @Inject
    private EpaymentFacade epayments;

    @Override
    public XmlPaymentURIQualifierResponse calling(XmlPaymentURIQualifierRequest request, Properties properties) {
	return reThrowAsUnchecked(() -> {
	    try {
		final Invoice invoice = epayments.invoiceByNumber(request.getInvoiceNumber());
		final URI uri = epayments.getDefaultPaymentURI(invoice);
		return new XmlPaymentURIQualifierResponse(uri);
	    } catch (InvoiceNotFound e) {
		throw new RuntimeException(e);
	    }
	});
    }

}
