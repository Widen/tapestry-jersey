package org.apache.tapestry5.services.jersey.rest.app2;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 *
 */
@Path("/coucou")
public class GoodbyeService
{

    @GET
    public String getHello()
    {
        throw new RuntimeException("TOOOOO");
        //return "coucou";
    }

}
