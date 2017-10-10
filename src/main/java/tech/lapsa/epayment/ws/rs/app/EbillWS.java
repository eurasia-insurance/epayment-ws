package tech.lapsa.epayment.ws.rs.app;

import static com.lapsa.utils.RESTUtils.*;

import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import javax.ejb.EJB;
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

import com.lapsa.epayment.facade.Ebill;
import com.lapsa.epayment.facade.EpaymentFacade;
import com.lapsa.validation.NotNullValue;

import tech.lapsa.epayment.ws.jaxb.entity.EbillMethodType;
import tech.lapsa.epayment.ws.jaxb.entity.EbillStatus;
import tech.lapsa.epayment.ws.jaxb.entity.XmlEbillInfo;
import tech.lapsa.epayment.ws.jaxb.entity.XmlEbillMethod;
import tech.lapsa.epayment.ws.jaxb.entity.XmlEbillPurpose;
import tech.lapsa.epayment.ws.jaxb.entity.XmlEbillPurposeItem;
import tech.lapsa.epayment.ws.jaxb.entity.XmlEbillResult;
import tech.lapsa.epayment.ws.jaxb.entity.XmlEbillShort;
import tech.lapsa.epayment.ws.jaxb.entity.XmlHttpForm;
import tech.lapsa.epayment.ws.jaxb.entity.XmlHttpFormParam;
import tech.lapsa.epayment.ws.jaxb.validator.EbillValid;

@Path("/" + WSPathNames.WS_EBILL)
@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Singleton
public class EbillWS extends ALanguageDetectorWS {

    @Context
    private UriInfo uriInfo;

    @POST
    @Path("/" + WSPathNames.WS_EBILL_FETCH)
    public Response fetchEbillPOST(@NotNullValue @EbillValid @Valid XmlEbillShort request) {
	return fetchEbill(request);
    }

    private Response fetchEbill(XmlEbillShort request) {
	try {
	    XmlEbillInfo reply = _fetchEbill(request);
	    return responseOk(reply, getLocaleOrDefault());
	} catch (WrongArgumentException e) {
	    return responseBadRequest(e.getMessage(), getLocaleOrDefault());
	} catch (ServerException e) {
	    return responseServerError(e.getMessage(), getLocaleOrDefault());
	}
    }

    @EJB
    private EpaymentFacade facade;

    private XmlEbillInfo _fetchEbill(XmlEbillShort request)
	    throws WrongArgumentException, ServerException {

	Ebill m = facade.newEbillBuilder() //
		.withFetched(request.getId()) //
		.withPostbackURI(uriInfo.getBaseUriBuilder() //
			.path(WSPathNames.WS_QAZKOM) //
			.path(WSPathNames.WS_QAZKOM_OK) //
			.build())
		.build();

	XmlEbillInfo response = new XmlEbillInfo();
	response.setId(m.getId());
	response.setCreated(m.getCreated());
	response.setAmount(m.getAmount());
	response.setUserLanguage(m.getConsumerLanguage());

	XmlEbillPurpose purpose = new XmlEbillPurpose(
		m.getItems().stream()
			.map(item -> new XmlEbillPurposeItem(item.getName(), item.getAmount(), item.getQuantity()))
			.toArray(XmlEbillPurposeItem[]::new));
	response.setPurpose(purpose);

	switch (m.getStatus()) {
	case READY:
	    response.setStatus(EbillStatus.READY);

	    Builder<XmlEbillMethod> builder = Stream.builder(); //
	{ // qazkom method
	    XmlHttpForm form = new XmlHttpForm();
	    form.setUrl(m.getForm().getURL());
	    form.setMethod(m.getForm().getMethod());
	    form.setParams(m.getForm().getParams() //
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
	    response.setResult(new XmlEbillResult(EbillMethodType.QAZKOM, m.getReference()));
	    break;
	default:
	    throw new ServerException(String.format("Invalid payment status '%1$s'", m.getStatus()));
	}

	return response;
    }
}
