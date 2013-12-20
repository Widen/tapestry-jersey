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
import java.util.Collections;
import java.util.Enumeration;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.google.common.base.Preconditions;
import org.apache.tapestry5.services.jersey.internal.JerseyTapestryRequestContext;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class TapestryBackedJerseyApplication extends Application
{

    private static final Logger log = LoggerFactory.getLogger(TapestryBackedJerseyApplication.class);

    private final String appPath;

    private JerseyTapestryRequestContext requestContext;

    private ServletContainer servletContainer;

    private static final FilterChain END_OF_CHAIN = new EndOfChainFilterChain();

    public TapestryBackedJerseyApplication(JerseyTapestryRequestContext requestContext)
    {
        this.requestContext = requestContext;

        ApplicationPath path = getClass().getAnnotation(ApplicationPath.class);
        if (path == null)
        {
            throw new IllegalArgumentException("No @ApplicationPath configured for application " + getClass().getName());
        }

        appPath = path.value();
    }

    public void configureJaxRsApplication()
    {
        try
        {
            final ServletContext servletContext = Preconditions.checkNotNull(requestContext.getApplicationGlobals().getServletContext(), "ServletContext");

            final ResourceConfig config = ResourceConfig.forApplication(this);
            config.property(ServletProperties.FILTER_CONTEXT_PATH, getAppPath());

            config.registerClasses(getClasses());
            config.registerInstances(getSingletons());

            servletContainer = new ServletContainer(config);
            servletContainer.init(new FilterConfig()
            {
                @Override
                public ServletContext getServletContext()
                {
                    return servletContext;
                }

                @Override
                public Enumeration<String> getInitParameterNames()
                {
                    return Collections.enumeration(config.getPropertyNames());
                }

                @Override
                public String getInitParameter(String name)
                {
                    return (String) config.getProperty(name);
                }

                @Override
                public String getFilterName()
                {
                    return "JerseyHttpServletRequestFilter";
                }
            });
        }
        catch (ServletException e)
        {
            throw new RuntimeException("Error building JAX-RS servlet container: " + e.getMessage(), e);
        }
    }

    public String getAppPath()
    {
        return appPath;
    }

    public boolean accept(String servletPath)
    {
        return servletPath.startsWith(getAppPath());
    }

    public boolean service(String requestPath) throws IOException
    {
        if (!accept(requestPath))
        {
            throw new IllegalArgumentException(String.format("Invalid appPath '%s'; must start with '%s'", requestPath, appPath));
        }

        HttpServletRequest request = requestContext.getHttpServletRequest();
        String requestUrl = request.getRequestURL() + (request.getQueryString() == null ? "" : "?" + request.getQueryString());

        HttpServletResponse response = requestContext.getHttpServletResponse();
        try
        {
            log.debug("Servicing JAX-RS request for '{}'", requestUrl);
            servletContainer.doFilter(request, response, END_OF_CHAIN);
        }
        catch (Throwable e)
        {
            log.error("Status code {} returned to client for JAX-RS request '{}'", response.getStatus(), requestUrl, e);
        }

        return true;
    }

    private static final class EndOfChainFilterChain implements FilterChain
    {
        @Override
        public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException
        {
            // no-op
        }
    }

}