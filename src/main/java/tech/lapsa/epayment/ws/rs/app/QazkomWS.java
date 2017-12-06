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

import tech.lapsa.epayment.facade.EpaymentFacade;
import tech.lapsa.javax.rs.utility.InternalServerErrorException;
import tech.lapsa.javax.rs.utility.WrongArgumentException;

@Path("/" + WSPathNames.WS_QAZKOM)
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.TEXT_PLAIN)
@PermitAll
@Singleton
public class QazkomWS extends ABaseWS {

    @Inject
    private EpaymentFacade epayments;

    @POST
    @Path("/" + WSPathNames.WS_QAZKOM_OK)
    public Response postbackPOST(@FormParam("response") final String postbackXml) {
	return postback(postbackXml);
    }

    @GET
    @Path("/" + WSPathNames.WS_QAZKOM_OK)
    public Response postbackGET(@QueryParam("response") final String postbackXml) {
	return postback(postbackXml);
    }

    private Response postback(final String postbackXml) {
	try {
	    _postback(postbackXml);
	    return responseOk(0);
	} catch (final WrongArgumentException e) {
	    mailApplicationErrorAdmin(e, postbackXml);
	    return responseWrongArgument(e, getLocaleOrDefault());
	} catch (final InternalServerErrorException e) {
	    mailServerErrorAdmin(e, postbackXml);
	    return responseInternalServerError(e, getLocaleOrDefault());
	}
    }

    private void _postback(final String postbackXml)
	    throws WrongArgumentException, InternalServerErrorException {
	try {
	    reThrowAsUnchecked(() -> epayments.completeWithQazkomPayment(postbackXml));
	} catch (final IllegalArgumentException | IllegalStateException e) {
	    throw new WrongArgumentException(e);
	} catch (final RuntimeException e) {
	    throw new InternalServerErrorException(e);
	}
    }

    @POST
    @Path("/" + WSPathNames.WS_QAZKOM_FAILURE)
    public Response failurePOST(@FormParam("response") final String failureXml) {
	return failure(failureXml);
    }

    @GET
    @Path("/" + WSPathNames.WS_QAZKOM_FAILURE)
    public Response failureGET(@QueryParam("response") final String failureXml) {
	return failure(failureXml);
    }

    private Response failure(final String failureXml) {
	try {
	    final String message = _failure(failureXml);
	    mailAdmin("QAZKOM EPAY FAILURE RESPONSE: " + message, failureXml);
	    return responseOk(0);
	} catch (final WrongArgumentException e) {
	    mailApplicationErrorAdmin(e, failureXml);
	    return responseWrongArgument(e, getLocaleOrDefault());
	} catch (final InternalServerErrorException e) {
	    mailServerErrorAdmin(e, failureXml);
	    return responseInternalServerError(e, getLocaleOrDefault());
	}
    }

    private String _failure(final String failureXml) throws WrongArgumentException, InternalServerErrorException {
	try {
	    return reThrowAsUnchecked(() -> epayments.processQazkomFailure(failureXml));
	} catch (final IllegalArgumentException | IllegalStateException e) {
	    throw new WrongArgumentException(e);
	} catch (final RuntimeException e) {
	    throw new InternalServerErrorException(e);
	}
    }
}
