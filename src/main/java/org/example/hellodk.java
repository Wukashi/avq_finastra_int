package org.example;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hellodk")
public class hellodk {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hellodk() {
        return "Hello World Quarkus";
    }
}
