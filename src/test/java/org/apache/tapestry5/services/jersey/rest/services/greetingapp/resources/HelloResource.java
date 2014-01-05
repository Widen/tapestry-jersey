package org.apache.tapestry5.services.jersey.rest.services.greetingapp.resources;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import org.apache.tapestry5.services.jersey.rest.services.greetingapp.entities.Greeting;

@Path("/hello/{name}")
public interface HelloResource
{
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Greeting getHelloResponse(@PathParam("name") String name,
                                     @QueryParam("lastname") @DefaultValue("unknown") String lastname,
                                     @QueryParam("catch-phrase") @DefaultValue("An apple a day keeps...") String catchPhrase,
                                     @Context Request request,
                                     @Context UriInfo uriInfo);
}
