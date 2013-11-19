package org.apache.tapestry5.services.jersey;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ApplicationGlobals;
import org.apache.tapestry5.services.RequestGlobals;

public class TapestryRequestContext
{

    @Inject
    private ApplicationGlobals applicationGlobals;

    @Inject
    private RequestGlobals requestGlobals;

    public ApplicationGlobals getApplicationGlobals()
    {
        return applicationGlobals;
    }

    public HttpServletRequest getHttpServletRequest()
    {
        return requestGlobals.getHTTPServletRequest();
    }

    public HttpServletResponse getHttpServletResponse()
    {
        return requestGlobals.getHTTPServletResponse();
    }

}
