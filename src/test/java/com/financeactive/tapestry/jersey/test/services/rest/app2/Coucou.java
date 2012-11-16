package com.financeactive.tapestry.jersey.test.services.rest.app2;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 *
 */
@Path("/coucou")
public class Coucou {

    @GET
    public String getHello(){
        throw new RuntimeException("TOOOOO");
        //return "coucou";
    }

}
