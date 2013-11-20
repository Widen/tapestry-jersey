package org.apache.tapestry5.services.jersey.rest.app1.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.tapestry5.services.jersey.rest.app1.entities.Greeting;

@Path("/hello")
public interface HelloResource
{
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Greeting getHelloResponse();
}
