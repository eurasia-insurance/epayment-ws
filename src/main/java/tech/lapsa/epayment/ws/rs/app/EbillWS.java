package tech.lapsa.epayment.ws.rs.app;

import static tech.lapsa.javax.rs.utility.RESTUtils.*;

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
import tech.lapsa.javax.validation.NotNullValue;

@Path("/" + WSPathNames.WS_EBILL)
@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@RolesAllowed({ EpaymentSecurity.ROLE_ADMIN, EpaymentSecurity.ROLE_ROBOT })
@Singleton
public class EbillWS extends ALanguageDetectorWS {

    @Context
    private UriInfo uriInfo;

    @POST
    @Path("/" + WSPathNames.WS_EBILL_FETCH)
    public Response fetchEbillPOST(@NotNullValue @Valid XmlEbillRequest request) {
	return fetchEbill(request);
    }

    private Response fetchEbill(XmlEbillRequest request) {
	try {
	    XmlEbillInfo reply = _fetchEbill(request);
	    return responseOk(reply, getLocaleOrDefault());
	} catch (WrongArgumentException e) {
	    return responseBadRequest(e.getMessage(), getLocaleOrDefault());
	} catch (ServerException e) {
	    return responseServerError(e.getMessage(), getLocaleOrDefault());
	}
    }

    @Inject
    private EpaymentFacade facade;

    @Inject
    private QazkomFacade qazkom;

    private XmlEbillInfo _fetchEbill(XmlEbillRequest request)
	    throws WrongArgumentException, ServerException {

	Invoice m = facade.forNumber(request.getId());

	XmlEbillInfo response = new XmlEbillInfo();
	response.setId(m.getNumber());
	response.setCreated(m.getCreated());

	XmlEbillPayer payer = new XmlEbillPayer(m.getConsumerName(), m.getConsumerEmail());
	response.setPayer(payer);

	XmlEbillPayment payment = new XmlEbillPayment( //
		m.getAmount(), //
		m.getItems().stream()
			.map(item -> new XmlEbillPurposeItem(item.getName(), item.getPrice(), item.getQuantity(),
				item.getTotal()))
			.toArray(XmlEbillPurposeItem[]::new),
		m.getExternalId());
	response.setPayment(payment);

	switch (m.getStatus()) {
	case READY:
	    response.setStatus(EbillStatus.READY);

	    Builder<XmlEbillMethod> builder = Stream.builder(); //
	// TODO qazkom method
	{
	    Http http = qazkom.httpMethod(uriInfo.getBaseUriBuilder() //
		    .path(WSPathNames.WS_QAZKOM) //
		    .path(WSPathNames.WS_QAZKOM_OK) //
		    .build(), request.getReturnUri(), m) //
		    .getHttp();

	    XmlHttpForm form = new XmlHttpForm();
	    form.setUri(http.getHttpAddress());
	    form.setMethod(http.getHttpMethod());
	    form.setParams(http.getHttpParams() //
		    .entrySet() //
		    .stream() //
		    .map(x -> new XmlHttpFormParam(x.getKey(), x.getValue())) //
		    .toArray(XmlHttpFormParam[]::new));
	    XmlEbillMethod qazkomMethod = new XmlEbillMethod(PaymentMethod.QAZKOM, form);
	    builder.accept(qazkomMethod);
	}

	    response.setAvailableMethods(builder.build().toArray(XmlEbillMethod[]::new));

	    break;
	case EXPIRED:
	    response.setStatus(EbillStatus.CANCELED);
	    break;
	case PAID:
	    response.setStatus(EbillStatus.PAID);
	    response.setPaid(m.getPayment().getCreated());
	    switch (m.getPayment().getMethod()) {
	    case QAZKOM:
		QazkomPayment qp = MyObjects.requireA(m.getPayment(), QazkomPayment.class);
		response.setResult(new XmlEbillResult(PaymentMethod.QAZKOM, qp.getReference(), m.getCreated()));
	    }
	    break;
	default:
	    throw new ServerException(String.format("Invalid payment status '%1$s'", m.getStatus()));
	}

	return response;
    }
}
