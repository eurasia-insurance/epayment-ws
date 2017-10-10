package tech.lapsa.epayment.ws.rs.app;

import static com.lapsa.utils.RESTUtils.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.lapsa.commons.function.MyStrings;
import com.lapsa.epayment.facade.QazkomFacade;
import com.lapsa.mail2.MailException;
import com.lapsa.mail2.MailFactory;
import com.lapsa.mail2.MailMessageBuilder;

import tech.lapsa.epayment.ws.mail.QAdmin;

@Path("/" + WSPathNames.WS_QAZKOM)
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.TEXT_PLAIN)
public class QazkomWS {
    private Logger logger = Logger.getLogger(this.getClass().getName());

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
	    facade.newResponseBuilder() //
		    .withXml(rawResponse) //
		    .build() //
		    .handle();
	    return ok();
	} catch (IllegalArgumentException e) {
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
	logger.log(Level.SEVERE, String.format("Server throws %1$s exception while handling response '%2$s'",
		e.getClass().getSimpleName(), rawResponse), e);
	mailServerErrorAdmin(e, rawResponse);
	return serverError(e, rawResponse);
    }

    private Response handleApplicationError(Exception e, String rawResponse) {
	logger.log(Level.SEVERE, String.format("Server throws %1$s exception while handling response '%2$s'",
		e.getClass().getSimpleName(), rawResponse), e);
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
	    logger.log(Level.SEVERE, e1.getMessage(), e1);
	}

    }
}
