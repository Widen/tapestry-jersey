package org.apache.tapestry5.services.jersey;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface JerseyRequestHandler
{

    boolean service(JerseyEndpoint endpoint, HttpServletRequest request, HttpServletResponse response) throws IOException;

}
