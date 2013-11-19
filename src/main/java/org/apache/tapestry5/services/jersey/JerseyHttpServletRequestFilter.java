// Copyright 2007, 2008, 2009 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry5.services.jersey;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.internal.services.RequestImpl;
import org.apache.tapestry5.internal.services.ResponseImpl;
import org.apache.tapestry5.internal.services.TapestrySessionFactory;
import org.apache.tapestry5.ioc.annotations.EagerLoad;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.HttpServletRequestFilter;
import org.apache.tapestry5.services.HttpServletRequestHandler;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestGlobals;
import org.apache.tapestry5.services.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HttpServletRequestFilter that passes requests to configured endpoints.
 */
@EagerLoad
public class JerseyHttpServletRequestFilter implements HttpServletRequestFilter
{

    private static final Logger log = LoggerFactory.getLogger(JerseyModule.class);

    @Inject
    private JerseyApplications applications;

    @Inject
    private JerseyRequestHandler jerseyHandler;

    @Override
    public boolean service(HttpServletRequest request, HttpServletResponse response, HttpServletRequestHandler handler) throws IOException
    {
        for (JerseyEndpoint endPoint : applications.getEndPoints())
        {
            if (endPoint.accept(request.getServletPath()))
            {
                return jerseyHandler.service(endPoint, request, response);
            }
        }

        return handler.service(request, response);
    }

    public static class Terminator implements JerseyRequestHandler
    {

        @Inject
        private RequestGlobals requestGlobals;

        @Inject
        private TapestrySessionFactory sessionFactory;

        @Inject
        @Symbol(SymbolConstants.CHARSET)
        private String applicationCharset;

        @Override
        public boolean service(JerseyEndpoint endpoint, HttpServletRequest request, HttpServletResponse response) throws IOException
        {
            log.info("Servicing Terminator request '{}'", request.getRequestURL());

            // make the request/response available in jersey managed services.
            storeInToGlobals(request, response);
            return endpoint.service(request.getServletPath());
        }

        private void storeInToGlobals(HttpServletRequest request, HttpServletResponse response)
        {
            Request t5request = new RequestImpl(request, applicationCharset, sessionFactory);
            Response t5response = new ResponseImpl(request, response);
            requestGlobals.storeRequestResponse(t5request, t5response);
        }

    }

}
