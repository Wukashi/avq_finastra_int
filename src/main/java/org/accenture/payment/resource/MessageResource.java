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
    @Path("/send-multipart")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_XML)
    public void send(String queue, MultipartFormDataInput mpfData){
        messageServiceMultipart.sendMessage("testQueue", mpfData);
    }
//    public Response processServiceMultipart(M ultipartFormDataInput mpfData) {
//        try {
////            String resp = messageServiceMultipart.processMultipart(mpfData);
//            Response resp = messageServiceMultipart.sendMessage("asd", mpfData);
//            return Response.ok(resp).build();
//        } catch (Exception e) {
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//                    .entity("Multipart error: " + e.toString()).build();
//        }
//    }
}