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
import tech.lapsa.epayment.domain.QazkomPayment;
import tech.lapsa.epayment.facade.EpaymentFacade;
import tech.lapsa.epayment.facade.InvoiceNotFound;
import tech.lapsa.epayment.facade.PaymentMethod.Http;
import tech.lapsa.epayment.facade.QazkomFacade;
import tech.lapsa.epayment.ws.auth.EpaymentSecurity;
import tech.lapsa.epayment.ws.jaxb.entity.EbillStatus;
import tech.lapsa.epayment.ws.jaxb.entity.XmlEbillInfo;
import tech.lapsa.epayment.ws.jaxb.entity.XmlEbillMethod;
import tech.lapsa.epayment.ws.jaxb.entity.XmlEbillPayer;
import tech.lapsa.epayment.ws.jaxb.entity.XmlEbillPayment;
import tech.lapsa.epayment.ws.jaxb.entity.XmlEbillPurposeItem;
import tech.lapsa.epayment.ws.jaxb.entity.XmlEbillRequest;
import tech.lapsa.epayment.ws.jaxb.entity.XmlEbillResult;
import tech.lapsa.epayment.ws.jaxb.entity.XmlHttpForm;
import tech.lapsa.epayment.ws.jaxb.entity.XmlHttpFormParam;
import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.javax.rs.utility.InternalServerErrorException;
import tech.lapsa.javax.rs.utility.WrongArgumentException;
import tech.lapsa.javax.validation.NotNullValue;

@Path("/" + WSPathNames.WS_EBILL)
@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@RolesAllowed({ EpaymentSecurity.ROLE_ADMIN, EpaymentSecurity.ROLE_ROBOT })
@Singleton
public class EbillWS extends ABaseWS {

    @Context
    private UriInfo uriInfo;

    @POST
    @Path("/" + WSPathNames.WS_EBILL_FETCH)
    public Response fetchEbillPOST(@NotNullValue @Valid final XmlEbillRequest request) {
	return fetchEbill(request);
    }

    private Response fetchEbill(final XmlEbillRequest request) {
	try {
	    final XmlEbillInfo reply = _fetchEbill(request);
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

    private XmlEbillInfo _fetchEbill(final XmlEbillRequest request)
	    throws WrongArgumentException, InternalServerErrorException {
	try {

	    Invoice i;
	    try {
		i = reThrowAsUnchecked(() -> epayments.forNumber(request.getId()));
	    } catch (final InvoiceNotFound e) {
		// this is because invoice number must be validated and checked
		// at this point
		throw new InternalServerErrorException(e);
	    } catch (final IllegalArgumentException e) {
		// this is because invoice number must be validated and checked
		// at this point
		throw new InternalServerErrorException(e);
	    }

	    final XmlEbillInfo response = new XmlEbillInfo();
	    response.setId(i.getNumber());
	    response.setCreated(i.getCreated());

	    final XmlEbillPayer payer = new XmlEbillPayer(i.getConsumerName(), i.getConsumerEmail());
	    response.setPayer(payer);

	    final XmlEbillPayment payment = new XmlEbillPayment( //
		    i.getAmount(), //
		    i.getItems().stream()
			    .map(item -> new XmlEbillPurposeItem(item.getName(), item.getPrice(), item.getQuantity(),
				    item.getTotal()))
			    .toArray(XmlEbillPurposeItem[]::new),
		    i.getExternalId());
	    response.setPayment(payment);

	    switch (i.getStatus()) {
	    case PENDING:
		response.setStatus(EbillStatus.READY);

		final Builder<XmlEbillMethod> builder = Stream.builder(); //
	    {
		Http http;

		final URI uri = uriInfo.getBaseUriBuilder() //
			.path(WSPathNames.WS_QAZKOM) //
			.path(WSPathNames.WS_QAZKOM_OK) //
			.build();
		try {
		    http = reThrowAsUnchecked(() -> qazkoms.httpMethod(uri, request.getReturnUri(), i) //
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
		final XmlEbillMethod qazkomMethod = new XmlEbillMethod(PaymentMethod.QAZKOM, form);
		builder.accept(qazkomMethod);
	    }

		response.setAvailableMethods(builder.build().toArray(XmlEbillMethod[]::new));

		break;
	    case EXPIRED:
		response.setStatus(EbillStatus.CANCELED);
		break;
	    case PAID:
		response.setStatus(EbillStatus.PAID);
		response.setPaid(i.getPayment().getCreated());
		switch (i.getPayment().getMethod()) {
		case QAZKOM:
		    final QazkomPayment qp = MyObjects.requireA(i.getPayment(), QazkomPayment.class);
		    response.setResult(new XmlEbillResult(PaymentMethod.QAZKOM, qp.getReference(), i.getCreated()));
		}
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
}
