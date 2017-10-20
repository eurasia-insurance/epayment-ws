package tech.lapsa.epayment.ws.rs.app;

import static tech.lapsa.javax.rs.utility.RESTUtils.*;

import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

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

import tech.lapsa.epayment.facade.Ebill;
import tech.lapsa.epayment.facade.EpaymentFacade;
import tech.lapsa.epayment.facade.QazkomFacade;
import tech.lapsa.epayment.facade.QazkomFacade.PaymentMethodBuilder;
import tech.lapsa.epayment.facade.QazkomFacade.PaymentMethodBuilder.PaymentMethod.HttpMethod;
import tech.lapsa.epayment.ws.jaxb.entity.EbillMethodType;
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
import tech.lapsa.javax.validation.NotNullValue;

@Path("/" + WSPathNames.WS_EBILL)
@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
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

	Ebill m = facade.newEbillFetcherBuilder() //
		.usingId(request.getId()) //
		.build() //
		.fetch();

	XmlEbillInfo response = new XmlEbillInfo();
	response.setId(m.getId());
	response.setCreated(m.getCreated());

	XmlEbillPayer payer = new XmlEbillPayer(m.getConsumerName(), m.getConsumerEmail());
	response.setPayer(payer);

	XmlEbillPayment payment = new XmlEbillPayment( //
		m.getAmount(), //
		m.getItems().stream()
			.map(item -> new XmlEbillPurposeItem(item.getName(), item.getPrice(), item.getQuantity(),
				item.getTotalAmount()))
			.toArray(XmlEbillPurposeItem[]::new),
		m.getExternalId());
	response.setPayment(payment);

	switch (m.getStatus()) {
	case READY:
	    response.setStatus(EbillStatus.READY);

	    Builder<XmlEbillMethod> builder = Stream.builder(); //
	{ // qazkom method

	    PaymentMethodBuilder paymentMethodBuilder = qazkom.newPaymentMethodBuilder() //
		    .withPostbackURI(uriInfo.getBaseUriBuilder() //
			    .path(WSPathNames.WS_QAZKOM) //
			    .path(WSPathNames.WS_QAZKOM_OK) //
			    .build()) //
		    .withReturnURI(request.getReturnUri()) //
		    .forEbill(m);

	    getAcceptLanguage().ifPresent(paymentMethodBuilder::withConsumerLanguage);

	    HttpMethod paymentMethod = paymentMethodBuilder
		    .build() //
		    .getHttp();

	    XmlHttpForm form = new XmlHttpForm();
	    form.setUri(paymentMethod.getHttpAddress());
	    form.setMethod(paymentMethod.getHttpMethod());
	    form.setParams(paymentMethod.getHttpParams() //
		    .entrySet() //
		    .stream() //
		    .map(x -> new XmlHttpFormParam(x.getKey(), x.getValue())) //
		    .toArray(XmlHttpFormParam[]::new));
	    XmlEbillMethod qazkomMethod = new XmlEbillMethod(EbillMethodType.QAZKOM, form);
	    builder.accept(qazkomMethod);
	}

	    response.setAvailableMethods(builder.build().toArray(XmlEbillMethod[]::new));

	    break;
	case CANCELED:
	    response.setStatus(EbillStatus.CANCELED);
	    break;
	case FAILED:
	    response.setStatus(EbillStatus.FAILED);
	    break;
	case PAID:
	    response.setStatus(EbillStatus.PAID);
	    response.setPaid(m.getPaid());
	    response.setResult(new XmlEbillResult(EbillMethodType.QAZKOM, m.getReference(), m.getPaid()));
	    break;
	default:
	    throw new ServerException(String.format("Invalid payment status '%1$s'", m.getStatus()));
	}

	return response;
    }
}
