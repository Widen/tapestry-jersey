package com.financeactive.tapestry.jersey.services;

import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.spi.container.WebApplication;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import org.apache.tapestry5.TapestryFilter;
import org.apache.tapestry5.ioc.ObjectLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.ws.rs.core.Application;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import static java.lang.Boolean.TRUE;

/**
 *
 */
public class JerseyEndPoint {

    private static final Logger LOG = LoggerFactory.getLogger(JerseyModule.class);

    private String path;

    private Application application;

    private ObjectLocator objectLocator;

    private TapestryRequestContext requestContext;

    private ServletContainer jaxwsContainer;

    private static final FilterChain END_OF_CHAIN = new EndOfChainFilterChain();

    public JerseyEndPoint(String path, Application application, TapestryRequestContext requestContext) {
        this.path = path;
        this.application = application;
        this.objectLocator = (ObjectLocator) requestContext.getApplicationGlobals().getContext().getAttribute(TapestryFilter.REGISTRY_CONTEXT_NAME);
        this.requestContext = requestContext;
        try {
            buildContainer(requestContext.getApplicationGlobals().getServletContext());
            LOG.info("Jersey application {} mounted at path {}", application.getClass().getName(), path);
        } catch (ServletException e) {
            throw new RuntimeException("Failed to build JAX-RS container: " + e.getMessage(), e);
        }
    }

    public String getPath() {
        return path;
    }

    public Application getApplication() {
        return application;
    }

    private void buildContainer(final ServletContext servletContext) throws ServletException {
        if (servletContext == null){
            LOG.warn("ServletContext is null. Jersey endpoint will not be mounted.");
            return;
        }
        jaxwsContainer = new ServletContainer(application) {

            @Override
            protected void initiate(ResourceConfig rc, WebApplication wa) {
                wa.initiate(rc, new TapestryComponentProviderFactory(objectLocator));
            }
        };

        final Hashtable<String, String> params = new Hashtable<String, String>();
        params.put(ServletContainer.APPLICATION_CONFIG_CLASS, application.getClass().getName());
        params.put(ServletContainer.PROPERTY_FILTER_CONTEXT_PATH, this.path);
        params.put(JSONConfiguration.FEATURE_POJO_MAPPING, TRUE.toString());

        jaxwsContainer.init(new FilterConfig() {

            @Override
            public ServletContext getServletContext() {
                return servletContext;
            }

            @Override
            public Enumeration<?> getInitParameterNames() {
                return params.elements();
            }

            @Override
            public String getInitParameter(String name) {
                return params.get(name);
            }

            @Override
            public String getFilterName() {
                return "JerseyHttpServletRequestFilter";
            }
        });
    }

    public boolean accept(String servletPath) {
        return servletPath.startsWith(path);
    }

    public boolean service(String requestPath) throws IOException {
        if (jaxwsContainer == null){
            LOG.warn("No jaxws container. Skip processing.");
            return false;
        }
        if (!accept(requestPath)){
            throw new IllegalArgumentException("This endpoint does not accept path " + requestPath);
        }

        try {
            this.jaxwsContainer.doFilter(
                    requestContext.getHTTPRequest(),
                    requestContext.getHTTPResponse(),
                    END_OF_CHAIN);
            return true;
        } catch (ServletException e) {
            LOG.error("Jersey failed to handler the request", e);
            //TODO : write a response!
            return true;
        }
    }

    private static final class EndOfChainFilterChain implements FilterChain {

        @Override
        public void doFilter(ServletRequest request,
                             ServletResponse response) throws IOException, ServletException {
            //NOOP
        }
    }
}
