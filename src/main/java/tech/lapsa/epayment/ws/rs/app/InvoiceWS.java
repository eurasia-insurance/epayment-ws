package tech.lapsa.epayment.ws.rs.app;

import static tech.lapsa.java.commons.function.MyExceptions.*;
import static tech.lapsa.javax.rs.utility.RESTUtils.*;

import java.net.URI;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import tech.lapsa.epayment.domain.Invoice;
import tech.lapsa.epayment.domain.PaymentMethod;
import tech.lapsa.epayment.facade.EpaymentFacade;
import tech.lapsa.epayment.facade.InvoiceNotFound;
import tech.lapsa.epayment.facade.PaymentMethod.Http;
import tech.lapsa.epayment.facade.QazkomFacade;
import tech.lapsa.epayment.ws.auth.EpaymentSecurity;
import tech.lapsa.epayment.ws.jaxb.entity.InvoiceStatus;
import tech.lapsa.epayment.ws.jaxb.entity.XmlInvoiceInfo;
import tech.lapsa.epayment.ws.jaxb.entity.XmlPaymentMethod;
import tech.lapsa.epayment.ws.jaxb.entity.XmlPayer;
import tech.lapsa.epayment.ws.jaxb.entity.XmlPayment;
import tech.lapsa.epayment.ws.jaxb.entity.XmlInvoicePurposeItem;
import tech.lapsa.epayment.ws.jaxb.entity.XmlInvoiceFetchRequest;
import tech.lapsa.epayment.ws.jaxb.entity.XmlPaymentResult;
import tech.lapsa.epayment.ws.jaxb.entity.XmlHttpForm;
import tech.lapsa.epayment.ws.jaxb.entity.XmlHttpFormParam;
import tech.lapsa.javax.rs.utility.InternalServerErrorException;
import tech.lapsa.javax.rs.utility.WrongArgumentException;
import tech.lapsa.javax.validation.NotNullValue;

@Path("/" + WSPathNames.WS_INVOICE)
@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@RolesAllowed({ EpaymentSecurity.ROLE_ADMIN, EpaymentSecurity.ROLE_ROBOT })
@Singleton
public class InvoiceWS extends ABaseWS {

    @Context
    private UriInfo uriInfo;

    @POST
    @Path("/" + WSPathNames.WS_INVOICE_FETCH)
    public Response fetchInvoicePOST(@NotNullValue @Valid final XmlInvoiceFetchRequest request) {
	return fetchInvoice(request);
    }

    private Response fetchInvoice(final XmlInvoiceFetchRequest request) {
	try {
	    final XmlInvoiceInfo reply = _fetchInvoice(request);
	    return responseOk(reply, getLocaleOrDefault());
	} catch (final WrongArgumentException e) {
	    mailApplicationErrorAdmin(e, null);
	    return responseWrongArgument(e, getLocaleOrDefault());
	} catch (final InternalServerErrorException e) {
	    mailServerErrorAdmin(e, null);
	    return responseInternalServerError(e, getLocaleOrDefault());
	}
    }

    @Inject
    private EpaymentFacade epayments;

    @Inject
    private QazkomFacade qazkoms;

    private XmlInvoiceInfo _fetchInvoice(final XmlInvoiceFetchRequest request)
	    throws WrongArgumentException, InternalServerErrorException {
	try {

	    Invoice i;
	    try {
		i = reThrowAsUnchecked(() -> epayments.invoiceByNumber(request.getId()));
	    } catch (final InvoiceNotFound e) {
		// this is because invoice number must be validated and checked
		// at this point
		throw new InternalServerErrorException(e);
	    } catch (final IllegalArgumentException e) {
		// this is because invoice number must be validated and checked
		// at this point
		throw new InternalServerErrorException(e);
	    }

	    final XmlInvoiceInfo response = new XmlInvoiceInfo();
	    response.setId(i.getNumber());
	    response.setCreated(i.getCreated());

	    final XmlPayer payer = new XmlPayer(i.getConsumerName(), i.getConsumerEmail());
	    response.setPayer(payer);

	    final XmlPayment payment = new XmlPayment( //
		    i.getAmount(), //
		    i.getItems().stream()
			    .map(item -> new XmlInvoicePurposeItem(item.getName(), item.getPrice(), item.getQuantity(),
				    item.getTotal()))
			    .toArray(XmlInvoicePurposeItem[]::new),
		    i.getExternalId());
	    response.setPayment(payment);

	    switch (i.getStatus()) {
	    case PENDING:
		response.setStatus(InvoiceStatus.READY);
		final Builder<XmlPaymentMethod> builder = Stream.builder();

		// only one method supported at this time
		builder.accept(qazkomPaymentMethod(request.getReturnUri(), i));

		response.setAvailableMethods(builder.build().toArray(XmlPaymentMethod[]::new));
		break;
	    case EXPIRED:
		response.setStatus(InvoiceStatus.CANCELED);
		break;
	    case PAID:
		response.setStatus(InvoiceStatus.PAID);
		response.setPaid(i.getPayment().getCreated());
		final XmlPaymentResult result = new XmlPaymentResult(i.getPayment().getMethod(),
			i.getPayment().getReferenceNumber(), i.getCreated());
		response.setResult(result);
		break;
	    default:
		throw new InternalServerErrorException(String.format("Invalid payment status '%1$s'", i.getStatus()));
	    }
	    return response;
	} catch (final IllegalArgumentException e) {
	    throw new WrongArgumentException(e);
	} catch (final RuntimeException e) {
	    throw new InternalServerErrorException(e);
	}
    }

    private XmlPaymentMethod qazkomPaymentMethod(final URI returnURI, final Invoice invoice)
	    throws InternalServerErrorException {

	final URI uri = uriInfo.getBaseUriBuilder() //
		.path(WSPathNames.WS_QAZKOM) //
		.path(WSPathNames.WS_QAZKOM_OK) //
		.build();

	final Http http;
	try {
	    http = reThrowAsUnchecked(() -> qazkoms.httpMethod(uri, returnURI, invoice) //
		    .getHttp());
	} catch (final IllegalArgumentException e) {
	    // this is because something goes wrong
	    throw new InternalServerErrorException(e);
	}

	final XmlHttpForm form = new XmlHttpForm();
	form.setUri(http.getHttpAddress());
	form.setMethod(http.getHttpMethod());
	form.setParams(http.getHttpParams() //
		.entrySet() //
		.stream() //
		.map(x -> new XmlHttpFormParam(x.getKey(), x.getValue())) //
		.toArray(XmlHttpFormParam[]::new));
	return new XmlPaymentMethod(PaymentMethod.QAZKOM, form);
    }
}
