package org.accenture.payment.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.accenture.payment.service.MessageSender;
import org.accenture.payment.service.MessageServiceJsonBase64;
import org.eclipse.microprofile.config.inject.ConfigProperty;


@Path("/")
public class MessageResource {
    @Inject
    MessageServiceJsonBase64 messageServiceJsonBase64;

    @Inject
    MessageSender sender;

    @ConfigProperty(name = "payment.queue.name")
    String queueName;

    @POST
    @Path("/jsonbase64")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_XML)
    public Response processServiceJsonBase64(String jsonBody) {
        try {
            String pain01 = messageServiceJsonBase64.getPain001FromJsonBase64(jsonBody);
            sender.sendXml(queueName, pain01);
            return Response.ok(pain01, MediaType.APPLICATION_XML).build();
        } catch (Exception e) {
            String errorXml = "<error>Invalid JSON or Base64 data: " + e + "</error>";
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(errorXml)
                    .type(MediaType.APPLICATION_XML)
                    .build();
        }
    }
}