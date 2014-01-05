package org.apache.tapestry5.services.jersey.rest.services.colorapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;

import com.google.common.collect.Lists;

public class ColorsResourceImpl implements ColorsResource
{

    private final ContainerRequestContext requestContext;

    public ColorsResourceImpl(ContainerRequestContext requestContext)
    {
        this.requestContext = requestContext;
    }

    @Override
    public String getColor()
    {
        ArrayList<String> colors = Lists.newArrayList();

        for (Color color : Color.values())
        {
            colors.add(color.toString());
        }

        return "Your random color is " + colors.get(new Random().nextInt(colors.size()));
    }

    @Override
    public String getColor(Color color)
    {
        return String.format("You picked %s (ordinal #%s)", color, color.ordinal());
    }

    @Override
    public String getListOfColors()
    {
        return "Available choices are: " + Arrays.toString(Color.values());
    }

    @Override
    public String demoException()
    {
        throw new RuntimeException("Boom");
    }

    @Override
    public Object demoCustomStatusException()
    {
        return Response.status(HttpServletResponse.SC_EXPECTATION_FAILED).entity("This is a custom message").build();
    }

    @Override
    public Object dumpContainerRequestContext()
    {
        return String.format("%s %s; Security Name: %s", requestContext.getMethod(), requestContext.getUriInfo().getRequestUri(), requestContext.getSecurityContext().getUserPrincipal() != null ? requestContext.getSecurityContext().getUserPrincipal().getName() : "[ None ]");
    }

}
