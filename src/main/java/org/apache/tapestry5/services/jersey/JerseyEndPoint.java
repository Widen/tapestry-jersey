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
import javax.ws.rs.core.Application;

import org.apache.tapestry5.TapestryFilter;
import org.apache.tapestry5.ioc.ObjectLocator;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class JerseyEndpoint
{

    private static final Logger log = LoggerFactory.getLogger(JerseyModule.class);

    private String path;

    private Application application;

    private ObjectLocator objectLocator;

    private TapestryRequestContext requestContext;

    private ServletContainer jaxwsContainer;

    private static final FilterChain END_OF_CHAIN = new EndOfChainFilterChain();

    public JerseyEndpoint(String path, Application application, TapestryRequestContext requestContext)
    {
        this.path = path;
        this.application = application;
        this.objectLocator = (ObjectLocator) requestContext.getApplicationGlobals().getContext().getAttribute(TapestryFilter.REGISTRY_CONTEXT_NAME);
        this.requestContext = requestContext;
        try
        {
            buildContainer(requestContext.getApplicationGlobals().getServletContext());
            log.info("Jersey application {} mounted at path {}", application.getClass().getName(), path);
        }
        catch (ServletException e)
        {
            throw new RuntimeException("Failed to build JAX-RS container: " + e.getMessage(), e);
        }
    }

    public String getPath()
    {
        return path;
    }

    public Application getApplication()
    {
        return application;
    }

    private void buildContainer(final ServletContext servletContext) throws ServletException
    {
        if (servletContext == null)
        {
            log.warn("ServletContext is null. Jersey endpoint will not be mounted.");
            return;
        }

        log.info("Building container for {}", application.getClass().getName());

        final ResourceConfig config = ResourceConfig.forApplication(application);
        config.property(ServletProperties.FILTER_CONTEXT_PATH, path);
        config.register(GsonMessageBodyHandler.class);

        jaxwsContainer = new ServletContainer(config);
        jaxwsContainer.init(new FilterConfig()
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

    public boolean accept(String servletPath)
    {
        return servletPath.startsWith(path);
    }

    public boolean service(String requestPath) throws IOException
    {
        if (jaxwsContainer == null)
        {
            log.warn("No jaxws container. Skip processing.");
            return false;
        }

        if (!accept(requestPath))
        {
            throw new IllegalArgumentException("This endpoint does not accept path " + requestPath);
        }

        try
        {
            log.info("Servicing JAXWS container request for {}", requestContext.getHttpServletRequest().getRequestURL());

            this.jaxwsContainer.doFilter(requestContext.getHttpServletRequest(), requestContext.getHttpServletResponse(), END_OF_CHAIN);
            return true;
        }
        catch (ServletException e)
        {
            log.error("Jersey failed to handler the request", e);
            //TODO : write a response!
            return true;
        }
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
