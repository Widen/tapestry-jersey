package org.apache.tapestry5.services.jersey.rest.services.color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.common.collect.Lists;

@Path("/colors")
public class ColorsResource
{

    private final ContainerRequestContext requestContext;

    public ColorsResource(ContainerRequestContext requestContext)
    {
        this.requestContext = requestContext;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getColor()
    {
        ArrayList<String> colors = Lists.newArrayList();

        for (Color color : Color.values())
        {
            colors.add(color.toString());
        }

        return "Your random color is " + colors.get(new Random().nextInt(colors.size()));
    }

    @GET
    @Path("/{color}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getColor(@PathParam("color") Color color)
    {
        return String.format("You picked %s (ordinal #%s)", color, color.ordinal());
    }

    @GET
    @Path("/list")
    @Produces(MediaType.TEXT_PLAIN)
    public String getListOfColors()
    {
        return "Available choices are: " + Arrays.toString(Color.values());
    }

    @GET
    @Path("/exception")
    public String demoException()
    {
        throw new RuntimeException("Boom");
    }

    @GET
    @Path("/customstatusexception")
    public Object demoCustomStatusException()
    {
        return Response.status(HttpServletResponse.SC_EXPECTATION_FAILED).entity("This is a custom message").build();
    }

    @GET
    @Path("/debugdump")
    @Produces(MediaType.TEXT_PLAIN)
    public Object dumpContainerRequestContext()
    {
        return String.format("%s %s; Security Name: %s", requestContext.getMethod(), requestContext.getUriInfo().getAbsolutePath(), requestContext.getSecurityContext().getUserPrincipal() != null ? requestContext.getSecurityContext().getUserPrincipal().getName() : "[ None ]");
    }

}
