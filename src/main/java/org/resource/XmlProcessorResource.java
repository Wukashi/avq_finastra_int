package org.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.service.XmlMapperServiceDOM;
import org.service.XmlMapperServiceNs;

import java.io.InputStream;

@Path("/")
@Consumes(MediaType.APPLICATION_XML)
@Produces(MediaType.APPLICATION_XML)
public class XmlProcessorResource {

    @Inject
    XmlMapperServiceDOM mapperServiceDOM;

    @Inject
    XmlMapperServiceNs mapperServiceNs;


    @POST
    @Path("/transfer-dom")
    public Response processXmlDOM(InputStream inputXmlStream) {
        try {
            String resp = mapperServiceDOM.processDOM(inputXmlStream);
            return Response.ok(resp).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("DOM Error: " + e.toString()).build();
        }
    }


    @POST
    @Path("/transfer-ns")
    public Response processXmlNs(InputStream xml) {
        try {
            String resp = mapperServiceNs.processNs(xml);
            return Response.ok(resp).build();
        } catch (Exception e) {
            return Response.serverError().entity("NS error: " + e.getMessage()).build();
        }
    }
}