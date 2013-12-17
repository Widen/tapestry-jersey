package org.apache.tapestry5.services.jersey;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

class JerseyStatusCodeResponseExceptionConverter implements ExceptionMapper<JerseyStatusCodeResponseException>
{

    @Override
    public Response toResponse(JerseyStatusCodeResponseException exception)
    {
        return Response.status(exception.getStatusCode()).type(MediaType.TEXT_HTML_TYPE).entity(exception.getMessage()).build();
    }

}
