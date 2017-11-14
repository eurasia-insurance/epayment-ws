package tech.lapsa.epayment.ws.rs.app;

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

import tech.lapsa.epayment.facade.QazkomFacade;
import tech.lapsa.epayment.ws.mail.QAdmin;
import tech.lapsa.java.commons.function.MyStrings;
import tech.lapsa.java.commons.logging.MyLogger;
import tech.lapsa.javax.mail.MailException;
import tech.lapsa.javax.mail.MailFactory;
import tech.lapsa.javax.mail.MailMessageBuilder;

@Path("/" + WSPathNames.WS_QAZKOM)
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.TEXT_PLAIN)
@PermitAll
@Singleton
public class QazkomWS {

    private final MyLogger logger = MyLogger.newBuilder() //
	    .withNameOf(QazkomWS.class) //
	    .build();

    @Inject
    @QAdmin
    private MailFactory mailFactory;

    @POST
    @Path("/" + WSPathNames.WS_QAZKOM_OK)
    public Response postBackPaymentOK(@FormParam("response") String response) {
	return postbackPayment(response);
    }

    @GET
    @Path("/" + WSPathNames.WS_QAZKOM_OK)
    public Response getBackPaymentOK(@QueryParam("response") String response) {
	return postbackPayment(response);
    }

    // PRIVATE

    @Inject
    private QazkomFacade facade;

    private Response postbackPayment(String rawResponse) {
	try {
	    facade.handleResponse(rawResponse);
	    return ok();
	} catch (IllegalArgumentException | IllegalStateException e) {
	    return handleApplicationError(e, rawResponse);
	} catch (RuntimeException e) {
	    return handleServerError(e, rawResponse);
	}
    }

    private Response serverError(Exception e, String rawResponse) {
	return responseServerError(e.getMessage());
    }

    private Response applicationError(Exception e, String rawResponse) {
	return responseBadRequest(
		e.getClass().getSimpleName() + (e.getMessage() != null ? (" " + e.getMessage()) : ""));
    }

    private Response ok() {
	return responseOk(0);
    }

    private Response handleServerError(Exception e, String rawResponse) {
	logger.SEVERE.log(e, "Server throws %1$s exception while handling response '%2$s'",
		e.getClass().getSimpleName(), rawResponse);
	mailServerErrorAdmin(e, rawResponse);
	return serverError(e, rawResponse);
    }

    private Response handleApplicationError(Exception e, String rawResponse) {
	logger.SEVERE.log(e, "Server throws %1$s exception while handling response '%2$s'",
		e.getClass().getSimpleName(), rawResponse);
	mailApplicationErrorAdmin(e, rawResponse);
	return applicationError(e, rawResponse);
    }

    private void mailServerErrorAdmin(Exception e, String rawResponse) {
	mailAdmin(String.format("CRITICAL ERROR : %1$s : %2$s", e.getClass().getSimpleName(),
		(e.getMessage() != null ? e.getMessage() : "NONE")), e, rawResponse);
    }

    private void mailApplicationErrorAdmin(Exception e, String rawResponse) {
	mailAdmin(String.format("Error : %1$s : %2$s", e.getClass().getSimpleName(),
		(e.getMessage() != null ? e.getMessage() : "NONE")), e, rawResponse);
    }

    private void mailAdmin(String subject, Exception e, String rawResponse) {
	try {
	    MailMessageBuilder messageBuilder = mailFactory.newMailBuilder()
		    .withDefaultSender()
		    .withDefaultRecipient()
		    .withSubject(subject);
	    if (e != null)
		messageBuilder.withExceptionAttached(e, "stacktrace.txt");
	    if (MyStrings.nonEmpty(rawResponse))
		messageBuilder.withTextAttached(rawResponse, "response.txt");
	    messageBuilder.build()
		    .send();
	} catch (MailException e1) {
	    logger.SEVERE.log(e1);
	}

    }
}
