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

package com.financeactive.tapestry.jersey.services;

import com.sun.jersey.spi.container.servlet.ServletContainer;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.internal.services.RequestImpl;
import org.apache.tapestry5.internal.services.ResponseImpl;
import org.apache.tapestry5.internal.services.TapestrySessionFactory;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * HttpServletRequestFilter that passes requests with a predefined prefix 
 * (see {@link JerseySymbols#REQUEST_PATH_PREFIX) to Jersey container.
 * 
 */
public class JerseyRequestFilter implements HttpServletRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(JerseyRequestFilter.class);

    private static final FilterChain END_OF_CHAIN = new EndOfChainFilterChain();

    private ServletContainer jaxwscontainer;

    @Inject
    private RequestGlobals requestGlobals;
    
    @Inject
    private TapestrySessionFactory sessionFactory;
    
    @Symbol(SymbolConstants.CHARSET)
    private String applicationCharset;

    private List<String> pathPrefixes;

    public JerseyRequestFilter(@Inject @Symbol(JerseySymbols.REQUEST_PATH_PREFIX) String pathPrefix){
        pathPrefixes = Arrays.asList(pathPrefix.split(","));
    }

    public void setServletContainer(ServletContainer jaxwsContainer) {
        jaxwscontainer = jaxwsContainer;
    }

    @Override
    public boolean service(HttpServletRequest request, HttpServletResponse response, HttpServletRequestHandler handler)
            throws IOException {

        boolean jerseyRequest = false;
        //Tyhe request should be handled by jersey if it starts with one of the path prefixes
        for (String prefix : pathPrefixes) {
            if (request.getServletPath().startsWith(prefix)){
                jerseyRequest = true;
                break;
            }
        }

        if (!jerseyRequest) {
            LOG.debug("Skipping request " + request.getRequestURI() + "?" + request.getQueryString());
            return handler.service(request, response);
        }

        LOG.debug("Handling request " + request.getRequestURI() + "?" + request.getQueryString());

        try {
            
            // made the request/response available in jersey managed services. 
            Request t5request = new RequestImpl(request, applicationCharset, sessionFactory);
            Response t5response = new ResponseImpl(request, response);
            requestGlobals.storeRequestResponse(t5request, t5response);
            
            jaxwscontainer.doFilter(request, response, END_OF_CHAIN);
            return true;
        }
        catch (ServletException e) {
            LOG.error("Jersey failed to handler the request", e);
            return false;
        }
    }

    private static final class EndOfChainFilterChain implements FilterChain {

        @Override
        public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {

        }
    }


}
