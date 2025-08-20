package org.accenture.payment.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.accenture.payment.service.MessageServiceJsonBase64;
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
                    .entity("Multipart error: " + e).build();
        }
    }

    @POST
    @Path("/jsonbase64")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_XML)
    public Response processServiceJsonBase64(String jsonBody) {
        try {
            String xmlResponse = MessageServiceJsonBase64.processJsonBase64(jsonBody);
            return Response.ok(xmlResponse, MediaType.APPLICATION_XML).build();
        } catch (Exception e) {
            String errorXml = "<error>\nInvalid JSON or Base64 data:\n " + e + "\n</error>";
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(errorXml)
                    .type(MediaType.APPLICATION_XML)
                    .build();
        }
    }
}