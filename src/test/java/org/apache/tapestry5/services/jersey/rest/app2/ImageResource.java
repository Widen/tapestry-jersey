package org.apache.tapestry5.services.jersey.rest.app2;

import java.io.InputStream;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/image")
public class ImageResource
{

    @GET
    public Response streamImage()
    {
        InputStream in = ImageResource.class.getResourceAsStream("grumpy-cat.jpg");
        return Response.ok(in, "image/jpeg").build();
    }

}
