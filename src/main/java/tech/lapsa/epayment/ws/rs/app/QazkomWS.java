package tech.lapsa.epayment.ws.rs.app;

import static tech.lapsa.java.commons.function.MyExceptions.*;
import static tech.lapsa.javax.rs.utility.RESTUtils.*;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import tech.lapsa.epayment.domain.Invoice;
import tech.lapsa.epayment.facade.QazkomFacade;
import tech.lapsa.javax.rs.utility.InternalServerErrorException;
import tech.lapsa.javax.rs.utility.WrongArgumentException;

@Path("/" + WSPathNames.WS_QAZKOM)
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.TEXT_PLAIN)
@PermitAll
@Singleton
public class QazkomWS extends ABaseWS {

    @POST
    @Path("/" + WSPathNames.WS_QAZKOM_OK)
    public Response postBackPaymentOK(@FormParam("response") final String response) {
	return postbackPayment(response);
    }

    @GET
    @Path("/" + WSPathNames.WS_QAZKOM_OK)
    public Response getBackPaymentOK(@QueryParam("response") final String response) {
	return postbackPayment(response);
    }

    // PRIVATE

    private Response postbackPayment(final String rawXml) {
	try {
	    _postbackPayment(rawXml);
	    return responseOk(0);
	} catch (final WrongArgumentException e) {
	    mailApplicationErrorAdmin(e, rawXml);
	    return responseWrongArgument(e, getLocaleOrDefault());
	} catch (final InternalServerErrorException e) {
	    mailServerErrorAdmin(e, rawXml);
	    return responseInternalServerError(e, getLocaleOrDefault());
	}
    }

    @Inject
    private QazkomFacade qazkoms;

    private Invoice _postbackPayment(final String rawXml) throws WrongArgumentException, InternalServerErrorException {
	try {
	    return reThrowAsUnchecked(() -> qazkoms.processPayment(rawXml).getForInvoice());
	} catch (final IllegalArgumentException | IllegalStateException e) {
	    throw new WrongArgumentException(e);
	} catch (final RuntimeException e) {
	    throw new InternalServerErrorException(e);
	}
    }
}
