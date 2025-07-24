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
            InputStream outputXml = mapperService.process(inputXmlStream);
            return Response.ok(outputXml).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error: " + e.getMessage()).build();
        }
    }
}
