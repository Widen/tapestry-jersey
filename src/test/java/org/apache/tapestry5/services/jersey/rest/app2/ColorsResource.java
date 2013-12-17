package org.apache.tapestry5.services.jersey.rest.app2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.common.collect.Lists;
import org.apache.tapestry5.services.jersey.JerseyStatusCodeResponseException;

@Path("/colors")
public class ColorsResource
{

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
    public String demoCustomStatusException()
    {
        throw new JerseyStatusCodeResponseException("This is a custom message", HttpServletResponse.SC_EXPECTATION_FAILED);
    }

}
