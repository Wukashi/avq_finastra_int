package org.accenture.payment.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.server.multipart.MultipartFormDataInput;
import org.accenture.payment.service.MessageServiceMultipart;


@Path("/")
public class MessageResource {
    @Inject
    MessageServiceMultipart messageServiceMultipart;


    @POST
    @Path("/multipart")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_XML)
    public Response processServiceMultipart(MultipartFormDataInput mpfData) {
        try {
            String resp = messageServiceMultipart.processMultipart(mpfData);
            return Response.ok(resp).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Multipart error: " + e.toString()).build();
        }
    }
}