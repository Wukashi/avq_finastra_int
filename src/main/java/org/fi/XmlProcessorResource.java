package org.fi;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.InputStream;

@Path("/xml")
@Consumes(MediaType.APPLICATION_XML)
@Produces(MediaType.APPLICATION_XML)
public class XmlProcessorResource {

    private final XmlMapperService mapperService = new XmlMapperService();

    @POST
    public Response processXml(InputStream inputXmlStream) {
        try {
            mapperService.process(inputXmlStream);
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error: " + e.toString()).build();
        }
    }
}
