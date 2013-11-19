package org.apache.tapestry5.services.jersey.rest.app1;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hellor")
public interface HelloResponseResource
{

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public HelloResponse getHelloResponse();

}
