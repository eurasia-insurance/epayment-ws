package tech.lapsa.epayment.ws.rs.app;

import static tech.lapsa.epayment.ws.rs.app.SamplesUtil.*;
import static tech.lapsa.javax.rs.utility.RESTUtils.*;

import javax.annotation.security.PermitAll;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import tech.lapsa.epayment.ws.jaxb.entity.XmlEbillInfo;

@Path("/" + WSPathNames.WS_SAMPLES)
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@PermitAll
@Singleton
public class SamplesWS extends ABaseWS {

    @GET
    @Path("/ebill")
    public Response ebillSampleGET() {
	final XmlEbillInfo sample = ebillInfoSample();
	return responseOk(sample, getLocaleOrDefault());
    }

}
