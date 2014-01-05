package org.apache.tapestry5.services.jersey.rest.services.colorapp;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/colors")
public interface ColorsResource
{

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    String getColor();

    @GET
    @Path("/{color}")
    @Produces(MediaType.TEXT_PLAIN)
    String getColor(@PathParam("color") Color color);

    @GET
    @Path("/list")
    @Produces(MediaType.TEXT_PLAIN)
    String getListOfColors();

    @GET
    @Path("/exception")
    String demoException();

    @GET
    @Path("/customstatusexception")
    Object demoCustomStatusException();

    @GET
    @Path("/debugdump")
    @Produces(MediaType.TEXT_PLAIN)
    Object dumpContainerRequestContext();

}
