package org.apache.tapestry5.services.jersey.rest.app2;

import java.util.ArrayList;
import java.util.Random;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.common.collect.Lists;

@Path("/color")
public class ColorsResource
{

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getColor()
    {
//        if (1 == 1)
//        {
//            throw new RuntimeException("Boom");
//        }

        ArrayList<String> colors = Lists.newArrayList("red", "blue", "green", "purple");
        return colors.get(new Random().nextInt(colors.size()));
    }

}
